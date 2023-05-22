package com.nttdata.ct.api.base.config.imp;

import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.util.Map;

public interface IValidateConfiguration {

    void validateStatusCode(int statusCode);

    int getStatusCode();

    void validateValuesFromResponse(DataTable dataTable);

    void validateValuesFromResponse(Map<String, Object> mapResponseDataToValidate);

    void validateValuesFromResponse(Response response, String key, String value);

    void saveResponseValue(Map<String, String> mapResponseDataToSave);

    void saveResponseValue(DataTable dataTable);


}
