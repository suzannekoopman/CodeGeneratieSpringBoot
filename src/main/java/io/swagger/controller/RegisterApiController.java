package io.swagger.controller;

import io.swagger.api.RegisterApi;
import io.swagger.model.RegisterRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class RegisterApiController implements RegisterApi {

    private static final Logger log = LoggerFactory.getLogger(RegisterApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private AuthenticationService authenticationService;

    @org.springframework.beans.factory.annotation.Autowired
    public RegisterApiController(ObjectMapper objectMapper, HttpServletRequest request,AuthenticationService authenticationService ) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.authenticationService = authenticationService;
    }

    public ResponseEntity<RegisterRequest> registerUser(@ApiParam(value = ""  )  @Valid @RequestBody RegisterRequest body
) {
        String accept = request.getHeader("Accept");

        RegisterRequest request = authenticationService.createRegisterRequest(body);

        //if no there already exist a request or email/password is invalid
        if(request == null)
            return new ResponseEntity<RegisterRequest>(HttpStatus.valueOf(406));

        return new ResponseEntity<RegisterRequest>(request, HttpStatus.CREATED);
    }

}
