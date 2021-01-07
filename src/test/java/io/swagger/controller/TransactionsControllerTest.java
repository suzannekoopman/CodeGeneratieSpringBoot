package io.swagger.controller;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }



    //create transaction
    @Test
    public void userPerformsTransactionWithValidInputShouldReturnCreated() throws Exception {

        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL88INHO0993873040");
        transaction.put("account_to", "NL06INHO0463973767");
        transaction.put("amount", 5);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\n" +
                        "    'transaction_id': 100021,\n" +
                        "    'transaction_type': 'transaction',\n" +
                        "    'account_from': 'NL88INHO0993873040',\n" +
                        "    'account_to': 'NL06INHO0463973767',\n" +
                        "    'amount': 5.0,\n" +
                        "    'user_performing': 100001\n" +
                        "}"));

    }

    @Test
    public void userPerformsTransactionWithoutValidTokenShouldReturnForbidden() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL04INHO0463973767");
        transaction.put("account_to", "NL04INHO0463973767");
        transaction.put("amount", 5);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());

    }

    @Test
    public void userPerformsTransactionWithoutRequiredInputShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL04INHO0463973767");
        transaction.put("account_to", null);
        transaction.put("amount", 5);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void userPerformsTransactionWithNotAcceptableAmountShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL88INHO0993873040");
        transaction.put("account_to", "NL05INHO0993873040");
        transaction.put("amount", 50000);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\n" +
                        "   'message': 'You do not have enough balance to transfer this amount!' \n"+
                        "}"));

    }

    @Test
    public void userPerformsTransactionWithNegativeAmountShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL88INHO0993873040");
        transaction.put("account_to", "NL05INHO0993873040");
        transaction.put("amount", -10);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\n" +
                        "   'message': 'You cannot transfer a negative number.' \n"+
                        "}"));

    }

    @Test
    public void userPerformsTransactionWithZeroAmountShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL88INHO0993873040");
        transaction.put("account_to", "NL05INHO0993873040");
        transaction.put("amount", 0);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\n" +
                        "   'message': 'You cannot transfer nothing.' \n"+
                        "}"));

    }

    @Test
    public void userPerformsTransactionWithBothAccountToAndFromShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL05INHO0993873040");
        transaction.put("account_to", "NL05INHO0993873040");
        transaction.put("amount", 5);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\n" +
                        "   'message': 'You cannot transfer to your own account!' \n"+
                        "}"));

    }

    @Test
    public void userPerformsTransactionToOtherSavingAccountShouldReturnUnprocessableEntity() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL67INHO0463973767");
        transaction.put("account_to", "NL88INHO0993873040");
        transaction.put("amount", 5);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().json("{\n" +
                        "   'message': 'You cannot transfer the funds to a savings account.' \n"+
                        "}"));

    }

    //External Bank account
    @Test
    public void userPerformsTransactionToExternalBankAccountShouldReturnCreated() throws Exception{
        JSONObject transaction = new JSONObject();
        transaction.put("account_from", "NL88INHO0993873040");
        transaction.put("account_to", "NL00INHO0993873040");
        transaction.put("amount", 10);

        mvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(transaction.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isCreated());

    }


    //getAllTransactions
    @Test
    public void employeeGetsAllTransactionsWithValidAuthTokenTheValidRoleShouldReturnIsOk() throws Exception{
        mvc.perform(get("/transactions")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeGetsAllTransactionsWithInValidAuthTokenAndValidRoleShouldReturnForbidded() throws Exception{
        mvc.perform(get("/transactions")
                .header("ApiKeyAuth", "invalid token"))
                .andExpect(status().isForbidden());
    }

    //getAllTransactions with offset
    @Test
    public void employeeGetsAllTransactionsWithOffsetShouldReturnOk() throws Exception{
        mvc.perform(get("/transactions?offset=1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeGetsAllTransactionsWithLimitShouldReturnOk() throws Exception{
        mvc.perform(get("/transactions?limit=4")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeGetsAllTransactionsWithInvalidLimitShouldReturnUnprocessableEntity() throws Exception{
        mvc.perform(get("/transactions?limit=-4")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity());
    }

    //getAllTransactions with offset and limit
    @Test
    public void employeeGetsAllTransactionsWithOffsetAndLimitShouldReturnOk() throws Exception{
        mvc.perform(get("/transactions?offset=1&limit=4")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void employeeGetsAllTransactionsWithValidIbanShouldReturnOk() throws Exception{
        mvc.perform(get("/transactions?iban=NL88INHO0993873040")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetsAllTransactionsWithValidAuthTokenButInvalidRoleShouldReturnForbidden() throws Exception{
        mvc.perform(get("/transactions")
                .header("ApiKeyAuth", "1000-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }


}
