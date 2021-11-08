package com.rafa.web.base;

import com.rafa.util.EnumConverter;
import com.rafa.util.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.AttributeConverter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum MessageStatusCd implements Serializable, Code<MessageStatusCd> {

    Active("A"),
    Deleted("D");
    
    private String code;
    
    public static MessageStatusCd getByCd(String cd) {
        return EnumConverter.getByCode(cd, values());
    }

    public static class Converter implements AttributeConverter<MessageStatusCd, String> {

        @Override
        public String convertToDatabaseColumn(MessageStatusCd code) {
            return code.code;
        }

        @Override
        public MessageStatusCd convertToEntityAttribute(String s) {
            return getByCd(s);
        }
    }
}
