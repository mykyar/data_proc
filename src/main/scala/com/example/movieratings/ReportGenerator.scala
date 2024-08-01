package com.example.movieratings

import com.example.movieratings.data.{CommandLineArguments, ReportMovieData}
import com.example.movieratings.errors.ProcessingError
import com.example.movieratings.errors.ProcessingError.NotEnoughArguments

object ReportGenerator {
  def argsWorker(args: Vector[String]): Either[ProcessingError, CommandLineArguments] = {
    if (args.length < 3) {
      Left(NotEnoughArguments)
    } else {
      Right(CommandLineArguments(args(0), args(1), args(2)))
    }
  }

  def main(args: Array[String]): Unit = {
    // check out CsvUtils for methods to read and write CSV files

    // I assume most of the data is correct
    // If it require to speedup, it can be used to Future.sequence, but for this task it will be overkill

    val processing = for {
      arguments <- argsWorker(args.toVector)
      movieTitlesFile <- ProjectFileUtils.checkFileExists(arguments.pathToMovieTitles, "Movie Titles File")
      trainingDirectory <- ProjectFileUtils.checkDirectoryExists(arguments.pathToTrainingSetDirectory)
      reportFile <- ProjectFileUtils.checkIsWritingAllowed(arguments.fileToWrite)
      filteredMovieData <- MovieTitlesReader.getFilteredDataFromFile(movieTitlesFile)
      requiredMovieId = filteredMovieData.keySet
      allTrainingFiles <- ProjectFileUtils.receiveAllTrainingFiles(trainingDirectory)
      filteredFiles = allTrainingFiles
        .filter(file => file.isFile && requiredMovieId(file.getName.dropRight(4).replaceFirst("mv_0+", "")))
        .sortBy(_.length()) // small files first
        .dropWhile(f => !MovieReviewCalculator.doesEnoughReviews(f)) // find files greater then has enough reviews
      allAverageValues = filteredFiles.map(e => MovieReviewCalculator.countAverageReviews(e))
      averageValues <- MovieReviewCalculator.validatedReports(allAverageValues, trainingDirectory)
      reportData <- ReportDataAssembler.assembleReportData(averageValues, filteredMovieData)
      _ = CsvUtils.writeToFile(reportData.map(ReportMovieData.asList), reportFile)
    } yield ()

    processing match {
      case Left(value) =>
        System.err.println(s"there was an error during processing:\n${value.message}")
        sys.exit(1)
      case Right(_) =>
        println("processing completed successfully")
    }
  }
}
