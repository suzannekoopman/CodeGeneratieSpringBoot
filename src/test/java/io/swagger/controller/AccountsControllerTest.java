package io.swagger.controller;



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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllAccountsShouldReturnIsOk() throws Exception {
        mvc.perform(get("/accounts")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllAccountsWithoutAuthenticationShouldReturnIsForbidden() throws Exception {
        mvc.perform(get("/accounts")
                .header("ApiKeyAuth", "1235-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllAccountsWithoutValidTokenShouldReturnIsForbidden() throws Exception {
        mvc.perform(get("/accounts")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }


    @Test
    public void getAccountByIbanShouldReturnIsOk() throws Exception {
        mvc.perform(get("/accounts/NL05INHO0993873040")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    'iban': 'NL05INHO0993873040',\n" +
                        "    'account_type': 'savings',\n" +
                        "    'balance': 25.0, \n" +
                        "    'transactionDayLimit': 200.0,\n" +
                        "    'transactionAmountLimit': 3500.0,\n" +
                        "    'balanceLimit': 3.5E7,\n" +
                        "    'owner': 100002\n" +
                        "}"));
    }

    @Test
    public void getAccountByIbanWithNotExistingIbanShouldReturnDeleted() throws Exception{
        mvc.perform(get("/accounts/NL11INHO0111111111")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }



    @Test
    public void getAllAccountByIbanWithoutAuthenticationShouldReturnIsForbidden() throws Exception {
        mvc.perform(get("/accounts/NL05INHO0993873040")
                .header("ApiKeyAuth", "1235-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }



    @Test
    public void updateAccountWithValidInputShouldReturnJsonObjectAndStatusIsOk() throws Exception{
        JSONObject updateAccount = new JSONObject();

        updateAccount.put("transactionDayLimit", 150000);
        updateAccount.put("transactionAmountLimit", 6000);
        updateAccount.put("balanceLimit", 50);

        mvc.perform(put("/accounts/NL02INHO0463973769")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateAccount.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    'iban': 'NL02INHO0463973769',\n" +
                        "    'account_type': 'savings',\n" +
                        "    'balance': 25.0, \n" +
                        "    'transactionDayLimit': 150000,\n" +
                        "    'transactionAmountLimit': 6000.0,\n" +
                        "    'balanceLimit': 50.0,\n" +
                        "    'owner': 100006\n" +
                        "}"));
    }

    @Test
    public void updateAccountWithInvalidInputShouldReturnStatusIsBadRequest() throws Exception{
        JSONObject updateAccount = new JSONObject();
        updateAccount.put("transactionDayLimit", 150000);
        updateAccount.put("transactionAmountLimit", 6000);
        updateAccount.put("balanceLimit", "no valid balancelimit");

        mvc.perform(put("/accounts/NL12INHO0123456789")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateAccount.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAccountWithoutAuthenticationShouldReturnStatusIsForbidden() throws Exception{
        JSONObject updateAccount = new JSONObject();

        updateAccount.put("transactionDayLimit", 1500);
        updateAccount.put("transactionAmountLimit", 60);
        updateAccount.put("balanceLimit", 0);

        mvc.perform(put("/accounts/NL01INHO0463973769")
                .header("ApiKeyAuth", "no valid token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateAccount.toString()))
                .andExpect(status().isForbidden());
    }





    @Test
    public void deleteAccountShouldReturnIsNoContent() throws Exception{
        mvc.perform(delete("/accounts/NL01INHO0463973769")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAccountWithNotExistingIbanShouldReturnIsNotAcceptable() throws Exception{
        mvc.perform(delete("/accounts/NL99INHO0993873049")
                .header("ApiKeyAuth", "2222-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void deleteAccountWithoutValidTokenShouldReturnIsForbidden() throws Exception{
        mvc.perform(delete("/accounts/NL12INHO0123456789")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteAccountWithoutAuthenticationShouldReturnIsForbidden() throws Exception {
        mvc.perform(delete("/accounts/NL12INHO0123456789")
                .header("ApiKeyAuth", "1235-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }
    
    
    @Test
    public void getAllAccountWithJSONHeaderShouldRetunIsNotImplemented() throws Exception {
        mvc.perform(get("/accounts")
        		.header("ApiKeyAuth", "2222-abcd-5678-efgh")
                .header("Accept",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotImplemented());
    }
    
    @Test
    public void getSpecificAccountWithJSONHeaderShouldRetunIsNotImplemented() throws Exception {
        mvc.perform(get("/accounts/NL12INHO0123456789")
        		.header("ApiKeyAuth", "2222-abcd-5678-efgh")
                .header("Accept",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotImplemented());
    }
}

