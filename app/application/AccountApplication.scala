package application

import application.forms.Account.{ passwordCheckConstraint, usernameCheckConstraint }
import application.forms.CreatingInput
import domain.Account.entity.Account
import domain.Account.repository.AccountRepository
import javax.inject.{ Inject, Singleton }
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ Lang, Messages }
import play.api.mvc.{ AbstractController, ControllerComponents, EssentialAction }
import application.forms.LoginData
import application.forms.Login.loginForm

@Singleton
class AccountApplication @Inject() (cc: ControllerComponents, repository: AccountRepository) extends AbstractController(cc) {

  implicit val messages: Messages = messagesApi.preferred(Seq(Lang.defaultLang))

  val creatingForm = forms.Account.initCreatingForm

  def show(): EssentialAction = Action { implicit request =>
    Ok(ui.html.form_account(creatingForm))
  }

  def create(): EssentialAction = Action(parse.form(creatingForm)) {
    implicit request =>
      val input = request.body
      val account = Account(input.username, input.mailAddress, input.password)
      repository.store(account).map {
        case Right(id) =>
          play.api.Logger.info(s"New account is created successfully with id " + id)
          Redirect(routes.AccountApplication.show())
        case Left(errorNotification) => InternalServerError("INTERNAL SERVER ERROR (500) - " + errorNotification)
      }.recover {
        case exception: RuntimeException => InternalServerError("INTERNAL SERVER ERROR (500) - " + exception.getMessage)
      }.get
  }

  def login() = Action(parse.form(loginForm)) { implicit request =>
    val result = repository.resolveBy(request.body.email, request.body.password)
    val message = result.username match {
      case "" => "Wrong email or Password! Please try again."
      case _  => "Login successfully!"
    }
    Redirect(request.body.origin).flashing("info" -> message).withSession("username" -> result.username)
  }
}
