package org.koderama.railroad
package maps

import graphs.mutable.SingleEdgeDirectedGraph

/**
 * Represents a graph of the cities and the routes.
 * Each node is a city and each edge is a route.
 *
 * The value of the node is the name of the city and the value of the edge the distance of the directed edge.
 * Notice that names are case sensitive.
 *
 * @author alejandro@koderama.com
 */
class RoutingGraph extends SingleEdgeDirectedGraph[String, Int](0)


object RoutingGraph {

  /**
   * Factory method to create a graph based on the given parameters.
   *
   * @param nodes a Set of cities
   * @param edges a List containing all the routes in the for of (from city, to city, distance)
   */
  def apply(nodes: Set[String], edges: List[(String, String, Int)]): RoutingGraph = {
    val graph = new RoutingGraph
    nodes.foreach(graph += _)
    edges.foreach(graph << _)

    graph
  }

  /**
   * Factory method to create a graph based on a string.
   * The format should be: AB5, BC6, etc
   * Where each item represents a route from X to Y with a value V.
   * In the example AB5 means a route from A to B with a distance of 5.
   *
   * Note: Just a single directed route is allowed. Then if AB5 and AB4 is provided, the last will be stored.
   *
   * @throws RailRoadMapDefinitionException if there is a problem parsing the definition
   *
   */
  def apply(definition: String): RoutingGraph = {

    try {
      val items = definition.split(',').map(_.trim())
      val edges = for (i <- items) yield (i.charAt(0).toString, i.charAt(1).toString, i.charAt(2).toString.toInt)
      val nodes = items.foldLeft(Set[String]())((s, i) => s + i.charAt(0).toString + i.charAt(1).toString)

      apply(nodes, edges.toList)
    } catch {
      case e: Exception =>
        throw new RailRoadMapDefinitionException("The definition of the map is not valid. %s".format(definition), e)
    }
  }
}



