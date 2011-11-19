package org.koderama.railroad
package graphs.algorithms

import org.specs2.Specification
import maps.RoutingGraph
import graphs.IllegalGraphActionException

/**
 * Defines the general spec for the [[DepthFirstSearch]].
 * Notice that it's not strictly a DFS, it's more general.
 *
 * @author alejandro@koderama.com
 */
class DepthFirstSearchSpec extends Specification {

  val graph = RoutingGraph("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7, DF2, FG2")
  val dfs = new DepthFirstSearch(graph)


  def is =
    "This is a specification to check the general behavior of the DepthFirstSearch class" ^
      p ^
      "The DepthFirstSearch with a graph %s should".format(graph) ^
      "Return an empty list if there is no path D-A" ! e1 ^
      "Return an empty list if there is no path for same start/end node A-A" ! e2 ^
      "Return a single path for F-G -> F-G" ! e3 ^
      "Return a single path for C-C max cost 10" ! e4 ^
      "Return 3 paths for A-C with 4 stops" ! e5 ^
      "Throw an IllegalGraphActionException if the nodes are not in the graph" ! e6 ^
  "Throw an IllegalGraphActionException if maxCost and maxLevels are -1" ! e7
  end

  def e1 = dfs.findPaths("D", "A", -1, -1, 10) must equalTo(Nil)

  def e2 = dfs.findPaths("A", "A", -1, -1, 10) must equalTo(Nil)

  def e3 = dfs.findPaths("F", "G", -1, -1, 30) must equalTo(List(List("F", "G")))

  def e4 = dfs.findPaths("C", "C", -1, -1, 10) must equalTo(List(List("C", "E", "B", "C")))

  def e5 = dfs.findPaths("A", "C", 4, 4, 50).length must equalTo(3)

  def e6 = dfs.findPaths("X", "Y", 4, 4, 50) must throwA[IllegalGraphActionException]
  def e7 = dfs.findPaths("F", "G", -1, -1, -1) must throwA[IllegalGraphActionException]

}