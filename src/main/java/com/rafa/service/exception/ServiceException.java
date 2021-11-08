package com.rafa.service.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    private String messageCd;
    private Object[] params = null;


    public static final String UNHANDLED_EXCEPTION_CODE = "SYS-001";

    public static ServiceException getUnhandledException(Exception e) {
        return new ServiceException(UNHANDLED_EXCEPTION_CODE, e);
    }

    public ServiceException() {
        super();
    }

    public ServiceException(String msgCd, Exception nested, Object ...params) {
        super(nested);
        this.messageCd = msgCd;
        this.params = params;
    }
    
}
