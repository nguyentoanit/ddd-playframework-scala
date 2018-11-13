package application

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ArticleApplication @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def create() = Action { implicit request =>
    // Ok(ui.html.form_article())
    if (request.body.asFormUrlEncoded.toString != "None") {
      //Ok(request.body.asFormUrlEncoded.get("title").toString)
      Ok("Submit")
    } else Ok(ui.html.form_article())
  }
}
