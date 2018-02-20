package org.exaspace.avrotool

import scala.collection.mutable


class TestingConsole extends ConsoleOutput {

  private val sbStdout = new StringBuilder
  private val sbStderr = new StringBuilder
  private val bytes = mutable.ArrayBuffer.empty[Byte]

  override def print(x: Any): Unit = sbStdout.append(x)

  override def println(x: Any): Unit = sbStdout.append(x).append("\n")

  override def debug(x: Any): Unit = sbStderr.append(x).append("\n")

  override def write(xs: Array[Byte]): Unit = bytes ++= xs

  def stdout: String = sbStdout.toString

  def stderr: String = sbStderr.toString

  def bytesWritten: Array[Byte] = bytes.toArray

}
