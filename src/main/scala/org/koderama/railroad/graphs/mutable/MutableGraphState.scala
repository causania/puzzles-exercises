package org.koderama.railroad
package graphs.mutable

import collection.mutable.Map

/**
 * Defines a container for the graph object.
 * Instances of this class are mutable, then it's not thread safe.
 *
 * @author alejandro@koderama.com
 */
trait MutableGraphState[N, E] {
  type Edge
  type Node

  /** Map containing all the nodes as N->Node entries */
  protected val nodesMap: Map[N, Node] = Map()
  /** Map containing all the edges as N->N->Edge entries */
  protected val edgesMap: Map[N, Map[N, Edge]] = Map()

  def nodes = nodesMap.values.toList

  def edges = edgesMap.values.map(_.values.toList).foldLeft(List[Edge]())((b, a) => a ::: b)

  def edgesCount = edgesMap.values.map(_.size).reduce(_ + _)

}