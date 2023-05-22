package com.nttdata.ct.api.service.aspect.evidence;

import com.nttdata.ct.api.base.config.ExecutionConfiguration;
import com.nttdata.ct.api.stepdefinition.AttachScenarioReport;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceTrackingAspect {

    @AfterReturning("@annotation(ServiceTracking)")
    public void serviceEvidence() {
        AttachScenarioReport.getEvidence(ExecutionConfiguration.getPopulateQueryableRequestSpecification(),
                ExecutionConfiguration.getPopulateResponse());
    }

}
