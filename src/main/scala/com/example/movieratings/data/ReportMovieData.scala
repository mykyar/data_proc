package com.example.movieratings.data

case class ReportMovieData(
  title: String,
  releaseYear: String,
  averageRating: BigDecimal, // maybe Double
  numberOfReviews: Int
)

object ReportMovieData {
  implicit val ordering: Ordering[ReportMovieData] = Ordering.fromLessThan((report1, report2) =>
    (report1.averageRating == report2.averageRating && report1.title > report2.title) || report1.averageRating > report2.averageRating
  )

  val asList: ReportMovieData => List[Any] = (rd) => rd.title :: rd.releaseYear :: rd.averageRating :: rd.numberOfReviews :: Nil
}
