package io.swagger.controller;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.api.UsersApi;
import io.swagger.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AuthenticationService;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;


    private AuthenticationService authService;
    private UserService userService;
    private TransactionService transactionService;
    private AccountService accountService;


    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, AuthenticationService authService, UserService userService, TransactionService transactionService, AccountService accountService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.authService = authService;
        this.userService = userService;
        this.transactionService = transactionService;
        this.accountService = accountService;
    }
    public ResponseEntity<Void> deleteRegisterRequestById(@ApiParam(value = "The id from the request",required=true) @PathVariable("requestId") Integer requestId
    ) {
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.valueOf(authService.deleteRegisterRequest(requestId)));
    }


    public ResponseEntity<Account> createAccountByUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Account body
            , @ApiParam(value = "user of a specific account",required=true) @PathVariable("userId") Integer userId
    ){
        String accept = request.getHeader("Accept");
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if(accountService.createAccount(body, userId) == null)
            return new ResponseEntity<Account>(HttpStatus.valueOf(406));

        return new ResponseEntity<Account>(accountService.createAccount(body, userId), HttpStatus.CREATED);
    }

    public ResponseEntity<User> createUser(@ApiParam(value = "" ,required=true )  @Valid @RequestBody User body
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, 0, true)) {
            try {
                authService.createRegisterRequest(new RegisterRequest(body.getFirstName(), body.getLastName(), body.getPassword(), body.getEmail()));
                return new ResponseEntity(HttpStatus.CREATED);
            }
            catch (Exception e) {
                System.out.println(e);
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            }
        }

        try {
            return new ResponseEntity<User>(userService.signUpUser(body), HttpStatus.CREATED);
        }
        catch (Exception e){
            System.out.println(e);
            return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<Void> deleteUserById(@ApiParam(value = "The id from the user",required=true) @PathVariable("userId") Integer userId
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity<Void>(HttpStatus.valueOf(userService.deleteUserById(userId)));
    }

    public ResponseEntity<List<Account>> getAccountsByUser(@ApiParam(value = "user of a specific account",required=true) @PathVariable("userId") Integer userId
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Account>>(objectMapper.readValue("[ {\n  \"owner\" : 1,\n  \"account_type\" : [ \"current\", \"current\" ],\n  \"transactionDayLimit\" : 100,\n  \"balance\" : 200,\n  \"transactionAmountLimit\" : 200,\n  \"iban\" : \"NL11INHO0123456789\",\n  \"balanceLimit\" : -1200\n}, {\n  \"owner\" : 1,\n  \"account_type\" : [ \"current\", \"current\" ],\n  \"transactionDayLimit\" : 100,\n  \"balance\" : 200,\n  \"transactionAmountLimit\" : 200,\n  \"iban\" : \"NL11INHO0123456789\",\n  \"balanceLimit\" : -1200\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(accountService.getAllAccountsFromUser(userId), HttpStatus.OK);
    }

    public ResponseEntity<List<RegisterRequest>> getAllRegisterRequests() {

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if (!authService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<RegisterRequest>>(objectMapper.readValue("[ {\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"password\" : \"\",\n  \"email\" : \"email\"\n}, {\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"password\" : \"\",\n  \"email\" : \"email\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<RegisterRequest>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<RegisterRequest>>(userService.findAllRegisterRequests(), HttpStatus.OK);
    }

    public ResponseEntity<List<Transaction>> getAllTransactionsFromUser(@ApiParam(value = "Get details of transaction based on iban",required=true) @PathVariable("userId") Integer userId
            ,@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            ,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
    ) {
        String accept = request.getHeader("Accept");
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"transaction_id\" : 10034,\n  \"amount\" : 22.30,\n  \"account_to\" : \"NL39ING008451843\",\n  \"account_from\" : \"NL39INGB007801007\",\n  \"transaction_type\" : [ \"withdraw\", \"withdraw\" ],\n  \"user_performing\" : 1,\n  \"timestamp\" : \"995-09-07T10:40:52Z\"\n}, {\n  \"transaction_id\" : 10034,\n  \"amount\" : 22.30,\n  \"account_to\" : \"NL39ING008451843\",\n  \"account_from\" : \"NL39INGB007801007\",\n  \"transaction_type\" : [ \"withdraw\", \"withdraw\" ],\n  \"user_performing\" : 1,\n  \"timestamp\" : \"995-09-07T10:40:52Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        try {
            return new ResponseEntity<List<Transaction>>(transactionService.getAllTransactionsOfUser(userId, apiKeyAuth, offset, limit), HttpStatus.OK);
        } catch (Exception e) {
            ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message",e.getMessage()));
            return responseEntity;
        }
    }


    public ResponseEntity<List<User>> getAllUsers(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            ,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
            ,@ApiParam(value = "The name the user should have") @Valid @RequestParam(value = "name", required = false) String name
            ,@ApiParam(value = "The email the user should have") @Valid @RequestParam(value = "email", required = false) String email
    ) {
        String accept = request.getHeader("Accept");
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<User>>(objectMapper.readValue("[ {\n  \"firstName\" : \"John\",\n  \"lastName\" : \"van Vuuren\",\n  \"password\" : \"thisismypassword3485\",\n  \"user_type\" : [ \"customer\", \"customer\" ],\n  \"user_id\" : 1,\n  \"email\" : \"john@vanVuuren.com\"\n}, {\n  \"firstName\" : \"John\",\n  \"lastName\" : \"van Vuuren\",\n  \"password\" : \"thisismypassword3485\",\n  \"user_type\" : [ \"customer\", \"customer\" ],\n  \"user_id\" : 1,\n  \"email\" : \"john@vanVuuren.com\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (email != null) {
            User u = userService.findUserByEmail(email);
            List<User> users = new ArrayList<User>();
            if (u == null)
                return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
            users.add(u);
            return new ResponseEntity<List<User>>(users, HttpStatus.OK);
        }
        else if (name != null) {
            return new ResponseEntity<List<User>>(userService.findUserByName(name), HttpStatus.OK);
        }
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    public ResponseEntity<User> getUserById(@ApiParam(value = "The id from the user",required=true) @PathVariable("userId") Integer userId
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity<User>(userService.findUserById(userId),HttpStatus.OK);
    }

    public ResponseEntity<Transaction> machineTransfer(@ApiParam(value = "",required=true) @PathVariable("userId") Integer userId
            ,@ApiParam(value = ""  )  @Valid @RequestBody MachineTransfer body
    ) {
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Transaction>(objectMapper.readValue("{\n  \"transaction_id\" : 0,\n  \"amount\" : 22.30,\n  \"account_to\" : \"NLxxINHO0xxxxxxxxx\",\n  \"account_from\" : \"NLxxINHO0xxxxxxxxx\",\n  \"transaction_type\" : [ \"withdraw\", \"withdraw\" ],\n  \"user_performing\" : 6,\n  \"timestamp\" : \"timestamp\"\n}", Transaction.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Transaction>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        Transaction machineTransfer = transactionService.createMachineTransfer(userId, body);
        //if machine transfer is invalid (no current account, amount to low)
        if(machineTransfer == null)
            return new ResponseEntity<Transaction>(HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<Transaction>(machineTransfer, HttpStatus.CREATED);
    }


    public ResponseEntity<User> updateUserById(@ApiParam(value = "" ,required=true )  @Valid @RequestBody User body
            ,@ApiParam(value = "The id from the user",required=true) @PathVariable("userId") Integer userId
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, userId, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        try {
            return new ResponseEntity<User>(userService.updateUserById(body, userId), HttpStatus.OK);
        }
        catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}

