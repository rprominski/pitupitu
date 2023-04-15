package pitupitu.core

import org.scalatest.EitherValues
import org.scalatest.Inside.inside
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.time.LocalDate
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

class NBPCurrencyRateFetcherTest extends AnyFlatSpec with should.Matchers with EitherValues {
  "Currency rate fetcher" should "fetch currency rate in working day" in {
    val expectedRate = BigDecimal("4.6862")
    val friday = LocalDate.parse("2023-04-07")

    val rate = NBPCurrencyRateFetcher().fetch("EUR",  friday)

    rate.value shouldEqual expectedRate
  }

  it should "return Left when fetching currency rate of weekend date" in {
    val date = "2023-04-08"
    val currency = "EUR"
    val saturday =  LocalDate.parse(date)
    
    val rate = NBPCurrencyRateFetcher().fetch(currency,  saturday)

    rate.left.value should include regex currency
    rate.left.value should include regex date
  }
}
