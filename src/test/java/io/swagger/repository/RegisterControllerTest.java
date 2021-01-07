package io.swagger.repository;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
    this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void registerRequestShouldWithValidInputShouldReturnCreated() throws Exception {

        JSONObject user = new JSONObject();
        user.put("firstName", "Pascalle");
        user.put("lastName", "Schipper");
        user.put("password", "Password123!");
        user.put("email", "pascalle@test.com");

        // performing POST with JSON
        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\n" +
                        " 'registerId' : 100004,\n" +
                        "  'firstName': 'Pascalle',\n" +
                        "  'lastName': 'Schipper',\n" +
                        "  'password': '2c103f2c4ed1e59cb4e2e1821',\n" +
                        "  'email': 'pascalle@test.com'\n" +
                        "}"));

    }

    @Test
    public void registerRequestWithNotSecurityProofPasswordShouldReturnIsNotAcceptable() throws Exception {

        JSONObject user = new JSONObject();
        user.put("firstName", "Pascalle");
        user.put("lastName", "Schipper");
        user.put("password", "notSecurityProofPassword");
        user.put("email", "pascalle@test.com");

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void registerRequestWithoutRequiredValuesShouldReturnBadRequest() throws Exception {

        JSONObject user = new JSONObject();
        user.put("firstName", "Pascalle");
        user.put("lastName", "Schipper");
        user.put("password", null);
        user.put("email", "pascalle@test.com");


        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(status().isBadRequest());
        }

    @Test
    public void registerRequestWithAlreadyUsedEmailShouldReturnIsNotAcceptable() throws Exception {

        JSONObject user = new JSONObject();
        user.put("firstName", "Pascalle");
        user.put("lastName", "Schipper");
        user.put("password", "Password123!");
        user.put("email", "BankCodeGeneratie");

        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void registerRequestWithInvalidEmailShouldReturnIsNotAcceptable() throws Exception {

        JSONObject user = new JSONObject();
        user.put("firstName", "Pascalle");
        user.put("lastName", "Schipper");
        user.put("password", "Password123!");
        user.put("email", "email");


        mvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(user.toString()))
                .andExpect(status().isNotAcceptable());
    }


}
