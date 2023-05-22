package com.nttdata.ct.api.service.aspect.log;

import com.nttdata.ct.api.service.util.UtilApi;
import com.nttdata.ct.api.stepdefinition.ManageScenario;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private ManageScenario scenario;

    @AfterThrowing(value = "execution(* com.pichincha.sw.api.glue.*.*(..))", throwing = "e")
    public void screenShotThrowingAllMethods(JoinPoint point, Throwable e) {
        if (scenario != null) {
            UtilApi.logger(this.getClass()).log(Level.WARNING,
                    "\n" +
                            "\nSomething went wrong!  >>> \n" +
                            "Feature: \"{0}\",\n" +
                            "Scenario: \"{1}\",\n" +
                            "Class: \"{2}\",\n" +
                            "Method: \"{3}\",\n" +
                            "Throwable: \"{4}\""
                    , new Object[]{
                            ManageScenario.getScenario().getUri(),
                            ManageScenario.getScenario().getName(),
                            point.getSignature().getDeclaringTypeName(),
                            point.getSignature().getName(),
                            "Message: " + e.getMessage() + " - Cause:" + e.getCause()});
        }
    }

    @Pointcut("execution(public * *(..)) && @within(LogTime)") //this should work for the public pointcut
    public void anyMethodWithLogTimeTag() {
        //Do nothing because is a poincut
    }

    @Around("anyMethodWithLogTimeTag()")
    public Object logTimeMethod(ProceedingJoinPoint point) throws Throwable {
        long initTime = System.currentTimeMillis();
        Object obj = point.proceed();
        long endTime = System.currentTimeMillis();
        UtilApi.logger(this.getClass()).log(Level.INFO, "Method >>> \"{0}\", " +
                        "Time for execution >>> {1} ms.",
                new Object[]{
                        point.getSignature().getName(),
                        (endTime - initTime)});
        return obj;
    }

    @After("@annotation(LogParams)")
    public void logParamsMethod(JoinPoint point) {
        UtilApi.logger(this.getClass()).log(Level.INFO,
                "\n- Method >>> {0}",
                point.getSignature());
        for (Object arg : point.getArgs()) {
            UtilApi.logger(this.getClass()).log(Level.INFO,
                    "\n- Params >>>\n{0}", arg);
        }
    }

}
