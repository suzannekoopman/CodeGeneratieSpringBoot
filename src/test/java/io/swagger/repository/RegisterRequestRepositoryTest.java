package io.swagger.repository;

import io.swagger.model.RegisterRequest;
import io.swagger.repository.RegisterRequestRepository;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterRequestRepositoryTest {

    @Autowired
    private RegisterRequestRepository registerRequestRepository;


    @Before
    public void setUp() {  }

    @Test
    public void findRegisterRequestByExistingEmailShouldReturnNotNull() throws Exception
    {
        RegisterRequest registerRequest = registerRequestRepository.findUserByEmail("su@test.com");
        assertTrue(registerRequest != null);

    }

    @Test
    public void findRegisterRequestByNotExistingEmailShouldReturnNull() throws Exception
    {
        RegisterRequest registerRequest = registerRequestRepository.findUserByEmail("notExistingEmail");
        assertNull(registerRequest);
    }



}
