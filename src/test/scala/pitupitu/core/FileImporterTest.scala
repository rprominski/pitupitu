package pitupitu.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class FileImporterTest extends AnyFlatSpec with should.Matchers {
  private val fileName = "src/test/resources/degiro_transactions.txt"
  private val fileImporter = FileImporter(DegiroImporter())
  "File importer" should "import from file" in {
    val imported = fileImporter.importTransactions(fileName)
    
    imported should have size 2
  }
}
