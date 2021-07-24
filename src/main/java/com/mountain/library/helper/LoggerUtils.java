package com.mountain.library.helper;

import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class LoggerUtils {
    public LoggerUtils() {
    }

    public static void putPrincipalMDC(Authentication authentication) {
        if (authentication != null) {
            UserDetails principal = (UserDetails)authentication.getPrincipal();
            MDC.put("username", principal != null ? principal.getUsername() : "");
        }
    }

    public static void logTime(Logger logger, String methodName, long start, long end) {
        logger.info("Request to {} completed in {} s.", methodName, (double)(end - start) / 1000.0D);
    }
}
