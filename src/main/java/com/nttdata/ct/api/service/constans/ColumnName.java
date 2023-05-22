package com.nttdata.ct.api.service.constans;

public class ColumnName {

    private ColumnName(){
        throw new IllegalStateException("Utility class");
    }

    public static final String HEADER_COLUMN = "HEADER";
    public static final String PARAMETER_COLUM = "PARAMETER";
    public static final String JSONPATH_COLUMN = "JSONPATH";
    public static final String VALUE_COLUMN = "VALUE";
    public static final String VARIABLE_COLUMN = "VARIABLE";
}
