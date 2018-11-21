package utils
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException;

trait AccountUtil extends TestDBUtil {
  def store(connection: Connection, email: String, password: String) {
    val query: PreparedStatement = connection.prepareStatement("insert into accounts(username, email, password) values ('User 1', ?, ?)")
    query.setString(1, email)
    query.setString(2, password)
    query.execute()
  }
}

