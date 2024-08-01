package com.example.movieratings

import com.example.movieratings.errors.ProcessingError
import com.example.movieratings.errors.ProcessingError.{DirectoryDoesNotExists, DirectoryIsEmpty, FileNotFound, ReportWritingNotPermitted}

import java.io.File

object ProjectFileUtils {

  def checkFileExists(pathToFile: String, fileRole: String): Either[ProcessingError, File] = {
    val file = new File(pathToFile)

    if (file.exists()) {
      Right(file)
    } else {
      Left(FileNotFound(pathToFile, fileRole))
    }
  }

  def checkDirectoryExists(pathToDirectory: String): Either[ProcessingError, File] = {
    val directory = new File(pathToDirectory)

    if (directory.isDirectory) {
      Right(directory)
    } else {
      Left(DirectoryDoesNotExists(pathToDirectory))
    }
  }

  def receiveAllTrainingFiles(directory: File): Either[ProcessingError, List[File]] = {
    val allFiles = directory.listFiles().toList

    if(allFiles.isEmpty) {
      Left(DirectoryIsEmpty(directory.getPath))
    } else {
      Right(allFiles)
    }
  }

  def checkIsWritingAllowed(reportFilePath: String): Either[ProcessingError, File] = {
    val file = new File(reportFilePath)

    if(file.exists() && file.canWrite() || !file.exists()) {
      Right(file)
    } else {
      Left(ReportWritingNotPermitted(reportFilePath))
    }
  }
}
