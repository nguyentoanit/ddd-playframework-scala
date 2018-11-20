package application.forms
import play.api.data.Forms._
import play.api.data._

case class LoginData(email: String, password: String, origin: String)
object Login {
  val loginForm = Form(
    mapping(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText,
      "origin" -> nonEmptyText
    )(LoginData.apply)(LoginData.unapply)
  )
}
