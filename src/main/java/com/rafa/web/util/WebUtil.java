package com.rafa.web.util;

import com.rafa.util.ParserUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WebUtil {
    
    public static Map<String, String> getRequestHeadersInfo(HttpServletRequest request) {
        
        Map<String, String> map = new HashMap<>();
        
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        
        return map;
    }
    
    public static Map<String, String> getResponseHeadersInfo(HttpServletResponse response) {
        
        Map<String, String> map = new HashMap<>();
        
        Collection<String> headerNames = response.getHeaderNames();
        for (String key : headerNames) {
            String value = response.getHeader(key);
            map.put(key, value);
        }
        
        return map;
    }
    
    public static String getWrapperByteBufferPayload(byte[] buf, String charEncoding) {
        
        String wrapperPayload = null;
        
        if (buf.length > 0) {
            int length = Math.min(buf.length, 5120);
            try {
                wrapperPayload = new String(buf, 0, length, charEncoding);
            } catch (UnsupportedEncodingException ex) {
                // NOOP
            }
        }
        
        return wrapperPayload;
    }
    
    /**
     * TODO: this can be made much more bullet proof by checking other common http header values etc.
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
    
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (ip == null || ParserUtil.isEmpty(ip)) {
            ip = request.getRemoteAddr();
        }
    
        log.debug("Parsed user ip as [{}]", ip);
    
        return ip;
    
    }
    
}
