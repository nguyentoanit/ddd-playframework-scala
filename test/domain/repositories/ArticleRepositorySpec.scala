package domain.repositories

import java.util.Date

import domain.Article.entity.Article
import domain.Article.repositoy.ArticleRepository
import domain.Article.valueobject.{ArticleID, FilePath}
import infrastructure.dao.ArticleDAO
import infrastructure.dto.ArticleDTO
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.util.Success

class ArticleRepositorySpec extends Specification with Mockito {
  val articleDAO: ArticleDAO = mock[ArticleDAO]
  val articleRepository: ArticleRepository = new ArticleRepository(articleDAO)

  "ArticleRepository" >> {
    "getByID" >> {
      "when [request an existing id] return [Susscess[Some[ArticleDTO]]]" >> {
        val createdOn: Date = new Date
        articleDAO.getByID(1) returns Some(ArticleDTO("a", "a", "a", createdOn, 1))
        articleRepository.getByID(1) mustEqual Success(Some(Article("a", FilePath("a"), "a", ArticleID(1), createdOn)))
      }
      "when [request an id that doesn't exist] return [Success(None)]" >> {
        articleDAO.getByID(-1) returns None
        articleRepository.getByID(-1) mustEqual Success(None)
      }
    }


  }
}
