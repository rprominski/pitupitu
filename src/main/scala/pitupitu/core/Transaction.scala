package pitupitu.core

import java.time.LocalDate

case class Transaction(date: LocalDate, productName: String, amount: Int, price: Price, stockMarket: String, fee: Price) {
  def isBuy: Boolean = amount > 0
  def isSell: Boolean = amount < 0
}

case class Price(amount: BigDecimal, currency: String)

object Price {
  val EMPTY: Price = Price(0, "PLN")
}