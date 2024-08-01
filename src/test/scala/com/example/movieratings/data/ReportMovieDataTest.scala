package com.example.movieratings.data

import org.scalatest.funsuite.AnyFunSuiteLike

import scala.math.Ordered.orderingToOrdered

class ReportMovieDataTest extends AnyFunSuiteLike {

  test("testOrdering by average review") {
    val rmd1 = ReportMovieData("b", "", BigDecimal(1), 200)
    val rmd2 = ReportMovieData("a", "", BigDecimal(2), 200)

    assert(rmd2 < rmd1)
  }

  test("testOrdering by movie name with equal score") {
    val rmd1 = ReportMovieData("a", "", BigDecimal(1), 200)
    val rmd2 = ReportMovieData("b", "", BigDecimal(1), 200)

    assert(rmd2 < rmd1)
  }
}
