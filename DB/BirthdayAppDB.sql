create database `BirthdayService`;

use `BirthdayService`;

create table `app_installs`(
`id` INTEGER NOT NULL AUTO_INCREMENT,
`auth_token` VARCHAR(50) NOT NULL,
`user_id` VARCHAR(50) NOT NULL UNIQUE,
`is_processed` tinyint NOT NULL,
`installation_time` DATETIME DEFAULT NULL,
PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table `employee`(
	`id` INTEGER NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(50) DEFAULT NULL UNIQUE,
    `email` VARCHAR(50) NOT NULL UNIQUE,
    `first_name` VARCHAR(40) NOT NULL,
    `last_name` VARCHAR(40) NOT NULL,
    `role` VARCHAR(20) DEFAULT NULL,
    `profile_image` VARCHAR(80) DEFAULT NULL,
    `dob` DATE DEFAULT NULL,
    `doj` DATE DEFAULT NULL,
    `installation_status` VARCHAR(20) DEFAULT NULL,
    `app_installs_id` INTEGER,
    PRIMARY KEY(`id`),
    FOREIGN KEY (`app_installs_id`) REFERENCES `app_installs`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table `special_events`(
 `id` INTEGER NOT NULL AUTO_INCREMENT,
 `event_code` VARCHAR(30) NOT NULL UNIQUE,
 `employee` INTEGER NOT NULL,
 `event_type` varchar(20) NOT NULL,
 `event_date` DATE NOT NULL,
 `event_status` VARCHAR(20) NOT NULL,
 `greeting_card` BLOB DEFAULT NULL,
 PRIMARY KEY(`id`),
 FOREIGN KEY(`employee`) REFERENCES `employee`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table `testimonials`(
`id` INTEGER NOT NULL AUTO_INCREMENT,
`message` VARCHAR(160) NOT NULL,
`special_event_id` INTEGER NOT NULL,
`from_employee` INTEGER NOT NULL,
`recorded_date` DATE NOT NULL,
PRIMARY KEY(`id`),
FOREIGN KEY(`special_event_id`) REFERENCES `special_events`(`id`),
FOREIGN KEY(`from_employee`) REFERENCES `employee`(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;