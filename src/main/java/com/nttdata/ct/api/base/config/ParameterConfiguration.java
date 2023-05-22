package com.nttdata.ct.api.base.config;

import com.nttdata.ct.api.base.config.imp.IParameterConfiguration;
import com.nttdata.ct.api.base.methods.JsonPathDomImp;
import com.nttdata.ct.api.service.constans.ColumnName;
import com.nttdata.ct.api.service.constans.ErrorMessageService;
import com.nttdata.ct.api.service.datamanage.ValidateSessionData;
import com.nttdata.ct.api.service.util.UtilApi;
import io.cucumber.datatable.DataTable;
import io.restassured.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import static com.nttdata.ct.api.service.io.ManageFiles.readAsString;

public class ParameterConfiguration implements IParameterConfiguration {

    private static final ThreadLocal<String> bodyTheadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> queryParamsThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> pathParamsThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Map<String, Object>> paramsThreadLocal = new ThreadLocal<>();

    private static final String LOG_BODY_REQUEST = "Configured bodyRequest";

    //BodyRequest
    public static void setPopulateBodyRequest(String bodyRequest) {
        bodyTheadLocal.set(bodyRequest);
    }

    public static String getPopulateBodyRequest() {
        return bodyTheadLocal.get();
    }

    public void removeBodyThreads() {
        bodyTheadLocal.remove();
    }

    //QueryParams
    public static void setPopulateQueryParams(Map<String, Object> queryParams) {
        queryParamsThreadLocal.set(queryParams);
    }

    public static Map<String, Object> getPopulateQueryParams() {
        return queryParamsThreadLocal.get();
    }

    public void removeQueryParamsThreads() {
        queryParamsThreadLocal.remove();
    }

    //PathParams
    public static void setPopulatePathParams(Map<String, Object> pathParams) {
        pathParamsThreadLocal.set(pathParams);
    }

    public static Map<String, Object> getPopulatePathParams() {
        return pathParamsThreadLocal.get();
    }

    public void removePathParamsThreads() {
        pathParamsThreadLocal.remove();
    }

    //Params
    public static void setPopulateParams(Map<String, Object> params) {
        paramsThreadLocal.set(params);
    }

    public static Map<String, Object> getPopulateParams() {
        return paramsThreadLocal.get();
    }

    public void removeParamsThreads() {
        paramsThreadLocal.remove();
    }

    private final Class<? extends ParameterConfiguration> thisClass = this.getClass();

    /**
     * Configura los parametros del servicio a traves de un Map de Strings compuesto por el nombre del parametros y el valor del parametro
     *
     * @param parameterList Mapa de 'String, String' con el nombre del parametros y el valo del parametros
     * @return lista de cabeceras configuradas para ser enviadas directamente al servicio
     */
    @Override
    public Map<String, Object> fetchParameters(Map<String, String> parameterList) {
        UtilApi.logger(thisClass).log(Level.INFO, "Configuring service Parameters >>>\n{0}\n", parameterList);
        Map<String, Object> parameters = new HashMap<>();
        for (Map.Entry<String, String> parameterMap : parameterList.entrySet()) {
            var key = parameterMap.getKey();
            var value = parameterMap.getValue();
            value = UtilApi.replaceBlank(value);
            Header header = new Header(key, value);
            Map<String, Object> sessionVariables = ValidateSessionData.findSessionVariables(header.getValue()); // Obtiene variable y valore que esten guardadas en sesion si existen.
            if (!sessionVariables.isEmpty())
                for (Map.Entry<String, Object> entry : sessionVariables.entrySet())
                    FetchParameter.updateMap(parameters, new String[]{key, value}, entry.getValue());
            else
                parameters.put(key, value);

        }
        UtilApi.logger(thisClass).log(Level.INFO, "Configured Parameters >>>\n{0}\n", parameters);
        return parameters;
    }

    /**
     * Configura los parametros del servicio a traves de el objeto DataTable de Cucumber,
     * enviado desde la redaccion en gherkin en un archivo .feature o creando el dataTable desde codigo.
     * Compatible para queryParams, pathVariables, body (parametros)
     *
     * @param dataTable Objecto dataTable de dos columnas. La columna 'PARAMETER' tendra el nombre de los parametros y la columna 'VALUE' el valor de las parametros
     * @return mapa de parametros configuradas para ser enviadas directamente al servicio.
     */
    @Override
    public Map<String, Object> fetchParameters(DataTable dataTable) {
        UtilApi.logger(thisClass).log(Level.INFO, "Configuring service Parameters dataTable >>>\n{0}", dataTable);
        validateColumnParametersName(dataTable);
        Map<String, Object> parameters = new HashMap<>();
        for (Map<String, String> mapParameters : dataTable.asMaps()) {
            var key = mapParameters.get(ColumnName.PARAMETER_COLUM);
            var value = mapParameters.get(ColumnName.VALUE_COLUMN);
            value = UtilApi.replaceBlank(value);
            Map<String, Object> sessionVariables = ValidateSessionData.findSessionVariables(value);
            if (!sessionVariables.isEmpty())
                for (Map.Entry<String, Object> entry : sessionVariables.entrySet())
                    FetchParameter.updateMap(parameters, new String[]{key, value}, entry.getValue());
            else
                parameters.put(key, value);
        }
        UtilApi.logger(thisClass).log(Level.INFO, "Configured Parameters >>>\n{0}\n", parameters);
        return parameters;
    }

    @Override
    public void setQueryParamsConfig(Map<String, String> parameterList) {
        setPopulateQueryParams(fetchParameters(parameterList));
    }

    @Override
    public void setQueryParamsConfig(DataTable parameterDataTable) {
        setPopulateQueryParams(fetchParameters(parameterDataTable));
    }

    @Override
    public Map<String, Object> getQueryParamsConfig() {
        return getPopulateQueryParams();
    }

    @Override
    public void setPathParamsConfig(Map<String, String> parameterList) {
        setPopulatePathParams(fetchParameters(parameterList));
    }

    @Override
    public void setPathParamsConfig(DataTable parameterDataTable) {
        setPopulatePathParams(fetchParameters(parameterDataTable));
    }

    @Override
    public Map<String, Object> getPathParamsConfig() {
        return getPopulatePathParams();
    }


    @Override
    public String fetchBodyRequest(String jsonFileReqPath, DataTable dataTable) {
        UtilApi.logger(thisClass).log(Level.INFO, "Configuring service bodyRequest dataTable >>>\n{0}", dataTable);
        validateColumnBodyJsonPathName(dataTable);
        String bodyRequestString = readAsString(jsonFileReqPath);
        for (Map<String, String> stringMap : dataTable.asMaps()) {
            var jsonPath = stringMap.get(ColumnName.JSONPATH_COLUMN);
            var newValue = stringMap.get(ColumnName.VALUE_COLUMN);
            newValue = UtilApi.replaceBlank(newValue);
            Map<String, Object> sessionVariables = ValidateSessionData.findSessionVariables(newValue);
            if (!sessionVariables.isEmpty())
                for (Map.Entry<String, Object> entry : sessionVariables.entrySet())
                    bodyRequestString = JsonPathDomImp.updateValueByJsonPathQuery(bodyRequestString, jsonPath, entry.getValue());
            else
                bodyRequestString = JsonPathDomImp.updateValueByJsonPathQuery(bodyRequestString, jsonPath, ConvertType.setTypeTo(newValue));

        }
        UtilApi.logger(thisClass).log(Level.INFO, LOG_BODY_REQUEST + " >>>\n{0}\n", bodyRequestString);
        return bodyRequestString;
    }

    @Override
    public String fetchBodyRequest(String jsonFileReqPath, Map<String, String> jsonPathRequest) {
        String bodyRequestString = readAsString(jsonFileReqPath);
        for (Map.Entry<String, String> requestMap : jsonPathRequest.entrySet()) {
            var jsonPath = requestMap.getKey();
            var value = requestMap.getValue();
            Map<String, Object> sessionVariables = ValidateSessionData.findSessionVariables(value);
            if (!sessionVariables.isEmpty())
                for (Map.Entry<String, Object> entry : sessionVariables.entrySet())
                    bodyRequestString = JsonPathDomImp.updateValueByJsonPathQuery(bodyRequestString, jsonPath, entry.getValue());
            else
                bodyRequestString = JsonPathDomImp.updateValueByJsonPathQuery(bodyRequestString, jsonPath, ConvertType.setTypeTo(value));
        }
        UtilApi.logger(thisClass).log(Level.INFO, LOG_BODY_REQUEST + "  >>>\n{0}\n", bodyRequestString);
        return bodyRequestString;
    }

    @Override
    public void setBodyRequestConfig(String bodyRequest) {
        if (bodyRequest.isEmpty()) UtilApi.logger(thisClass).info(LOG_BODY_REQUEST + "  >>> EMPTY");
        else UtilApi.logger(thisClass).log(Level.INFO, LOG_BODY_REQUEST + "  >>>\n{0}\n", bodyRequest);
        setPopulateBodyRequest(bodyRequest);
    }

    @Override
    public void setBodyRequestConfig(String jsonFileReqPath, DataTable dataTable) {
        setPopulateBodyRequest(fetchBodyRequest(jsonFileReqPath, dataTable));
    }

    @Override
    public void setBodyRequestConfig(String jsonFileReqPath, Map<String, String> jsonPathRequest) {
        setPopulateBodyRequest(fetchBodyRequest(jsonFileReqPath, jsonPathRequest));
    }

    @Override
    public String getBodyRequestConfig() {
        return getPopulateBodyRequest();
    }

    private void validateColumnParametersName(DataTable dataTable) {
        if (!dataTable.asMaps().isEmpty() &&
                (!(dataTable.asMaps().get(0).containsKey(ColumnName.PARAMETER_COLUM) &&
                        dataTable.asMaps().get(0).containsKey(ColumnName.VALUE_COLUMN)))) {
            UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_PARAMETER_COLUMN_NAME_DATATABLE,
                    new String[]{ColumnName.PARAMETER_COLUM, ColumnName.VALUE_COLUMN});
            throw new IllegalArgumentException();
        }
    }

    private void validateColumnBodyJsonPathName(DataTable dataTable) {
        if (!dataTable.asMaps().isEmpty() &&
                (!(dataTable.asMaps().get(0).containsKey(ColumnName.JSONPATH_COLUMN) &&
                        dataTable.asMaps().get(0).containsKey(ColumnName.VALUE_COLUMN)))) {
            UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_BODYREQUEST_COLUMN_NAME_DATATABLE,
                    new String[]{ColumnName.JSONPATH_COLUMN, ColumnName.VALUE_COLUMN});
            throw new IllegalArgumentException();
        }
    }

}

class FetchParameter {

    private FetchParameter(){
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends FetchParameter> thisClass = FetchParameter.class;

    /**
     * Actualiza el valor de la llama del mapa si fue guardado en pasos anteriores y requerido a traves del formato {{NOMBRE_VARIABLE}}
     *
     * @param parameters Objecto Map que alamcenara los parametros configurados si encuentra o no una referencia guardada en sesion
     * @param params     Arreglo de strings con los valores originales de los parametros
     * @param newValue   nuevo valor obtenido en sesion
     */
    public static void updateMap(Map<String, Object> parameters, String[] params, Object newValue) {
        UtilApi.logger(thisClass).log(Level.INFO, "Getting variable \"{0}\" value, from paramater \"{1}\".", new String[]{params[1], params[0]});
        if (newValue == null)
            throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_PARAM_VALUE_NULL, params[1], params[0]));
        parameters.put(params[0], newValue);
    }

}