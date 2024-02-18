package com.formssafe.domain.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/hello")
    @ResponseStatus(HttpStatus.OK)
    public String hello() {
        return "Hello";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    @ResponseStatus(HttpStatus.OK)
    public String admin() {
        return "Admin";
    }

    @Autowired
    private TestService testService;

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public List<Test> getAll(){
        return testService.getAll();
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public Test add(@RequestBody Test t){
        return testService.add(t);
    }
}