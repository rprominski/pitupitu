package pitupitu.core

import cats.data.Validated.{Invalid, Valid}
import cats.data.{NonEmptyChain, Validated, ValidatedNec}
import cats.implicits.catsSyntaxEither
import cats.syntax.all.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.::
import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

class DegiroImporter extends Importer :
  def importTransactions(transactions: String): Either[Errors, List[Transaction]] = {
    val headers = 1
    val lines = transactions split "\n" filterNot (_.isEmpty) drop headers
    val validations = lines map validateTransaction

    validations.foldLeft(List[Transaction]().asRight[Errors]) { (acc, v) =>
      (acc, v) match {
        case (Left(acc), Left(v)) => Left(acc.combine(v))
        case (Left(acc), Right(_)) => Left(acc)
        case (Right(_), Left(v)) => Left(v)
        case (Right(acc), Right(v)) => Right(v :: acc)
      }
    }
  }

  private def validateTransaction(line: String): Either[Errors, Transaction] = {
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
    if (name.isEmpty) s"$errorPrefix can't be empty".invalidNec else name.validNec

  private def validateAmount(amount: String): ValidatedNec[String, Int] =
    amount.toIntOption match
      case Some(0) => "Amount can't be 0".invalidNec
      case Some(n) => n.validNec
      case None => "Amount not a number".invalidNec

  private def validatePrice(amount: String, currency: String): ValidatedNec[String, Price] =
    Valid(Price(BigDecimal(amount), currency))

  private def validateStockMarket(stockMarket: String) = validateName(stockMarket)

  private def validateFee(amount: String, currency: String): ValidatedNec[String, Price] =
    if (amount.isEmpty) Price.EMPTY.validNec else Price(BigDecimal(amount), currency).validNec

    
