package scala.spores.serialization.run.capture

import java.io.{Externalizable, ObjectInput, ObjectOutput}

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import scala.spores._

@RunWith(classOf[JUnit4])
class SerializableSpec {
  @Test
  def `A spore that does not capture anything can be serialized`(): Unit = {
    val s: Spore[String, String] = spore {
      case s: String => s
    }
    assert(s("Hello, World!") == "Hello, World!")
  }

  @Test
  def `A case class is serializable`(): Unit = {
    final case class A(number: Int)
    val a = A(1)
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `A case object is serializable`(): Unit = {
    case object A { val s = 1 }
    val a = A
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `An object extending Serializable is serializable`(): Unit = {
    object A extends Serializable { val s = 1 }
    val a = A
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `An abstract class extending Serializable is serializable`(): Unit = {
    sealed abstract class A extends Serializable { val i: Int }
    val a = new A { val i = 1 }
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `A class extending Serializable is serializable`(): Unit = {
    sealed class A(val i: Int) extends Serializable
    val a = new A(1)
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `An object extending Externalizable is serializable`(): Unit = {
    object A extends Externalizable {
      val s = 1
      // These are stubs
      override def readExternal(objectInput: ObjectInput): Unit = ???
      override def writeExternal(objectOutput: ObjectOutput): Unit = ???
    }
    val a = A
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `An abstract class extending Externalizable is serializable`(): Unit = {
    sealed abstract class A extends Externalizable { val i: Int }
    val a = new A {
      val i = 1
      // These are stubs
      override def readExternal(objectInput: ObjectInput): Unit = ???
      override def writeExternal(objectOutput: ObjectOutput): Unit = ???
    }
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }

  @Test
  def `A class extending Externalizable is serializable`(): Unit = {
    sealed class A(val i: Int) extends Externalizable {
      // These are stubs
      override def readExternal(objectInput: ObjectInput): Unit = ???
      override def writeExternal(objectOutput: ObjectOutput): Unit = ???
    }
    val a = new A(1)
    val s = spore {
      () => capture(a)
    }
    assert(s() == a)
  }
}
