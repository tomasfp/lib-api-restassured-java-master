package com.nttdata.ct.api.lib;

import com.nttdata.ct.api.base.config.imp.IExecutionConfiguration;
import com.nttdata.ct.api.base.config.imp.IHeaderConfiguration;
import com.nttdata.ct.api.base.config.imp.IParameterConfiguration;
import com.nttdata.ct.api.base.config.imp.IValidateConfiguration;

public interface IServiceDom {

    IHeaderConfiguration header();

    IParameterConfiguration params();

    IExecutionConfiguration execute();

    IValidateConfiguration validate();

}
