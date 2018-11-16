package infrastructure.dao

import java.sql.Connection
import java.util.Date

import infrastructure.dto.ArticleDTO
import org.specs2.mutable.Specification
import org.specs2.specification.{ AfterEach, BeforeEach }
import play.api.db.evolutions.Evolutions
import utils.TestDBUtil

class ArticleDAOSpec extends Specification with TestDBUtil with BeforeEach with AfterEach {
  val connection: Connection = database.getConnection()
  val articleDAO: ArticleDAO = new ArticleDAO()

  override protected def before: Any = {
    initTestDB()
  }

  "ArticleDAO" >> {
    "create" >> {
      "when [request information of an article that need to be created] return [id of the article]" >> {
        val articleDTO: ArticleDTO = ArticleDTO("a", "a", "a", createdOn = new Date())
        articleDAO.create(articleDTO) mustEqual 1
      }
    }

    "getByID" >> {
      "when [request an existing id] return [Some[ArticleDTO]]" >> {
        val articleDTO: ArticleDTO = ArticleDTO("a", "a", "a", createdOn = new Date())
        articleDAO.create(articleDTO)
        articleDAO.getByID(1) must beSome[ArticleDTO]
      }
      "when [request an id that doesn't exist] return [None]" >> {
        articleDAO.getByID(-1) must beNone
      }
    }
  }

  override protected def after: Any = {
    Evolutions.cleanupEvolutions(database)
  }
}