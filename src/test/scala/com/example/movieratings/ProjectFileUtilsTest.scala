package com.example.movieratings

import org.scalatest.EitherValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import java.io.File

class ProjectFileUtilsTest extends AnyWordSpecLike with Matchers with EitherValues {

  "testCheckIsWritingAllowed" should {
    "can be written when no file is present" in {
      val filePath = "src/test/resources/movie_titles.csv"
      ProjectFileUtils.checkIsWritingAllowed(filePath).right.value
    }
  }

  "testCheckFileExists" should {
    "file not exists" in {
      ProjectFileUtils.checkFileExists("src/test/resources/movie_titles.csv", "Test File").left.value
        .message should endWith("Test File did not found. Please check path to file")
    }

    "file exists" in {
      ProjectFileUtils.checkFileExists("src/test/resources/movie_titles.txt", "Test File").right.value
        .isFile shouldBe true
    }
  }

  "testCheckDirectoryExists" should {
    "directory not exists" in {
      ProjectFileUtils.checkDirectoryExists("src/test/resources/directory/").left.value
        .message should endWith("does not exists. Please provide correct directory")
    }

    "directory exists" in {
      ProjectFileUtils.checkDirectoryExists("src/test/resources/").right.value
        .isDirectory shouldBe true
    }
  }

  "testReceiveAllTrainingFiles" should {
    "get files from non empty directory" in {
      val dir = new File("src/test/resources/")
      ProjectFileUtils.receiveAllTrainingFiles(dir).right.value.length shouldBe >(1)
    }

    "not get files from empty directory" in {
      val dir = new File("src/test/resources/test_directory/")
      dir.mkdir()
      ProjectFileUtils.receiveAllTrainingFiles(dir).left.value
        .message should startWith("directory src\\test\\resources\\test_directory is empty")
      dir.delete()
    }
  }
}
