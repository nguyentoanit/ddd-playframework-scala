# --- !Ups
CREATE TABLE articles (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    title nvarchar(251) NOT NULL,
    content text  NOT NULL,
    thumbnail varchar(255) NOT NULL,
    createOn date,
    PRIMARY KEY (id)
);

# --- !Downs
DROP TABLE articles;
