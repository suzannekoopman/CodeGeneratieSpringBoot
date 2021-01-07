package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.repository.TransactionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.List;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {

    }

    @Test
    public void findTransactionByAccountToShouldReturnNotNull() throws Exception {
        List<Transaction> transactions = transactionRepository.findByIban("NL80INHO0993873041");
        assertNotNull(transactions);
    }
}
