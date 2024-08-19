-- Drop the database and user if they already exist
DROP DATABASE IF EXISTS schooldb;
DROP USER IF EXISTS schooluser;

-- Create a new user and database
CREATE USER schooluser WITH PASSWORD 'password';
CREATE DATABASE schooldb WITH OWNER=schooluser;

-- Connect to the newly created database
\connect schooldb;

-- Grant privileges to the user
ALTER DEFAULT PRIVILEGES GRANT ALL ON TABLES TO schooluser;
ALTER DEFAULT PRIVILEGES GRANT ALL ON SEQUENCES TO schooluser;

-- Create the Students table
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    user_name VARCHAR(30) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL
);

-- Create the Courses table
CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create the junction table to manage the many-to-many relationship
CREATE TABLE student_courses (
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    PRIMARY KEY (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES students (student_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id)
);

-- Create sequences for students and courses
CREATE SEQUENCE students_seq INCREMENT 1 START 1;
CREATE SEQUENCE courses_seq INCREMENT 1 START 1;
