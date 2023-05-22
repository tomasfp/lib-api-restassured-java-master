package com.nttdata.ct.api.base.methods;

import com.jayway.jsonpath.JsonPath;
import com.nttdata.ct.api.service.util.UtilApi;

import java.util.logging.Level;

public class JsonPathDomImp {

    private JsonPathDomImp() {
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends JsonPathDomImp> THIS_CLASS = JsonPathDomImp.class;

    public static Object getValueByJsonPathQuery(String jsonString, String jsonPath) {
        isJsonStringNull(jsonString);
        Object readJsonPath = JsonPath.read(jsonString, jsonPath);
        UtilApi.logger(THIS_CLASS).log(Level.INFO, "Value get by query jsonPath >>> {0}", readJsonPath);
        return readJsonPath;
    }

    public static String updateValueByJsonPathQuery(String jsonString, String jsonPath, Object newValue) {
        isJsonStringNull(jsonString);
        if (jsonPath.isEmpty() || newValue == null)
            return jsonString;
        else
            return JsonPath.parse(jsonString).set(jsonPath, newValue).jsonString();
    }

    private static void isJsonStringNull(String jsonString) {
        if (jsonString == null)
            throw new IllegalArgumentException("Body jsonString must not be null!");
    }

}