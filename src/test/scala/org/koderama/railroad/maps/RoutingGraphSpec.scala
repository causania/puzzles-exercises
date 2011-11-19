package org.koderama.railroad
package maps

import org.specs2.Specification

/**
 * Defines the general spec for the [[RoutingGraph]] implementation.
 *
 * @author alejandro@koderama.com
 */
class RoutingGraphSpec extends Specification {

  val graph = RoutingGraph("AB5, BC4, CD8, DF8")

  def is =
    "This is a specification to check the general behavior of the RoutingGraph class" ^
      p ^
      "The RoutingGraph with a graph %s should".format(graph) ^
      "Have 5 nodes" ! e1 ^
      "Have 4 edges" ! e2 ^
      "Contains an edge from C-D of value 8" ! e3 ^
      "Not contains an edge from X-D" ! e4 ^
      "Contains a node A" ! e5 ^
      "Not contains a node X" ! e6 ^
      "Throw an RailRoadMapDefinitionException for an invalid graph definition" ! e7

  end

  def e1 = graph.nodes.length must equalTo(5)

  def e2 = graph.edges.length must equalTo(4)

  def e3 = graph.findEdge("C", "D").get.value must equalTo(8)

  def e4 = graph.findEdge("X", "D") must equalTo(None)

  def e5 = graph.findNode("A").get.value must equalTo("A")

  def e6 = graph.findNode("X") must equalTo(None)

  def e7 = RoutingGraph("XXX- YYY") must throwA[RailRoadMapDefinitionException]

}