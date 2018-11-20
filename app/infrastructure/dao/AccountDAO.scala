package infrastructure.dao

import infrastructure.dto.AccountDTO
import scalikejdbc._

private object AccountDAO extends SQLSyntaxSupport[AccountDTO] {
  override val tableName = "accounts"
  override val columns = Seq("id", "username", "email", "password")

  def apply(a: ResultName[AccountDTO])(rs: WrappedResultSet): AccountDTO =
    AccountDTO(
      rs.string(a.username),
      rs.string(a.email),
      rs.string(a.password),
      rs.long(a.id)
    )
}

class AccountDAO {
  implicit val session: DBSession = AccountDAO.autoSession
  val c = AccountDAO.column
  val a = AccountDAO.syntax("a")

  def store(accountDTO: AccountDTO): Long = withSQL {
    insert.into(AccountDAO).columns(c.username, c.email, c.password)
      .values(accountDTO.username, accountDTO.email, accountDTO.password)
  }.updateAndReturnGeneratedKey.apply()

  def resolveBy(email: String, password: String): Option[AccountDTO] = withSQL {
    select.from(AccountDAO as a).where.eq(a.email, email).and.eq(a.password, password)
  }.map(rs => AccountDAO(a.resultName)(rs)).single().apply()
}
