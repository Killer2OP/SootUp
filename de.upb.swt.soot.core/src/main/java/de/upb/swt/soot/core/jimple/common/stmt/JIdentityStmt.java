package de.upb.swt.soot.core.jimple.common.stmt;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1999-2020 Patrick Lam, , Linghui Luo, Christian Brüggemann
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

import de.upb.swt.soot.core.jimple.basic.JimpleComparator;
import de.upb.swt.soot.core.jimple.basic.Local;
import de.upb.swt.soot.core.jimple.basic.StmtPositionInfo;
import de.upb.swt.soot.core.jimple.common.ref.IdentityRef;
import de.upb.swt.soot.core.jimple.visitor.StmtVisitor;
import de.upb.swt.soot.core.types.Type;
import de.upb.swt.soot.core.util.Copyable;
import de.upb.swt.soot.core.util.printer.StmtPrinter;
import javax.annotation.Nonnull;

public final class JIdentityStmt extends AbstractDefinitionStmt implements Copyable {

  public JIdentityStmt(
      @Nonnull Local local,
      @Nonnull IdentityRef identityValue,
      @Nonnull StmtPositionInfo positionInfo) {
    super(local, identityValue, positionInfo);
  }

  @Override
  public String toString() {
    return getLeftOp().toString() + " := " + getRightOp().toString();
  }

  @Override
  public void toString(@Nonnull StmtPrinter up) {
    getLeftOp().toString(up);
    up.literal(" := ");
    getRightOp().toString(up);
  }

  @Override
  public void accept(@Nonnull StmtVisitor sw) {
    sw.caseIdentityStmt(this);
  }

  public Type getType() {
    return getLeftOp().getType();
  }

  @Override
  public boolean equivTo(Object o, @Nonnull JimpleComparator comparator) {
    return comparator.caseIdentityStmt(this, o);
  }

  @Override
  public int equivHashCode() {
    return getLeftOp().equivHashCode() + 31 * getRightOp().equivHashCode();
  }

  @Nonnull
  public JIdentityStmt withLocal(@Nonnull Local local) {
    return new JIdentityStmt(local, (IdentityRef) getRightOp(), getPositionInfo());
  }

  @Nonnull
  public JIdentityStmt withIdentityValue(@Nonnull IdentityRef identityValue) {
    return new JIdentityStmt((Local) getLeftOp(), identityValue, getPositionInfo());
  }

  @Nonnull
  public JIdentityStmt withPositionInfo(@Nonnull StmtPositionInfo positionInfo) {
    return new JIdentityStmt((Local) getLeftOp(), (IdentityRef) getRightOp(), positionInfo);
  }
}
