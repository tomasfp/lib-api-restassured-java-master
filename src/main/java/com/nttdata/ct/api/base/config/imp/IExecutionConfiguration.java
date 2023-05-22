package com.nttdata.ct.api.base.config.imp;

import com.nttdata.ct.api.base.config.api.ApiConfigBuilder;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public interface IExecutionConfiguration {

    Response executeService(ApiConfigBuilder apiConfig);

    Response executeService(
            RequestSpecification requestSpecification,
            String uri,
            String method);

    Response executeService(String uri, 
                            String method,
                            Headers headers,
                            Map<String, Object> queryParams,
                            Map<String, Object> pathVariables,
                            Map<String, Object> params,
                            String bodyRequest);

    RequestSpecification getRequestSpecification();

    AuthenticationSpecification getAutoSpecification();

}
