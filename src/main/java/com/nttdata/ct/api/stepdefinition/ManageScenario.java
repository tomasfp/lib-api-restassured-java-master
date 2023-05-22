package com.nttdata.ct.api.stepdefinition;

import io.cucumber.java.Scenario;
import org.springframework.stereotype.Component;

@Component
public class ManageScenario {

    private static final ThreadLocal<Scenario> threadLocal = new ThreadLocal<>();

    public void removeThread() {
        threadLocal.remove();
    }

    public void setScenario(Scenario scenario) {
        threadLocal.set(scenario);
    }

    public static Scenario getScenario() {
        return threadLocal.get();
    }

}