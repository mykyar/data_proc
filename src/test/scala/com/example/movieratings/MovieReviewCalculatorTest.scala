package com.example.movieratings

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.File

class MovieReviewCalculatorTest extends AnyWordSpecLike with Matchers with EitherValues {

  "testDoesEnoughReviews" should {
    "contains not enough reviews" in {
      val file = new File("src/test/resources/mv_couple_reviews.txt")
      assert(!MovieReviewCalculator.doesEnoughReviews(file))
    }

    "contains enough reviews" in {
      val file = new File("src/test/resources/mv_many_reviews.txt")
      assert(MovieReviewCalculator.doesEnoughReviews(file))
    }
  }


  "countAverageReviews" should {
    "should be able to count average " in {
      val file = new File("src/test/resources/mv_many_reviews.txt")
      val countResult = MovieReviewCalculator.countAverageReviews(file).right.value
      countResult.reviewsCount should be > 1000
      countResult.averageRating.toDouble shouldBe 3.83 +- 0.01
    }

    "should return error in the case of the non expected csv file format (empty file)" in {
      val file = new File("src/test/resources/empty_file.txt")
      MovieReviewCalculator.countAverageReviews(file).left.value.message should endWith ("Please check is this file correct")
    }

    "should return error in the case of the non expected csv file format (non empty file)" in {
      val file = new File("src/test/resources/non_correct.txt")
      MovieReviewCalculator.countAverageReviews(file).left.value.message should endWith ("Please check is this file correct")
    }
  }
}
