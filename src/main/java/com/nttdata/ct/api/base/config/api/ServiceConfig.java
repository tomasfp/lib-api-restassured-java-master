package com.nttdata.ct.api.base.config.api;

public class ServiceConfig {

    private ServiceConfig(){
        throw new IllegalStateException("Utility class");
    }
    public static String getMethod(String service) {
        return service.split("::")[0];
    }

    public static String getURI(String service) {
        return service.split("::")[1];
    }

    public static String left(String stringToSplit){
        return  stringToSplit.split("::")[0];
    }

    public static String rigth(String stringToSplit){
        return  stringToSplit.split("::")[1];
    }

}
