package de.upb.swt.soot.java.bytecode.frontend.apk.dexpler;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1997 - 2018 Raja Vallée-Rai and others
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import de.upb.swt.soot.core.jimple.basic.Local;
import de.upb.swt.soot.core.jimple.basic.Value;
import de.upb.swt.soot.core.jimple.common.constant.Constant;
import de.upb.swt.soot.core.jimple.common.ref.JArrayRef;
import de.upb.swt.soot.core.jimple.common.stmt.JAssignStmt;
import de.upb.swt.soot.core.jimple.common.stmt.Stmt;
import de.upb.swt.soot.core.model.Body;
import de.upb.swt.soot.core.transform.BodyInterceptor;
import de.upb.swt.soot.java.bytecode.interceptors.UnusedLocalEliminator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;

/**
 * Transformer that simplifies array initializations. It converts
 *
 * a = 0 b = 42 c[a] = b
 *
 * to
 *
 * c[0] 42
 *
 * This transformer performs copy propagation, dead assignment elimination, and unused local elimination at once for this
 * special case. The idea is to reduce the code as much as possible for this special case before applying the more expensive
 * other transformers.
 *
 * @author Steven Arzt
 *
 */
public class DexArrayInitReducer implements BodyInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(DexArrayInitReducer.class);
  private boolean options_verbose = true;

  protected void internalTransform(Body.BodyBuilder bodyBuilder, String phaseName, Map<String, String> options) {
    // Make sure that we only have linear control flow
    if (!bodyBuilder.getTraps().isEmpty()) {
      return;
    }

    // Look for a chain of two constant assignments followed by an array put
    Stmt stmt1 = null, stmt2 = null;
    for (Stmt stmt : bodyBuilder.getStmts()) {
      // If this is not an assignment, it does not matter.
      if (!(stmt instanceof JAssignStmt) || !stmt.getBoxesPointingToThis().isEmpty()) {
        stmt1 = null;
        stmt2 = null;
        continue;
      }

      // If this is an assignment to an array, we must already have two
      // preceding constant assignments
      JAssignStmt assignStmt = (JAssignStmt) stmt;
      if (assignStmt.getLeftOp() instanceof JArrayRef) {
        if (stmt1 != null && stmt2 != null && stmt2.getBoxesPointingToThis().isEmpty()
            && assignStmt.getBoxesPointingToThis().isEmpty()) {
          JArrayRef arrayRef = (JArrayRef) assignStmt.getLeftOp();

          Value u1val = stmt1.getDefs().get(0);
          Value u2val = stmt2.getDefs().get(0);

          // index
          if (arrayRef.getIndex() == u1val) {
            arrayRef.withIndex(((JAssignStmt) stmt1).getRightOp());
          } else if (arrayRef.getIndex() == u2val) {
            arrayRef.withIndex(((JAssignStmt) stmt2).getRightOp());
          }

          // value
          if (assignStmt.getRightOp() == u1val) {
            assignStmt.withRightOp(((JAssignStmt) stmt1).getRightOp());
          } else if (assignStmt.getRightOp() == u2val) {
            assignStmt.withRightOp(((JAssignStmt) stmt2).getRightOp());
          }

          // Remove the unnecessary assignments
          Iterator<Stmt> checkIt = bodyBuilder.getStmts().iterator();
          boolean foundU1 = false, foundU2 = false, doneU1 = false, doneU2 = false;
          while (!(doneU1 && doneU2) && !(foundU1 && foundU2) && checkIt.hasNext()) {
            Stmt checkU = checkIt.next();

            // Does the current statement use the value?
            for (Value vb : checkU.getUses()) {
              if (!doneU1 && vb == u1val) {
                foundU1 = true;
              }
              if (!doneU2 && vb == u2val) {
                foundU2 = true;
              }
            }

            // Does the current statement overwrite the value?
            for (Value vb : checkU.getDefs()) {
              if (vb == u1val) {
                doneU1 = true;
              } else if (vb == u2val) {
                doneU2 = true;
              }
            }

            // If this statement branches, we abort
            if (checkU.branches()) {
              foundU1 = true;
              foundU2 = true;
              break;
            }
          }
          if (!foundU1) {
            // only remove constant assignment if the left value is Local
            if (u1val instanceof Local) {
              bodyBuilder.getStmts().remove(stmt1);
              if (options_verbose) {
                logger.debug("[" + bodyBuilder.getMethodSignature().getName() + "]    remove 1 " + stmt1);
              }
            }
          }
          if (!foundU2) {
            // only remove constant assignment if the left value is Local
            if (u2val instanceof Local) {
              bodyBuilder.getStmts().remove(stmt2);
              if (options_verbose) {
                logger.debug("[" + bodyBuilder.getMethodSignature().getName() + "]    remove 2 " + stmt2);
              }
            }
          }

          stmt1 = null;
          stmt2 = null;
        } else {
          // No proper initialization before
          stmt1 = null;
          stmt2 = null;
          continue;
        }
      }

      // We have a normal assignment. This could be an array index or
      // value.
      if (!(assignStmt.getRightOp() instanceof Constant)) {
        stmt1 = null;
        stmt2 = null;
        continue;
      }

      if (stmt1 == null) {
        stmt1 = assignStmt;
      } else if (stmt2 == null) {
        stmt2 = assignStmt;

        // If the last value is overwritten again, we start again at the beginning
        if (stmt1 != null) {
          Value op1 = ((JAssignStmt) stmt1).getLeftOp();
          if (op1 == ((JAssignStmt) stmt2).getLeftOp()) {
            stmt1 = stmt2;
            stmt2 = null;
          }
        }
      } else {
        stmt1 = stmt2;
        stmt2 = assignStmt;
      }
    }

    // Remove all locals that are no longer necessary
    new UnusedLocalEliminator().interceptBody(bodyBuilder);
  }

  @Override
  public void interceptBody(@Nonnull Body.BodyBuilder builder) {

  }
}