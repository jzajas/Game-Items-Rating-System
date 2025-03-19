package com.jzajas.RatingSystem.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Pointcut("within(com.jzajas.RatingSystem.Controllers..*) && !within(com.jzajas.RatingSystem.Controllers.AdminController)")
    private void allPublicControllers() {
    }

    @Before("allPublicControllers()")
    public void beforeMethodNameAndParams(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Anonymous";

        log.info("User '{}' is attempting to execute: {} with arguments: {}", username, methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "allPublicControllers()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();

        log.info("Execution of : " + methodName + " Completed successfully with return type: " + result);
    }
}
