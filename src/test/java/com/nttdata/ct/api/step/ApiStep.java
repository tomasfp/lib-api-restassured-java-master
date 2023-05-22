package com.nttdata.ct.api.step;

import com.nttdata.ct.api.base.config.api.ApiConfigBuilder;
import com.nttdata.ct.api.base.config.api.ServiceConfig;
import com.nttdata.ct.api.lib.ServiceDom;
import com.nttdata.ct.api.service.aspect.evidence.ServiceTracking;
import io.cucumber.datatable.DataTable;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ApiStep extends ServiceDom {

    public void configurarCabeceras(DataTable dataTable) {
        header().setHeaderConfig(dataTable);
    }

    public void configuroQueryParams(DataTable dataTable) {
        params().setQueryParamsConfig(dataTable);
    }

    public void configuroPathParams(DataTable dataTable) {
        params().setPathParamsConfig(dataTable);
    }

    public void configurarBodyRequest(String requestPath) {
        Map<String, String> map = new LinkedHashMap<>();
        params().setBodyRequestConfig(requestPath, map);
    }

    public void configurarBodyRequest(String requestPath, DataTable dataTable) {
        params().setBodyRequestConfig(requestPath, dataTable);
    }

    @ServiceTracking
    public void ejecutarServicio(String serviceConfig) {
        execute().executeService(
                ApiConfigBuilder.builder()
                        .apiURI(ServiceConfig.getURI(serviceConfig))
                        .method(ServiceConfig.getMethod(serviceConfig))
                        .headers(header().getHeaderConfig())
                        .queryParams(params().getQueryParamsConfig())
                        .pathParams(params().getPathParamsConfig())
                        .bodyRequest(params().getBodyRequestConfig())
                        .build()
        );
    }

    public int getStatusCode() {
        return validate().getStatusCode();
    }

    public void validarRespuesta(DataTable dataTable) {
        validate().validateValuesFromResponse(dataTable);
    }
}