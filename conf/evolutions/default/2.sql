# --- !Ups
CREATE TABLE accounts (
  id         bigint(20)   NOT NULL AUTO_INCREMENT,
  username   varchar(51)  NOT NULL,
  email      varchar(255) NOT NULL,
  password   varchar(21)  NOT NULL,
  created_at timestamp    NOT NULL DEFAULT NOW(),
  updated_at timestamp    NOT NULL DEFAULT NOW()
  ON UPDATE now(),
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE accounts;