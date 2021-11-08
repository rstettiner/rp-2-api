package com.rafa.web.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rafa.domain.base.SysMessageLkup;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Slf4j
@NoArgsConstructor
public class ServiceMessage implements Serializable {
    
    public static final String UNKNOWN_MESSAGE_EXCEPTION_CODE = "SYS-000";
    public static final String UNKNOWN_EXCEPTION_CODE = "SYS-001";
    
    private Type type;
    private String typeCd;
    private Level level;
    private String levelCd;
    private String cd;
    private String description;
    private String details;
    private String exception;
    
    @JsonIgnore
    private HttpStatus httpStatus;
    
    @JsonIgnore
    private int httpStatusNum;
    
    public static ServiceMessage unknownWithCode(String code) {
        ServiceMessage unknownMsg = new ServiceMessage();
        unknownMsg.setCd(code);
        unknownMsg.setDescription("Unknown message code: " + code);
        unknownMsg.setLevel(ServiceMessage.Level.Error);
        unknownMsg.setType(ServiceMessage.Type.System);
        return unknownMsg;
    }

    public static ServiceMessage from(SysMessageLkup sysMessage) {

        if (sysMessage == null) {
            return null;
        }

        String cd = sysMessage.getCode();
        ServiceMessage.Type type = ServiceMessage.Type.getType(sysMessage.getMessageTypeCd().getCode());
        ServiceMessage.Level level = ServiceMessage.Level.getLevel(sysMessage.getMessageLevelCd());
    
        ServiceMessage serviceMessage = new ServiceMessage(
            cd,
            type,
            level,
            sysMessage.getMessageText()
        );
    
        if (sysMessage.getHttpStatusCd() != null) {
            serviceMessage.setHttpStatusNum(sysMessage.getHttpStatusCd());
        }
        
        return serviceMessage;
    }
    
    public void setHttpStatusNum(int httpStatusNum) {
        this.httpStatusNum = httpStatusNum;
        this.setHttpStatus(HttpStatus.valueOf(httpStatusNum));
    }
    
    public String getLevelCd() {
        return level == null ? null : level.cd;
    }
    
    public void setLevelCd(String levelCd) {
        this.levelCd = levelCd;
        this.level = Level.getLevel(levelCd);
    }
    
    public String getTypeCd() {
        return type == null ? null : type.cd;
    }
    
    public void setTypeCd(String typeCd) {
        this.typeCd = typeCd;
        this.type = Type.getType(typeCd);
    }
    
    public void setDesc(String desc,Object [] parms) {
        this.description = getMessageReplaceText(desc,parms);
    }
    
    public void setException(Throwable t) {
        // TODO: if stack trace config, set stack trace; - receive as parameter
        this.exception = t.getMessage();
    }
    
    /**
     * Formats message substituting parameters.
     *
     * Message text with {} markers can be replace with dynamic parameters.
     *
     * This method will replace {} markers with the specified parm values.
     *
     * @param text text with substitution markers
     * @param params parameters to be substituted in text.
     *
     * @return formatted message text
     */
    public static String getMessageReplaceText(String text, Object[] params) {
        
        text = text.replaceAll("\\{\\}", "%s");
        text = String.format(text, params);
        
        return text;
    }
    
    
    // Enums
    
    public enum Type {
        User("U"),System("S");
        
        private String cd;
        
        Type(String cd) {
            this.cd = cd;
        }
        
        public static Type getType(String cd) {
            for (Type type : Type.values()) {
                if (type.cd.equals(cd)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public ServiceMessage(String cd, Type type, Level lvl, String text) {
        this.cd = cd;
        this.type = type;
        this.description = text;
        this.level = lvl;
    }
    
    public enum Level {
        Info("I"),
        Warn("W"),
        Error("E");
        
        private String cd;
        
        Level(String cd) {
            this.cd = cd;
        }
    
        public static Level getLevel(String cd) {
            for (Level lvl : Level.values()) {
                if (lvl.cd.equals(cd)) {
                    return lvl;
                }
            }
            return null;
        }
        
        public static Level getLevel(MessageLevelCd cd) {
            switch (cd) {
                case INFO:
                    return Info;
                case WARNING:
                    return Warn;
                case ERROR:
                    return Error;
            }
            return null;
        }
    }
    
}
