// 
// (c) 2012 University of Luxembourg - Interdisciplinary Centre for 
// Security Reliability and Trust (SnT) - All rights reserved
//
// Author: Alexandre Bartel
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 2.1 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>. 
//

package de.upb.swt.soot.java.bytecode.frontend.apk.dexpler.tags;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 1997 - 2021 Raja Vallée-Rai, Kadiray Karakaya and Others
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

import de.upb.swt.soot.java.bytecode.frontend.apk.tag.Tag;

public class LongOpTag implements Tag {
  public String getName() {
    return "LongOpTag";
  }

  public byte[] getValue() {
    byte[] b = new byte[1];
    b[0] = 0;
    return b;
  }

}
