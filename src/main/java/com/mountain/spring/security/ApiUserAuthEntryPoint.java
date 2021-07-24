package com.mountain.spring.security;

import com.mountain.controller.v1.user.UserAuthController;
import com.mountain.library.domain.ErrCode;
import com.mountain.library.domain.ResponseEnvelope;
import com.mountain.library.exceptions.JwtTokenException;
import com.mountain.spring.service.JsonMapperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserAuthEntryPoint implements AuthenticationEntryPoint {

    protected JsonMapperService mapper;

    public ApiUserAuthEntryPoint(JsonMapperService m) {
        this.mapper = m;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        ErrCode errCode = ErrCode.BAD_GATEWAY;
        if (authException instanceof JwtTokenException) {
            JwtTokenException ex = (JwtTokenException) authException;
            errCode = ex.getErrCode();
        }

        String errMessage = authException.getMessage();
        ResponseEnvelope rm = new ResponseEnvelope(errCode.getCode(), errMessage);

        rm.add(linkTo(methodOn(UserAuthController.class).login(null, null, null, null, null)).withRel("login"));

        mapper.write(response.getWriter(), rm);
    }
}
