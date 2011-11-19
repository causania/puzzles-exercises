package org.koderama.railroad
package maps

import graphs.algorithms.{Dijkstra, DepthFirstSearch}


/**
 * Represents the map for
 *
 * @param graph the graph representing the cities and the routes between cities
 *
 * @author alejandro@koderama.com
 */
class RailRoadMap(graph: RoutingGraph) {

  private val finder = new Dijkstra(graph)
  private val transverser = new DepthFirstSearch(graph)

  /**
   * Calculates the cost for the given route.
   *
   * @param cities The list of cities representing the route from cities.head to cities.last
   *
   * @throws RailRoadMapException if there is no route for some combination of cities
   */
  def calculateCost(cities: List[String]) = {
    (cities.zipWithIndex foldLeft 0) {
      case (acc, (city, index)) if index > 0 => {
        val resultEdge = graph.findEdge(cities(index - 1), city)
        resultEdge match {
          case Some(edge) => acc + edge.value
          case None => throw new RailRoadMapException("No souch route")
        }
      }
      case _ => 0
    }
  }

  /**
   * Searches for all the routes that matches the given criteria (levels, cost, etc).
   * Return an empty list if there is no route.
   *
   * Notice that the same node can be repeated. E.g.: C-D-C and C-D-C-D-C might be two possible routes.
   * Whenever one or both are returned depends on the provided parameters.
   *
   * @param fromCity The name of the city where the search should starts
   * @param toCity The name of the city where the search should ends
   * @param minStops the min amount of stops for the route. Use -1 to no consider it (default value).
   * @param maxStops the max amount of stops for the route. Use -1 to no consider it(default to the amount of routes).
   * If this is -1 maxDistance can't be -1
   * @param maxDistance the max distance to travel. Use -1 to no consider it (default value).
   * If this is -1 maxStops can't be -1
   *
   * @throws IllegalGraphActionException if one of the provided cities do not belongs to the map.
   * Additionally the exception will be thrown if minStops and maxDistance are -1 (which means go forever)
   */
  def findRoute(fromCity: String, toCity: String, minStops: Int = -1, maxStops: Int = graph.edgesCount,
                maxDistance: Int = -1): List[List[String]] = {
    transverser.findPaths(fromCity, toCity, minStops, maxStops, maxDistance)
  }

  /**
   * Find the shortest route from A to B. If there are several routes, just one is returned.
   *
   * @param fromCity The name of the city where the search should starts
   * @param toCity The name of the city where the search should ends
   *
   * @throws IllegalGraphActionException if one of the provided cities do not belongs to the map.
   */
  def findShortestRoute(fromCity: String, toCity: String): List[String] = {
    if (fromCity == toCity) {

      //TODO: it might be possible to add an heuristic to Dijkstra to manage this

      // With the same nodes Dijkstra does not work. Executing search in parallel
      // Searching for the same city should be very rare for a train app...
      val edges = graph.findEdges(fromCity)
      val result = edges.par.map(e => (e, finder.findShortestPath(e.to.value, toCity)))

      result match {
        case x if !x.isEmpty => {
          fromCity :: result.min(new Ordering[(graph.Edge, List[String])] {
            def compare(x: (graph.Edge, List[String]), y: (graph.Edge, List[String])): Int = {
              (calculateCost(x._2) + x._1.value) compare (calculateCost(y._2) + y._1.value)
            }
          })._2
        }
        case _ => Nil
      }
    }
    else {
      finder.findShortestPath(fromCity, toCity)
    }
  }

  override def toString = graph.toString

}

object RailRoadMap {

  /**
   * Factory method to create a map based on a string.
   *
   * @see [[RoutingGraph.apply(String]]
   * @throws RailRoadMapDefinitionException if there is a problem parsing the definition
   *
   */
  def apply(definition: String) = {
    val g = RoutingGraph(definition)
    new RailRoadMap(g)
  }

}

