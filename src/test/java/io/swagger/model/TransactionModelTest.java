package io.swagger.model;
import io.swagger.model.Transaction;
import org.json.JSONObject;
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
public class TransactionModelTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {

    }

    @Test
    public void createTransactionsShouldNotBeNull() {
        Transaction transaction = new Transaction();
        assertNotNull(transaction);
    }
}
