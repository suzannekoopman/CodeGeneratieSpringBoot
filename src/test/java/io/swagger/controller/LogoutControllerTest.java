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

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest

public class LogoutControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void logoutWithValidAuthTokenShouldReturnIsNoContent() throws Exception {

        mvc.perform(delete("/logout")
                .header("ApiKeyAuth", "1234-abcd-5678-efgh"))
                .andExpect(status().isNoContent());
    }
    @Test
    public void logoutWithInvalidAuthTokenShouldReturnIsUnauthorized() throws Exception {

        mvc.perform(delete("/logout")
                .header("ApiKeyAuth", "no valid token"))
                .andExpect(status().isForbidden());
    }
}
