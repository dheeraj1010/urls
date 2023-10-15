package com.dheeraj.pers.urls.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController {


    @GetMapping("/helloworld")
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello, Connected to URLS.", HttpStatus.OK);
    }


}
