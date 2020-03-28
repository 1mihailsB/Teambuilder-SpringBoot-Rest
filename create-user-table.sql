use website_rest;

drop table if exists users;

CREATE TABLE users (
  googlesub varchar(255) NOT NULL,
  name varchar (50) NOT NULL,
  email varchar (50) NOT NULL,
  nickname varchar (16) NOT NULL UNIQUE,
  enabled tinyint(1) NOT NULL,
  roles VARCHAR (100) NOT NULL,
  permissions VARCHAR (200) NOT NULL,
  PRIMARY KEY (googlesub)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
