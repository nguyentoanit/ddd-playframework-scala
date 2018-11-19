package domain.services

import play.api.test.Helpers._
import play.api.test._
import org.specs2.mock.Mockito
import org.specs2.mutable._
import play.api.libs.Files.{ SingletonTemporaryFileCreator, TemporaryFile }
import play.api.mvc._
import play.api.mvc.MultipartFormData.FilePart
import play.api.test.CSRFTokenHelper._
import java.nio.file.{ Paths, Files }

object UploadServiceSpec extends Specification with Mockito {

  "UploadService" >> {
    val file = SingletonTemporaryFileCreator.create(Paths.get("test/application/a.png"))
    val part = new FilePart[TemporaryFile]("thumbnail", "a.png", None, file)

    val multipartBody = MultipartFormData[TemporaryFile](Map("title" -> Seq("title"), "content" -> Seq("content")), Seq(part), Nil)
    val request = FakeRequest[MultipartFormData[TemporaryFile]]("POST", "/article/create", FakeHeaders(), multipartBody).withCSRFToken
    val uploadServiceObj = new UploadService()
    val Some(result) = uploadServiceObj.uploadFile(request)
    "When call a request then store file in uploads/thumbnails folder" >> {
      Files.exists(Paths.get("uploads/thumbnails/" + result)) must beTrue
    }
    "When call a request return file name" >> {
      result must contain("a.png")
    }
  }
}