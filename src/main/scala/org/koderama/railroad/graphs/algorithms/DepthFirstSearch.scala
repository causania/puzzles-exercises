package org.koderama.railroad
package graphs.algorithms

import graphs.{IllegalGraphActionException, LabeledGraph}


/**
 * Depth-first search (DFS) is an algorithm for traversing or searching a tree, tree structure, or graph.
 * One starts at the root (selecting some node as the root in the graph case)
 * and explores as far as possible along each branch before backtracking.
 *
 * There are different algorithms to traver a graph.
 * Even a matrix representation can be used, where position i,j represents the edge for nodes i and j.
 *
 * This implementation is a more general implementation of the DFS, it allows several searching heuristics.
 *
 *
 * This class is thread safe as long as the given graph is.
 *
 * @see http://en.wikipedia.org/wiki/Depth-first_search
 *
 *
 * @param graph The related graph where the search will be performed. Notice that it's expected a graph where nodes
 * have an unique label.
 * @tparam N the type of the node value
 * @tparam E the type of the edge value. Values are expected to be numbers in order to be able to calculate the cost.
 *
 * @author alejandro@koderama.com
 */
class DepthFirstSearch[N, E <: Int](graph: LabeledGraph[N, E]) {

  /**
   * Searches for all the paths that matches the given criteria (levels, cost, etc).
   * Return an empty list if there is no path.
   *
   * Notice that the same node can be repeated. E.g.: C-D-C and C-D-C-D-C might be two possible routes.
   * Whenever one or both are returned depends on the provided parameters.
   *
   * @param start The value of the node where the search should starts
   * @param goal The value of the node where the search should ends
   * @param minLevels the min length of the path. Each level is a jump. Use -1 to no consider it.
   * @param maxLevels the max length of the path. Each level is a jump. Use -1 to no consider it.
   * If this is -1 maxCost can't be -1
   * @param maxCost the max cost of the path. It's calculated using the edge's labels. Use -1 to no consider it
   * If this is -1 maxLevels can't be -1
   *
   * (default value).
   *
   * @throws IllegalGraphActionException if one of the provided nodes do not belongs to the graph.
   * Additionally the exception will be thrown if maxLevels and maxCost are -1 (which means go forever)
   */
  def findPaths(start: N, goal: N, minLevels: Int, maxLevels: Int, maxCost: Int): List[List[N]] = {
    if (maxLevels == -1 && maxCost == -1) {
      throw new IllegalGraphActionException("maxLevels and maxCost can't be both -1. An ending criteria must be provided")
    }
    
    // Check that the nodes exist
    (graph.findNode(start), graph.findNode(goal)) match {
      case (Some(root), Some(target)) => {
        search(root.value, target.value, minLevels, maxLevels, maxCost)
      }
      case _ => throw new IllegalGraphActionException("No souch route. Node %s or %s are not in the graph".format(start, goal))
    }
  }

  private def search(start: N, goal: N, minLevels: Int, maxLevels: Int, maxCost: Int): List[List[N]] = {

    //TODO: use tail recursion or, probably better, an iterative approach with a stack if the graph might be too large.

    // Just in case there is no path to the goal.
    val maxToExplore = graph.edges.length
    var explored = 0
    var goalFound = false

    def explore(current: N, cost: Int = 0, path: List[N] = List()): List[List[N]] = {
      // Notice that depending on the params a path like C-D-C and
      // a path C-D-C-D-C might be also allowed. Then it's more tricky than just doing a DFS
      val exhausted = maxToExplore == explored && !goalFound
      val notValidPath = (path.length > maxLevels && maxLevels != -1) || (cost > maxCost && maxCost != -1)

      if (notValidPath || exhausted) {
        Nil
      } else {
        val edges = graph.findEdges(current)
        val children = for (e <- edges) yield {
          explore(e.to.value, cost + e.value, path ::: List(current))
        }
        explored += 1

        if (current == goal && path.length > 0 && path.length >= minLevels) {
          goalFound = true
          (path ::: List(current)) :: children.foldLeft(List[List[N]]())(_ ::: _)
        } else {
          children.foldLeft(List[List[N]]())(_ ::: _)
        }
      }
    }

    explore(start)
  }


}

