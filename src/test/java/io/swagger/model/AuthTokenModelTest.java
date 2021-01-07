package io.swagger.model;

import java.time.LocalDateTime;

import io.swagger.model.AuthToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthTokenModelTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private AuthToken authTokenWithParameters;

    @Before
    public void setUp() {
    	this.authTokenWithParameters = new AuthToken("1234-abcd-5678-efgh", 100053, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));

    }

    @Test
    public void createAuthTokenShouldNotBeNull() throws Exception
    {
        AuthToken authToken = new AuthToken();
        assertNotNull(authToken);
    }
    
    @Test
    public void createAuthTokenWithParametersShouldNotBeNull() throws Exception
    {
        assertNotNull(authTokenWithParameters);
        assertNotNull(authTokenWithParameters.authToken("1234-abcd-5678-efgh"));
    }
    
    @Test
    public void createAuthTokenWithSetterMethodShouldSouldNotBeNull() throws Exception
    {
    	AuthToken authToken = new AuthToken("1234-abcd-5678-efgh", 100053, LocalDateTime.now(), LocalDateTime.now().plusMinutes(30));
    	authToken.setAuthToken("1234-abcd-5678-efgh");
    	authToken.setTokenExpires(LocalDateTime.now().plusMinutes(30));
        assertNotNull(authTokenWithParameters.getAuthToken());
        assertNotNull(authTokenWithParameters.toString());
    	assertNotNull(authTokenWithParameters.getTokenCreated());
    	assertNotNull(authTokenWithParameters.getTokenExpires());
    	assertNotNull(authTokenWithParameters.getUserId());
    	assertEquals(authToken, authTokenWithParameters);
       
    }
}
