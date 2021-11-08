package com.rafa.web.base;

import com.rafa.util.EnumConverter;
import com.rafa.util.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum MessageTypeCd implements Serializable, Code<MessageTypeCd> {

    System("S"),
    User("U");
    
    private String code;
    
    public static MessageTypeCd getByCd(String cd) {
        return EnumConverter.getByCode(cd, values());
    }

    public static class Converter implements AttributeConverter<MessageTypeCd, String> {

        @Override
        public String convertToDatabaseColumn(MessageTypeCd code) {
            return code.code;
        }

        @Override
        public MessageTypeCd convertToEntityAttribute(String s) {
            return getByCd(s);
        }
    }
}
