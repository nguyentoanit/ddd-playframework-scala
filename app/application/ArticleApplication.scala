package application
import java.nio.file.Paths

import domain.Article.entity.Article
import domain.Article.repositoy.ArticleRepository
import domain.Article.valueobject.FilePath
import domain.services.UploadService
import exception.EntityNotFoundException
import javax.inject._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ArticleApplication @Inject() (cc: ControllerComponents, repository: ArticleRepository, uploadService: UploadService) extends AbstractController(cc) {
  def create() = Action(parse.multipartFormData) { implicit request =>

    val Some(fileName) = uploadService.uploadFile(request)
    val dataParts = request.body.dataParts
    val Some(title) = dataParts.get("title")
    val Some(content) = dataParts.get("content")

    val filePathVO = FilePath(fileName)
    val articleEntity: Article = Article(title.head, filePathVO, content.head)
    val result: Article = repository.create(articleEntity)

    Redirect("/article/" + result.id.value)
  }

  def show() = Action { implicit request =>
    Ok(ui.html.form_article())
  }

  def file(path: String) = Action {
    Ok.sendFile(new java.io.File("uploads/thumbnails/" + Paths.get(path).getFileName.toString))
  }

  def get(id: Long) = Action { implicit request =>
    repository.getByID(id).map {
      article => Ok(ui.html.article_detail(article))
    }.recover {
      case notFound: EntityNotFoundException => NotFound(notFound.getMessage)
      case exception: RuntimeException       => InternalServerError("INTERNAL SERVER ERROR (500) - " + exception.getMessage)
    }.get
  }
}
