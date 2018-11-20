package domain.Account.entity

import domain.Account.valueobject.AccountID
import javax.validation.constraints._

case class Account(usernameParam: String, emailParam: String, passwordParam: String, idParam: Long = 0L) {
  @NotNull(message = "name is required")
  @Max(value = 50, message = "max lenth of name is 50 characters")
  val username: String = usernameParam

  @NotNull(message = "email is required")
  @Email(message = "email is not valid")
  val email: String = emailParam

  @Max(value = 20, message = "max lenth of password is 20 characters")
  @Min(value = 8, message = "password is weak")
  @Pattern(
    regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])$",
    message = "password must contain at lease one lower case letter, one upper case letter and one digit"
  )
  val password: String = passwordParam

  val id: AccountID = AccountID(idParam)
}

