package com.nttdata.ct.api.lib;

import com.nttdata.ct.api.base.config.ExecutionConfiguration;
import com.nttdata.ct.api.base.config.HeaderConfiguration;
import com.nttdata.ct.api.base.config.ParameterConfiguration;
import com.nttdata.ct.api.base.config.ValidateConfiguration;
import com.nttdata.ct.api.base.config.imp.IExecutionConfiguration;
import com.nttdata.ct.api.base.config.imp.IHeaderConfiguration;
import com.nttdata.ct.api.base.config.imp.IParameterConfiguration;
import com.nttdata.ct.api.base.config.imp.IValidateConfiguration;

public class ServiceDom implements IServiceDom {

    @Override
    public IHeaderConfiguration header() {
        return new HeaderConfiguration();
    }

    @Override
    public IParameterConfiguration params() {
        return new ParameterConfiguration();
    }

    @Override
    public IExecutionConfiguration execute() {
        return new ExecutionConfiguration();
    }

    @Override
    public IValidateConfiguration validate() {
        return new ValidateConfiguration();
    }

}
