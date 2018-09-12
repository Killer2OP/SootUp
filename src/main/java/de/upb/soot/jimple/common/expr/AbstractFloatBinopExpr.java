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

import de.upb.soot.jimple.Jimple;
import de.upb.soot.jimple.basic.Value;
import de.upb.soot.jimple.basic.ValueBox;
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

@SuppressWarnings("serial")
public abstract class AbstractFloatBinopExpr extends AbstractBinopExpr {

  AbstractFloatBinopExpr(Value op1, Value op2) {
    this(Jimple.getInstance().newArgBox(op1), Jimple.getInstance().newArgBox(op2));
  }

  protected AbstractFloatBinopExpr(ValueBox op1Box, ValueBox op2Box) {
    this.op1Box = op1Box;
    this.op2Box = op2Box;
  }

  @Override
  public Type getType() {
    Value op1 = op1Box.getValue();
    Value op2 = op2Box.getValue();
    Type op1t = op1.getType();
    Type op2t = op2.getType();
    if ((op1t.equals(IntType.getInstance()) || op1t.equals(ByteType.getInstance()) || op1t.equals(ShortType.getInstance())
        || op1t.equals(CharType.getInstance()) || op1t.equals(BooleanType.getInstance()))
        && (op2t.equals(IntType.getInstance()) || op2t.equals(ByteType.getInstance()) || op2t.equals(ShortType.getInstance())
            || op2t.equals(CharType.getInstance()) || op2t.equals(BooleanType.getInstance()))) {
      return IntType.getInstance();
    } else if (op1t.equals(LongType.getInstance()) || op2t.equals(LongType.getInstance())) {
      return LongType.getInstance();
    } else if (op1t.equals(DoubleType.getInstance()) || op2t.equals(DoubleType.getInstance())) {
      return DoubleType.getInstance();
    } else if (op1t.equals(FloatType.getInstance()) || op2t.equals(FloatType.getInstance())) {
      return FloatType.getInstance();
    } else {
      return UnknownType.getInstance();
    }
  }
}