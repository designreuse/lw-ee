package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class MainPageController {

    @RequestMapping("/manager")
    public ModelAndView showManagerMainPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/MainPage");

        return mav;
    }
}
