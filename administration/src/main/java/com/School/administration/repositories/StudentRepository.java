package com.School.administration.repositories;

import com.School.administration.domain.Student;
import com.School.administration.exeptions.EtAuthException;

public interface StudentRepository {
    Integer create(String userName, String firstName, String lastName, String password) throws EtAuthException;
    Student findByUserNameAndPassword(String userName, String password) throws EtAuthException;
    Integer getCountByUserName(String userName);
    Student findById(Integer studentId);

    void update(Integer studentId, String userName, String firstName, String lastName, String password) throws EtAuthException;
    void delete(Integer studentId);
}
