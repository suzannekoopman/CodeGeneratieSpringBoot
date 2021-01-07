package io.swagger.controller;

import io.swagger.api.LoginApi;
import io.swagger.model.AuthToken;
import io.swagger.model.Login;
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
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class LoginApiController implements LoginApi {

    private static final Logger log = LoggerFactory.getLogger(LoginApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private AuthenticationService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public LoginApiController(ObjectMapper objectMapper, HttpServletRequest request, AuthenticationService authService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.authService = authService;
    }

    public ResponseEntity<AuthToken> loginUser(@ApiParam(value = ""  )  @Valid @RequestBody Login body
) {
        String accept = request.getHeader("Accept");
        //bad json format
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<AuthToken>(objectMapper.readValue("{\n  \"AuthToken\" : \"1234-abcd-5678-efgh\"\n}", AuthToken.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<AuthToken>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        AuthToken authToken = authService.validateUserAndReturnAuthToken(body);

        //if no user found, token is null
        if (authToken == null)
            return new ResponseEntity(HttpStatus.valueOf(401));

        return new ResponseEntity<AuthToken>(authToken,HttpStatus.valueOf(201));
    }

}
