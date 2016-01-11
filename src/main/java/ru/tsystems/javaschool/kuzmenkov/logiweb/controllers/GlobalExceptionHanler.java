package ru.tsystems.javaschool.kuzmenkov.logiweb.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.tsystems.javaschool.kuzmenkov.logiweb.exceptions.LogiwebDAOException;

/**
 * @author Nikolay Kuzmenkov.
 */
@ControllerAdvice
public class GlobalExceptionHanler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LogiwebDAOException.class)
    public ModelAndView exception(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/Error");
        mav.addObject("errorCode", status.value());
        mav.addObject("errorDiscription", status.getReasonPhrase());
        mav.addObject("errorMsg", e.getMessage());

        return mav;
    }
}
