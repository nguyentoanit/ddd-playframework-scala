package infrastructure.dto

import java.util.Date

case class ArticleDTO(title: String, thumbnail: String, content: String, createdOn: Date, id: Long = 0L)