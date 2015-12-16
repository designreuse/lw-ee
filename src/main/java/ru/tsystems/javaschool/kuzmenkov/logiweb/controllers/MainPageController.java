package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Nikolay Kuzmenkov.
 */
@Controller
public class MainPageController {

    @RequestMapping("/manager")
    public ModelAndView frontPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("main/MainPage");

        return mav;
    }
}
