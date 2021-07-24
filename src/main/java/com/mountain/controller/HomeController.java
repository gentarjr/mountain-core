package com.mountain.controller;

import com.mountain.library.domain.ResponseEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/")
@RequiredArgsConstructor
public class HomeController {

    @GetMapping
    public ResponseEntity<ResponseEnvelope> handleServletError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        HttpStatus status = HttpStatus.NOT_FOUND;
        try {
            status = HttpStatus.valueOf(statusCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEnvelope rm = new ResponseEnvelope(status.value(), status.getReasonPhrase());
        return ResponseEntity.status(rm.getCodeAsInt()).body(rm);
    }

}
