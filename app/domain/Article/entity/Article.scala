package domain.Article.entity

import java.util.Date

import domain.Article.valueobject.{ ArticleID, FilePath }

case class Article(title: String, thumbnail: FilePath, content: String, id: ArticleID = null, createdOn: Date = null) {
  require(title.length > 0, "title is required")
  require(title.length <= 250, "max lenth of title is 250 characters")
  require(thumbnail.value.length > 0, "thumbnail is required")
  require(content.length > 0, "content is required")
}
