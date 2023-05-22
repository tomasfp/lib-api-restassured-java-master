package com.nttdata.ct.api.base.config.imp;

import io.cucumber.datatable.DataTable;
import io.restassured.http.Headers;

import java.util.Map;

public interface IHeaderConfiguration {

    Headers fetchHeaders(Map<String, String> headersList);

    Headers fetchHeaders(DataTable headerDataTable);

    void setHeaderConfig(Map<String, String> headersList);

    void setHeaderConfig(DataTable headerDataTable);

    Headers getHeaderConfig();

}
