package pitupitu.core

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyChain, Validated, ValidatedNec}
import cats.implicits.catsSyntaxEither
import cats.syntax.all.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

class DegiroImporter: //extends Importer:
  def importTransactions(transactions: String): ValidatedNec[String, Transaction] = {
    val x = (transactions split "\n" filterNot (_.isEmpty) drop 1).map(validateTransaction)
    x.foldLeft(Validated.valid(Nil): ValidatedNec[String, List[Transaction]]) { (acc, validatedTransaction) =>
      acc.combine(validatedTransaction.map(List(_)))
    }.map(_.toIterable)
  }

  private def validateTransaction(line: String): Either[NonEmptyChain[String], Transaction] = {
    val columns = line.split(",")
    (validateDate(columns(0)),
      validateName(columns(2)),
      validateAmount(columns(6)),
      validatePrice(columns(7), columns(8)),
      validateStockMarket(columns(4)),
      validateFee(columns(14), columns(15))).mapN(Transaction.apply).toEither
  }

  private def validateDate(date: String): ValidatedNec[String, LocalDate] =
     Try(LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
       .toEither
       .leftMap(_.getMessage)
       .toValidatedNec

  private def validateName(name: String, errorPrefix: String = "Name"): ValidatedNec[String, String] =
    if(name.isEmpty) s"$errorPrefix can't be empty".invalidNec else name.validNec

  def validateAmount(amount: String): ValidatedNec[String, Int] =
    amount.toIntOption match
      case Some(0) => "Amount can't be 0".invalidNec
      case Some(n) => n.validNec
      case None => "Amount not a number".invalidNec

  private def validatePrice(amount: String, currency: String): ValidatedNec[String, Price] =
    Valid(Price(BigDecimal(amount), currency))
  private def validateStockMarket(stockMarket: String) = validateName(stockMarket)

  private def validateFee(amount: String, currency: String): ValidatedNec[String, Price] =
    Price(BigDecimal(amount), currency).validNec
    
