package domain.services

import java.nio.file.Paths

import play.api.libs.Files.TemporaryFile
import play.api.mvc._

class UploadService {
  def uploadFile(request: Request[MultipartFormData[TemporaryFile]]): Option[String] = {
    // Store thumbnail
    request.body.file("thumbnail").map { thumbnail =>
      val fileName = System.currentTimeMillis().toString + "_" + Paths.get(thumbnail.filename.replaceAll("\\s", "")).getFileName
      val path = "uploads/thumbnails/" + fileName
      thumbnail.ref.moveTo(Paths.get(path), replace = false)
      fileName
    }
  }
}