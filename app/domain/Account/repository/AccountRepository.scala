package domain.Account.repository

import java.sql.SQLException

import domain.Account.entity.Account
import infrastructure.dao.AccountDAO
import infrastructure.dto.AccountDTO
import javax.inject.Inject

import scala.util.Try

class AccountRepository @Inject() (dao: AccountDAO) {
  def store(input: Account): Try[Either[String, Account]] = Try {
    try {
      val id: Long = dao.store(toDTO(input))
      Right(Account(input.username, input.email, input.password, id))
    } catch {
      case e: SQLException => Left(e.getMessage)
    }
  }

  def resolveBy(email: String, password: String): Option[Account] = {
    dao.resolveBy(email, password) match {
      case Some(dto) => Option(toEntity(dto))
      case None      => None
    }
  }

  private def toDTO(entity: Account): AccountDTO = AccountDTO(entity.username, entity.email, entity.password)
  private def toEntity(dto: AccountDTO): Account = Account(dto.username, dto.email, dto.password, dto.id)
}

