package com.nttdata.ct.api.jira;

import com.nttdata.ct.api.service.constans.Constants;
import com.nttdata.ct.api.service.io.ManageFiles;
import com.nttdata.ct.api.service.util.UtilApi;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.stereotype.Component;

import java.util.logging.Level;

@Component
public class JiraXrayIntegration extends JiraProperties {

    private final String CUCUMBER_JSON_PATH = System.getProperty("user.dir") + "%s";

    public void importJXrayExecutionResults(String cucumberJsonPath) {
        if (jxrayIntegration) {
            if (jxrayPropertiesDefined()) {
                UtilApi.logger(JiraProperties.class).warning(ManageFiles.readAsString("logs/log-jira-properties-notdefined.txt"));
            } else {
                RestAssured.useRelaxedHTTPSValidation();
                Response jwt = getJWTTokenAuth();
                if (jwt.getStatusCode() != 200) {
                    UtilApi.logger(JiraXrayIntegration.class).log(Level.WARNING, "Ocurrio un error al generar el token de autentificacion: {0}",
                            jwt.getStatusLine());
                } else {
                    UtilApi.logger(JiraXrayIntegration.class).log(Level.INFO, "Importando resultados a Jira Xray >>> {0}", jiraHost);
                    var jwtToken = jwt.body().asString().replace("\"", "");
                    importResults(jwtToken, cucumberJsonPath);
                }
            }
        } else {
            UtilApi.logger(JiraXrayIntegration.class).info("Integracion con Jira: " + jxrayIntegration);
        }
    }

    private Response getJWTTokenAuth() {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(String.format(ManageFiles.readAsString("req/req-jwt-auth-jira.json"), clientId, clientSecret))
                .when().log().all()
                .post(jiraHost + jiraAuthEndPoint);
    }

    private void importResults(String jwtToken, String cucumberJsonPath) {
        String jsonContentAsString;
        var defaultCucumberJsonPath = "/target/build/cucumber.json";
        if (cucumberJsonPath.equals(Constants.NOT_DEFINED))
            jsonContentAsString = ManageFiles.getJsonContentAsString(String.format(CUCUMBER_JSON_PATH, defaultCucumberJsonPath));
        else
            jsonContentAsString = ManageFiles.getJsonContentAsString(String.format(CUCUMBER_JSON_PATH, cucumberJsonPath));

        RequestSpecification req = RestAssured
                .given()
                .header(new Header("Content-Type", "application/json"))
                .header(new Header("Authorization", "Bearer " + jwtToken));
        req.log().uri();
        req.log().headers();
        Response response = req
                .body(jsonContentAsString)
                .when()
                .post(jiraHost + jiraCucumberImportEndPoint);
        response.prettyPeek();

        if (response.getStatusCode() == 200)
            UtilApi.logger(JiraXrayIntegration.class).log(Level.INFO, "Test Execution --> {0}",
                    response.body().jsonPath().getString("key"));
        else
            UtilApi.logger(JiraXrayIntegration.class).log(Level.WARNING, "Ocurrio un error en la respuesta de Xray {0}",
                    response.getStatusCode());
    }

}
