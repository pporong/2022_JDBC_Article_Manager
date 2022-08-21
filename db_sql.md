DROP DATABASE IF EXISTS article_manager;
CREATE DATABASE article_manager;

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

STRING SQL ="INSERT INTO article";
SQL += " SET regDate = NOW()";
SQL += ", updateDate = NOW()";
SQL += ", title = CONCAT('제목',RAND())";
SQL += ", `body` = CONCAT('내용',RAND())";

SELECT COUNT(*) 
FROM article
WHERE id = 9;

CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    loginId CHAR(20) NOT NULL,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(200) NOT NULL
);

SELECT * FROM `member`;
DESC `member`;

SELECT COUNT(*) = 0
FROM `member`
WHERE loginId = 'aaa1';
INSERT INTO article  SET regDate = NOW() , updateDate = NOW() , title = '123' , `body = '123';
# select rand()
