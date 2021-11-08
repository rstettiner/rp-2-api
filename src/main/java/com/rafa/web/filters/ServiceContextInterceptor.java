package com.rafa.web.filters;

import com.rafa.service.base.ServiceMessageService;
import com.rafa.web.util.WebUtil;
import com.rafa.web.base.ServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class ServiceContextInterceptor implements HandlerInterceptor {


    public static String USER_AGENT_HEADER = "User-Agent";
    public static String EXECUTION_ID_HEADER = "Execution-Id";
    public static String SRC_SYS_HEADER = "Source-System";
    public static String SRC_SYS_VERSION_HEADER = "Source-System-Version";
    public static String SRC_SYS_TIMEZONE_HEADER = "Source-System-Timezone";

    ArrayList<String> probes = new ArrayList<String>() {{
        add("/");
        add("/error");
    }};
    
    @Autowired
    private ServiceMessageService messageService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        // Add message on logout?
        // Cache (make sure classes are serializable)
        // Redact password from callLog
        // Remove password from responses
        // Redact tokens from call logs
        // Test logging calls from interceptor
        // Try to tame /error
        // Sys messages with time
        // Scheduled task to invalidate sessions
    
        ServiceContext context = this.startNewServiceContext(request, response);
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        this.finishContext(ServiceContext.getContext(), request, response);
    }
    
    private ServiceContext startNewServiceContext(HttpServletRequest request, HttpServletResponse response) {

        if(!this.probes.contains(request.getRequestURI())) {
            log.info("Started service call [{}]", request.getRequestURI());
        }

        // Execution ID
        String executionId = request.getHeader(EXECUTION_ID_HEADER);
        
        log.debug("Received caller Execution-Id: [{}]", executionId);
        
        // Setting up service context
        ServiceContext.clearContext();
        
        ServiceContext context = ServiceContext.getContext(executionId);
        
        context.startServiceCall(
            request.getRequestURI(),
            WebUtil.getIp(request),
            request.getHeader(USER_AGENT_HEADER)
        );
        
        context.setTimeZone(request.getHeader(SRC_SYS_TIMEZONE_HEADER));
        context.setUserAgent(request.getHeader(USER_AGENT_HEADER));
        
        context.setMessageService(this.messageService);
        
        log.debug("Started context with Execution-Id: [{}]", context.getExecutionId());
    
        response.setHeader(EXECUTION_ID_HEADER, context.getExecutionId());
        
        return context;
    }
    
    private void finishContext(ServiceContext context, HttpServletRequest requestToCache, HttpServletResponse responseToCache) {
        
        // Setting Execution-Id response header
        responseToCache.setHeader(EXECUTION_ID_HEADER, context.getExecutionId());
        if(!this.probes.contains(requestToCache.getRequestURI())) {
            log.debug("Finishing execution of [{}]. End time: [{}]", requestToCache.getRequestURI(), new Date
                    (context.getEndTime()));
        }

        context.endServiceCall();

        if(!this.probes.contains(requestToCache.getRequestURI())) {
            log.info("Finished service call [{}]", requestToCache.getRequestURI());
        }
        ServiceContext.clearContext();
        
    }


}
