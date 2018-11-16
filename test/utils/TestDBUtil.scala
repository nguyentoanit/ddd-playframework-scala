package utils

import java.sql.Connection

import play.api.db.Databases
import play.api.db.evolutions.Evolutions
import scalikejdbc.ConnectionPool

trait TestDBUtil {
  val url = "jdbc:mysql://127.0.0.1:8889/ddd_test?useSSL=false"
  val username = "root"
  val password = "root"

  val database = Databases(
    driver = "com.mysql.jdbc.Driver",
    url = url,
    name = "default",
    config = Map(
      "username" -> username,
      "password" -> password
    )
  )

  def initTestDB() {
    Evolutions.applyEvolutions(database)
    ConnectionPool.singleton(url, username, password)
  }

  def deleteTable(tableName: String) {
    val connection: Connection = database.getConnection()
    connection.prepareStatement("DELETE FROM " + tableName).execute()
  }
}
