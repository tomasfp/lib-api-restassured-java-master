package com.nttdata.ct.api.service.ddt.engine;

import com.nttdata.ct.api.service.util.UtilApi;
import org.springframework.stereotype.Component;

@Component
public class JsonReader {

    public void getValue(){
        UtilApi.logger(this.getClass()).info("Get Value");
    }

}
