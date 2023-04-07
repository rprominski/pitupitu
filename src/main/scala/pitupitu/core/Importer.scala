package pitupitu.core

import cats.data.NonEmptyChain

type Errors = NonEmptyChain[String]

trait Importer {
  def importTransactions(transactions: String): Either[Errors, Iterable[Transaction]]
}