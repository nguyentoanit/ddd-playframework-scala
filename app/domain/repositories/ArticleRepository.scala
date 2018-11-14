package domain.repositories

import java.util.Date

import domain.entities.Article
import domain.valueobjects.ArticleID
import infrastructure.dao.ArticleDAO
import infrastructure.dto.ArticleDTO
import javax.inject.Inject

import scala.util.Try

class ArticleRepository @Inject() (articleDAO: ArticleDAO) {

  def create(article: Article): Article = {
    val createdOn = new Date()
    val articleDTO: ArticleDTO = ArticleDTO(title = article.title, thumbnail = article.thumbnail.value,
      content = article.content, createdOn = createdOn)
    val id: ArticleID = ArticleID(articleDAO.create(articleDTO))
    Article(article.title, article.thumbnail, article.content, id, createdOn)
  }
}