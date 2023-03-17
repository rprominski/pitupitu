package pitupitu.core

trait Importer {
  def importTransactions(transactions: String): Either[String, Iterable[Transaction]]
}