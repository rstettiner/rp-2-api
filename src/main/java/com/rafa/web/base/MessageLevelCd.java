package com.rafa.web.base;

import com.rafa.util.EnumConverter;
import com.rafa.util.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum MessageLevelCd implements Serializable, Code<MessageLevelCd> {

    INFO("I"),
    WARNING("W"),
    ERROR("E");
    
    private String code;
    
    public static MessageLevelCd getByCd(String cd) {
        return EnumConverter.getByCode(cd, values());
    }

    public static class Converter implements AttributeConverter<MessageLevelCd, String> {

        @Override
        public String convertToDatabaseColumn(MessageLevelCd levelCd) {
            return levelCd.code;
        }

        @Override
        public MessageLevelCd convertToEntityAttribute(String s) {
            return getByCd(s);
        }
    }
}
