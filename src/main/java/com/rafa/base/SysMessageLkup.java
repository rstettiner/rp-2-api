package com.rafa.domain.base;

import com.rafa.domain.auditable.Auditable;
import com.rafa.repository.base.AuditableFields;
import com.rafa.web.base.MessageLevelCd;
import com.rafa.web.base.MessageStatusCd;
import com.rafa.web.base.MessageTypeCd;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_message_lkup")
@EntityListeners(AuditableFields.class)
public class SysMessageLkup extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    
    @Column(name = "system_cd")
    private String systemCd;

    @Column(name = "subsystem_cd")
    private String subsystemCd;
    
    @Column(name = "message_cd")
    private String messageCd;

    @Convert(converter = MessageTypeCd.Converter.class)
    @Column(name = "message_type_cd")
    private MessageTypeCd messageTypeCd;
    
    @Convert(converter = MessageLevelCd.Converter.class)
    @Column(name = "message_level_cd")
    private MessageLevelCd messageLevelCd;
    
    @Column(name = "http_status_cd")
    private Integer httpStatusCd;
    
    @Column(name = "message_text")
    private String messageText;

    @Convert(converter = MessageStatusCd.Converter.class)
    @Column(name = "status_cd")
    private MessageStatusCd statusCd;
    
    public String getCode() {
        return subsystemCd + "-" + messageCd;
    }
    
}
