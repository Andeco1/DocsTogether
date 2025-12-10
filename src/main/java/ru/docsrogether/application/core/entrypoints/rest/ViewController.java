package ru.docsrogether.application.core.entrypoints.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // вернет login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // вернет register.html
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile"; // вернет profile.html
    }

    @GetMapping("/")
    public String indexPage() {
        return "redirect:/profile"; // На главной перенаправляем в профиль
    }
}