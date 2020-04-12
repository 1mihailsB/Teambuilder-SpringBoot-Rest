use website_rest;

SET FOREIGN_KEY_CHECKS = 0;

drop table if exists users;
drop table if exists gameplans;

CREATE TABLE users (
  `googlesub` varchar(255) UNIQUE NOT NULL,
  `name` varchar (50) NOT NULL,
  `email` varchar (50) NOT NULL,
  `nickname` varchar (16) UNIQUE,
  `enabled` tinyint(1) NOT NULL,
  `role` VARCHAR (30) NOT NULL,
  PRIMARY KEY (`googlesub`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE gameplans (
	`id` int(11) NOT NULL AUTO_INCREMENT,
    `title` varchar(50) NOT NULL,
    `main_text` varchar(3000),
    `author_googlesub` varchar (50) NOT NULL,
    `creation_datetime` DATETIME not null,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_gameplans` FOREIGN KEY (`author_googlesub`)
    REFERENCES `users` (`googlesub`) ON DELETE NO ACTION,
    CONSTRAINT `UQ_author_title_` UNIQUE(`author_googlesub`, `title`)
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 0;