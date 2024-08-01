package com.example.movieratings.errors

trait ProcessingError {
  def message: String
}

object ProcessingError {
  case object NotEnoughArguments extends ProcessingError {
    override val message: String = "not enough arguments. Please provide in the order:\n* a path to the movies description file\n* a path to the training dataset directory\n* a report output path"
  }

  case class FileNotFound(fileName: String, fileRole: String) extends ProcessingError {
    override val message: String = s"File $fileName for $fileRole did not found. Please check path to file"
  }

  case class DirectoryDoesNotExists(address: String) extends ProcessingError {
    override val message: String = s"directory $address does not exists. Please provide correct directory"
  }

  case class DirectoryIsEmpty(address: String) extends ProcessingError {
    override val message: String = s"directory $address is empty. Please check is this correct directory"
  }

  case class NotingToReport(reason: String) extends ProcessingError {
    override def message: String = s"noting to report because of $reason"
  }

  case class NonCsvFile(fileName: String, errorMessage: String) extends ProcessingError {
    override val message: String = s"the provided file $fileName has not expected csv format. Processed with msg: $errorMessage. Please check is this file correct"
  }

  case class ReportWritingNotPermitted(fileName: String) extends ProcessingError {
    override val message: String = s"can not write report to file $fileName. Have no writing permissions"
  }
}
