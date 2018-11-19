package domain.Article.repository

import java.util.Date

import domain.Article.entity.Article
import domain.Article.valueobject.{ ArticleID, FilePath }
import infrastructure.dao.ArticleDAO
import infrastructure.dto.ArticleDTO
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.util.Success

class ArticleRepositorySpec extends Specification with Mockito {
  val articleDAO: ArticleDAO = mock[ArticleDAO]
  val articleRepository: ArticleRepository = new ArticleRepository(articleDAO)

  "ArticleRepository" >> {
    "create" >> {
      "when [request information of an article] return [id of created article is correct]" >> {
        articleDAO.create(any[ArticleDTO]) returns 1
        articleRepository.create(Article("a", FilePath("a"), "a")).id mustEqual ArticleID(1)
      }
    }

    "getByID" >> {
      "when [request an existing id] return [Susscess[ArticleDTO]]" >> {
        val createdAt: Date = new Date
        val updatedAt: Date = new Date
        articleDAO.getByID(1) returns Some(ArticleDTO("a", "a", "a", 1, createdAt, updatedAt))
        articleRepository.getByID(1) mustEqual Success(Article("a", FilePath("a"), "a", ArticleID(1), createdAt, updatedAt))
      }
      "when [request an id that doesn't exist] return [Success(None)]" >> {
        articleDAO.getByID(-1) returns None
        articleRepository.getByID(-1) must beFailedTry
      }
    }
  }
}
