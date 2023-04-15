package pitupitu.core

import io.circe.generic.auto.*
import sttp.client4.*
import sttp.client4.circe.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NBPCurrencyRateFetcher(val url: String = "https://api.nbp.pl/api/exchangerates/rates/A") {
  private val backend = DefaultSyncBackend()

  def fetch(currency: String, date: LocalDate): Either[String, BigDecimal] =
    val formattedDate = date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))

    val request = basicRequest
      .get(uri"$url/$currency/$formattedDate")
      .header("Accept", "application/json")
    request.response(asJson[NBPResponse]).send(backend).body match
      case Left(err) => Left(f"Problem occured during fetching $currency $formattedDate : \n${err.getMessage}")
      case Right(res) => Right(res.rates.head.mid)

  private case class NBPResponse(table: String, currency: String, code: String, rates: List[Rate])

  private case class Rate(no: String, effectiveDate: LocalDate, mid: BigDecimal)
}

