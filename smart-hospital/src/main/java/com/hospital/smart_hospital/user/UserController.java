package com.hospital.smart_hospital.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping()
    public String name(){
        return "Hello Hospital";
    }

}
