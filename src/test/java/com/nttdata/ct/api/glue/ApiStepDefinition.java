package com.nttdata.ct.api.glue;

import com.nttdata.ct.api.ApiAutomationApplication;
import com.nttdata.ct.api.step.ApiStep;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = ApiAutomationApplication.class)
public class ApiStepDefinition {

    @Autowired
    private ApiStep apiStep;

    @Given("que configuro las cabeceras del servicio")
    public void queConfiguroLasCabecerasDelServicio(DataTable dataTable) {
        apiStep.configurarCabeceras(dataTable);
    }

    @Given("que configuro los queryParams")
    public void queConfiguroLosQueryParams(DataTable dataTable) {
        apiStep.configuroQueryParams(dataTable);
    }

    @Given("que configuro los pathParams")
    public void queConfiguroLosPathParams(DataTable dataTable) {
        apiStep.configuroPathParams(dataTable);
    }

    @And("que configuro el bodyRequest: {string}")
    public void queConfiguroElBodyRequest(String requestPath, DataTable dataTable) {
        apiStep.configurarBodyRequest(requestPath, dataTable);
    }

    @When("ejecuto el servicio {string}")
    public void ejecutoElServicio(String serviceConfig) {
        apiStep.ejecutarServicio(serviceConfig);
    }

    @Then("valido que el codigo de respuesta sea {int}")
    public void validoQueElCodigoDeRespuestaSea(int statusCode) {
        Assert.assertEquals(statusCode, apiStep.getStatusCode());
    }

    @And("valido el valor de la respuesta")
    public void validoElValorDeLaRespuesta(DataTable dataTable) {
        apiStep.validarRespuesta(dataTable);
    }

}