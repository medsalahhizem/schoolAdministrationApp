package com.School.administration.resouces;

import com.School.administration.Constants;
import com.School.administration.domain.Student;
import com.School.administration.Services.StudentService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class studentResource {

    @Autowired
    StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginStudent(@RequestBody Map<String, Object> studentMap) {
        String userName = (String) studentMap.get("userName");
        String password = (String) studentMap.get("password");
        Student student = studentService.validateStudent(userName, password);
        return new ResponseEntity<>(generateJWTToken(student), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerStudent(@RequestBody Map<String, Object> studentMap) {
        String userName = (String) studentMap.get("userName");
        String firstName = (String) studentMap.get("firstName");
        String lastName = (String) studentMap.get("lastName");
        String password = (String) studentMap.get("password");
        Student student = studentService.registerStudent(userName, firstName, lastName, password);
        Map<String, String> map = new HashMap<>();
        map.put("message", "registered successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateStudent(@PathVariable("id") Integer studentId, @RequestBody Map<String, Object> studentMap) {
        String userName = (String) studentMap.get("userName");
        String firstName = (String) studentMap.get("firstName");
        String lastName = (String) studentMap.get("lastName");
        String password = (String) studentMap.get("password");
        studentService.updateStudent(studentId, userName, firstName, lastName, password);
        Map<String, String> map = new HashMap<>();
        map.put("message", "updated successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable("id") Integer studentId) {
        studentService.deleteStudent(studentId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "deleted successfully");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(Student student) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("userId", student.getStudentId())
                .claim("userName", student.getUserName())
                .claim("firstName", student.getFirstName())
                .claim("lastName", student.getLastName())
                .compact();

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
