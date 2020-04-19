use website_rest;

SET FOREIGN_KEY_CHECKS = 0;

drop table if exists users;
drop table if exists gameplans;
drop table if exists friendships;

CREATE TABLE users (
  `googlesub` varchar(255) UNIQUE NOT NULL,
  `name` varchar (50) NOT NULL,
  `email` varchar (50) NOT NULL,
  `creation_datetime` DATETIME not null,
  `enabled` tinyint(1) NOT NULL,
  `role` VARCHAR (30) NOT NULL,
  `nickname` varchar (16) UNIQUE,
  PRIMARY KEY (`googlesub`),
  KEY (`nickname`)
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

CREATE TABLE friendships (
`id` int(11) NOT NULL AUTO_INCREMENT,
`friend_1_googlesub` varchar(255),
`friend_2_googlesub` varchar(255),
`status`  tinyint(1) NOT NULL,
PRIMARY KEY (`id`),
KEY `FK_friend_1_idx` (`friend_1_googlesub`),
KEY `FK_friend_2_idx` (`friend_2_googlesub`),
UNIQUE KEY `fiends_combination` (`friend_1_googlesub`, `friend_2_googlesub`),

CHECK(`status` between 0 and 1),
CHECK(`friend_1_googlesub`<>`friend_2_googlesub`),

CONSTRAINT `FK_friend_1` FOREIGN KEY (`friend_1_googlesub`)
REFERENCES `users` (`googlesub`)
ON DELETE NO ACTION ON UPDATE NO ACTION,

CONSTRAINT `FK_friend_2` FOREIGN KEY (`friend_2_googlesub`)
REFERENCES `users` (`googlesub`)
ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

delimiter $
create trigger uniquefriendships before insert on friendships
for each row
begin
  if exists (
    select *
    from friendships
    where friend_1_googlesub = new.friend_2_googlesub and friend_2_googlesub = new.friend_1_googlesub
  )
  then
    signal sqlstate '45000' set message_text = ' uniquefriendships trigger: inverse friendship exists! ';
  end if;
end$
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;