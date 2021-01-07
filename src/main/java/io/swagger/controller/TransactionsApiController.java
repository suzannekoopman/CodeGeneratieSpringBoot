package io.swagger.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.api.TransactionsApi;

import io.swagger.model.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;

import io.swagger.service.AuthenticationService;
import io.swagger.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-04-28T15:21:59.457Z[GMT]")
@Controller
public class TransactionsApiController implements TransactionsApi {
    private TransactionService service;

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private AuthenticationService authService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request, TransactionService service, AuthenticationService authService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.service = service;
        this.authService = authService;
    }

    public ResponseEntity<Transaction> createTransaction(@ApiParam(value = "Created transaction object" ,required=true)  @Valid @RequestBody Transaction transaction) {

        String apiKeyAuth = request.getHeader("ApiKeyAuth");

        if(!authService.isUserAuthenticated(apiKeyAuth, 0, false))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        try {
            return new ResponseEntity<Transaction>(	service.createTransactionForUser(transaction , apiKeyAuth),	HttpStatus.CREATED);
        }  catch (Exception e) {
            ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message",e.getMessage()));
            return responseEntity;
        }
    }



    public ResponseEntity<List<Transaction>> getAllTransactions(@ApiParam(value = "The number of items to skip before starting to collect the result set") @Valid @RequestParam(value = "offset", required = false) Integer offset
            ,@ApiParam(value = "The numbers of items to return") @Valid @RequestParam(value = "limit", required = false) Integer limit
            ,@ApiParam(value = "The id of the user thats should ne involved within the transaction") @Valid @RequestParam(value = "userId", required = false) Integer userId
            ,@ApiParam(value = "The iban that should be involved within the transactions") @Valid @RequestParam(value = "iban", required = false) String iban
    ) {
        //get auth token
        String accept = request.getHeader("Accept");

        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Transaction>>(objectMapper.readValue("[ {\n  \"transaction_id\" : 10034,\n  \"amount\" : 22.30,\n  \"account_to\" : \"NL39ING008451843\",\n  \"account_from\" : \"NL39INGB007801007\",\n  \"transaction_type\" : [ \"withdraw\", \"withdraw\" ],\n  \"user_performing\" : 1,\n  \"timestamp\" : \"995-09-07T10:40:52Z\"\n}, {\n  \"transaction_id\" : 10034,\n  \"amount\" : 22.30,\n  \"account_to\" : \"NL39ING008451843\",\n  \"account_from\" : \"NL39INGB007801007\",\n  \"transaction_type\" : [ \"withdraw\", \"withdraw\" ],\n  \"user_performing\" : 1,\n  \"timestamp\" : \"995-09-07T10:40:52Z\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Transaction>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        //if not authenticated
        String apiKeyAuth = request.getHeader("ApiKeyAuth");
        if(!authService.isUserAuthenticated(apiKeyAuth, 0, true))
            return new ResponseEntity(HttpStatus.FORBIDDEN);


        try{
            if(iban==null){
                return new ResponseEntity<List<Transaction>>(service.getAllTransactions(limit, offset), HttpStatus.OK);
            }else{
                return new ResponseEntity<List<Transaction>>(service.getAllTransactionOfAccount(iban, apiKeyAuth, offset, limit), HttpStatus.OK);
            }
        } catch (Exception e) {
            ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body((JsonNode) objectMapper.createObjectNode().put("message",e.getMessage()));
            return responseEntity;
        }

    }

}
