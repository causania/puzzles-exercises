package org.koderama.railroad

/**
 * Defines a general runtime exception in the system.
 * Custom runtime exception should inherit from this class.
 *
 * @author alejandro@koderama.com
 */
class RailRoadRuntimeException(message: String, e: Exception = null) extends RuntimeException(message) {

}

package maps {

/**
 * Represents whenever a given action is illegal for a given map.
 * E.g.: searching for cities that are not part of the map.
 *
 * @author alejandro@koderama.com
 */
class RailRoadMapException(message: String) extends RailRoadRuntimeException(message) {

}

/**
 * Represents whenever there is a problem with the graph definition.
 *
 * @author alejandro@koderama.com
 */
class RailRoadMapDefinitionException(message: String, e: Exception) extends RailRoadRuntimeException(message, e) {

}

}

package graphs {

/**
 * Represents whenever a given action is illegal for a graph.
 * E.g.: searching for nodes that are not part of the graph.
 *
 * @author alejandro@koderama.com
 */
class IllegalGraphActionException(message: String) extends RailRoadRuntimeException(message) {

}

}