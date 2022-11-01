package org.rvsoft.aspect;

import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Log4j
public class LoggerAspect {
    @Pointcut("execution(* org.rvsoft.controller.TelegramBot.*(..))")
    public  void loggingBot() {
    }

    @Before("loggingBot()")
    public void beforeCallMethod(JoinPoint jp) {
        log.info("Method called: " + jp.getSignature().getName() + "\n" +
                "Proxy: " + jp.getThis() + "\n" +
                "Target: " + jp.getTarget() + "\n" +
                "Arguments: " + Arrays.toString(jp.getArgs()) + "\n");
    }
}
