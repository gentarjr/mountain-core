package com.mountain.spring.security;

import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.spring.service.JsonMapperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ApiAccessDeniedHandler implements AccessDeniedHandler {

    private final JsonMapperService mapper;

    public ApiAccessDeniedHandler(JsonMapperService mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ResponseEnvelope rm =
                new ResponseEnvelope(
                        ErrCode.FORBIDDEN.getCode(),
                        "Forbidden Access");

        try (PrintWriter out = response.getWriter()) {
            mapper.write(out, rm);
        }
    }
}
