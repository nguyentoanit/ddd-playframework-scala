package domain.repositories

import java.util.Date

import domain.entities.Article
import domain.valueobjects.FilePath
import infrastructure.dao.ArticleDAO
import infrastructure.dto.ArticleDTO
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class ArticleRepositorySpec extends Specification with Mockito {
  val articleDAO: ArticleDAO = mock[ArticleDAO]
  val articleRepository: ArticleRepository = new ArticleRepository(articleDAO)

  "ArticleRepository" >> {
    "create" >> {
      "when [request a valid article] return [Success[Article]]" >> {
        articleDAO.create(any[ArticleDTO]) returns 1
        articleRepository.create(Article("a", FilePath("a"), "a")) must beSuccessfulTry[Article]
      }
    }
  }
}
