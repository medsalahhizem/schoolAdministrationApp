package com.School.administration.Services;

import com.School.administration.domain.Student;
import com.School.administration.exeptions.EtAuthException;
import com.School.administration.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public Student validateStudent(String userName, String password) throws EtAuthException {
        if(userName != null) userName = userName.toLowerCase();
        return studentRepository.findByUserNameAndPassword(userName, password);
    }

    @Override
    public Student registerStudent(String userName, String firstName, String lastName, String password) throws EtAuthException {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        if (userName != null) userName = userName.toLowerCase();
        if (!pattern.matcher(userName).matches()) {
            throw new EtAuthException("Invalid userName format");
        }

        Integer count = studentRepository.getCountByUserName(userName);
        if (count > 0) {
            throw new EtAuthException("userName already in use");
        }

        Integer studentId = studentRepository.create(userName, firstName, lastName, password);
        return studentRepository.findById(studentId);
    }

    @Override
    public void updateStudent(Integer studentId, String userName, String firstName, String lastName, String password) throws EtAuthException {
        // This method assumes that a student with the given studentId exists.
        // Implement logic to handle this assumption as necessary.
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new EtAuthException("Student not found");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        studentRepository.update(studentId, userName, firstName, lastName, hashedPassword);
    }

    @Override
    public void deleteStudent(Integer studentId) throws EtAuthException {
        // This method assumes that a student with the given studentId exists.
        // Implement logic to handle this assumption as necessary.
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            throw new EtAuthException("Student not found");
        }

        studentRepository.delete(studentId);
    }
}
