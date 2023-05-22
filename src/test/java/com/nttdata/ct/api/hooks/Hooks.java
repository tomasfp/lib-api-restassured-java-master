package com.nttdata.ct.api.hooks;

import com.nttdata.ct.api.stepdefinition.ManageScenario;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private ManageScenario scenario;

    @DataTableType(replaceWithEmptyString = "[blank]")
    public String stringType(String cell) {
        return cell;
    }

    @ParameterType(value = "true|false")
    public Boolean booleanValue(String value) {
        return Boolean.valueOf(value);
    }

    @Before
    public void handleScenario(Scenario scenario) {
        this.scenario.setScenario(scenario);
    }

}