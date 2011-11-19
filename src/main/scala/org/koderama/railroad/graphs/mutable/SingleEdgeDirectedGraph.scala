package org.koderama.railroad
package graphs.mutable

import collection.mutable.Map
import graphs.{IllegalGraphActionException, LabeledGraph}

/**
 * Defines a graph which can only contains a single directed edge from A to B.
 * Notice that it means that there might be an edge from B->A.
 *
 * Instances of this class are not thread safe.
 *
 * @param defaultValue The default value for a new edge
 * @tparam N the type of the node value
 * @tparam E the type of the edge value.
 *
 * @author alejandro@koderama.com
 */
class SingleEdgeDirectedGraph[N: Manifest, E](defaultValue: E) extends LabeledGraph[N, E] with MutableGraphState[N, E] {
  type Edge = LabeledEdge
  type Node = LabeledNode
  type Graph = SingleEdgeDirectedGraph[N, E]

  protected def newEdge(from: Node, to: Node): Edge = LabeledEdge(from, to, defaultValue)

  protected override def newEdge(from: Node, to: Node, value: E): Edge = LabeledEdge(from, to, value)

  override def +=(node: => Node): Graph = {
    nodesMap += (node.value -> node)
    this
  }

  override def +=(nodeValue: N): Graph = {
    nodesMap += (nodeValue -> new LabeledNode(nodeValue))
    this
  }

  override def <<(edge: => Edge): Graph = {
    this <<(edge.from.value, edge.to.value, edge.value)
  }

  override def findNode(nodeValue: N) = nodesMap.get(nodeValue)

  override def findEdges(from: N): List[Edge] = {
    edgesMap.get(from) match {
      case Some(v) => v.values.toList
      case None => Nil
    }
  }

  override def findEdges(from: => Node): List[Edge] = {
    findEdges(from.value)
  }

  /**
   * Finds the edge for the given related with the given nodes.
   *
   * @param from the node which is the source of the relation
   * @param to the node which is the destination of the relation
   */
  def findEdge(from: => Node, to: => Node): Option[Edge] = {
    findEdge(from.value, to.value)
  }

  /**
   * Adds a new edge for the given triple
   *
   * @param edge (source node value, destination node value, edge value)
   */
  def <<(edge: (N, N, E)): Graph = {
    (findNode(edge._1), findNode(edge._2)) match {
      case (Some(source), Some(target)) => {
        edgesMap.get(source.value) match {
          case Some(m) => m += (target.value -> (source ->(target, edge._3)))
          case None => edgesMap += (source.value -> Map(target.value -> (source ->(target, edge._3))))
        }
      }
      case _ => throw new IllegalGraphActionException("Provided nodes must exits in the graph")
    }

    this
  }

  /**
   * Finds the node based on its value.
   *
   * @param nodeValue the value for the node to find
   */
  def findEdge(from: N, to: N): Option[Edge] = {
    edgesMap.get(from) match {
      case Some(m) => m.get(to)
      case None => None
    }
  }

}