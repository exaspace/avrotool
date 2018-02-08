package org.exaspace.avrotool


sealed abstract class CompatibilityLevel(val value: String) {
  override def toString() = value
}

case object Full extends CompatibilityLevel("FULL")

case object Backward extends CompatibilityLevel("BACKWARD")

case object Forward extends CompatibilityLevel("FORWARD")

case object None extends CompatibilityLevel("NONE")

object CompatibilityLevel {

  def apply(s: String): CompatibilityLevel = s match {
    case Full.value => Full
    case Backward.value => Backward
    case Forward.value => Forward
    case None.value => None
  }

  def meetsLevel(actual: CompatibilityLevel, desired: CompatibilityLevel): Boolean = {
    desired match {
      case Full => actual == Full
      case Backward => actual == Full || actual == Backward
      case Forward => actual == Full || actual == Forward
      case None => true
    }
  }

  def meetsLevel(levels: Seq[CompatibilityLevel], desired: CompatibilityLevel): Boolean = {
    levels.forall(meetsLevel(_, desired))
  }

}
