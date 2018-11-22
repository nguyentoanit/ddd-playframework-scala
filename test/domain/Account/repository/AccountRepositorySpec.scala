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
    }
    "resolveBy" >> {
      "When login with incorrect email and password return None" >> {
        accountDAO.resolveBy(any[String], any[String]) returns None
        accountRepository.resolveBy("", "") must beNone
      }
      "When login with correct email and password return a Account Entity" >> {
        accountDAO.resolveBy(any[String], any[String]) returns Option(mock[AccountDTO])
        accountRepository.resolveBy("", "").get must beAnInstanceOf[Account]
      }
    }
  }
}
