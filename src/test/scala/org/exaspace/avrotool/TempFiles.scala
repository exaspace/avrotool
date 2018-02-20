package org.exaspace.avrotool

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths}


trait TempFiles {

  def withTempFile[T](contents: String)(func: (Path) => T): T = {
    withTempFile(contents.getBytes(StandardCharsets.UTF_8)) { path =>
      func(path)
    }
  }

  def withTempFile[T](contents: Array[Byte])(func: (Path) => T): T = {
    val f = File.createTempFile("schema", ".tmp")
    val p = Paths.get(f.getAbsolutePath)
    Files.write(p, contents)
    try {
      func(p)
    }
    finally {
      f.delete()
    }
  }

}
