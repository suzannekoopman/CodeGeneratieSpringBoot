package io.swagger.service;

import io.swagger.service.AccountService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {

    }

    @Test
    public void CreateIbanShouldReturnNotNull() throws Exception
    {
        String iban = accountService.createIban();
        assertNotNull(iban);
    }
}
