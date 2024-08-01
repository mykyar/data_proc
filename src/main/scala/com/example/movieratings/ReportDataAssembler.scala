package com.example.movieratings

import com.example.movieratings.data.{AverageReviews, MovieDescription, ReportMovieData}
import com.example.movieratings.errors.ProcessingError
import com.example.movieratings.errors.ProcessingError.NotingToReport

object ReportDataAssembler {
  def assembleReportData(averageValues: List[AverageReviews], movieData: Map[String, MovieDescription]): Either[ProcessingError, List[ReportMovieData]] = {
    val sortedReport = averageValues.flatMap { av =>
      val movieDescription = movieData.get(av.movieId)
      movieDescription.map(md => ReportMovieData(md.title, md.yearOfRelease, av.averageRating, av.reviewsCount))
    }.sorted

    if (sortedReport.isEmpty) {
      Left(NotingToReport("movie ID in the training files does not match to data in movie description file"))
    } else {
      Right(sortedReport)
    }
  }
}
