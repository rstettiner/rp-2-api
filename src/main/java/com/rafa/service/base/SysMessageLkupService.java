package com.rafa.service.base;

import com.rafa.domain.base.SysMessageLkup;
import com.rafa.repository.base.SysMessageLkupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class SysMessageLkupService {
    
    private static final String ACTIVE_MESSAGE_CACHE = "active_messages";
    
    private final ApplicationContext ctx;
    
    private SysMessageLkupService self;

    private final SysMessageLkupRepository sysMessageLkupRepository;
    
    @Autowired
    public SysMessageLkupService(SysMessageLkupRepository sysMessageLkupRepository,
                                ApplicationContext ctx) {
        this.sysMessageLkupRepository = sysMessageLkupRepository;
        this.ctx = ctx;
    }
    
    @PostConstruct
    public void init() {
        // Creating reference to instance. Necessary to call cached methods from withing class.
        self = ctx.getBean(SysMessageLkupService.class);
        // FIX: Preload at start time not working.
//        log.debug("Pre-loading active messages cache...");
//        List<SysMessageLkup> activeMessages = self.getAllActiveMessages();
//        log.debug("Pre-loaded [{}] active messages", activeMessages.size());
    }
    
    @Cacheable(cacheNames = ACTIVE_MESSAGE_CACHE)
    public List<SysMessageLkup> getAllActiveMessages() {
        return this.sysMessageLkupRepository.findAllByStatusCd("A");
    }

    @Cacheable(cacheNames = "Message_Item_Cache")
    public SysMessageLkup getByCode(String code) {
        // message codes are "subsystem-code"
        // Look in the DB and go go go
        if(code==null){
            return null;
        }

        String[] tokens = code.split("-");
        if(tokens==null||tokens.length!=2){
            log.error("Invalid message code passed to cache [{}]",code);
            return null;
        }
        List<SysMessageLkup> messages = sysMessageLkupRepository.findBySubsystemCdAndMessageCd(tokens[0],tokens[1]);
        if(messages==null||messages.isEmpty()){
            log.warn("Unknown message code passed to cache [{}]",code);
            return null;
        }

        if(messages.size()>1){
            log.warn("More than one record on code [{}]",code);
        }

        return messages.get(0);
    }

    @CacheEvict(cacheNames = ACTIVE_MESSAGE_CACHE)
    public void clearActiveMessagesCache() {
        log.debug("Cleared active messages cache!");
    }

}
