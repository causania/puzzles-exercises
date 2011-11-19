package org.koderama.railroad
package clients

import org.specs2.Specification

/**
 * Defines the general spec for the [[MessagesReading]] implementation.
 *
 * @author alejandro@koderama.com
 */
class MessagesReadingSpec extends Specification {

  val m = new MessagesReading {
    val messages = read("src/test/resources/messages.dat")
  }

  def is =
    "This is a specification to check the general behavior of the MessagesReading class" ^
      p ^
      "The MessagesReading should" ^
      "Return all lines if there are several lines for the same key" ! e1 ^
      "One line for one key" ! e2 ^
      "Return None if the key does not exits" ! e3

  end

  def e1 = m.messages.get("key1").get must equalTo("line1\nline2")

  def e2 = m.messages.get("key3").get must equalTo("hi")

  def e3 = m.messages.get("key21") must equalTo(None)

}