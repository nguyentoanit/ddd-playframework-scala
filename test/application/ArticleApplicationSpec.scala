package application

import java.nio.file.Paths
import java.util.Date

import domain.Article.entity.Article
import domain.Article.repository.ArticleRepository
import domain.Article.valueobject.{ ArticleID, FilePath }
import domain.services.UploadService
import exception.EntityNotFoundException
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.libs.Files.{ SingletonTemporaryFileCreator, TemporaryFile }
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc._
import play.api.test.CSRFTokenHelper._
import play.api.test.Helpers._
import play.api.test._
import utils.TestDBUtil

import scala.concurrent.Future
import scala.util.{ Failure, Success }

object ArticleApplicationSpec extends Specification with Mockito with TestDBUtil {
  initTestDB()
  val filePathVO: FilePath = FilePath("/a.jpg")
  val articleEntity: Article = Article("Test title", filePathVO, "Test content")
  val repository: ArticleRepository = mock[ArticleRepository]
  val controllerComponents: ControllerComponents = stubControllerComponents()
  val uploadService: UploadService = mock[UploadService]
  val application: ArticleApplication = new ArticleApplication(controllerComponents, repository, uploadService)

  "ArticleApplication" >> {
    "get" >> {
      "when [request an existing id] return [Response(OK(200), html page showing information of the article]" >> new WithApplication() {
        val createdAt: Date = new Date()
        val updatedAt: Date = new Date()
        repository.getByID(1) returns Success(Article("a", FilePath("a"), "a", ArticleID(1), createdAt, updatedAt))
        val res: Future[Result] = call(application.get(1), FakeRequest(GET, "/article/1?"))

        status(res) mustEqual OK
        contentAsString(res) must contain("a</h1>")
      }
      "when [request an id that doesn't exist] return [ResponseCode = NOT_FOUND(404)]" >> new WithApplication() {
        repository.getByID(-1) returns Failure(new EntityNotFoundException("ID doesn't exist"))
        val res: Future[Result] = call(application.get(-1), FakeRequest(GET, "/article/-1?"))

        status(res) mustEqual NOT_FOUND
        contentAsString(res) must contain("ID doesn't exist")
      }
      "when [an exception occurs] return [ResponseCode = INTERNAL_SERVER_ERROR(500)]" >> new WithApplication() {
        repository.getByID(1) returns Failure(new RuntimeException("An exception occur"))
        val res: Future[Result] = call(application.get(1), FakeRequest(GET, "/article/1?"))

        status(res) mustEqual INTERNAL_SERVER_ERROR
      }
    }
    "create method" in new WithApplication {
      repository.create(any[Article]) returns articleEntity

      val file = SingletonTemporaryFileCreator.create(Paths.get("test/application/a.png"))
      val part = new FilePart[TemporaryFile]("thumbnail", "file.png", None, file)

      val multipartBody = MultipartFormData[TemporaryFile](Map("title" -> Seq("title"), "content" -> Seq("content")), Seq(part), Nil)
      val request = FakeRequest[MultipartFormData[TemporaryFile]]("POST", "/article/create", FakeHeaders(), multipartBody).withCSRFToken
      uploadService.uploadFile(request) returns Option("example.png")

      val result = application.create()(request)
      request must equalTo(301)
    }
  }
}
