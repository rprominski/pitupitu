package pitupitu.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import org.scalatest.EitherValues
class FileImporterTest extends AnyFlatSpec with should.Matchers with EitherValues {
  private val fileName = "src/test/resources/degiro_transactions.txt"
  private val fileImporter = FileImporter(DegiroImporter())
  "File importer" should "import from file" in {
    val imported = fileImporter.importTransactions(fileName)

    imported.value should have size 2
  }
}
