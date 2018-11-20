package infrastructure.dto

import java.util.Date

case class ArticleDTO(title: String, thumbnail: String, content: String, id: Long = 0L, createdAt: Date = null, updatedAt: Date = null)