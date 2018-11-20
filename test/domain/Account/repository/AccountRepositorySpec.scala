package domain.Account.repository

import domain.Account.entity.Account
import infrastructure.dao.AccountDAO
import infrastructure.dto.AccountDTO
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class AccountRepositorySpec extends Specification with Mockito {
  val accountDAO: AccountDAO = mock[AccountDAO]
  val accountRepository: AccountRepository = new AccountRepository(accountDAO)

  "AccountRepository" >> {
    "store" >> {
      "when [request valid information of an account] return [Right[Account]]" >> {
        accountDAO.store(any[AccountDTO]) returns 1
        accountRepository.store(Account("a", "abc@abc", "123456aA")).get must beRight[Account]
      }
      "when [store an account with existing username] return [Left(errorNotification)]" >> {
        accountDAO.store(any[AccountDTO]) throws new RuntimeException("Duplicate entry 'a' for key 'username'")
        accountRepository.store(Account("a", "abc@abc", "123456aA")) === Left("Duplicate entry 'a' for key 'username'")
      }
    }
  }
}
