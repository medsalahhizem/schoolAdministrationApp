package com.School.administration.repositories;

import com.School.administration.domain.Student;
import com.School.administration.exeptions.EtAuthException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private static final String SQL_CREATE = "INSERT INTO ET_STUDENTS (USER_NAME, FIRST_NAME, LAST_NAME, PASSWORD) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_STUDENTS WHERE USER_NAME = ?";
    private static final String SQL_FIND_BY_ID = "SELECT STUDENT_ID, FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD " +
            "FROM ET_STUDENTS WHERE STUDENT_ID = ?";
    private static final String SQL_FIND_BY_USER_NAME = "SELECT STUDENT_ID, FIRST_NAME, LAST_NAME, USER_NAME, PASSWORD " +
            "FROM ET_USERS WHERE USER_NAME= ?";
    private static final String SQL_UPDATE = "UPDATE ET_STUDENTS SET USER_NAME = ?, FIRST_NAME = ?, LAST_NAME = ?, PASSWORD = ? WHERE STUDENT_ID = ?";
    private static final String SQL_DELETE = "DELETE FROM ET_STUDENTS WHERE STUDENT_ID = ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String userName, String firstName, String lastName, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, userName);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, hashedPassword);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("STUDENT_ID");
        } catch (Exception e) {
            throw new EtAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public Student findByUserNameAndPassword(String userName, String password) throws EtAuthException {
        try {
            Student student = jdbcTemplate.queryForObject(SQL_FIND_BY_USER_NAME, new Object[] { userName }, studentRowMapper);
            if (!BCrypt.checkpw(password, student.getPassword())) {
                throw new EtAuthException("Invalid username/password");
            }
            return student;
        } catch (EmptyResultDataAccessException e) {
            throw new EtAuthException("Invalid username/password");
        }
    }

    @Override
    public Integer getCountByUserName(String userName) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{userName}, Integer.class);
    }

    @Override
    public Student findById(Integer studentId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{studentId}, studentRowMapper);
    }

    @Override
    public void update(Integer studentId, String userName, String firstName, String lastName, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            jdbcTemplate.update(SQL_UPDATE, userName, firstName, lastName, hashedPassword, studentId);
        } catch (Exception e) {
            throw new EtAuthException("Failed to update student details");
        }
    }

    @Override
    public void delete(Integer studentId) {
        jdbcTemplate.update(SQL_DELETE, studentId);
    }

    private RowMapper<Student> studentRowMapper = (rs, rowNum) -> {
        return new Student(
                rs.getLong("STUDENT_ID"),
                rs.getString("USER_NAME"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("PASSWORD")
        );
    };
}
