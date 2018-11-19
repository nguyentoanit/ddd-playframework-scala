package application

import domain.Account.entity.Account
import domain.Account.repository.AccountRepository
import javax.inject.{ Inject, Singleton }
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.{ Constraint, Invalid, Valid, ValidationError }
import play.api.mvc.{ AbstractController, ControllerComponents, EssentialAction }

@Singleton
class AccountApplication @Inject() (cc: ControllerComponents, repository: AccountRepository) extends AbstractController(cc) {
  private val allNumbers = """\d*""".r
  private val allUpperCaseLetters = """[A-Z]*""".r
  private val allLowerCaseLetters = """[a-z]*""".r

  private val passwordCheckConstraint: Constraint[String] = Constraint("constraints.passwordcheck")({
    plainText =>
      val errors = plainText match {
        case allNumbers()          => Seq(ValidationError("Password is all numbers"))
        case allUpperCaseLetters() => Seq(ValidationError("Password is all upper case letters"))
        case allLowerCaseLetters() => Seq(ValidationError("Password is all lower case letters"))
        case _                     => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })

  private val passwordCheck: Mapping[String] = nonEmptyText(minLength = 8, maxLength = 20)
    .verifying(passwordCheckConstraint)

  val accountForm = Form(
    mapping(
      "Name" -> nonEmptyText(maxLength = 50),
      "Mail address" -> email,
      "Password" -> passwordCheck,
      "ID" -> default(longNumber, 0L)
    )(Account.apply)(Account.unapply)
  )
  val loginForm = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(LoginData.apply)(LoginData.unapply)
  )

  def create(): EssentialAction = Action {
    Ok("Created successfully!")
  }

  def show(): EssentialAction = Action {
    Ok("Creating form")
  }

  def login() = Action(parse.form(loginForm)) { implicit request =>
    Ok("Creating form")
  }
}
