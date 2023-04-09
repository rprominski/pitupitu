package pitupitu.core

import scala.annotation.tailrec
import scala.collection.mutable

type Transactions = List[Transaction]

object TransactionPairing {
  private type SplitResult = Either[String, (Transactions, Transactions)]

  def pairTransactions(transactions: Transactions): Either[String, List[BoughtSold]] =
    @tailrec
    def rec(cache: mutable.HashMap[String, Transactions], ts: Transactions, res: List[BoughtSold]): Either[String, List[BoughtSold]] =
      ts match
        case Nil => Right(res)
        case h :: t => if (h.isBuy) {
          cache.updateOrCreate(h)
          rec(cache, t, res)
        } else {
          splitUntilSum(-h.amount, cache(h.productName)) match
            case Right(a) => rec(cache += (h.productName -> a._2), t, res :+ BoughtSold(a._1, h)) //TODO probably can get rid of :+ - bad complexity O(n)
            case Left(err) => Left[String, List[BoughtSold]](err)
        }

    rec(mutable.HashMap(), transactions, List())

  private def splitUntilSum(n: Int, transactions: Transactions): SplitResult =
    @tailrec
    def rec(sum: Int, left: Transactions, right: Transactions): SplitResult = {
      (left, right) match
        case (l, r) if sum == n => Right((l, r))
        case (h :: t, r) if sum > n => Right((h.copy(amount = h.amount - sum + n) :: t, h.copy(amount = sum - n) :: r))
        case (_, Nil) => Left(s"not enough stock amount: expected: $n, but found only $sum")
        case (l, h :: t) => rec(sum + h.amount, h :: l, t)
    }

    rec(0, List(), transactions).map(x => (x._1.reverse, x._2))

  extension (map: mutable.HashMap[String, Transactions]) {
    private def updateOrCreate(t: Transaction): mutable.HashMap[String, Transactions] = {
      if (map.contains(t.productName)) map += (t.productName -> (map(t.productName) :+ t)) //TODO probably can get rid of :+ - bad complexity O(n)
      else map += (t.productName -> List(t))
    }
  }
}

case class BoughtSold(bought: Transactions, sold: Transaction)