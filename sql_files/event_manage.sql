use events;

DROP TABLE IF EXISTS `access_tokens`;
CREATE TABLE access_tokens (
  id                BIGINT          NOT NULL AUTO_INCREMENT,
  token             varchar(200)    NOT NULL UNIQUE,
  user_id           BIGINT          DEFAULT NULL,
  role              varchar(200)    NOT NULL,
  create_time       timestamp       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);


DROP TABLE IF EXISTS `students`;
CREATE TABLE students (
  id                BIGINT          NOT NULL AUTO_INCREMENT,
  first_name        varchar(200)    NOT NULL,
  last_name         varchar(200)    NOT NULL,
  email             varchar(255)    NOT NULL UNIQUE,
  password          varchar(200)    NOT NULL,
  faculty           varchar(15)     DEFAULT NULL,
  index_no          varchar(15)     DEFAULT NULL,
  contact_no        varchar(15)     DEFAULT NULL,
  verified          BIT             DEFAULT 0,
  create_time       timestamp       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `admins`;
CREATE TABLE admins (
  id                BIGINT          NOT NULL AUTO_INCREMENT,
  first_name        varchar(200)    NOT NULL,
  last_name         varchar(200)    NOT NULL,
  email             varchar(255)    NOT NULL,
  password          varchar(200)    NOT NULL,
  faculty           varchar(15)      DEFAULT NULL,
  verified          BIT             DEFAULT 0,
  create_time       timestamp       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS `event_types`;
CREATE TABLE event_types (
  id               INT             NOT NULL AUTO_INCREMENT,
  name             varchar(200)    NOT NULL,
  PRIMARY KEY (id)
);

-- event type data
insert into event_types (name) VALUES ('Event type 1');
insert into event_types (name) VALUES ('Event type 2');



DROP TABLE IF EXISTS `venues`;
CREATE TABLE venues (
  id               INT             NOT NULL AUTO_INCREMENT,
  name             varchar(200)    NOT NULL,
  PRIMARY KEY (id)
);

insert into venues (name) VALUES ('Venue 1');
insert into venues (name) VALUES ('Venue 2');


DROP TABLE IF EXISTS `events`;
CREATE TABLE events (
  id                BIGINT          NOT NULL AUTO_INCREMENT,
  name              varchar(200)    NOT NULL,
  event_date        DATE            NOT NULL,
  event_time        TIME            NOT NULL,
  student_id        BIGINT          NOT NULL,
  admin_id          BIGINT          DEFAULT NULL,
  event_type_id     INT             NOT NULL,
  venue_id          INT             NOT NULL,
  permission_file   VARCHAR (200)   DEFAULT NULL,
  status            varchar(10)     DEFAULT 'pending',
  create_time       timestamp       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_event_student FOREIGN KEY (student_id) REFERENCES students(id),
  CONSTRAINT fk_event_admin   FOREIGN KEY (admin_id)   REFERENCES admins(id),
  CONSTRAINT fk_event_event_type   FOREIGN KEY (event_type_id)   REFERENCES event_types(id),
  CONSTRAINT fk_event_event_venue   FOREIGN KEY (venue_id)       REFERENCES venues(id)
);

DROP TABLE IF EXISTS `verification_code`;
CREATE TABLE verification_code (
  id                BIGINT          NOT NULL AUTO_INCREMENT,
  email             varchar(200)    NOT NULL,
  code              varchar(200)    NOT NULL,
  user_type         varchar(10)     NOT NULL,
  create_time       timestamp       DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

