package org.koderama.railroad
package clients

import maps.{RailRoadMap}

/**
 * Represents an input event in the console.
 *
 * @author alejandro@koderama.com
 */
sealed trait LineEvent

case class Line(value: String) extends LineEvent

case object EmptyLine extends LineEvent

case object EOF extends LineEvent


/**
 * This class defines a VERY basic client to be able to interact with the user.
 * The idea is that users might be able to input their own graph definition and run the related tests.
 *
 * @author alejandro@koderama.com
 */
object RailRoadConsoleClient extends App with MessagesReading {

  private var railMap = RailRoadMap("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7")

  override val messages = read("src/main/resources/messages.dat")

  // 1. Say hello
  welcome()

  // 2. Process input
  console {
    case EmptyLine =>
      false
    case EOF =>
      println("Ctrl-d")
      println(messages.get("bye").get)
      true
    case Line(s) =>
      processCommand(s)
    case _ =>
      false
  }

  /**
   * Prints the initial greetings message
   */
  protected def welcome() {
    println()
    List("welcome", "options", "commands").foreach(m => println(messages.get(m).get))
    println("\nCurrent graph:\n%s".format(railMap))
    println()
  }

  /**
   * Loops until the quit event is found.
   *
   * @param handler the object in charge of determine whenever or not the loop should end
   */
  protected def console(handler: LineEvent => Boolean) {
    var finished = false
    while (!finished) {
      val line = Console.readLine("railroad> ")
      if (line == null) {
        finished = handler(EOF)
      } else if (line.size == 0) {
        finished = handler(EmptyLine)
      } else if (line.size > 0) {
        finished = handler(Line(line))
      }
    }
  }

  /**
   * Process a given command. If there is an exception, a general message will be printed out.
   *
   * @param cmd the String representing the command to process
   */
  protected def processCommand(cmd: String): Boolean = {
    try {
      UserCommand(cmd).execute()
    }
    catch {
      case e: RailRoadRuntimeException =>
        println(e.getMessage)
        false
      case e: Exception =>
        println("%s (%s)".format(messages.get("error").get, e.getMessage))
        false
    }
  }


  /**
   * Represents an user request.
   *
   * @author alejandro@koderama.com
   */
  sealed trait UserCommand {

    /**
     * Perform the given command. This includes showing to the user any message
     */
    def execute(): Boolean
  }

  object UserCommand {
    // There might be several ways to do this. But, for now this more than ok
    def apply(cmd: String) = {
      cmd match {
        case "map" =>
          ShowMap
        case c if c startsWith "map:" =>
          UpdateMap(cmd)
        case c if c startsWith "cost:" =>
          RouteCost(cmd)
        case c if c startsWith "routes:" =>
          PossibleRoutes(cmd)
        case c if c startsWith "shortestroute:" =>
          ShortestRoute(cmd)
        case "q" =>
          Quit
        case _ =>
          NoCommand
      }
    }

    /**
     * A request to leave the interactive shell
     */
    case object Quit extends UserCommand {
      override def execute() = {
        println(messages.get("bye").get)
        true
      }
    }

    /**
     * Whenever the request from the user has no the expected format
     */
    case object NoCommand extends UserCommand {
      override def execute() = {
        List("nocommand", "commands").foreach(m => println(messages.get(m).get))
        false
      }
    }

    /**
     * Calculates the shortest path from A to B. Input example: "shortestroute:A-B"
     *
     * @param input the input related with the command
     */
    case class ShortestRoute(input: String) extends UserCommand {
      override def execute() = {
        val cities = input.split("shortestroute:")(1).split("-").toList
        val route = railMap.findShortestRoute(cities.head, cities.last)
        println(route.reduceLeft(_ + "-" + _))
        println(messages.get("totalcost").get + railMap.calculateCost(route))
        false
      }
    }

    /**
     * Calculates all possible routes for a path.
     * Input example: "routes:C-C -1 3 -1" where minStops is -1, maxStops is 3, maxDistance is -1
     *
     * @param input the input related with the command
     */
    case class PossibleRoutes(input: String) extends UserCommand {
      override def execute() = {
        val params = input.split("routes:")(1).split(" ")
        val from = params(0).split("-")(0)
        val to = params(0).split("-")(1)
        val routes = railMap.findRoute(from, to, params(1).toInt, params(2).toInt, params(3).toInt)
        routes.foreach(r => println(r.reduceLeft(_ + "-" + _)))
        println(messages.get("totalroutes").get + routes.length)
        false
      }
    }

    /**
     * Calculates the total cost for a path.
     * Input example: "cost:A-B-D"
     *
     * @param input the input related with the command
     */
    case class RouteCost(input: String) extends UserCommand {
      override def execute() = {
        println(railMap.calculateCost(input.split("cost:")(1).split("-").toList))
        false
      }
    }

    /**
     * Shows the current map
     */
    case object ShowMap extends UserCommand {
      override def execute() = {
        println(railMap)
        false
      }
    }

    /**
     * Updates the map with a new definition
     *
     * @param input the input related with the command
     */
    case class UpdateMap(input: String) extends UserCommand {
      override def execute() = {
        railMap = RailRoadMap(input.split("map:")(1))
        println(messages.get("newmap").get)
        println(railMap)
        false
      }
    }

  }

}



