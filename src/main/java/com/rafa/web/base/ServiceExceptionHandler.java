package com.rafa.web.base;

import com.rafa.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler {
    
    
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ServiceResponse> serviceException(ServiceException e) {
        
        log.info("Service exception", e);
        
        ServiceContext ctx = ServiceContext.getContext();
        ctx.addExceptionToContext(e);
        ctx.setFailureException(e);
        
        ServiceResponse resp = ServiceResponse.buildResponse(ctx);
        resp.setException(e.getCause() == null ? e : e.getCause());
        
        return new ResponseEntity<>(resp, ctx.getHttpStatus());
    }
    
    @ExceptionHandler
    public ResponseEntity<ServiceResponse> unhandledException(Exception e) {
        
        log.error("Unhandled exception ", e);
        
        ServiceContext ctx = ServiceContext.getContext();
        ctx.addExceptionToContext(ServiceException.getUnhandledException(e));
        ctx.setFailureException(e);
        
        ServiceResponse resp = ServiceResponse.buildResponse(ctx);
        resp.setException(e);
        
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
}
