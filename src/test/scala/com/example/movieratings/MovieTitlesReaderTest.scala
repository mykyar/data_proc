package com.example.movieratings

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.File

class MovieTitlesReaderTest extends AnyWordSpecLike with Matchers with EitherValues {

  "MovieTitlesReaderTest" should {
    "read data from correct file" in {
      val file = new File("src/test/resources/movie_titles.txt")

      MovieTitlesReader.getFilteredDataFromFile(file).right.value.size shouldBe >(30)
    }

    "read data from incorrect file" in {
      val file = new File("src/test/resources/empty_file.txt")

      MovieTitlesReader.getFilteredDataFromFile(file).right.value shouldBe empty
    }
  }
}
