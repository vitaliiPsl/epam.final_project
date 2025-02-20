DROP TABLE IF EXISTS role CASCADE;
CREATE TABLE role
(
    id   SERIAL,
    name VARCHAR(50),
    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uq_role_name UNIQUE (name)
);

DROP TABLE IF EXISTS person CASCADE;
CREATE TABLE person
(
    id         BIGSERIAL,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(50)  NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(100) NOT NULL,
    is_blocked BOOL DEFAULT FALSE,
    role_id    INT,
    image_name VARCHAR(256),
    CONSTRAINT pk_person PRIMARY KEY (id),
    CONSTRAINT fk_person_role FOREIGN KEY (role_id)
        REFERENCES role (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT uq_person_email UNIQUE (email)
);

DROP TABLE IF EXISTS language CASCADE;
CREATE TABLE language
(
    id   BIGSERIAL,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(10) NOT NULL,
    CONSTRAINT pk_language PRIMARY KEY (id)
);


DROP TABLE IF EXISTS language_translation CASCADE;
CREATE TABLE language_translation
(
    language_id             BIGINT      NOT NULL,
    translation_language_id BIGINT      NOT NULL,
    name_translation        VARCHAR(50) NOT NULL,
    code_translation        VARCHAR(10) NOT NULL,
    CONSTRAINT pk_language_translation PRIMARY KEY (language_id, translation_language_id),
    CONSTRAINT fk_language_translation_language FOREIGN KEY (language_id)
        REFERENCES language (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_language_translation_language_translation FOREIGN KEY (translation_language_id)
        REFERENCES language (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

DROP TABLE IF EXISTS course CASCADE;
CREATE TABLE course
(
    id          BIGSERIAL,
    teacher_id  BIGINT       NOT NULL,
    subject_id  BIGINT       NOT NULL,
    language_id INT,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(512),
    max_score   INT,
    start_date  TIMESTAMP,
    end_date    TIMESTAMP,
    image_name  VARCHAR(256),
    CONSTRAINT pk_course PRIMARY KEY (id),
    CONSTRAINT fk_course_person FOREIGN KEY (teacher_id)
        REFERENCES person (id),
    CONSTRAINT fk_course_subject FOREIGN KEY (subject_id)
        REFERENCES subject (id),
    CONSTRAINT fk_course_language FOREIGN KEY (language_id)
        REFERENCES language (id)
);

-- bridge table between student(person) and course (person M : N course)
DROP TABLE IF EXISTS student_course;
CREATE TABLE student_course
(
    student_id        BIGINT NOT NULL,
    course_id         BIGINT NOT NULL,
    score             INT,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_student_course PRIMARY KEY (student_id, course_id),
    CONSTRAINT fk_student_course_person FOREIGN KEY (student_id)
        REFERENCES person (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_student_course_course FOREIGN KEY (course_id)
        REFERENCES course (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

DROP TABLE IF EXISTS subject CASCADE;
CREATE TABLE subject
(
    id   BIGSERIAL,
    name VARCHAR(50),
    CONSTRAINT pk_subject PRIMARY KEY (id)
);

DROP TABLE IF EXISTS subject_translation CASCADE;
CREATE TABLE subject_translation
(
    subject_id       BIGINT,
    language_id      BIGINT,
    name_translation VARCHAR(50),
    CONSTRAINT pk_subject_translation PRIMARY KEY (subject_id, language_id),
    CONSTRAINT fk_subject_translation_subject FOREIGN KEY (subject_id)
        REFERENCES subject (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_subject_translation_language FOREIGN KEY (language_id)
        REFERENCES language (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- students view
DROP VIEW IF EXISTS student;
CREATE VIEW student AS
SELECT *
FROM person
WHERE role_id = (SELECT id FROM role WHERE name ilike 'student');

-- teacher view
DROP VIEW IF EXISTS teacher;
CREATE VIEW teacher AS
SELECT *
FROM person
WHERE role_id = (SELECT id FROM role WHERE name ilike 'teacher');


-- insert roles
INSERT INTO role(name)
VALUES ('admin'),
       ('teacher'),
       ('student');

-- insert languages
INSERT INTO language(id, name, code)
VALUES (1, 'english', 'en'),
       (2, 'ukrainian', 'uk');

INSERT INTO language_translation(language_id, translation_language_id, name_translation, code_translation)
VALUES (1, 1, 'english', 'en'),
       (1, 2, 'англійська', 'англ'),
       (2, 1, 'ukrainian', 'uk'),
       (2, 2, 'українська', 'укр');
