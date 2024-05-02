package com.myproject.cleanplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class IndexController {
    @GetMapping("/getsseTest")
    public String sseTest(){
        return "sseTest";
    }

}
