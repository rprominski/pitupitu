package pitupitu.core

import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import pitupitu.core.TransactionPairing.pairTransactions

import java.time.LocalDate

class TransactionPairingTest extends AnyFlatSpec with should.Matchers with EitherValues {
    private val t = Transaction(LocalDate.parse("2020-01-09"), "VANGUARD S&P500", 1, Price(BigDecimal(55.8810), "EUR"), "EAM", Price.EMPTY)

    "When pairing buy and sold transactions" should "return error when insufficient amount of stocks" in {
        val transactions = List(t, t, t.copy(amount = -100))

        val result = pairTransactions(transactions)

        result.left.value should include regex "amount"
    }

    it should "split buy transaction, when it exceeds sold amount" in {
        val boughtN = 100
        val soldN = 10
        val transactions = List(t.copy(amount = boughtN), t.copy(amount = -soldN))

        val result = pairTransactions(transactions)
        result.value should contain theSameElementsAs List(BoughtSold(List(t.copy(amount = soldN)), t.copy(amount = -soldN)))
    }

    it should "pair using FIFO strategy" in {
        val firstBuy = t.copy(amount = 10, price = Price(BigDecimal(1), "PLN"))
        val secondBuy = t.copy(amount = 20, price = Price(BigDecimal(2), "PLN"))
        val thirdBuy = t.copy(amount = 30, price = Price(BigDecimal(3), "PLN"))
        val firstSell = t.copy(amount = -25, price = Price(BigDecimal(4), "PLN"))
        val secondSell = t.copy(amount = -35, price = Price(BigDecimal(5), "PLN"))
        val expectedOrder = List(
            BoughtSold(List(firstBuy, secondBuy.copy(amount = 15)), firstSell),
            BoughtSold(List(secondBuy.copy(amount = 5), thirdBuy), secondSell)
        )

        val result = pairTransactions(List(firstBuy, secondBuy, thirdBuy, firstSell, secondSell))

        result.value should contain theSameElementsInOrderAs expectedOrder
    }
}


