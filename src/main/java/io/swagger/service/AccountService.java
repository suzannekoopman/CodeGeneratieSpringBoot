
package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.BankAccount;
import io.swagger.repository.AccountRepository;

import io.swagger.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Random;


@Service
public class AccountService {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private BankAccount bankAccount = BankAccount.getBankAccount();

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account getSpecificAccount(String iban) {
        if(!accountRepository.existsById(iban))
            return null;
        return accountRepository.findById(iban).get();
    }

    public List<Account> getAllAccounts() {

        return (List<Account>)accountRepository.findAll();
    }

    public List<Account> getAllAccountsFromUser(Integer userId) {
        //if owner bankown account, return account version of bank own account
        if(userId == bankAccount.getBankAccountOwnerId())
        {
            List<Account> accountList = accountRepository.findAccountByOwner(userId);
            accountList.add( new Account(bankAccount.getBankAccountIban(), "current", 0, 0, 0,0,bankAccount.getBankAccountBalance()));
            return accountList;
        }

        return (List<Account>)accountRepository.findAccountByOwner(userId);
    }

    public Account createAccount(Account newAccount, Integer userId){
        // User does not exist
        if(!userRepository.existsById(userId))
            return null;

        List<Account> ownersAccounts = getAllAccountsFromUser(userId);

        // User already has two accounts, a user is allowed to have only one current account and one savings account.
        if(ownersAccounts.size() == 1) {
            Account ownersAccount = ownersAccounts.get(0);
            // User already has a current account
            if(ownersAccount.getAccountType() == Account.AccountTypeEnum.CURRENT && newAccount.getAccountType() == Account.AccountTypeEnum.CURRENT)
                return null;
            // User already has a savings account
            else if(ownersAccount.getAccountType() == Account.AccountTypeEnum.SAVINGS && newAccount.getAccountType() == Account.AccountTypeEnum.SAVINGS)
                return null;
        }
        if(!(newAccount.getAccountType() == Account.AccountTypeEnum.CURRENT || newAccount.getAccountType() == Account.AccountTypeEnum.SAVINGS))
            return null;

        // User does not already have an account with the same accounttype, he is allowed to make another account.
        newAccount.setIban(createIban());
        newAccount.setOwner(userId);

        bankAccount.addAmountToBankBalance(newAccount.getBalance());
        accountRepository.save(newAccount);
        return newAccount;

    }

    public Integer deleteAccount(String iban)
    {
        if(!accountRepository.existsById(iban))
            return 406;

        Account aboutToBeDeletedAccount = accountRepository.findById(iban).get();

        if(aboutToBeDeletedAccount.getBalance() < 0.00)
            return 403;
        accountRepository.deleteById(iban);
        return 204;
    }

    public Account updateAccount(Account account, String iban)
    {
        if(!accountRepository.existsById(iban))
            return null;

        Account oldAccount = accountRepository.findById(iban).get();

        oldAccount.setBalanceLimit(account.getBalanceLimit());
        oldAccount.setTransactionAmountLimit(account.getTransactionAmountLimit());
        oldAccount.setTransactionDayLimit(account.getTransactionDayLimit());

        return accountRepository.save(oldAccount);
    }

    public String createIban()
    {
        // This iban is used to add an iban for a new account.
        String iban = "";
        Random r = new Random();

        iban += "NL";
        for(int i = 1; i <=2; i++)
        {
            iban += Integer.toString(r.nextInt(10));
        }

        iban += "INHO0";
        for(int i = 1; i <=9; i++)
        {
            iban += Integer.toString(r.nextInt(10));
        }
        if(accountRepository.existsById(iban))
        {
            return createIban();
        }
        return iban;
    }
}




