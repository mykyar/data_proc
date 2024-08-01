package com.example.movieratings

import com.example.movieratings.data.AverageReviews
import com.example.movieratings.errors.ProcessingError
import com.example.movieratings.errors.ProcessingError.{NonCsvFile, NotingToReport}

import java.io.{File, FileReader, LineNumberReader}
import scala.util.Try

trait MovieReviewCalculator {
  def doesEnoughReviews(file: File): Boolean = {
    val count = new LineNumberReader(new FileReader(file))
    count.skip(Long.MaxValue)
    count.getLineNumber > 1000
  }

  def countAverageReviews(file: File): Either[ProcessingError, AverageReviews] = Try {
    val q = CsvUtils.readFromFileAsList(file)
    val movieId = q.head.get(0).dropRight(1)
    val reviewsCount = q.size - 1
    val csvData = q.tail.map(csvResult => csvResult.get(1).toInt).sum

    val averageRating = BigDecimal(csvData) / reviewsCount // Optionally can be added
                                                          // .setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
                                                          // if we require only 2 numbers after the decimal point

    AverageReviews(movieId, reviewsCount, averageRating)
  }.toEither.left.map(err => NonCsvFile(file.getName, err.getMessage))

  def validatedReports(reports: Seq[Either[ProcessingError, AverageReviews]], directory: File): Either[ProcessingError, List[AverageReviews]] = {
    val reportsCompacted = reports.foldLeft((List.empty[ProcessingError], List.empty[AverageReviews])) { (acc, report) =>
      report match {
        case Left(errValue) => acc.copy(_1 = errValue :: acc._1)
        case Right(review) => acc.copy(_2 = review :: acc._2)
      }
    }

    reportsCompacted match {
      case (Nil, Nil) => Left(NotingToReport(s"provided ${directory.getPath} has no files that corresponds to required conditions"))
      case (errors, Nil) => Left(NotingToReport(s"provided directory ${directory.getPath} has no files of expected formats.\nErrors:\n${errors.mkString("\n")}"))
      case (_, reports) => Right(reports)
    }
  }
}

object MovieReviewCalculator extends MovieReviewCalculator
