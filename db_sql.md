````mysql
```
DROP DATABASE IF EXISTS article_manager;
CREATE DATABASE article_manager;
```
````

````mysql

```

DROP DATABASE IF EXISTS article_manager;
CREATE DATABASE article_manager;
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` TEXT NOT NULL
);

DESC article;
SELECT * FROM article;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = CONCAT('제목',RAND()),
`body` = CONCAT('내용',RAND());

SELECT * FROM article;
```
```
String sql ="INSERT INTO article";
sql += " SET regDate = NOW()";
sql += ", updateDate = NOW()";
sql += ", title = CONCAT('제목',RAND())";
sql += ", `body` = CONCAT('내용',RAND())";
```
