package com.example.movieratings

import com.example.movieratings.data.MovieDescription
import com.example.movieratings.errors.ProcessingError
import com.example.movieratings.errors.ProcessingError.NonCsvFile

import java.io.File
import scala.util.Try

trait MovieTitlesReader {
  def getFilteredDataFromFile(file: File): Either[ProcessingError, Map[String, MovieDescription]] = {
    val csvRecords = Try(CsvUtils.readFromFileAsList(file)).toEither
      .left.map(error => NonCsvFile(file.getName, error.getMessage))

    csvRecords
      .map(_.toStream
        .withFilter(csvRecord => Try(isInRange(csvRecord.get(1).toInt)).getOrElse(false))
        .map(csvRecord => csvRecord.get(0) -> MovieDescription(csvRecord.get(1), csvRecord.get(2)))
        .toMap
      )
  }

  private def isInRange(year: Int): Boolean = {
    year > 1970 && year < 1990
  }
}

object MovieTitlesReader extends MovieTitlesReader
