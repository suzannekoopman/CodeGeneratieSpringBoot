package io.swagger.repository;

import io.swagger.model.AuthToken;

import io.swagger.repository.AuthTokenRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest

public class AuthTokenRepositoryTest {

    @Autowired
    private AuthTokenRepository authTokenRepository;


    @Before
    public void setUp() {  }

    @Test
    public void findAuthTokenByExistingUserIdShouldReturnNotNull() throws Exception
    {
        AuthToken authToken = authTokenRepository.findAuthTokenByUser(100003);
        assertNotNull(authToken);
    }

    @Test
    public void findAuthTokenByNotExistingUserIdShouldReturnNull() throws Exception
    {
        AuthToken authToken = authTokenRepository.findAuthTokenByUser(100009);
        assertNull(authToken);
    }

}
