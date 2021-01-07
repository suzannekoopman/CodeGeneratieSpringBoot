package io.swagger.model;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserTest {

    private User userWithAllParams;
    private User userWithoutParams;

    @Before
    public void setUp() {
        userWithAllParams = new User("John", "Doe", "Password1!", "johndoe@gmail.com", "customer");
        userWithoutParams = new User();
    }

    @Test
    public void creatingNewUserWithAllParamsShouldNotBeNull() throws Exception {
        assertNotNull(userWithAllParams);
    }

    @Test
    public void creatingNewUserWithoutParamsShouldNotBeNull() throws Exception {
        assertNotNull(userWithoutParams);
    }

}