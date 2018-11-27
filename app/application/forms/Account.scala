package application.forms

import play.api.data.Forms._
import play.api.data._
import play.api.data.validation.{Constraint, Invalid, Valid}

case class CreatingInput(username: String, mailAddress: String, password: String, repeatedPassword: String) {
  require(password == repeatedPassword, "Password not match")
}
object Account {
  private val passwordRegrex = """^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])\w*$""".r

  val passwordCheckConstraint: Constraint[String] = Constraint("Password must contain upper case letter, lower case letter and digit")({
    case passwordRegrex() => Valid
    case _                => Invalid("Password doesn't contain upper case letter, lower case letter and/or digit")
  })

  private val usernameRegrex = """^\w*$""".r

  val usernameCheckConstraint: Constraint[String] = Constraint("Username only can contain letter and digit")({
    case usernameRegrex() => Valid
    case _                => Invalid("Password doesn't contain upper case letter, lower case letter and/or digit")
  })

  val initCreatingForm = Form(
    mapping(
      "Username" -> nonEmptyText(maxLength = 50).verifying(usernameCheckConstraint),
      "Mail address" -> email,
      "Password" -> nonEmptyText(minLength = 8, maxLength = 20).verifying(passwordCheckConstraint),
      "Repeated password" -> nonEmptyText(minLength = 8, maxLength = 20)
    )(CreatingInput.apply)(CreatingInput.unapply)
  )
}
