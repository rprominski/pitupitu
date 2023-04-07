package pitupitu.core

import cats.data.NonEmptyChain

import scala.io.Source
import scala.util.Using

class FileImporter(importer: Importer) extends Importer {
  def importTransactions(transactionsFile: String): Either[Errors, Iterable[Transaction]] =
    Using(Source.fromFile(transactionsFile)) { reader =>
      importer.importTransactions(reader.mkString)
    }.toEither.left.map(ex => NonEmptyChain.one(ex.getMessage)).flatten
}
