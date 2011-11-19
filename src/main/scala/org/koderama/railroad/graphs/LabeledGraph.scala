package org.koderama.railroad
package graphs

/**
 * A graph representation where all nodes and edges are labeled (e.g.: with a weight)
 *
 * @see AbstractGraph
 *
 * @author alejandro@koderama.com
 */
abstract class LabeledGraph[N, E] extends AbstractGraph {
  type Edge <: LabeledEdge
  type Node <: LabeledNode

  case class LabeledEdge(origin: Node, dest: Node, label: E) {
    /**Represents the origin of the relation */
    def from = origin

    /**Represents the destination of the relation */
    def to = dest

    /**The associated value of the edge */
    def value =  label

    override def toString = "%s->%s (%s)".format(from.toString, to.toString, value.toString)
  }

  /**
   * This class represents a node in the graph with an associated value.
   *
   * This class redefines the [[Object.equals]] and [[Object.hashCode]] in order to determine if 2 nodes are the
   * same based on the value.
   */
  class LabeledNode(label: N) extends AbstractNode {
    self: Node =>
    /**
     * Creates an edge to the node
     *
     * @param node the target node
     * @param value the value for the new edge
     */
    def ->(node: Node, value: E): Edge = {
      newEdge(this, node, value)
    }

    /**The associated value of the node */
    def value = label

    override def toString = label.toString

    override def hashCode() = value.hashCode()

    override def equals(obj: Any) = {
      obj.isInstanceOf[LabeledNode] && obj.asInstanceOf[LabeledNode].value == this.value
    }
  }

  /**
   * Factory method to create a new edge for the given parameters.
   *
   * @param from source node
   * @param to target node
   * @param value the value associated to the edge
   */
  protected def newEdge(from: Node, to: Node, value: E): Edge

  /**
   * Adds a new node based on the passed value
   *
   * @param nodeValue the value for the new node
   */
  def +=(nodeValue: N): Graph

  /**
   * Finds the node based on its value.
   *
   * @param nodeValue the value for the node to find
   */
  def findNode(nodeValue: N): Option[Node]

  /**
   * Finds all the edges for the given node value
   *
   * @param nodeValue the value for the node to find
   */
  def findEdges(nodeValue: N): List[Edge]

}