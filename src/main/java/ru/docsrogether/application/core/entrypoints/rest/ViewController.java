package ru.docsrogether.application.core.entrypoints.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

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
        return "landing";
    }

    @GetMapping("/landing")
    public String landing() {
        return "landing";
    }

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents";
    }

    @GetMapping("/share/{token}")
    public String shareLanding(@PathVariable String token, @RequestParam(value = "redirect", required = false) String redirect) {
        return "share";
    }
}