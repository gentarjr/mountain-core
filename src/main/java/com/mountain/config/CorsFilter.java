package com.mountain.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse httpServletResponse = (HttpServletResponse) res;
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*"); // IMPORTANT: Allowed all the domains
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT, PATCH, HEAD");
        httpServletResponse.addHeader("Access-Control-Allow-Headers",
                "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, Authorization");
        httpServletResponse.addHeader("Access-Control-Expose-Headers",
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Content-Disposition");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.addIntHeader("Access-Control-Max-Age", 3600);
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
