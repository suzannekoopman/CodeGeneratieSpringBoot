package io.swagger.repository;



import io.swagger.model.Account;
import io.swagger.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;


    @Before
    public void setUp() {  }

    @Test
    public void findAccountByOwnerShouldReturnNotNull() throws Exception
    {
        List<Account> accounts = accountRepository.findAccountByOwner(100053);
        assertNotNull(accounts);
    }

    @Test
    public void findAccountByNotExistingOwnerShouldReturnNull() throws Exception
    {
        List<Account> accounts = accountRepository.findAccountByOwner(0);
        if(accounts.isEmpty())
            accounts = null;
        assertNull(accounts);
    }
}