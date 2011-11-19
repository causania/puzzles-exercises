package org.koderama.railroad
package clients

/**
 * Simple trait to include functionality related with the parsing of messages from different sources.
 *
 * @author alejandro@koderama.com
 */
trait MessagesReading {

  def messages: Map[String, String]

  /**
   * The given file should have the following format: KEY=TEXT
   * If the file has several lines with the same KEY, the texts will be appended.
   *
   * @param fileName the path of the file.
   * The file should be in the classpath or you should provide the full path
   */
  protected def read(fileName: String): Map[String, String] = {
    val map = new scala.collection.mutable.HashMap[String, String]()

    io.Source.fromFile(fileName).getLines().foreach(line => {
      line.split("=") match {
        case Array(k, v) => {
          map.get(k.trim) match {
            case Some(m) => map += (k.trim -> (m + "\n" + v))
            case None => map += (k.trim -> v)
          }
        }
        case _ => // do nothing
      }
    })

    new scala.collection.immutable.HashMap() ++ map
  }

}