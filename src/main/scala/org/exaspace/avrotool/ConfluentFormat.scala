package org.exaspace.avrotool

import java.nio.ByteBuffer


case class ConfluentBinary(magic: Byte, schemaId: Int, avroBytes: Array[Byte]) {

  def asBytes: Array[Byte] = {
    val bb = ByteBuffer.allocate(5 + avroBytes.length)
    val magic: Byte = 0
    bb.put(magic)
    bb.putInt(schemaId)
    bb.put(avroBytes)
    bb.array
  }

}

object ConfluentBinary {

  def apply(bytes: Array[Byte]): ConfluentBinary = {

    val byteBuffer = ByteBuffer.wrap(bytes)
    val magic = byteBuffer.get()
    if (magic != 0x0) sys.error("Magic byte must be zero")

    val schemaId = byteBuffer.getInt()

    val length = byteBuffer.limit() - 5
    val avroBytes = byteBuffer.array().slice(byteBuffer.position(), byteBuffer.limit())

    ConfluentBinary(magic, schemaId, avroBytes)
  }

}
