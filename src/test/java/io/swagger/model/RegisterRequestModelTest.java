package io.swagger.model;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterRequestModelTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private RegisterRequest registerRequestWithParams;

    @Before
    public void setUp(){
    	registerRequestWithParams = new RegisterRequest("firstName", "lastName", "password", "email@domain.com");
    }

    @Test
    public void createRegisterRequestShouldNotBeNull() throws Exception
    {
        RegisterRequest registerRequest = new RegisterRequest();
        assertNotNull(registerRequest);
    }
    
    @Test
    public void createRegisterRequestWithParametersShouldNotBeNull() throws Exception
    {
        assertNotNull(registerRequestWithParams);
        assertNotNull(registerRequestWithParams.password("password"));
       
        assertNotNull(registerRequestWithParams.lastName("lastName"));
    }
    
    @Test
    public void registerUserShouldSetFirstNameAndSouldNotBeNull()throws Exception{
    	 assertNotNull(registerRequestWithParams.firstName("firstName"));
    }

    @Test
    public void registerUserShouldSetLastNameAndSouldNotBeNull()throws Exception{
    	 assertNotNull(registerRequestWithParams.firstName("lastName"));
    }
    
    @Test
    public void registerUserShouldSetPasswordAndSouldNotBeNull()throws Exception{
    	 assertNotNull(registerRequestWithParams.firstName("password"));
    }
    
    @Test
    public void createRegisterRequestWithSettersShouldNotBeNull() throws Exception
    {
    	RegisterRequest registerRequest = new RegisterRequest();
    	registerRequest.setFirstName("firstName");
    	registerRequest.setLastName("lastName");
    	registerRequest.setPassword("password");
    	registerRequest.setEmail("email@domain.com");
        assertNotNull(registerRequest);
        assertNotNull(registerRequest.toString());
        assertNotNull(registerRequest.getEmail());
        assertNotNull(registerRequest.getFirstName());
        assertNotNull(registerRequest.getLastName());
        assertEquals(registerRequest,registerRequestWithParams);
    }
    
    
    
}
