package org.koderama.railroad
package graphs

/**
 * This class is intended to represent a very general structure for a graph.
 *
 * There are several kind of graphs (directed, mixed, multigraph, etc) and each one has a different logic.
 * Then, this class leaves most of the implementation details up to the child classes.
 *
 *
 * @tparam Node the type of the node for the graph
 * @tparam Edge the type of the edge for the graph
 *
 * @author alejandro@koderama.com
 */
abstract class AbstractGraph {
  type Edge
  type Node <: AbstractNode
  type Graph <: AbstractGraph

  trait AbstractNode {

  }

  /**Returns all the nodes of the graph */
  def nodes: List[Node]

  /**Returns all the edges of the graph */
  def edges: List[Edge]

  /**Adds a new node to the graph */
  def +=(node: => Node): Graph

  /**Adds a new edge to the graph */
  def <<(edge: => Edge): Graph

  /**
   * Finds all the edges which have the node as the origin
   *
   * @param from the node which is the origin of the relation
   */
  def findEdges(from: => Node): List[Edge]

  override def toString = {
    "Nodes: %s\nEdges: %s".format(nodes, edges)
  }
}









