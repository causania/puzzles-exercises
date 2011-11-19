package org.koderama.railroad
package graphs.algorithms

import collection.mutable.PriorityQueue
import collection.mutable.Map
import graphs.{IllegalGraphActionException, LabeledGraph}

/**
 * For a given source vertex (node) in the graph, the algorithm finds the path with lowest cost
 * (i.e. the shortest path) between that vertex and every other vertex. It can also be used for
 * finding costs of shortest paths from a single vertex to a single destination vertex by stopping
 * the algorithm once the shortest path to the destination vertex has been determined.
 *
 * This class is thread safe as long as the related graph is.
 *
 * @see http://en.wikipedia.org/wiki/Dijkstra's_algorithm
 *
 * @param graph The related graph where the search will be performed. Notice that it's expected a graph where nodes
 * have an unique label.
 * @tparam N the type of the node value
 * @tparam E the type of the edge value. Values are expected to be numbers in order to be able to calculate the cost.
 *
 * @author alejandro@koderama.com
 */
class Dijkstra[N, E <: Int](graph: LabeledGraph[N, E]) {
  private type TagClass = (Int, Option[graph.Node])

  /**
   * Gives the shortest path between the start and the goal.
   * If there is no path, and empty list is returned.
   *
   * @param start The value of the node where the search should starts
   * @param goal The value of the node where the search should ends
   *
   * @throws IllegalGraphActionException if the provided one of the provided nodes do not belongs to the graph.
   */
  def findShortestPath(start: N, goal: N): List[N] = {
    (graph.findNode(start), graph.findNode(goal)) match {
      case (Some(root), Some(target)) => {
        search(root, target)
      }
      case _ => throw new IllegalGraphActionException("No souch route. Node %s or %s are not in the graph".format(start, goal))
    }
  }


  private def search(s: graph.Node, t: graph.Node): List[N] = {
    // The tags contains data of the current search
    val tags: Map[N, TagClass] = Map() ++= graph.nodes.map(t => (t.value ->(Int.MaxValue, None)))

    implicit def nodeToInt(v: graph.Node) = v.value

    // Required for the priority queue in order to return the current shortest path
    implicit def nodeToOrdered(thisNode: graph.Node): Ordered[graph.Node] = new Ordered[graph.Node] {
      def compare(thatNode: graph.Node): Int = {
        tags.get(thatNode.value).get.asInstanceOf[TagClass]._1 - tags.get(thisNode.value).get.asInstanceOf[TagClass]._1 match {
          case d if (d > 0) => 1
          case d if (d < 0) => -1
          case _ => 0
        }
      }
    }

    // This is the main part of the algorithm
    tags += (s.value ->(0, None))

    val Q = new PriorityQueue[graph.Node]()
    Q ++= graph.nodes // add all to the queue

    while (!Q.isEmpty && Q.head != t) {
      val u = Q.dequeue() // dequeues the node with the most promising path
      val wu = tags.get(u.value).get._1

      graph.findEdges(u).foreach(e => {
        val v = e.to
        val w = e.value
        val wv = tags.get(v.value).get._1

        // Is the new route better?
        if (wv > wu + w) {
          tags += (v.value ->(wu + w, Some(u)))
        }
      })
    }

    generatePath(s, t, tags)
  }

  private def generatePath(s: graph.Node, t: graph.Node, tags: Map[N, TagClass]): List[N] = {
    if (s == t) {
      List(s.value)
    } else {
      val tag = tags.get(t.value).get.asInstanceOf[TagClass]
      tag match {
        case (w, Some(v)) => generatePath(s, v, tags) ::: List(t.value)
        case (w, None) => Nil
      }
    }
  }


}