package com.rafa.web.base;
import com.rafa.service.base.ServiceMessageService;
import com.rafa.service.exception.ServiceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service Context is generated at service execution start and passed on thread local as a persistent data share
 * across business method calls.
 *
 * contains diagnostic information about the call, most importantly a unique transaction ID which can be user to
 * track this service invocation.
 *
 */
@Slf4j
@Data
public class ServiceContext {
    
    public static String EXECUTION_ID_KEY = "executionId";
    
    private static ThreadLocal<ServiceContext> threadLocal = new ThreadLocal<>();
    
    private ServiceMessageService messageService;
    
    private String executionId;
    
    private Date startDt;
    private Date endDt;
    private long startTime;
    private long endTime;
    
    private HttpStatus httpStatus;
    
    private List<ServiceMessage> messages = new ArrayList<>();

    private String ipAddress;
    private String userAgent;
    private String timeZone;
    private Exception failureException;
    private String originalServiceCallUrl;
    
    /**
     * Overloaded helper getContext method, not requiring executionId.
     * <p>
     * Passes null to ServiceContext.getContext(executionId).
     *
     * @return thread ServiceContext
     */
    public static ServiceContext getContext() {
        // TODO: Should it be synchronized also?
        return ServiceContext.getContext(null);
    }
    
    /**
     * Lazy loads thread's ServiceContext.
     * <p>
     * A executionId value can be passed as argument, but it will only be used
     * for new ServiceContext instantiations.
     *
     * @param executionId Optional executionId, if null, one will be created.
     * @return thread Service Context
     */
    public synchronized static ServiceContext getContext(String executionId) {
        
        ServiceContext ctx = threadLocal.get();
        
        if (ctx == null) {
            ctx = newContext(executionId);
            log.debug("Creating new context. Context: [{}]", ctx);
            return ctx;
        }
        
        log.trace("Returning existing context. Context: [{}]", ctx);
        return ctx;
    }
    
    /**
     * Creates a new service execution context and puts it in Thread scope for later retrieval.
     *
     * @param executionId the executionId
     * @return newly created ServiceContext
     */
    protected static ServiceContext newContext(String executionId) {
        ServiceContext ctx = new ServiceContext(executionId);
        threadLocal.set(ctx);
        return ctx;
    }
    
    /**
     * Clears the thread's ServiceContext.
     * <p>
     * Obs: Should always be called to avoid memory leak/cross execution data leakage.
     */
    public static void clearContext() {
        log.debug("Clearing context: [{}]", threadLocal.get() != null ? threadLocal.get().executionId : null);
        threadLocal.remove();
        MDC.remove("executionId");
    }
    
    /**
     * Service context constructor.
     * <p>
     * Accepts a optional executionId value. If not sent, a new one will be
     * created for he instance.
     *
     * @param executionId the execution id value - optional
     */
    private ServiceContext(String executionId) {
        
        String txnId = executionId;
        
        if (executionId == null || executionId.isEmpty()) {
            txnId = UUID.randomUUID().toString();
        }
        
        this.executionId = txnId;
        
        // Default HttpStatus is Success 200
        this.httpStatus = HttpStatus.OK;
        
        MDC.put(EXECUTION_ID_KEY, txnId);
    }
    
    /**
     * Adds a ServiceMessage to ServiceContext message list.
     * <p>
     * If message has HttpStatus, will set as ServiceContext status.
     */
    public void addMessage(String msgCd) {
        this.addMessage(msgCd, null);
    }
    
    /**
     * Adds a message to the context, looking up by it's code in the Messages cache.
     * <p>
     * Replaces parameters.
     *
     * @param msgCd  the message code
     * @param params the parameters
     */
    public void addMessage(String msgCd, Object... params) {
        
        this.addMessage(getMessage(msgCd, null, params));
    }
    
    private ServiceMessage getMessage(String msgCd, Throwable t, Object... params) {
        
        ServiceMessage msg = null;
        
        try {
            if (this.messageService != null) {
                msg = this.messageService.getByCode(msgCd);
            } else {
                log.warn("Message Service is null");
            }
        } catch (Exception e) {
            log.error("Failed adding message [{}] to context with params [{}] and throwable: [{}]", msgCd, params, t, e);
        }
        
        
        if (msg == null) {
            msg = ServiceMessage.unknownWithCode(msgCd);
        }
        
        if (t != null) {
            // TODO: if stack trace config, set stack trace; - pass in flag parameter.
            msg.setException(t);
        }
        
        if (params != null) {
            msg.setDesc(msg.getDescription(), params);
        }
        
        return msg;
    }
    
    /**
     * Adds a ServiceMessage to ServiceContext message list.
     * <p>
     * If message has HttpStatus, will set as ServiceContext status.
     */
    public void addMessage(ServiceMessage message) {
        if (message.getHttpStatus() != null) {
            this.httpStatus = message.getHttpStatus();
        }
        messages.add(message);
    }
    
    /**
     * Adds an exception message to the ServiceContext
     *
     * @param serviceException the service exception.
     */
    public void addExceptionToContext(ServiceException serviceException) {
        
        // TODO: Clean up unused methods
        // TODO: Add stack to message, controlled by config
        
        this.addMessage(getMessage(serviceException.getMessageCd(), serviceException.getCause(), serviceException.getParams()));
    }
    
    /**
     * Adds an exception message to the ServiceContext
     *
     * @param e the exception (Throwable)
     */
    public void addExceptionToContext(Throwable e) {
        this.addExceptionToContext(e, null);
    }
    
    /**
     * Adds an exception message to the ServiceContext
     *
     * @param status the HttpStatus
     * @param e      the exception (Throwable)
     */
    public void addExceptionToContext(Throwable e, HttpStatus status) {
        
        ServiceMessage message = new ServiceMessage();
        
        message.setType(ServiceMessage.Type.System);
        message.setCd("EXP-001");
        message.setLevel(ServiceMessage.Level.Error);
        message.setDescription(e.toString());
        
        if (status != null) {
            message.setHttpStatus(status);
        }
        
        this.addMessage(message);
    }
    
    
    public String getCurrentUser() {
        String userId = "SYSTEM";
        return userId;
    }
    
    public void startServiceCall(String callUrl, String ipAddr, String userAgent) {
        this.setStartTime(System.currentTimeMillis());
        this.setStartDt(new Date());
        this.setOriginalServiceCallUrl(callUrl);
        this.setStartTime(System.currentTimeMillis());
        this.setIpAddress(ipAddr);
        this.setUserAgent(userAgent);
    }
    
    public void endServiceCall() {
        this.setEndTime(System.currentTimeMillis());
        this.setEndDt(new Date());
    }
}
