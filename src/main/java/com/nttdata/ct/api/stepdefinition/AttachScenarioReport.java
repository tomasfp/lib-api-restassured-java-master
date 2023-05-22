package com.nttdata.ct.api.stepdefinition;

import com.nttdata.ct.api.service.io.ManageFiles;
import com.nttdata.ct.api.service.util.UtilApi;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;

import java.util.logging.Level;

public class AttachScenarioReport {

    private AttachScenarioReport() {
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends AttachScenarioReport> THIS_CLASS = AttachScenarioReport.class;

    public static void getEvidence(QueryableRequestSpecification requestSpecification, Response responseBody) {

        var evidence = ManageFiles.readAsString("evidence/req-res-evidence.txt");
        try {
            evidence = String.format(evidence,
                    ManageScenario.getScenario().getName(),
                    ManageScenario.getScenario().getUri(),
                    Thread.currentThread().getId(), Thread.currentThread().getName(),
                    requestSpecification.getMethod(),
                    requestSpecification.getURI(),
                    requestSpecification.getCookies(),
                    requestSpecification.getContentType(),
                    requestSpecification.getHeaders(),
                    requestSpecification.getPathParams(),
                    requestSpecification.getFormParams(),
                    requestSpecification.getQueryParams(),
                    requestSpecification.getRequestParams(),
                    requestSpecification.getBody(),
                    responseBody.getStatusLine(),
                    responseBody.getHeaders(),
                    responseBody.getCookies(),
                    responseBody.getTime(),
                    responseBody.getSessionId(),
                    responseBody.getBody().peek().asString());
            UtilApi.logger(THIS_CLASS).log(Level.INFO, "Attaching evidence into Cucumber Report...");
            UtilApi.logger(UtilApi.class).log(Level.INFO, evidence);
            ManageScenario.getScenario().attach(evidence.getBytes(), "text/plain", "service_evidence");
        } catch (NullPointerException e) {
            UtilApi.logger(AttachScenarioReport.class).warning(
                    "The aspect annotation '@ServiceTracking' is only active if the class Scenario from cucumber is set by ManageScenario");
        }
    }

}