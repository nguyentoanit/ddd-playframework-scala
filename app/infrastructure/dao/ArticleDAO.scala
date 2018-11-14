package infrastructure.dao

import infrastructure.dao.ArticleDAO.autoSession
import infrastructure.dto.ArticleDTO
import scalikejdbc._

private object ArticleDAO extends SQLSyntaxSupport[ArticleDTO] {
  override val tableName = "articles"
  override val columns = Seq("id", "title", "thumbnail", "content", "created_on")

  def apply(rs: WrappedResultSet): ArticleDTO =
    ArticleDTO(
      id = rs.long("id"),
      title = rs.string("content"),
      thumbnail = rs.string("thumbnail"),
      content = rs.string("content"),
      createdOn = rs.date("created_on")
    )
}

class ArticleDAO {
  implicit val session: DBSession = autoSession
  val c = ArticleDAO.column
  val a = ArticleDAO.syntax("a")

  def create(articleDTO: ArticleDTO): Long = withSQL {
    insert.into(ArticleDAO).columns(c.title, c.thumbnail, c.content, c.createdOn)
      .values(articleDTO.title, articleDTO.thumbnail, articleDTO.content, articleDTO.createdOn)
  }.updateAndReturnGeneratedKey.apply()

  def getByID(id: Long): Option[ArticleDTO] =
    withSQL {
      select.from(ArticleDAO as a).where.eq(a.id, id)
    }.map(rs => ArticleDAO(rs)).single().apply()
}

