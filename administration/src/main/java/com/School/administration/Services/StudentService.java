package com.School.administration.Services;

import com.School.administration.domain.Student;
import com.School.administration.exeptions.EtAuthException;

public interface StudentService {
    Student validateStudent(String userName, String password) throws EtAuthException;
    Student registerStudent(String userName, String firstName, String lastName, String password) throws EtAuthException;
    void updateStudent(Integer studentId, String userName, String firstName, String lastName, String password) throws EtAuthException;
    void deleteStudent(Integer studentId) throws EtAuthException;
}
