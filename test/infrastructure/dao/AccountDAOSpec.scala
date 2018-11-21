package infrastructure.dao

import java.sql.Connection

import infrastructure.dto.AccountDTO
import org.specs2.mutable.Specification
import org.specs2.specification.{ AfterEach, BeforeEach }
import play.api.db.evolutions.Evolutions
import utils.{ TestDBUtil, AccountUtil }

class AccountDAOSpec extends Specification with TestDBUtil with AccountUtil with BeforeEach with AfterEach {
  val connection: Connection = database.getConnection()
  val accountDAO: AccountDAO = new AccountDAO()

  override protected def before: Any = {
    initTestDB()
  }

  "AccountDAO" >> {
    "create" >> {
      "when [request information of an account that need to be created] return [id of the account]" >> {
        val accounDTO: AccountDTO = AccountDTO("a", "abc@abc", "123456aA")
        accountDAO.store(accounDTO) mustEqual 1
      }
    }
    "resolveBy" >> {
      "When login with incorrect account return None" >> {
        accountDAO.resolveBy("a@a.com", "aaaa") must beNone
      }
      "When login with correct account return Option[AccountDTO]" >> {
        val email: String = "user1@gmail.com"
        val password: String = "test"
        store(connection, email, password)
        accountDAO.resolveBy(email, password) must beSome[AccountDTO]
      }
    }
  }

  override protected def after: Any = {
    Evolutions.cleanupEvolutions(database)
  }
}
