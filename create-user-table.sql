use website_rest;

drop table if exists users;
drop table if exists gameplans;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE users (
  `googlesub` varchar(255) UNIQUE NOT NULL,
  `name` varchar (50) NOT NULL,
  `email` varchar (50) NOT NULL,
  `nickname` varchar (16) UNIQUE,
  `enabled` tinyint(1) NOT NULL,
  `role` VARCHAR (30) NOT NULL,
  PRIMARY KEY (`googlesub`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

SET FOREIGN_KEY_CHECKS = 0;