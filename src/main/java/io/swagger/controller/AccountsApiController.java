package io.swagger.controller;

import io.swagger.api.AccountsApi;
import io.swagger.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import io.swagger.service.AccountService;
import io.swagger.service.AuthenticationService;
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
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private AccountService accountService;
    private AuthenticationService authenticationService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request, AccountService accountService, AuthenticationService authenticationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    public ResponseEntity<Void> deleteAccountByIban(@ApiParam(value = "iban of a specific account",required=true) @PathVariable("iban") String iban
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authenticationService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        return new ResponseEntity<Void>(HttpStatus.valueOf(accountService.deleteAccount(iban)));
    }

    public ResponseEntity<List<Account>> getAllAccounts(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            ,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
            ,@ApiParam(value = "The iban the account should have") @Valid @RequestParam(value = "iban", required = false) String iban
            ,@ApiParam(value = "The iban the account should have available accounts in the system", allowableValues = "current, savings") @Valid @RequestParam(value = "account_type", required = false) String accountType
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authenticationService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Account>>(objectMapper.readValue("[ {\n  \"owner\" : 1,\n  \"account_type\" : [ \"current\", \"current\" ],\n  \"transactionDayLimit\" : 100,\n  \"balance\" : 200,\n  \"transactionAmountLimit\" : 200,\n  \"iban\" : \"NL11INHO0123456789\",\n  \"balanceLimit\" : -1200\n}, {\n  \"owner\" : 1,\n  \"account_type\" : [ \"current\", \"current\" ],\n  \"transactionDayLimit\" : 100,\n  \"balance\" : 200,\n  \"transactionAmountLimit\" : 200,\n  \"iban\" : \"NL11INHO0123456789\",\n  \"balanceLimit\" : -1200\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Account>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Account>>(accountService.getAllAccounts(), HttpStatus.OK);
    }

    public ResponseEntity<Account> getSpecificAccount(@ApiParam(value = "user of a specific account",required=true) @PathVariable("iban") String iban
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authenticationService.isUserAuthenticated(apiKeyAuth, 0, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Account>(objectMapper.readValue("{\n  \"owner\" : 1,\n  \"account_type\" : [ \"current\", \"current\" ],\n  \"transactionDayLimit\" : 100,\n  \"balance\" : 200,\n  \"transactionAmountLimit\" : 200,\n  \"iban\" : \"NL11INHO0123456789\",\n  \"balanceLimit\" : -1200\n}", Account.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Account>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        if(accountService.getSpecificAccount(iban) == null)
            return new ResponseEntity<Account>(HttpStatus.valueOf(406));

        return new ResponseEntity<Account>(accountService.getSpecificAccount(iban), HttpStatus.OK);
    }

    public ResponseEntity<Account> updateSpecificAccount(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Account body
            ,@ApiParam(value = "user of a specific account",required=true) @PathVariable("iban") String iban
    ) {
        String accept = request.getHeader("Accept");

        String apiKeyAuth = request.getHeader("ApiKeyAuth");

        if(!authenticationService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if(accountService.updateAccount(body, iban) == null)
            return new ResponseEntity<Account>(HttpStatus.valueOf(406));

        return new ResponseEntity<Account>(accountService.updateAccount(body, iban), HttpStatus.OK);
    }

}

