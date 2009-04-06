
# This is a fix for InnoDB in MySQL >= 4.1.x
# It "suspends judgement" for fkey relationships until are tables are set.
SET FOREIGN_KEY_CHECKS = 0;

#-----------------------------------------------------------------------------
#-- roster
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `roster`;


CREATE TABLE `roster`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	`charclass` INTEGER  NOT NULL,
	`charrace` INTEGER  NOT NULL,
	`ep` FLOAT default 0 NOT NULL,
	`gp` FLOAT default 0 NOT NULL,
	`priority` FLOAT default 0 NOT NULL,
	`joined_on` DATETIME  NOT NULL,
	`is_active` TINYINT default 1 NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `roster_U_1` (`name`),
	INDEX `roster_FI_1` (`charclass`),
	CONSTRAINT `roster_FK_1`
		FOREIGN KEY (`charclass`)
		REFERENCES `classes` (`id`),
	INDEX `roster_FI_2` (`charrace`),
	CONSTRAINT `roster_FK_2`
		FOREIGN KEY (`charrace`)
		REFERENCES `races` (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- classes
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `classes`;


CREATE TABLE `classes`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	PRIMARY KEY (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- races
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `races`;


CREATE TABLE `races`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	PRIMARY KEY (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- raids
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `raids`;


CREATE TABLE `raids`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`note` VARCHAR(512)  NOT NULL,
	`boss` INTEGER  NOT NULL,
	`baseval` FLOAT default 100 NOT NULL,
	`inflatedval` FLOAT default 100 NOT NULL,
	`date` DATETIME  NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `raids_FI_1` (`boss`),
	CONSTRAINT `raids_FK_1`
		FOREIGN KEY (`boss`)
		REFERENCES `bosses` (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- attendees
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `attendees`;


CREATE TABLE `attendees`
(
	`raids_id` INTEGER  NOT NULL,
	`roster_id` INTEGER  NOT NULL,
	PRIMARY KEY (`raids_id`,`roster_id`),
	CONSTRAINT `attendees_FK_1`
		FOREIGN KEY (`raids_id`)
		REFERENCES `raids` (`id`)
		ON DELETE CASCADE,
	INDEX `attendees_FI_2` (`roster_id`),
	CONSTRAINT `attendees_FK_2`
		FOREIGN KEY (`roster_id`)
		REFERENCES `roster` (`id`)
		ON DELETE CASCADE
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- bosses
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `bosses`;


CREATE TABLE `bosses`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	`zone` INTEGER  NOT NULL,
	`baseval` FLOAT default 100 NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `bosses_U_1` (`name`),
	INDEX `bosses_FI_1` (`zone`),
	CONSTRAINT `bosses_FK_1`
		FOREIGN KEY (`zone`)
		REFERENCES `zones` (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- zones
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `zones`;


CREATE TABLE `zones`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `zones_U_1` (`name`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- items
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `items`;


CREATE TABLE `items`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(255)  NOT NULL,
	`playerid` INTEGER  NOT NULL,
	`raidid` INTEGER  NOT NULL,
	`itemid` INTEGER  NOT NULL,
	`baseval` FLOAT  NOT NULL,
	`inflatedval` FLOAT  NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `items_FI_1` (`playerid`),
	CONSTRAINT `items_FK_1`
		FOREIGN KEY (`playerid`)
		REFERENCES `roster` (`id`),
	INDEX `items_FI_2` (`raidid`),
	CONSTRAINT `items_FK_2`
		FOREIGN KEY (`raidid`)
		REFERENCES `raids` (`id`)
)Type=InnoDB;

#-----------------------------------------------------------------------------
#-- decays
#-----------------------------------------------------------------------------

DROP TABLE IF EXISTS `decays`;


CREATE TABLE `decays`
(
	`id` INTEGER  NOT NULL AUTO_INCREMENT,
	`performed_at` DATETIME  NOT NULL,
	PRIMARY KEY (`id`)
)Type=InnoDB;

# This restores the fkey checks, after having unset them earlier
SET FOREIGN_KEY_CHECKS = 1;
