package ru.tsystems.javaschool.kuzmenkov.logiweb.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * @author Nikolay Kuzmenkov.
 */
@Aspect
public class MainAspect {

    private static final Logger LOGGER = Logger.getLogger(MainAspect.class);

    @Pointcut("execution(* ru.tsystems.javaschool.kuzmenkov.logiweb.services.impl.*.* (..))")
    public void servicePointCut() {}

    @Before("servicePointCut()")
    public void loggingBeforeActionAdvice(JoinPoint joinPoint) {
        LOGGER.info(String.format("A method: {%s} is called, arguments passed: {%s}", joinPoint.toString(), Arrays.toString(joinPoint.getArgs())));
    }
    @After("servicePointCut()")
    public void loggingAfterActionAdvice(JoinPoint joinPoint) {
        LOGGER.info(String.format("Entity %s is successfully created", Arrays.toString(joinPoint.getArgs())));
    }
    @AfterThrowing(value = "execution(* ru.tsystems.javaschool.kuzmenkov.logiweb.services.*.*(..))", throwing = "e")
    public void logExceptions(JoinPoint joinPoint, Exception e){
        LOGGER.error(String.format("Exception thrown at the method: %s, the message is {%s}", joinPoint.toString(), e.getMessage()));
        LOGGER.error("The stack trace is below");
        for (StackTraceElement b : e.getStackTrace()) {
            LOGGER.error("at " + b);
        }
    }
}
