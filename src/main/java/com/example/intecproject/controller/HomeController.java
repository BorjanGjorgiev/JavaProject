package com.example.intecproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{


    @GetMapping("/homepage")
    public String homePage()
    {
        return "index";
    }

    @GetMapping("/about")
    public String aboutPage()
    {
        return "about";
    }
}
