package domain.Article.repository

import domain.Article.entity.Article
import domain.Article.valueobject.{ ArticleID, FilePath }
import exception.EntityNotFoundException
import infrastructure.dao.ArticleDAO
import infrastructure.dto.ArticleDTO
import javax.inject.Inject

import scala.util.Try

class ArticleRepository @Inject() (articleDAO: ArticleDAO) {

  def create(article: Article): Article = {
    val articleDTO: ArticleDTO = toDTO(article)
    val id: ArticleID = ArticleID(articleDAO.create(articleDTO))
    Article(article.title, article.thumbnail, article.content, id, articleDTO.createdAt, articleDTO.updatedAt)
  }

  def getByID(id: Long): Try[Article] = Try {
    articleDAO.getByID(id) match {
      case Some(dto) => toEntity(dto)
      case None      => throw new EntityNotFoundException("ID doesn't exist")
    }
  }

  private def toDTO(article: Article): ArticleDTO = {
    ArticleDTO(article.title, article.thumbnail.value, article.content)
  }

  private def toEntity(dto: ArticleDTO): Article =
    Article(dto.title, FilePath(dto.thumbnail), dto.content, ArticleID(dto.id), dto.createdAt, dto.updatedAt)
}