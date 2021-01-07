package io.swagger.controller;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    //Get All Users
    @Test
    public void getAllUsersWithValidTokenAndValidRoleShouldReturnOk() throws Exception {
         mvc.perform(get("/users")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersWithInvalidTokenShouldReturnForbidden() throws Exception {
        mvc.perform(get("/users")
                .header("ApiKeyAuth", "InvalidToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsersWithValidTokenAndInValidRoleShouldReturnForbidden() throws Exception {
        mvc.perform(get("/users")
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsersFilteredByEmailWithNonexistingEmailShouldReturnNotAcceptable() throws Exception {
        mvc.perform(get("/users?email=noEmail")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void getAllUsersFilteredByEmailShouldReturnOk() throws Exception {
        mvc.perform(get("/users?email=customer")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersFilteredByNameShouldReturnOk() throws Exception {
        mvc.perform(get("/users?name=customer")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    //Get User By ID
    @Test
    public void getUserByIdWithValidTokenAndValidRoleShouldReturnOk() throws Exception {
        mvc.perform(get("/users/100003")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByIdWithValidTokenAndInvalidRoleShouldReturnForbidden() throws Exception {
        mvc.perform(get("/users/100052")
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByIdWithInvalidTokenShouldReturnForbidden() throws Exception {
        mvc.perform(get("/users/100052")
                .header("ApiKeyAuth", "InvalidToken"))
                .andExpect(status().isForbidden());
    }

    //Create New User
    @Test
    public void createUserWithEmployeeTokenAndValidInputShouldReturnCreated() throws Exception {

        JSONObject createUser = new JSONObject();
        createUser.put("firstName", "John");
        createUser.put("lastName", "Doe");
        createUser.put("password", "Password1!");
        createUser.put("email", "johndoe@gmail.com");
        createUser.put("user_type", "customer");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\n" +
                        "    'user_id': 100007,\n" +
                        "    'firstName': 'John',\n" +
                        "    'lastName': 'Doe',\n" +
                        "    'password': 'cef1fb1f60529028a71f58e54',\n" +
                        "    'email': 'johndoe@gmail.com',\n" +
                        "    'user_type': 'customer'\n" +
                        "}"));
    }

    @Test
    public void createUserWithoutTokenAndValidInputShouldCreateRequestAndReturnCreated() throws Exception {

        JSONObject createUser = new JSONObject();
        createUser.put("firstName", "John");
        createUser.put("lastName", "Doe");
        createUser.put("password", "Password1!");
        createUser.put("email", "johndoe@gmail.com");
        createUser.put("user_type", "customer");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", ""))
                .andExpect(status().isCreated());
    }

    @Test
    public void createUserWithoutTokenAndInvalidInputShouldTryToCreateRequestAndReturnNotAcceptable() throws Exception {

        JSONObject createUser = new JSONObject();
        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", ""))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createUserWithoutRequiredInputShouldReturnNotAcceptable() throws Exception {

        JSONObject createUser = new JSONObject();

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createUserWithInvalidPassWordFormatShouldReturnNotAcceptable() throws Exception {

        JSONObject createUser = new JSONObject();
        createUser.put("firstName", "John");
        createUser.put("lastName", "Doe");
        createUser.put("password", "password");
        createUser.put("email", "johndoe@gmail.com");
        createUser.put("user_type", "customer");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createUserWithInvalidEmailFormatShouldReturnNotAcceptable() throws Exception {

        JSONObject createUser = new JSONObject();
        createUser.put("firstName", "John");
        createUser.put("lastName", "Doe");
        createUser.put("password", "Password1!");
        createUser.put("email", "johndoegmailcom");
        createUser.put("user_type", "customer");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void createUserWithValidInputButUserAlreadyExistsShouldReturnNotAcceptable() throws Exception {

        JSONObject createUser = new JSONObject();
        createUser.put("firstName", "John");
        createUser.put("lastName", "Doe");
        createUser.put("password", "Password1!");
        createUser.put("email", "employee");
        createUser.put("user_type", "customer");

        mvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    //Delete User
    @Test
    public void deleteUserByIdWithValidTokenAndValidRoleShouldReturnNoContent() throws Exception {
        mvc.perform(delete("/users/100002")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserByIdWithValidTokenAndInvalidRoleShouldReturnForbidden() throws Exception {
        mvc.perform(delete("/users/100053")
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserByIdWithInvalidTokenShouldReturnForbidden() throws Exception {
        mvc.perform(delete("/users/100053")
                .header("ApiKeyAuth", "InvalidToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserByIdWithNonExistingIdShouldReturnNotAcceptable() throws Exception {
        mvc.perform(delete("/users/0000")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    //Update User
    @Test
    public void updateUserByIdWithValidTokenAndValidRoleShouldReturnOk() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "Password1!");
        updateUser.put("email", "johndoe@gmail.com");

        mvc.perform(put("/users/100001")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserByIdWithValidTokenAndInvalidRoleShouldReturnForbidden() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "Password1!");
        updateUser.put("email", "johndoe@gmail.com");

        mvc.perform(put("/users/100052")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserByIdWithInvalidTokenShouldReturnForbidden() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "Password1!");
        updateUser.put("email", "johndoe@gmail.com");

        mvc.perform(put("/users/100052")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "InvalidToken"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserByIdWithNonExistingIdShouldReturnNotAcceptable() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "Password1!");
        updateUser.put("email", "johndoe@gmail.com");

        mvc.perform(put("/users/0000")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateUserByIdWithoutRequiredInputShouldReturnNotAcceptable() throws Exception {

        JSONObject updateUser = new JSONObject();

        mvc.perform(put("/users/100052")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateUserByIdWithInvalidPasswordFormatShouldReturnNotAcceptable() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "password");
        updateUser.put("email", "johndoe@gmail.com");

        mvc.perform(put("/users/100052")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void updateUserByIdWithInvalidEmailFormatShouldReturnNotAcceptable() throws Exception {

        JSONObject updateUser = new JSONObject();
        updateUser.put("firstName", "John");
        updateUser.put("lastName", "Doe");
        updateUser.put("password", "Password1!");
        updateUser.put("email", "johndoegmailcom");

        mvc.perform(put("/users/100052")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updateUser.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    //machine transfer
    @Test
    public void userPerformMachineTransferWithValidInputShouldReturnIsCreated() throws Exception{

        JSONObject machineTransfer = new JSONObject();
        machineTransfer.put("amount", 400);
        machineTransfer.put("transfer_type", "deposit");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        mvc.perform(post("/users/100001/machine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(machineTransfer.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\n" +
                        "    'transaction_id': 100019,\n" +
                        "    'transaction_type': 'deposit',\n" +
                        "    'timestamp': '"+  LocalDateTime.now().format(formatter)+ "',\n" +
                        "    'account_from': 'NL01INHO0000000001',\n" +
                        "    'account_to': 'NL01INHO0000000001',\n" +
                        "    'amount': 400.0,\n" +
                        "    'user_performing': 100001\n" +
                        "}"));

    }
    @Test
    public void userPerformMachineTransferWithoutRequiredInputShouldReturnBadRequest() throws Exception{
        JSONObject machineTransfer = new JSONObject();
        machineTransfer.put("amount", 400);
        machineTransfer.put("transfer_type", null);

        mvc.perform(post("/users/100001/machine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(machineTransfer.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void userPerformMachineTransferWithNotAcceptableAmountShouldReturnIsNotAcceptable() throws Exception{

        //machine withdraw that is more than the  account balance
        JSONObject machineTransfer = new JSONObject();
        machineTransfer.put("amount", 400000);
        machineTransfer.put("transfer_type", "withdraw");

        mvc.perform(post("/users/100001/machine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(machineTransfer.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }
    @Test
    public void userPerformMachineTransferWithoutHavingACurrentAccountShouldReturnIsNotAcceptable() throws Exception{

        JSONObject machineTransfer = new JSONObject();
        machineTransfer.put("amount", 10);
        machineTransfer.put("transfer_type", "withdraw");

        //user 100004 has no current account
        mvc.perform(post("/users/100004/machine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(machineTransfer.toString())
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }
    @Test
    public void userPerformMachineTransferWithoutValidTokenShouldReturnForbidden() throws Exception{

        JSONObject machineTransfer = new JSONObject();
        machineTransfer.put("amount", 400);
        machineTransfer.put("transfer_type", "deposit");

        mvc.perform(post("/users/100001/machine")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(machineTransfer.toString())
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }



    //user register requests
    @Test
    public void usersGetRegisterRequestsWithValidAuthTokenAndTheValidRoleShouldReturnIsOk() throws Exception{
        mvc.perform(get("/users/requests")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }
    @Test
    public void usersGetRegisterRequestsWithInvalidAuthTokenShouldReturnIsForbidden() throws Exception{
        mvc.perform(get("/users/requests")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void usersGetRegisterRequestsWithValidAuthTokenButInvalidRoleShouldReturnIsForbidden() throws Exception{
        mvc.perform(get("/users/requests")
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }


    @Test
    public void userDeleteRegisterRequestWithValidUserRoleAndAuthTokenShouldReturnNoContent() throws Exception {

        mvc.perform(delete("/users/requests/100001")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void userDeleteRegisterRequestWithInvalidUserRoleAndValidAuthTokenShouldIsForbidden() throws Exception {

        mvc.perform(delete("/users/requests/100001")
                .header("ApiKeyAuth", "1111-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userDeleteRegisterRequestWithValidUserRoleAndInvalidAuthTokenShouldIsForbidden() throws Exception {

        mvc.perform(delete("/users/requests/100001")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userDeleteNotExistingRegisterRequestShouldIsNotAcceptable() throws Exception {

        mvc.perform(delete("/users/requests/1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNotAcceptable());
    }

    //accounts

    // This test does not work, because the expected iban is not correct (because of the random iban generator)
//    @Test
//    public void CreateAccountWithValidInputShouldReturnJsonObjectAndStatusCreated() throws Exception{
//        JSONObject createAccount = new JSONObject();
//        createAccount.put("account_type", "savings");
//        createAccount.put("balance", 50);
//        createAccount.put("transactionDayLimit", 100000);
//        createAccount.put("transactionAmountLimit", 2000);
//        createAccount.put("balanceLimit", -500);
//
//        mvc.perform(post("/users/100001/accounts")
//                .header("ApiKeyAuth", "1234-abcd-5678-efgh")
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(createAccount.toString()))
//                .andExpect(status().isCreated())
//                .andExpect(content().json("{\n" +
//                        "    'iban': 'NL99INHO9999999999',\n" +
//                        "    'account_type': 'savings',\n" +
//                        "    'balance': 50.0, \n" +
//                        "    'transactionDayLimit': 100000,\n" +
//                        "    'transactionAmountLimit': 2000.0,\n" +
//                        "    'balanceLimit': -500.0,\n" +
//                        "    'owner': 100001\n" +
//                        "}"));
//    }

    @Test
    public void CreateSavingsAccountWithAlreadyASavingsAccountShouldReturnBadRequest() throws Exception{
        JSONObject createAccount = new JSONObject();
        createAccount.put("account_type", "savings");
        createAccount.put("balance", 0);
        createAccount.put("transactionDayLimit", 100000);
        createAccount.put("transactionAmountLimit", 2000);
        createAccount.put("balanceLimit", -500);

        mvc.perform(post("/users/100003/accounts")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createAccount.toString()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void CreateAccountWithoutValidInputShouldReturnNotAcceptable() throws Exception{
        JSONObject createAccount = new JSONObject();
        createAccount.put("account_type", "curr");
        createAccount.put("balance", 0);
        createAccount.put("transactionDayLimit", 100000);
        createAccount.put("transactionAmountLimit", 2000);
        createAccount.put("balanceLimit", -500);

        mvc.perform(post("/users/100003/accounts")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(createAccount.toString()))
                .andExpect(status().isNotAcceptable());
    }





    @Test
    public void GetAccountByUserIdShouldReturnIsOk() throws Exception {
        mvc.perform(get("/users/100003/accounts")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void GetAccountByUserIdWithoutValidTokenShouldReturnIsForbidden() throws Exception {
        mvc.perform(get("/users/100003/accounts")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void GetAccountByUserIdWithoutAuthenticationShouldReturnIsForbidden() throws Exception {
        mvc.perform(get("/users/100003/accounts")
                .header("ApiKeyAuth", "1235-abcd-5678-efgh"))
                .andExpect(status().isForbidden());
    }


    //getAllTransactionsFromUser  - /users/{userId}/transactions
    @Test
    public void userGetsAllTransactionsFromUserIdWithValidAuthTokenShouldReturnOk() throws Exception{
        mvc.perform(get("/users/100004/transactions")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithValidAuthTokenButInvalidUserIdShouldReturnUnprocessableEntity() throws Exception{
        mvc.perform(get("/users/20001/transactions")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithValidAuthTokenButInvalidAuthenticationShouldReturnForbidden() throws Exception{
        mvc.perform(get("/users/100002/transactions")
                .header("ApiKeyAuth", ""))
                .andExpect(status().isForbidden());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithLimitShouldReturnOk() throws Exception{
        mvc.perform(get("/users/100004/transactions?limit=1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithOffsetShouldReturnOk() throws Exception{
        mvc.perform(get("/users/100004/transactions?offset=1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithInvalidLimitShouldReturnUnprocessableEntity() throws Exception{
        mvc.perform(get("/users/100002/transactions?limit=-1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithInvalidRoleShouldReturnUnprocessableEntity() throws Exception{
        mvc.perform(get("/users/999999/transactions?limit=1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void userGetsAllTransactionsFromUserWithOffsetAndLimitShouldReturnOk() throws Exception{
        mvc.perform(get("/users/100004/transactions?limit=1&offset=1")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isOk());
    }

}
