package org.exaspace.avrotool


class TestingConsole extends ConsoleOutput {

  private val sbStdout = new StringBuilder
  private val sbStderr = new StringBuilder

  override def print(x: Any): Unit = sbStdout.append(x)

  override def println(x: Any): Unit = sbStdout.append(x).append("\n")

  override def debug(x: Any): Unit = sbStderr.append(x).append("\n")

  def stdout: String = sbStdout.toString

  def stderr: String = sbStderr.toString
}
