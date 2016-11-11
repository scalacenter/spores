package scala.spores.spark.run.transitive

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import scala.spores.spark.TestUtil._
import scala.spores.util.PluginFeedback.NonSerializableType

@RunWith(classOf[JUnit4])
class TransitiveNegSpec {
  @Test
  def `Depth 1: Non serializable fields in classes are detected`(): Unit = {
    expectError(
      NonSerializableType("Foo", "value bar", "Bar")
    ) {
      """
        |import scala.spores._
        |class Bar
        |class Foo(val bar: Bar) extends Serializable
        |val foo = new Foo(new Bar)
        |spore {
        |  val captured = foo
        |  () => captured
        |}
      """.stripMargin
    }
  }

  @Test
  def `Depth 2: Non serializable fields in classes are detected`(): Unit = {
    expectError(
      NonSerializableType("Bar", "value baz", "Baz")
    ) {
      """
        |import scala.spores._
        |abstract class Baz
        |class Bar(val baz: Baz) extends Serializable
        |class Foo(val bar: Bar) extends Serializable
        |val foo = new Foo(new Bar(new Baz {}))
        |spore {
        |  val captured = foo
        |  () => captured
        |}
      """.stripMargin
    }
  }

  @Test
  def `Depth 3: Non serializable fields in classes are detected`(): Unit = {
    expectError(
      NonSerializableType("Baz", "value baz2", "Baz2")
    ) {
      """
        |import scala.spores._
        |abstract class Baz2
        |abstract class Baz(val baz2: Baz2) extends Serializable
        |class Bar(val baz: Baz) extends Serializable
        |class Foo(val bar: Bar) extends Serializable
        |val foo = new Foo(new Bar(new Baz(new Baz2 {}) {}))
        |spore {
        |  val captured = foo
        |  () => captured
        |}
      """.stripMargin
    }
  }
}