package com.School.administration.resouces;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classes")
public class ClassResource {

    @GetMapping("")
    public String getAllCourses(HttpServletRequest request) {
        int studentId = (Integer) request.getAttribute("userId");
        return "Authenticated! StudentId: " + studentId;
    }
}
