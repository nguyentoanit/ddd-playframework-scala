package application

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import java.nio.file.Paths
import domain.repositories.ArticleRepository
import domain.entities.Article
import domain.valueobjects.FilePath
import java.io.IOException
import infrastructure.dao.ArticleDAO

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ArticleApplication @Inject() (cc: ControllerComponents) extends AbstractController(cc) {
  def create() = Action(parse.multipartFormData) { implicit request =>

    // Store thumbnail
    val Some(filePath) = request.body.file("thumbnail").map { thumbnail =>
      val filePath = "uploads/thumbnails/" + System.currentTimeMillis().toString + "_" + thumbnail.filename
      thumbnail.ref.moveTo(Paths.get(filePath), replace = true)
      filePath
    }

    val dataParts = request.body.dataParts
    val Some(title) = dataParts.get("title")
    val Some(content) = dataParts.get("content")

    val filePathVO = new FilePath(filePath)
    val articleRepository = new ArticleRepository(new ArticleDAO)
    try {
      val articleEntity: Article = new Article(title(0), filePathVO, content(0))
      val result: Article = articleRepository.create(articleEntity)

      Redirect("/article/" + result.id.value)
    } catch {
      case e: IOException => InternalServerError(e.printStackTrace().toString)
    }
  }

  def show() = Action { implicit request =>
    Ok(ui.html.form_article())
  }

  def get(id: String) = Action { implicit request =>
    Ok("The article is created successfully with id " + id)
  }
}
