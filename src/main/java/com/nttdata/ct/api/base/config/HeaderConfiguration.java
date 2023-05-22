package com.nttdata.ct.api.base.config;

import com.nttdata.ct.api.base.config.imp.IHeaderConfiguration;
import com.nttdata.ct.api.service.constans.ColumnName;
import com.nttdata.ct.api.service.constans.ErrorMessageService;
import com.nttdata.ct.api.service.datamanage.ValidateSessionData;
import com.nttdata.ct.api.service.util.UtilApi;
import io.cucumber.datatable.DataTable;
import io.restassured.http.Header;
import io.restassured.http.Headers;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class HeaderConfiguration implements IHeaderConfiguration {

    private final Class<? extends HeaderConfiguration> thisClass = this.getClass();

    private static final ThreadLocal<Headers> headersThreadLocal = new ThreadLocal<>();

    //Headers
    public static void setPopulateHeaders(Headers headers) {
        headersThreadLocal.set(headers);
    }

    public static Headers getPopulateHeaders() {
        return headersThreadLocal.get();
    }

    public void removeThreads() {
        headersThreadLocal.remove();
    }

    /**
     * Configura cabeceras del servicio a traves de un Map de Strings configurado con el nombre de la cabecea y el valor de la cabecera
     *
     * @param headersList Mapa de String, String con el nombre de la cabecera y el valo de la cabecera
     * @return lista de cabeceras configuradas para ser enviadas directamente al servicio
     */
    @Override
    public Headers fetchHeaders(Map<String, String> headersList) {
        UtilApi.logger(thisClass).log(Level.INFO, "Configuring service Headers >>>\n{0}\n", headersList);
        List<Header> headerList = new LinkedList<>();
        for (Map.Entry<String, String> headMap : headersList.entrySet()) {
            var key = headMap.getKey();
            var value = headMap.getValue();
            value = UtilApi.replaceBlank(value);
            Header header = new Header(key, value);
            Map<String, Object> variableFoundMap = ValidateSessionData.findSessionVariables(header.getValue()); // Obtiene variable y valore que esten guardadas en sesion si existen.
            header = FetchHeader.updateHeader(header, variableFoundMap); //actualiza valor de cabecera
            headerList.add(header);
        }
        Headers headers = new Headers(headerList);

        UtilApi.logger(thisClass).log(Level.INFO, "Configured Headers >>>\n{0}\n", headers);
        return headers;
    }

    /**
     * Configura cabeceras del servicio a traves de el objeto DataTable de Cucumber, enviado desde la redaccion en gherkin en un archivo .feature
     * o creando el dataTable desde codigo.
     *
     * @param dataTable Objecto dataTable de dos columnas. La columna 'HEADER' tendra el nombre de las cabeceras y la columna 'VALUE' el valor de las cabeceras
     * @return lista de cabeceras configuradas para ser enviadas directamente al servicio.
     */
    @Override
    public Headers fetchHeaders(DataTable dataTable) {
        UtilApi.logger(thisClass).log(Level.INFO, "Configuring service Headers dataTable >>>\n{0}", dataTable);
        validateColumnHeaderName(dataTable);
        List<Header> headerList = new LinkedList<>();
        for (Map<String, String> mapHeaders : dataTable.asMaps()) {
            var key = mapHeaders.get(ColumnName.HEADER_COLUMN);
            var value = mapHeaders.get(ColumnName.VALUE_COLUMN);
            value = UtilApi.replaceBlank(value);
            Header header = new Header(key, value);
            Map<String, Object> variableFoundMap = ValidateSessionData.findSessionVariables(header.getValue()); // Obtiene variable y valore que esten guardadas en sesion si existen.
            header = FetchHeader.updateHeader(header, variableFoundMap); //actualiza valor de cabecera
            headerList.add(header);
        }
        Headers headers = new Headers(headerList);
        UtilApi.logger(thisClass).log(Level.INFO, "Configured Headers >>>\n{0}\n", headers);
        return headers;
    }

    @Override
    public void setHeaderConfig(Map<String, String> headersList) {
        setPopulateHeaders(fetchHeaders(headersList));
    }

    @Override
    public void setHeaderConfig(DataTable dataTable) {
        setPopulateHeaders(fetchHeaders(dataTable));
    }

    @Override
    public Headers getHeaderConfig() {
        return getPopulateHeaders();
    }

    private void validateColumnHeaderName(DataTable dataTable) {
        if (!dataTable.asMaps().isEmpty() &&
                (!(dataTable.asMaps().get(0).containsKey(ColumnName.HEADER_COLUMN) &&
                        dataTable.asMaps().get(0).containsKey(ColumnName.VALUE_COLUMN)))) {
            UtilApi.logger(thisClass).log(Level.SEVERE, ErrorMessageService.ERROR_HEADER_COLUMN_NAME_DATATABLE,
                    new String[]{ColumnName.HEADER_COLUMN, ColumnName.VALUE_COLUMN});
            throw new IllegalArgumentException();
        }
    }
}

class FetchHeader {

    private FetchHeader() {
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends FetchHeader> thisClass = FetchHeader.class;

    /**
     * Actualiza el valor de la cabecera si fue guardado en pasos anteriores y requerido a traves del formato {{NOMBRE_CABECERA}}
     *
     * @param header           Objecto 'Header' que guarda las cabeceras y valores encontrados en los mapas y datatables enviados.
     * @param variableFoundMap Mapa de variables guardadas en sesion que fueron localizadas
     * @return el objeto 'Header' con el valor de las cabeceras guardadas en sesion
     */
    public static Header updateHeader(Header header, Map<String, Object> variableFoundMap) {
        if (!variableFoundMap.isEmpty()) {
            UtilApi.logger(thisClass).log(Level.INFO, "Getting variable \"{0}\" value , from header \"{1}\".",
                    new String[]{header.getValue(), header.getName()});
            for (Map.Entry<String, Object> entry : variableFoundMap.entrySet()) {
                if (entry.getValue() == null)
                    throw new IllegalArgumentException(String.format(ErrorMessageService.ERROR_HEAD_VALUE_NULL, header.getValue(),
                            header.getName()));
                header = new Header(header.getName(), (String) entry.getValue());
            }
        }
        return header;
    }

}