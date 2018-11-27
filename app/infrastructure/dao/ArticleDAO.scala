package infrastructure.dao

import infrastructure.dao.ArticleDAO.autoSession
import infrastructure.dto.ArticleDTO
import scalikejdbc._

private object ArticleDAO extends SQLSyntaxSupport[ArticleDTO] {
  override val tableName = "articles"
  override val columns = Seq("id", "title", "thumbnail", "content", "created_at", "updated_at")

  def apply(a: ResultName[ArticleDTO])(rs: WrappedResultSet): ArticleDTO =
    ArticleDTO(
      id        = rs.long(a.id),
      title     = rs.string(a.title),
      thumbnail = rs.string(a.thumbnail),
      content   = rs.string(a.content),
      createdAt = rs.date(a.createdAt),
      updatedAt = rs.date(a.updatedAt)
    )
}

class ArticleDAO {
  implicit val session: DBSession = autoSession
  val c = ArticleDAO.column
  val a = ArticleDAO.syntax("a")

  def create(articleDTO: ArticleDTO): Long = withSQL {
    insert.into(ArticleDAO).columns(c.title, c.thumbnail, c.content)
      .values(articleDTO.title, articleDTO.thumbnail, articleDTO.content)
  }.updateAndReturnGeneratedKey.apply()

  def getByID(id: Long): Option[ArticleDTO] =
    withSQL {
      select.from(ArticleDAO as a).where.eq(a.id, id)
    }.map(rs => ArticleDAO(a.resultName)(rs)).single().apply()
}

