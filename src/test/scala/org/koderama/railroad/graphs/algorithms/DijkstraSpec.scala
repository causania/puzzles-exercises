package org.koderama.railroad
package graphs.algorithms

import graphs.IllegalGraphActionException
import maps.RoutingGraph
import org.specs2.Specification


/**
 * Defines the general spec for the [[Dijkstra]] implementation.
 *
 * @author alejandro@koderama.com
 */
class DijkstraSpec extends Specification {

  val graph = RoutingGraph("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7, DF2, FG2")
  val d = new Dijkstra(graph)


  def is =
    "This is a specification to check the general behavior of the Dijkstra class" ^
      p ^
      "The Dijkstra with a graph %s should".format(graph) ^
      "Return an empty list if there is no path D-A" ! e1 ^
      "Return a List(A) for same start/end node A-A" ! e2 ^
      "Return a path F-G for F-G" ! e3 ^
      "Return a path D-F-G for D-G" ! e4 ^
      "Throw an IllegalGraphActionException if the nodes are not in the graph" ! e5

  end

  def e1 = d.findShortestPath("D", "A") must equalTo(Nil)

  def e2 = d.findShortestPath("A", "A") must equalTo(List("A"))

  def e3 = d.findShortestPath("F", "G") must equalTo(List("F", "G"))

  def e4 = d.findShortestPath("D", "G") must equalTo(List("D", "F", "G"))

  def e5 = d.findShortestPath("X", "Y") must throwA[IllegalGraphActionException]
}