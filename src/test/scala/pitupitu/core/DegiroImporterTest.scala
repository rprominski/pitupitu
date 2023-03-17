package pitupitu.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.time.LocalDate
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class DegiroImporterTest extends AnyFlatSpec with should.Matchers {
  private val importer = DegiroImporter()
  private val transactions =
    """
      |Data,Czas,Produkt,ISIN,Giełda referenc,Miejsce wykonania,Liczba,Kurs,,Wartość lokalna,,Wartość,,Kurs wymian,Opłata transakcyjna,,Razem,,Identyfikator zlecenia
      |09-01-2020,09:05,VANGUARD S&P500,IE00B3XXRP09,EAM,XAMS,1,55.8810,EUR,-55.88,EUR,-55.88,EUR,,,,-55.88,EUR,56314f5e-bbdc-43b7-9474-1a595c82af97
      |09-01-2020,09:05,ISHARES S&P 500,IE0031442068,EAM,XAMS,1,29.3250,EUR,-29.33,EUR,-29.33,EUR,,,,-29.33,EUR,439ef09a-450c-40be-8c80-b0972d7cc822
      |""".stripMargin

//  "Importer" should "import correct transactions file" in {
//    val expectedTransactions = List(
//      Transaction(LocalDate.parse("2020-01-09"), "VANGUARD S&P500", 1, Price(BigDecimal(55.8810), "EUR"), "EAM", Price.EMPTY),
//      Transaction(LocalDate.parse("2020-01-09"), "ISHARES S&P 500", 1, Price(BigDecimal(29.3250), "EUR"), "EAM", Price.EMPTY)
//    )
//    val importedTransactions = importer.importTransactions(transactions)
//
////    importedTransactions should have size 2
////
////    for (transaction <- expectedTransactions) {
////      importedTransactions.find(_ == transaction) shouldBe Some(transaction)
////    }
//  }
//
//  it should "return empty list when importing from empty string" in {
////    val result = importer.importTransactions("")
////
////    result should have size 0
//  }
//
//  it should "teach me cats" in {
//    val x: Try[Either[Int, String]] = Try(Right("String"))
//    val y: Either[Int, String] = x match
//      case Success(_) => _
//      case Failure(_) => Left(_)
//  }
  it should "valiadted" in {
    println(importer.validateAmount("1") )
    println(importer.validateAmount("0") )
    println(importer.validateAmount("1a") )
  }
}
