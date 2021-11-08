package com.rafa.web.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceResponseHeader {
    
    private String executionId;
    
    private long startTime;
    private long endTime;
    private int duration;
    
    private int status;
    
    private List<ServiceMessage> messages = new ArrayList<>();
    
    public void setEndTime(long endTime) {
        this.endTime = endTime;
        duration = (int) (endTime - startTime);
    }
}
