package org.koderama.railroad.maps

import org.specs2.Specification

/**
 * This Spec defines all the user acceptance criteria requested on the specifications (see README).
 * Note: this class does not mock the related classes because it's intended to be used as integration test.
 * 
 * @author alejandro@koderama.com
 */
class RoutingMapSpec extends Specification {

  val railMap = RailRoadMap("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7")

  def is =
    "This is a specification to check the general behavior of the RailRoadMap class" ^
      p ^
      "The RoutingMapSpec for a graph %s should".format(railMap) ^
      "1. The distance of the route A-B-C" ! e1 ^
      "2. The distance of the route A-D" ! e2 ^
      "3. The distance of the route A-D-C" ! e3 ^
      "4. The distance of the route A-E-B-C-D" ! e4 ^
      "5. The distance of the route A-E-D" ! e5 ^
      "6. The number of trips starting at C and ending at C with a maximum of 3\nstops." +
        "In the sample data below, there are two such trips: C-D-C (2\nstops). and C-E-B-C (3 stops)" ! e6 ^
      "7. The number of trips starting at A and ending at C with exactly 4 stops.\n" +
        "In the sample data below, there are three such trips: A to C (via B,C,D);\n" +
        "A to C (via D,C,D); and A to C (via D,E,B)" ! e7 ^
      "8. The length of the shortest route (in terms of distance to travel) from\nA to C." ! e8 ^
      "9. The length of the shortest route (in terms of distance to travel) from\nB to B." ! e9 ^
      "10. The number of different routes from C to C with a distance of less\nthan 30.  " +
        "In the sample data, the trips are: CDC, CEBC, CEBCDC, CDCEBC,\nCDEBC, CEBCEBC, CEBCEBCEBC." ! e10

  end

  def e1 = railMap.calculateCost(List("A", "B", "C")) must equalTo(9)

  def e2 = railMap.calculateCost(List("A", "D")) must equalTo(5)

  def e3 = railMap.calculateCost(List("A", "D", "C")) must equalTo(13)

  def e4 = railMap.calculateCost(List("A", "E", "B", "C", "D")) must equalTo(22)

  def e5 = railMap.calculateCost(List("A", "E", "D")) must throwA[RailRoadMapException]

  def e6 = railMap.findRoute("C", "C", maxStops = 3).length must equalTo(2)

  def e7 = railMap.findRoute("A", "C", 4, 4).length must equalTo(3)

  def e8 = railMap.calculateCost(railMap.findShortestRoute("A", "C")) must equalTo(9)

  def e9 = railMap.calculateCost(railMap.findShortestRoute("B", "B")) must equalTo(9)

  def e10 = railMap.findRoute("C", "C", maxDistance = 29).length must equalTo(7)
}