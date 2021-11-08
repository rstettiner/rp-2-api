package com.rafa.service.base;

import com.rafa.domain.base.SysMessageLkup;
import com.rafa.web.base.ServiceMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ServiceMessageService {
    
    private final ApplicationContext ctx;
    
    private ServiceMessageService self;
    
    private final SysMessageLkupService sysMessageLkupService;
    
    @Autowired
    public ServiceMessageService(SysMessageLkupService sysMessageLkupService, ApplicationContext ctx) {
        this.sysMessageLkupService = sysMessageLkupService;
        this.ctx = ctx;
    }

    @PostConstruct
    public void init() {
        // Creating reference to instance. Necessary to call cached methods from withing class.
        self = ctx.getBean(ServiceMessageService.class);
    }
    
    public ServiceMessage getByCode(String code) {
    
        // TODO: returnUnknown flag
        
        SysMessageLkup sysMessage = null;
        
        try {

            sysMessage = this.sysMessageLkupService.getByCode(code);

        } catch (Exception e) {
            log.warn("Error loading sys message lkup with code [{}]", code,e);
        }
        
        return ServiceMessage.from(sysMessage);
    }
    
}
