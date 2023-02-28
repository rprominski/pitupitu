package pitupitu.core

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer

class DegiroImporter:
  def importTransactions(transactions: String): Iterable[Transaction] =
    val result = ArrayBuffer[Transaction]()
    for (line <- transactions split "\n" filterNot(_.isEmpty) drop 1) {
      val columns = line.split(",")
      result += Transaction(
        date = LocalDate.parse(columns(0), DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        productName = columns(2),
        amount = columns(6).toInt,
        price = Price(BigDecimal(columns(7)), columns(8)),
        stockMarket = columns(4),
        fee = if(columns(14).isEmpty) Price.EMPTY else Price(BigDecimal(columns(14)), columns(15))
      )
    }
    result.toSeq