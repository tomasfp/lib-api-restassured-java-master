package com.nttdata.ct.api.base.config.api;

import io.restassured.http.Headers;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class ApiConfigBuilder {

    private final String apiType;
    private final String apiURI;
    private final String method;
    private final Headers headers;
    private final Map<String, Object> pathParams;
    private final Map<String, Object> queryParams;
    private final Map<String, Object> params;
    private final String bodyRequest;

}
