package com.nttdata.ct.api.service.util;

import com.nttdata.ct.api.sessiondata.SessionData;
import io.cucumber.datatable.DataTable;
import org.apache.commons.lang3.NotImplementedException;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class UtilApi {

    private UtilApi() {
        throw new IllegalStateException("Utility class");
    }

    private static final Class<? extends UtilApi> THIS_CLASS = UtilApi.class;

    public static Logger logger(Class<?> className) {
        return Logger.getLogger(className.getName());
    }

    public static void waitForSeconds(int tiemOnSeconds) {
        try {
            Thread.sleep(tiemOnSeconds * 1000L);
        } catch (InterruptedException e) {
            UtilApi.logger(THIS_CLASS).log(Level.WARNING, "waitForSeconds", e);
            Thread.currentThread().interrupt();
        }
    }

    public static void saveVariableOnSession(String key, Object value) {
        SessionData.setSessionVariable(key, value);
    }

    public static <T> T getVariableOnSession(String key) {
        return SessionData.getVariableCalled(key);
    }

    public static String getValueFromDataTable(DataTable dataTable, String title) {
        if (dataTable.getTableConverter().getClass().getSimpleName().equalsIgnoreCase("DataTableTypeRegistryTableConverter"))
            return dataTable.asMaps(String.class, String.class).get(0).get(title);
        else if (dataTable.getTableConverter().getClass().getSimpleName().equalsIgnoreCase("NoConverterDefined"))
            return dataTable.asMaps().get(0).get(title);
        else
            throw new NotImplementedException("DataTable getTableConverter() not implemented.");
    }

    public static String replaceBlank(String value) {
        return value.replace("[blank]", "");
    }

}