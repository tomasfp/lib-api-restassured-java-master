package com.nttdata.ct.api.base.config;

import com.nttdata.ct.api.base.config.api.ApiConfigBuilder;
import com.nttdata.ct.api.base.config.imp.IExecutionConfiguration;
import com.nttdata.ct.api.service.constans.ErrorMessageService;
import com.nttdata.ct.api.service.constans.MethodsEnum;
import com.nttdata.ct.api.service.io.ManageFiles;
import com.nttdata.ct.api.service.util.UtilApi;
import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.AuthenticationSpecification;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

public class ExecutionConfiguration implements IExecutionConfiguration {

    private static final ThreadLocal<Response> responseThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<QueryableRequestSpecification> queryableRequestSpecificationThreadLocal = new ThreadLocal<>();

    //Response
    public static void setPopulateResponse(Response response) {
        responseThreadLocal.set(response);
    }

    public static Response getPopulateResponse() {
        return responseThreadLocal.get();
    }

    public void removeResponseThread() {
        responseThreadLocal.remove();
    }

    //RequestSpecification
    public static void setPopulateQueryableRequestSpecification(QueryableRequestSpecification queryableRequestSpecification) {
        queryableRequestSpecificationThreadLocal.set(queryableRequestSpecification);
    }

    public static QueryableRequestSpecification getPopulateQueryableRequestSpecification() {
        return queryableRequestSpecificationThreadLocal.get();
    }

    public void removeRequestSpecificationThread() {
        queryableRequestSpecificationThreadLocal.remove();
    }

    private final Class<? extends ExecutionConfiguration> thisClass = this.getClass();

    /**
     * Devuelve la especificación del Request desde el primer punto
     *
     * @return RequestSpecification
     */
    @Override
    public RequestSpecification getRequestSpecification() {
        return RestAssured.given();
    }

    /**
     * Devuelve la especificación del Request desde el primer punto, junto con la interface de autorizacion
     *
     * @return AuthenticationSpecification
     */
    @Override
    public AuthenticationSpecification getAutoSpecification() {
        return RestAssured.given().auth();
    }

    /**
     * Ejecuta la especificacion del servicio armado con la clase ApiConfigBuilder
     *
     * @param apiConfig Clase de tipo builder que construye la especificación del servicio
     * @return la respuesta del servicio
     */
    @Override
    public Response executeService(ApiConfigBuilder apiConfig) {
        var uri = apiConfig.getApiURI();
        var method = apiConfig.getMethod();
        var headers = apiConfig.getHeaders();

        Map<String, Object> queryParams = Collections.emptyMap();
        Map<String, Object> pathParams = Collections.emptyMap();
        Map<String, Object> params = Collections.emptyMap();
        var bodyRequest = "";

        if (apiConfig.getBodyRequest() != null) bodyRequest = apiConfig.getBodyRequest();
        if (apiConfig.getQueryParams() != null) queryParams = apiConfig.getQueryParams();
        if (apiConfig.getPathParams() != null) pathParams = apiConfig.getPathParams();
        if (apiConfig.getParams() != null) params = apiConfig.getParams();

        return execute(uri, method, headers, queryParams, pathParams, params, bodyRequest);
    }

    /**
     * Ejecuta la especificacion del servicio a partir del RequestSpecification, uri y Metodo
     *
     * @param requestSpecification Espeficicacion del servicio construido por la clase de RestAssured RequestSpecification
     * @param uri                  Uri del servicio
     * @param method               Metodo de ejecucion del servicio
     * @return la respuesta del servicio
     */
    @Override
    public Response executeService(
            RequestSpecification requestSpecification,
            String uri,
            String method) {
        return execute(requestSpecification, uri, method);
    }

    /**
     * Ejecuta la especificacion del servicio a partir de parametros especificos
     *
     * @param uri           Uri del servicio
     * @param method        Metodo de ejecucion del servicio
     * @param headers       Cabeceras construidas a partir de la clase de RestAssured Headers
     * @param queryParams   Mapa de parametros queryParams
     * @param pathVariables Mapa de parametros pathParams
     * @param params        Mapa de parametros ReqParams
     * @param bodyRequest   Body request en tipo string
     * @return la respuesta del servicio
     */
    @Override
    public Response executeService(String uri,
                                   String method,
                                   Headers headers,
                                   Map<String, Object> queryParams,
                                   Map<String, Object> pathVariables,
                                   Map<String, Object> params,
                                   String bodyRequest) {

        return execute(uri, method, headers, queryParams, pathVariables, params, bodyRequest);
    }

    private Response execute(
            String uri,
            String method,
            Headers headers,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, Object> params,
            String bodyRequest) {

        RequestSpecification requestSpecification = getRequestSpecification()
                .headers(headers)
                .queryParams(queryParams)
                .pathParams(pathParams)
                .params(params)
                .body(bodyRequest);

        return getResponse(requestSpecification, uri, method);

    }

    private Response execute(RequestSpecification requestSpecification,
                             String uri,
                             String method) {
        return getResponse(requestSpecification, uri, method);
    }

    private Response getResponse(RequestSpecification requestSpecification, String uri, String method) {
        if (uri == null || uri.isEmpty())
            throw new IllegalArgumentException("Service uri can not be null or empty");
        if (method == null || method.isEmpty())
            throw new IllegalArgumentException("Service method can not be null or empty");

        UtilApi.logger(thisClass).log(Level.INFO, ManageFiles.readAsString("logs/log-thread-details.txt"),
                new Object[]{Thread.currentThread().getId() + " - " + Thread.currentThread().getName(),
                        uri, method});

        MethodsEnum reqMethod;
        try {
            reqMethod = MethodsEnum.valueOf(method);
        } catch (IllegalArgumentException argumentException) {
            UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_MESSAGE_METHOD,
                    new Object[]{method, MethodsEnum.getAllMethods()});
            throw new IllegalArgumentException();
        }
        Response response;
        try {

            switch (reqMethod) {
                case GET:
                    response = requestSpecification.when().log().all()
                            .get(uri);
                    break;
                case POST:
                    response = requestSpecification.when().log().all()
                            .post(uri);
                    break;
                case PUT:
                    response = requestSpecification.when().log().all()
                            .put(uri);
                    break;
                case PATCH:
                    response = requestSpecification.when().log().all()
                            .patch(uri);
                    break;
                case DELETE:
                    response = requestSpecification.when().log().all()
                            .delete(uri);
                    break;
                case OPTIONS:
                    response = requestSpecification.when().log().all()
                            .options(uri);
                    break;
                case HEAD:
                    response = requestSpecification.when().log().all()
                            .head(uri);
                    break;
                default:
                    throw new IllegalArgumentException("Method HTTP not supported >>> " + reqMethod);
            }
        } catch (Exception exception) {
            throw new IllegalArgumentException("An error occurred in the execution of the service. Message: " + exception.getMessage());
        }

        QueryableRequestSpecification queryableRequestSpecification = (QueryableRequestSpecification) requestSpecification;
        UtilApi.logger(thisClass).log(Level.INFO, "Executed service: {0}",
                (queryableRequestSpecification.getMethod() + "::" + queryableRequestSpecification.getURI()));
        cleanConfig();
        setPopulateResponse(response);
        setPopulateQueryableRequestSpecification(queryableRequestSpecification);
        return response;
    }

    private void cleanConfig() {
        HeaderConfiguration.setPopulateHeaders(null);
        ParameterConfiguration.setPopulateQueryParams(Collections.emptyMap());
        ParameterConfiguration.setPopulatePathParams(Collections.emptyMap());
        ParameterConfiguration.setPopulateParams(Collections.emptyMap());
        ParameterConfiguration.setPopulateBodyRequest("");
    }

}