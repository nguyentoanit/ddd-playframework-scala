package application

import domain.Account.entity.Account
import domain.Account.repository.AccountRepository
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.mvc.{ ControllerComponents, _ }
import play.api.test.Helpers._
import play.api.test.{ FakeRequest, WithApplication }
import utils.Inject

import scala.concurrent.Future
import scala.util.Success

class AccountApplicationSpec extends Specification with Mockito with Inject {

  val repository: AccountRepository = mock[AccountRepository]
  val controllerComponents: ControllerComponents = inject[ControllerComponents]
  val application = new AccountApplication(controllerComponents, repository)

  "AccountApplication" >> {
    "create" >> {
      "when [request a valid account] return [redirect to page contains creating form]" >> new WithApplication() {
        repository.store(any[Account]) returns Success(Right(Account("a", "abc@abc", "123456aA", 1)))
        val res: Future[Result] = call(application.create(), FakeRequest(POST, "/account/create").withFormUrlEncodedBody(
          ("Username", "a"),
          ("Mail address", "abc@abc.com"),
          ("Password", "123456aA"),
          ("Repeated password", "123456aA")
        ))
        status(res) mustEqual 303
      }
      "when [request an account with existing username] return [ResponseCode = INTERNAL_SERVER_ERROR(500)]" >> new WithApplication() {
        repository.store(any[Account]) returns Success(Left("Duplicate entry 'a' for key 'username"))
        val res: Future[Result] = call(application.create(), FakeRequest(POST, "/account/create").withFormUrlEncodedBody(
          ("Username", "a"),
          ("Mail address", "abc@abc.com"),
          ("Password", "123456aA"),
          ("Repeated password", "123456aA")
        ))
        status(res) mustEqual 500
      }
    }
    "login" >> {
      "When login with empty parameters Return 400 error code" in new WithApplication {
        repository.resolveBy(any[String], any[String]) returns None
        val request = FakeRequest("POST", "/login").withFormUrlEncodedBody(
          ("email", ""),
          ("password", ""),
          ("origin", "")
        )
        val result = call(application.login, request)
        status(result) must equalTo(303)
      }
      "When login with correct account Then redirect to current URL with username session and message" in new WithApplication {
        repository.resolveBy(any[String], any[String]) returns Option(Account("username1", "email@example.com", "password", 1))
        val request = FakeRequest("POST", "/login").withFormUrlEncodedBody(
          ("email", "email@example.comm"),
          ("password", "password"),
          ("origin", "/")
        )
        val result = call(application.login, request)
        status(result) must equalTo(303)
        flash(result).get("info").get must beEqualTo("Login successfully!")
        session(result).get("username").get must beEqualTo("username1")

      }
      "When login with incorrect account Then redirect to current URL with empty username session and message" in new WithApplication {
        repository.resolveBy(any[String], any[String]) returns None
        val request = FakeRequest("POST", "/login").withFormUrlEncodedBody(
          ("email", "email@example.comm"),
          ("password", "password"),
          ("origin", "/")
        )
        val result = call(application.login, request)
        status(result) must equalTo(303)
        flash(result).get("info").get must beEqualTo("Wrong email or Password! Please try again.")
        session(result).get("username").get must be empty
      }
    }
  }
}