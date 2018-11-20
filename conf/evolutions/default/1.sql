# --- !Ups
CREATE TABLE articles (
  id         bigint(20)    NOT NULL AUTO_INCREMENT,
  title      nvarchar(251) NOT NULL,
  content    longtext      NOT NULL,
  thumbnail  varchar(255)  NOT NULL,
  created_at timestamp     NOT NULL DEFAULT NOW(),
  updated_at timestamp     NOT NULL DEFAULT NOW()
  ON UPDATE now(),
  PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE articles;
