/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */

package de.upb.soot.jimple.common.expr;

import de.upb.soot.StmtPrinter;
import de.upb.soot.jimple.Jimple;
import de.upb.soot.jimple.basic.Value;
import de.upb.soot.jimple.common.type.BooleanType;
import de.upb.soot.jimple.common.type.ByteType;
import de.upb.soot.jimple.common.type.CharType;
import de.upb.soot.jimple.common.type.DoubleType;
import de.upb.soot.jimple.common.type.FloatType;
import de.upb.soot.jimple.common.type.IntType;
import de.upb.soot.jimple.common.type.LongType;
import de.upb.soot.jimple.common.type.ShortType;
import de.upb.soot.jimple.common.type.Type;
import de.upb.soot.jimple.common.type.UnknownType;
import de.upb.soot.jimple.visitor.IExprVisitor;
import de.upb.soot.jimple.visitor.IVisitor;

public class JNegExpr extends AbstractUnopExpr {
  public JNegExpr(Value op) {
    super(Jimple.getInstance().newImmediateBox(op));
  }

  @Override
  public Object clone() {
    return new JNegExpr(Jimple.cloneIfNecessary(getOp()));
  }

  /** Compares the specified object with this one for structural equality. */
  @Override
  public boolean equivTo(Object o) {
    if (o instanceof JNegExpr) {
      return opBox.getValue().equivTo(((JNegExpr) o).opBox.getValue());
    }
    return false;
  }

  /** Returns a hash code for this object, consistent with structural equality. */
  @Override
  public int equivHashCode() {
    return opBox.getValue().equivHashCode();
  }

  @Override
  public String toString() {
    return Jimple.NEG + " " + opBox.getValue().toString();
  }

  @Override
  public void toString(StmtPrinter up) {
    up.literal(Jimple.NEG);
    up.literal(" ");
    opBox.toString(up);
  }

  @Override
  public Type getType() {
    Value op = opBox.getValue();

    if (op.getType().equals(IntType.getInstance()) || op.getType().equals(ByteType.getInstance())
        || op.getType().equals(ShortType.getInstance()) || op.getType().equals(BooleanType.getInstance())
        || op.getType().equals(CharType.getInstance())) {
      return IntType.getInstance();
    } else if (op.getType().equals(LongType.getInstance())) {
      return LongType.getInstance();
    } else if (op.getType().equals(DoubleType.getInstance())) {
      return DoubleType.getInstance();
    } else if (op.getType().equals(FloatType.getInstance())) {
      return FloatType.getInstance();
    } else {
      return UnknownType.getInstance();
    }
  }

  @Override
  public void accept(IVisitor sw) {
    ((IExprVisitor) sw).caseNegExpr(this);
  }
}