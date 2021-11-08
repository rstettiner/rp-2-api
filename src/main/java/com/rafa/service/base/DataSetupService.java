package com.rafa.service.base;

import com.rafa.domain.base.SysMessageLkup;
import com.rafa.repository.base.SysMessageLkupRepository;
import com.rafa.web.base.MessageLevelCd;
import com.rafa.web.base.MessageStatusCd;
import com.rafa.web.base.MessageTypeCd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DataSetupService {

    private final SysMessageLkupRepository msgRepository;

    @Autowired
    public DataSetupService(SysMessageLkupRepository msgRepository) {
        this.msgRepository = msgRepository;
    }


    public void setupData() {
        makeMessages();
    }


    public void makeMessages() {
        makeMessage("USR","001",MessageTypeCd.User,MessageLevelCd.ERROR,HttpStatus.UNAUTHORIZED.value(),"User name / Password not valid");
    }

    public void makeMessage(String subSystem, String code, MessageTypeCd type, MessageLevelCd level, int httpStatus,String message ) {
        // First try and find it.
        List<SysMessageLkup> messages = msgRepository.findBySubsystemCdAndMessageCd(subSystem,code);
        SysMessageLkup msg = null;
        if(messages.isEmpty()){
            msg = new SysMessageLkup();
        }else {
            msg = messages.get(0);
        }

        msg.setSystemCd("Datafi");
        msg.setSubsystemCd(subSystem);
        msg.setMessageCd(code);
        msg.setMessageTypeCd(type);
        msg.setMessageLevelCd(level);
        msg.setHttpStatusCd(httpStatus);
        msg.setMessageText(message);

        // If we are making it, it should be active?
        msg.setStatusCd(MessageStatusCd.Active);


        if(msg.getMessageId() != null){
            log.info("Updating message  :  {}",msg);
        }else {
            log.info("Making new message:  {}",msg);
        }

        msgRepository.save(msg);
    }
}
