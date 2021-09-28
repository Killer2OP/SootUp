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

import de.upb.swt.soot.core.jimple.common.constant.IntConstant;
import de.upb.swt.soot.core.jimple.common.constant.NullConstant;
import de.upb.swt.soot.core.jimple.common.expr.JInstanceOfExpr;
import de.upb.swt.soot.core.jimple.common.stmt.JAssignStmt;
import de.upb.swt.soot.core.jimple.common.stmt.Stmt;
import de.upb.swt.soot.core.model.Body;
import de.upb.swt.soot.core.transform.BodyInterceptor;

import javax.annotation.Nonnull;

/**
 * Transformer that swaps
 * 
 * a = 0 instanceof _class_;
 * 
 * with
 * 
 * a = false
 * 
 * @author Steven Arzt
 *
 */
public class DexNullInstanceofTransformer implements BodyInterceptor {

  @Override
  public void interceptBody(@Nonnull Body.BodyBuilder builder) {
    for (Stmt stmt : builder.getStmts()) {
      if (stmt instanceof JAssignStmt) {
        JAssignStmt assignStmt = (JAssignStmt) stmt;
        if (assignStmt.getRightOp() instanceof JInstanceOfExpr) {
          JInstanceOfExpr iof = (JInstanceOfExpr) assignStmt.getRightOp();

          // If the operand of the "instanceof" expression is null or
          // the zero constant, we replace the whole operation with
          // its outcome "false"
          if (iof.getOp() == NullConstant.getInstance()) {
            assignStmt.setRightOp(IntConstant.getInstance(0));
          }
          if (iof.getOp() instanceof IntConstant && ((IntConstant) iof.getOp()).value == 0) {
            assignStmt.setRightOp(IntConstant.getInstance(0));
          }
        }
      }
    }
  }

}