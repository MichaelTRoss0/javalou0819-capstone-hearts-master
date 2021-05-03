/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  dnsfu
 * Created: Oct 25, 2019
 */

DROP DATABASE IF EXISTS ShrekBlogDB;
CREATE DATABASE ShrekBlogDB;

USE ShrekBlogDB;

DROP TABLE IF EXISTS Memo;
CREATE TABLE Memo (
	memoId INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    bodyText MEDIUMTEXT NOT NULL,
    uploadDate TIMESTAMP NOT NULL,
    deleteDate TIMESTAMP,
    isApproved BOOL NOT NULL
);

DROP TABLE IF EXISTS Tag;
CREATE TABLE Tag (
	tagId INT PRIMARY KEY AUTO_INCREMENT,
    hashTag VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS MemoTag;
CREATE TABLE MemoTag (
	memoId INT NOT NULL,
	tagId INT NOT NULL,
    PRIMARY KEY pk_MemoTag(memoId, tagId),
    FOREIGN KEY fk_memo(memoId)
		REFERENCES Memo(memoId),
	FOREIGN KEY fk_tag(tagId)
		REFERENCES Tag(tagId)
);

DROP TABLE IF EXISTS `User`;
CREATE TABLE `User` (
	userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    privilege TINYINT NOT NULL,
    enabled BOOLEAN NOT NULL
);

DROP TABLE IF EXISTS MemoUser;
CREATE TABLE MemoUser (
	memoId INT NOT NULL,
	userId INT NOT NULL,
    PRIMARY KEY pk_MemoUser(memoId, userId),
    FOREIGN KEY fk_memo(memoId)
		REFERENCES Memo(memoId),
	FOREIGN KEY fk_user(userId)
		REFERENCES `User`(userId)
);

drop table if exists `Role`;
create table `Role`(
    roleId int primary key auto_increment,
    `role` varchar(30) not null
);

drop table if exists UserRole;
create table UserRole(
    userId INT NOT NULL,
    roleId INT NOT NULL,
    PRIMARY KEY pk_UserRole(userId, roleId),
    FOREIGN KEY fk_user(userId)
        REFERENCES `User`(userId),
    FOREIGN KEY fk_role(roleId)
        REFERENCES `Role`(roleId));

-- To make a Memo:
-- INSERT INTO Memo AND INSERT INTO MemoUser [ Make sure the IDs are correct in the MemoUser. Each Memo HAS to have a Creator. ]

-- To make a Tag:
-- INSERT INTO Tag AND INSERT INTO MemoTag. [ Make sure the IDs are correct in the MemoTag 

INSERT INTO Memo (title, bodyText, uploadDate, deleteDate, isApproved) VALUES ("Donkey's Waffles are Great", "Every day I smell Donkey cooking his waffles. He gave me some. Very delicious.", '1970-01-01 00:00:01', '1970-01-05 07:00:01', 1);
INSERT INTO Tag (hashTag) VALUES ("#TestTag");
INSERT INTO Tag (hashTag) VALUES ("#Donkey");
INSERT INTO MemoTag (memoId, tagId) VALUES (1, 1);
INSERT INTO MemoTag (memoId, tagId) VALUES (1, 2);

INSERT INTO Memo (title, bodyText, uploadDate, deleteDate, isApproved) VALUES ("Why you should live in the swamp", "Cheap land, great people, night life.", '1970-01-01 00:00:01', '1970-01-05 07:00:01', 1);


INSERT INTO Memo (title, bodyText, uploadDate, deleteDate, isApproved) VALUES ("Donkey's Waffles are Great", "Once upon a time in a far away place, the polite tiara in a small horse quickly dreamed the proud knight. The ogre in the magic beanstalk somewhere dreamed those tiny curses. An Evil Queen somewhere sang a couple Aladdins. The tiara in the beautiful Little Red Riding Hood daily sings a fast Ginger Bread Man. Fast Little Red Riding Hoods daily eat brave Aladdins. The happy Little Red Riding Hood on a curse wishfully bites a couple polite dragons. Elegant Aladdins slowly see the dazzling Prince Charmings. The happy Fairy God Mothers lazily show a Fairy God Mother.

Both fairies here showed the proud Prince Charming. The polite horse quite bought the Dumbo. The faithful dwarf in the Rapunzel here walks both princesses. Fast princesses of witty princes daily sang the bald knight. The dragon never runs a couple curses.

A wonderful Big Bad Wolf slowly sees the polite castles. Slow Aladdins quite said a horse. The Evil Queen beautifully went Evil Queens. The clumsy Evil Queen slowly bought the happy wand. Those faithful princes in a couple horses rather say dazzling ogres. The happy Aladdin quite bites the Dumbos. Both beanstalks terribly ride a Snow White. A prince really kiss a tiny ogre. The wand really kiss elegant towers. Those horses in both elegant forests soon sang the knight, and they lived happily ever after.", '1970-01-01 14:32:35', '1970-01-05 07:00:01', 1);



INSERT INTO Memo (title, bodyText, uploadDate, deleteDate, isApproved) VALUES ("This post is not approved", "Cheap land, great people, night life.", '1970-01-01 00:00:01', '1970-01-05 07:00:01', 0);


insert into `user`(`userId`,`username`,`password`,`privilege`, `enabled`)
    values(1,"admin", "password", 1, true),
        (2,"user","password", 2, true);

insert into `Role`(roleId,`role`)
    values(1,"ROLE_ADMIN"), (2,"ROLE_USER");
    
insert into UserRole(userId,roleId)
    values(1,1),(1,2),(2,2);
    
INSERT INTO MemoUser (memoId, userId) VALUES (1, 1);
INSERT INTO MemoUser (memoId, userId) VALUES (2, 2);
INSERT INTO MemoUser (memoId, userId) VALUES (3, 2);
INSERT INTO MemoUser (memoId, userId) VALUES (4, 1);
UPDATE `User` SET `password` = '$2a$10$wfaoTQCOZII6uYNOTd1fiOJMfkZS.432cPTbt7MyZB7G9IJ6WbT66' WHERE userId = 1;
UPDATE `User` SET `password` = '$2a$10$wfaoTQCOZII6uYNOTd1fiOJMfkZS.432cPTbt7MyZB7G9IJ6WbT66' WHERE userId = 2;