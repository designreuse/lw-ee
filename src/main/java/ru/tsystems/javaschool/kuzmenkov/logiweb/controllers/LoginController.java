package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String login() {
        return "main/Login";
    }
}
