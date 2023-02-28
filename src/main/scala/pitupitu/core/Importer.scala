package pitupitu.core

trait Importer {
  def importTransactions(transactions: String): Iterable[Transaction]
}
