package pitupitu.core

import java.time.LocalDate

case class Transaction(date: LocalDate, productName: String, amount: Int, price: Price, stockMarket: String, fee: Price)

case class Price(amount: BigDecimal, currency: String)

object Price {
  val EMPTY: Price = Price(0, "PLN")
}