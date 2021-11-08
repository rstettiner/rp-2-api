package com.rafa.web.base;

import org.springframework.http.HttpStatus;

public class AbstractBaseResource {
    
    protected ServiceResponse success() {
        
        ServiceContext ctx = ServiceContext.getContext();
        ctx.setHttpStatus(HttpStatus.NO_CONTENT);
        
        return success(null);
    }

    protected <T> ServiceResponse<T> success(T response) {
        
        ServiceContext.getContext().setEndTime(System.currentTimeMillis());
        
        ServiceContext ctx = ServiceContext.getContext();
        
        ServiceResponse<T> serviceResponse = ServiceResponse.buildResponse(ctx);
        serviceResponse.getHeader().setStatus(ctx.getHttpStatus() != null ? ctx.getHttpStatus().value() : HttpStatus.OK.value());
        serviceResponse.setPayload(response);


        return serviceResponse;
    }
    
}
