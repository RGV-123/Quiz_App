DROP TABLE IF EXISTS quiz_questions;
DROP TABLE IF EXISTS quiz;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS question_seq;
DROP TABLE IF EXISTS quiz_seq;

CREATE TABLE question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question_title VARCHAR(500),
    option1 VARCHAR(255),
    option2 VARCHAR(255),
    option3 VARCHAR(255),
    option4 VARCHAR(255),
    right_answer VARCHAR(255),
    difficultylevel VARCHAR(255),
    category VARCHAR(255)
);

CREATE TABLE quiz (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255)
);

CREATE TABLE quiz_questions (
    quiz_id INT NOT NULL,
    question_id INT NOT NULL,
    PRIMARY KEY (quiz_id, question_id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id),
    FOREIGN KEY (question_id) REFERENCES question(id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);