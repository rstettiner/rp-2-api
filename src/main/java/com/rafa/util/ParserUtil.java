package com.rafa.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ParserUtil {
    
    public static boolean nonNullNonEmpty(String s) {
        return s != null && !s.isEmpty();
    }

    /**
     * Returns true if string is null or blank
     */
    public static boolean isEmpty(String s){
        return ! nonNullNonEmpty(s);
    }
    
    public static String redactCenter(String input, int percentage) {
        return ParserUtil.redactCenter(input, percentage, "*");
    }
    
    public static String redactCenter(String input, int percentage, String redactChar) {
        
        if (input == null) {
            return input;
        }
        
        if (percentage > 100) {
            percentage = 100;
        } else if (percentage < 0) {
            percentage = 0;
        }
        
        if (percentage == 0) {
            return input;
        }
        
        int length = input.length();
        int redactLength = length * percentage / 100;
        int redactStartIdx = (length - redactLength) / 2;
        
        String redacted = input.substring(0, redactStartIdx);
        redacted += ParserUtil.repeat(redactChar, redactLength) + input.substring(redactStartIdx + redactLength);
        
        return redacted;
    }
    
    public static String repeat(String input, int times) {
        
        String repeatedString = "";
        
        for (int i = 0; i < times; i++) {
            repeatedString += input;
        }
        
        return repeatedString;
    }
    
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
}
