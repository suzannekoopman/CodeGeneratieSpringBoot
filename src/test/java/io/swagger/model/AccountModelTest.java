package io.swagger.model;

import io.swagger.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountModelTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {

    }

    @Test
    public void createAccountShouldNotBeNull() throws Exception
    {
        Account account = new Account("NL12INHO0123456789","current", 200, 3500, 0, 100053, 25.00);
        assertNotNull(account);
    }
}
