package pitupitu.core

import scala.io.Source
import scala.util.Using

class FileImporter(importer: Importer) extends Importer {
  def importTransactions(transactionsFile: String): Either[String, Iterable[Transaction]] =
    val x = Using(Source.fromFile(transactionsFile)) { reader =>
      importer.importTransactions(reader.mkString)
    }
    ???
}
