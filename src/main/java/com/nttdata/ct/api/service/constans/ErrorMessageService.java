package com.nttdata.ct.api.service.constans;

public final class ErrorMessageService {

    private ErrorMessageService(){
        throw new IllegalStateException("Utility class");
    }

    public static final String ERROR_HEADER_COLUMN_NAME_DATATABLE = "Headers columns name in DataTable must exist and should be: \"{0}\" and \"{1}\". For Example >>>\n...\n" +
            "| HEADER       | VALUE            |\n" +
            "| Content-type | application/json |\n...";

    public static final String ERROR_PARAMETER_COLUMN_NAME_DATATABLE = "Parameters columns name in DataTable must exist and should be: \"{0}\" and \"{1}\". For Example >>>\n...\n" +
            "| PARAMETER | VALUE |\n" +
            "| priority  | 5     |\n...";

    public static final String ERROR_BODYREQUEST_COLUMN_NAME_DATATABLE = "BodyRequest columns name in DataTable must exist and should be: \"{0}\" and \"{1}\". For Example >>>\n...\n" +
            "| JSONPATH        | VALUE |\n" +
            "| $.client_id     | 1234  |\n" +
            "| $.client_secret | data  |\n...";

    public static final String ERROR_RESPONSE_JSONPATH_COLUMN_NAME_DATATABLE = "Response Validation columns name in DataTable must exist and should be: \"{0}\" and \"{1}\". For Example >>>\n...\n" +
            "| JSONPATH                                          | VALUE  |\n" +
            "| $.[?(@.accountId==000800000189)].totalCost.amount | 206000 |\n...";

    public static final String ERROR_RESPONSE_SAVE_COLUMN_NAME_DATATABLE = "Response save Values columns name in DataTable must exist and should be: \"{0}\" and \"{1}\". For Example >>>\n...\n" +
            "| JSONPATH                    | VARIABLE |\n" +
            "| $.[*].instalments[*].index  | 206000   |\n...";

    public static final String ERROR_MESSAGE_METHOD = "Method \"{0}\", is not supported. Pick one of these >>> {1}";

    public static final String ERROR_MENSAJE_PARSE_TYPE = "Tipo de dato \"{0}\", no es soportado. Elegir uno de estos tipos >> {1}";
    public static final String ERROR_FORMATO_VARIABLE = "Formato de variable de sesion %s incorrecto. Debe ser: {{variable}}";
    public static final String ERROR_VALUE_JSONARRAY_ASSERTION = "El dato <%s> de tipo '%s' no existe en el JSONArray >> %s";
    public static final String ERROR_VALUE_JSON_ASSERTION = "Se esperaba el dato <%s> de tipo '%s', pero se encontro el dato <%s> de tipo '%s'";
    public static final String ERROR_HEAD_VALUE_NULL = "El valor del variable de busqueda <%s> para la cabecera <%s>, no existe o es null";
    public static final String ERROR_PARAM_VALUE_NULL = "\"El valor del variable de busqueda <%s> para el parametro <%s>, no existe o es null\"";
}
