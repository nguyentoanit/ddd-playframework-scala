package infrastructure.dao

import java.sql.Connection

import infrastructure.dto.{ AccountDTO, ArticleDTO }
import org.specs2.mutable.Specification
import org.specs2.specification.{ AfterEach, BeforeEach }
import play.api.db.evolutions.Evolutions
import utils.TestDBUtil

class AccountDAOSpec extends Specification with TestDBUtil with BeforeEach with AfterEach {
  val connection: Connection = database.getConnection()
  val accountDAO: AccountDAO = new AccountDAO()

  override protected def before: Any = {
    initTestDB()
  }

  "AccountDAO" >> {
    "create" >> {
      "when [request information of an article that need to be created] return [id of the article]" >> {
        val accounDTO: AccountDTO = AccountDTO("a", "abc@abc", "123456aA")
        accountDAO.store(accounDTO) mustEqual 1
      }
    }
  }

  override protected def after: Any = {
    Evolutions.cleanupEvolutions(database)
  }
}
