package aoc2023.day1

import cats.effect.{IO, IOApp}
import cats.syntax.all._
import fs2.io.file.{Files, Path}
import fs2.text

object Main extends IOApp.Simple {
  private val input = Path("src/main/scala/aoc2023/day1/input")

  private val lines = Files[IO].readAll(input).through(text.utf8.decode[IO]).through(text.lines)

  private val firstSolution =
    lines
      .map { line =>
        val first = line.iterator.find(_.isDigit).map(_.asDigit).getOrElse(0)
        val last  = line.reverseIterator.find(_.isDigit).map(_.asDigit).getOrElse(0)
        (first * 10) + last
      }
      .compile
      .foldMonoid
      .flatMap(IO.println)

  private val secondSolution = {
    val digits: Map[String, Int] = List.range(0, 10).map(v => v.toString -> v).toMap ++
      Map(
        "one"   -> 1,
        "two"   -> 2,
        "three" -> 3,
        "four"  -> 4,
        "five"  -> 5,
        "six"   -> 6,
        "seven" -> 7,
        "eight" -> 8,
        "nine"  -> 9,
      )

    def firstIndex(haystack: String, needles: Set[String]): Option[String] =
      needles.toList
        .map(needle => needle -> haystack.indexOf(needle).some.filter(_ >= 0))
        .collect { case (needle, Some(pos)) => needle -> pos }
        .minByOption { case (_, pos) => pos }
        .map { case (needle, _) => needle }

    def lastIndex(haystack: String, needles: Set[String]): Option[String] =
      needles.toList
        .map(needle => needle -> haystack.lastIndexOf(needle).some.filter(_ >= 0))
        .collect { case (needle, Some(pos)) => needle -> pos }
        .maxByOption { case (_, pos) => pos }
        .map { case (needle, _) => needle }

    lines
      .map { line =>
        val first = firstIndex(line, digits.keySet).flatMap(found => digits.get(found)).getOrElse(0)
        val last  = lastIndex(line, digits.keySet).flatMap(found => digits.get(found)).getOrElse(0)
        (first * 10) + last
      }
      .compile
      .foldMonoid
      .flatMap(IO.println)
  }

  override def run: IO[Unit] = secondSolution

}
