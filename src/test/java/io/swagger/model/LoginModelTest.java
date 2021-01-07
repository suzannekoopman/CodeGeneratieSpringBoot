package io.swagger.model;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginModelTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    Login loginWithDetails ;

    @Before
    public void setUp(){
    	 loginWithDetails = new Login();
    	 loginWithDetails.setUsername("customer");
    	 loginWithDetails.setPassword("password");
    }

    @Test
    public void createLoginShouldNotBeNull() throws Exception
    {
        Login login = new Login();
        assertNotNull(login);
    }
    
    @Test
    public void createLoginWithUserNamePasswordShouldNotBeNull() throws Exception
    {
        Login login = new Login();
        login.username("customer");
        login.password("password");
        assertNotNull(login);
        assertNotNull(login.toString());
    }
    
    @Test
    public void createLoginWithUserNameAndPasswordShouldGetUserNameAndPasswordAsNotNull() throws Exception
    {
        Login login = new Login();
        login.setUsername("customer");
        login.setPassword("password");
        assertNotNull(login.getUsername());
        assertNotNull(login.getPassword());
        assertEquals(login, loginWithDetails);
        
    }
    
    
    
}
