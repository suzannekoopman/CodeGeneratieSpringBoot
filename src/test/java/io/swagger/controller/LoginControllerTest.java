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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
    this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void loginWithValidCredentialsShouldReturnTokenAndStatusCreated() throws Exception{
        JSONObject userLogin = new JSONObject();
        userLogin.put("username", "customer");
        userLogin.put("password", "password");

        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userLogin.toString()))
                        .andExpect(status().isCreated())
                        .andExpect(content().json("{'authToken': '1111-abcd-5678-efgh','userId': 100003}"));

    }

    @Test
    public void loginWithoutValidCredentialsShouldReturnUnauthorized() throws Exception{
        JSONObject userLogin = new JSONObject();
        userLogin.put("username", "BankCodeGeneratie");
        userLogin.put("password", "notvalidpassword");

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userLogin.toString()))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void loginWithMissingRequiredInputShouldReturnBadRequest() throws Exception{
        JSONObject userLogin = new JSONObject();
        userLogin.put("username", "BankCodeGeneratie");
        userLogin.put("password", null);

        mvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userLogin.toString()))
                .andExpect(status().isBadRequest());

    }
}
