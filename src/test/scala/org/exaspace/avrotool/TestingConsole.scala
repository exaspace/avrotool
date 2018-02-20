package org.exaspace.avrotool


class TestingConsole extends ConsoleOutput {

  val stdout = new StringBuilder
  val stderr = new StringBuilder

  override def print(x: Any): Unit = stdout.append(x)

  override def println(x: Any): Unit = stdout.append(x).append("\n")

  override def debug(x: Any): Unit = stderr.append(x).append("\n")
}
