package com.nttdata.ct.api.base.config;

import com.nttdata.ct.api.base.config.api.ServiceConfig;
import com.nttdata.ct.api.base.config.imp.IValidateConfiguration;
import com.nttdata.ct.api.base.methods.JsonPathDomImp;
import com.nttdata.ct.api.service.constans.ColumnName;
import com.nttdata.ct.api.service.constans.ErrorMessageService;
import com.nttdata.ct.api.service.util.UtilApi;
import com.nttdata.ct.api.sessiondata.SessionData;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

import java.util.Map;
import java.util.logging.Level;

public class ValidateConfiguration implements IValidateConfiguration {

    private final Class<? extends ValidateConfiguration> thisClass = this.getClass();

    /**
     * Valida el codigo de estado de ejecucion del servicio
     *
     * @param statusCode Codigo de estado esperado
     */
    @Override
    public void validateStatusCode(int statusCode) {
        ExecutionConfiguration.getPopulateResponse().then().assertThat().statusCode(statusCode);
    }

    /**
     * Obtiene el codigo de estado de ejecución del servicio
     *
     * @return Codigo de estado obtenido de la ejecucion del servicio
     */
    @Override
    public int getStatusCode() {
        return ExecutionConfiguration.getPopulateResponse().getStatusCode();
    }

    /**
     * Valida los datos devueltos del json response del servicio
     *
     * @param dataTable Tabla de datos con los valores a validar dentro del json de respuesta del servicio
     */
    @Override
    public void validateValuesFromResponse(DataTable dataTable) {

        UtilApi.logger(thisClass).log(Level.INFO, "Response values dataTable >>>\n{0}", dataTable);

        Response response = ExecutionConfiguration.getPopulateResponse();

        for (Map<String, String> map : dataTable.asMaps()) {
            String key = map.get(ColumnName.JSONPATH_COLUMN);
            String value = map.get(ColumnName.VALUE_COLUMN);

            if (key == null || value == null) {
                UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_RESPONSE_JSONPATH_COLUMN_NAME_DATATABLE,
                        new String[]{ColumnName.JSONPATH_COLUMN, ColumnName.VALUE_COLUMN});
                throw new IllegalArgumentException();
            }

            validateValuesFromResponse(response, key, value);
        }
    }

    /**
     * Valida los datos devueltos del json response del servicio
     *
     * @param mapResponseDataToValidate mapa con los valores a validar dentro del json de respuesta del servicio
     */
    @Override
    public void validateValuesFromResponse(Map<String, Object> mapResponseDataToValidate) {
        UtilApi.logger(thisClass).log(Level.INFO, "Response values dataTable >>>\n{0}", mapResponseDataToValidate);
        Response response = ExecutionConfiguration.getPopulateResponse();
        for (Map.Entry<String, Object> map : mapResponseDataToValidate.entrySet()) {
            String key = map.getKey();
            String value = map.getValue().toString();
            validateValuesFromResponse(response, key, value);
        }
    }

    @Override
    public void validateValuesFromResponse(Response response, String key, String value) {
        Object expectedValue = ConvertType.setTypeTo(value);
        String expectedValueType = expectedValue.getClass().getSimpleName();
        Object valueFromResponse = JsonPathDomImp.getValueByJsonPathQuery(response.asString(), key);
        String valueFromResponseType = valueFromResponse.getClass().getSimpleName();

        dataTableFormatError(key);

        if (valueFromResponse instanceof JSONArray) {
            isArrayResponseEmpty(key, valueFromResponse);
            validateValuesFromArray(valueFromResponse, expectedValue, expectedValueType, value);
        } else {
            if (!expectedValue.equals(valueFromResponse))
                throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_VALUE_JSON_ASSERTION, expectedValue,
                        expectedValueType, valueFromResponse, valueFromResponseType));
        }
    }

    private void validateValuesFromArray(Object valueFromResponse, Object expectedValue, String expectedValueType, String value) {
        JSONArray jsonArray = (JSONArray) valueFromResponse; //lista valores respuesta
        if (((JSONArray) valueFromResponse).size() > 1) {
            if (expectedValue.equals("STRING_ARRAY") && (!jsonArray.toJSONString().equals(ServiceConfig.rigth(value))))
                throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_VALUE_JSONARRAY_ASSERTION,
                        ServiceConfig.rigth(value), expectedValue, jsonArray));
            else if (jsonArray.contains(expectedValue))
                throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_VALUE_JSONARRAY_ASSERTION,
                        expectedValue, expectedValueType, jsonArray));
        } else {
            for (Object valueInJSONArray : (JSONArray) valueFromResponse) {
                if (!expectedValue.equals(valueInJSONArray))
                    throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_VALUE_JSONARRAY_ASSERTION,
                            expectedValue, expectedValueType, jsonArray));
            }
        }
    }

    private void isArrayResponseEmpty(String key, Object valueFromResponse) {
        if (((JSONArray) valueFromResponse).isEmpty())
            throw new IllegalArgumentException("Not values into array response '" + valueFromResponse + "'. " +
                    "Check the jsonPath query >>> '" + key + "'");
    }

    private void dataTableFormatError(String key) {
        if (key == null) {
            UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_RESPONSE_JSONPATH_COLUMN_NAME_DATATABLE,
                    new String[]{ColumnName.JSONPATH_COLUMN, ColumnName.VALUE_COLUMN});
            throw new IllegalArgumentException();
        }
    }

    /**
     * Guarda un valor especifico de la respuesta del servicio
     *
     * @param mapResponseDataToSave mapa con los campos a guardar a partir del jsonPath de un key
     */
    @Override
    public void saveResponseValue(Map<String, String> mapResponseDataToSave) {
        Response response = ExecutionConfiguration.getPopulateResponse();
        for (Map.Entry<String, String> map : mapResponseDataToSave.entrySet()) {
            saveValues(response, map.getKey(), map.getValue());
        }
    }

    /**
     * Guarda un valor especifico de la respuesta del servicio
     *
     * @param dataTable Tabla de datos con los campos a guardar a partir del jsonPath de un key
     */
    @Override
    public void saveResponseValue(DataTable dataTable) {
        Response response = ExecutionConfiguration.getPopulateResponse();
        for (Map<String, String> map : dataTable.asMaps()) {
            String key = map.get(ColumnName.JSONPATH_COLUMN);
            String value = map.get(ColumnName.VARIABLE_COLUMN);
            if (key == null || value == null) {
                UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_RESPONSE_SAVE_COLUMN_NAME_DATATABLE,
                        new String[]{ColumnName.JSONPATH_COLUMN, ColumnName.VARIABLE_COLUMN});
                throw new IllegalArgumentException();
            }
            saveValues(response, key, value);
        }

    }

    private void saveValues(Response response, String key, Object value) {
        Object valueToSave = JsonPathDomImp.getValueByJsonPathQuery(response.asString(), key);
        SessionData.setSessionVariable(value, valueToSave);
        UtilApi.logger(thisClass).log(Level.INFO, "Dato <{0}> guardado en variable \"{1}\"", new Object[]{valueToSave, value});
    }
}