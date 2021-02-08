package de.upb.swt.soot.callgraph.spark.pag;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2002-2021 Ondrej Lhotak, Kadiray Karakaya and others
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

import org.jgrapht.graph.DefaultEdge;

public class SparkEdge extends DefaultEdge {
  private EdgeType edgeType;
  private SparkVertex source;
  private SparkVertex target;

  public SparkEdge(SparkVertex source, SparkVertex target, EdgeType edgeType) {
    this.source = source;
    this.target = target;
    this.edgeType = edgeType;
  }

  public EdgeType getEdgeType() {
    return edgeType;
  }

  @Override
  public Object getSource() {
    return source;
  }

  @Override
  public Object getTarget() {
    return target;
  }

  @Override
  public String toString() {
    return "(" + getSource() + " : " + getTarget() + " : " + edgeType + ")";
  }
}
