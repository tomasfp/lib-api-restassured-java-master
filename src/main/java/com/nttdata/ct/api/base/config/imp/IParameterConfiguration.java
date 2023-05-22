package com.nttdata.ct.api.base.config.imp;

import io.cucumber.datatable.DataTable;

import java.util.Map;

public interface IParameterConfiguration {

    Map<String, Object> fetchParameters(Map<String, String> parameterList);

    Map<String, Object> fetchParameters(DataTable parameterDataTable);

    String fetchBodyRequest(String jsonFileReqPath, DataTable dataTable);

    String fetchBodyRequest(String jsonFileReqPath, Map<String, String> jsonPathRequest);

    //queryParams
    void setQueryParamsConfig(Map<String, String> parameterList);

    void setQueryParamsConfig(DataTable parameterDataTable);

    Map<String, Object> getQueryParamsConfig();

    //pathParams
    void setPathParamsConfig(Map<String, String> parameterList);

    void setPathParamsConfig(DataTable parameterDataTable);

    Map<String, Object> getPathParamsConfig();

    //bodyRequest

    void setBodyRequestConfig(String bodyRequest);

    void setBodyRequestConfig(String jsonFileReqPath, DataTable dataTable);

    void setBodyRequestConfig(String jsonFileReqPath, Map<String, String> jsonPathRequest);

    String getBodyRequestConfig();

}