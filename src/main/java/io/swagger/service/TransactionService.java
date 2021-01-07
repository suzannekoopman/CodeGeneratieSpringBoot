package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.BankAccount;
import io.swagger.model.MachineTransfer;
import io.swagger.model.Transaction;
import io.swagger.model.User;
import io.swagger.model.AuthToken;

import io.swagger.repository.AccountRepository;
import io.swagger.repository.AuthTokenRepository;
import org.springframework.stereotype.Service;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AuthTokenRepository authTokenRepository;
    private BankAccount bankAccount = BankAccount.getBankAccount();
    private DateTimeFormatter formatter;


    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, AccountRepository accountRepository, AuthTokenRepository authTokenRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository =  userRepository;
        this.accountRepository = accountRepository;
        this.authTokenRepository = authTokenRepository;
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }

    public Transaction createMachineTransfer(int userId, MachineTransfer machineTransfer)
    {
        List<Account> userAccounts = accountRepository.findAccountByOwner(userId);
        if(userAccounts == null)
            return null;

        String currentUserAccount = null;
        for (Account userAccount: userAccounts) {
            if(userAccount.getAccountType() == Account.AccountTypeEnum.CURRENT)
            {
                currentUserAccount = userAccount.getIban();
                switch (machineTransfer.getTransferType())
                {
                    //add amount to account and bank own account;
                    case DEPOSIT:
                        userAccount.setBalance(userAccount.getBalance() + machineTransfer.getAmount());
                        bankAccount.addAmountToBankBalance(machineTransfer.getAmount());
                        break;
                    //remove amount from user account and bank account
                    case WITHDRAW:
                        if(userAccount.getBalance() < machineTransfer.getAmount())
                            return null;
                        userAccount.setBalance(userAccount.getBalance() - machineTransfer.getAmount());
                        bankAccount.removeAmountFromBankBalance(machineTransfer.getAmount());
                        break;
                }
            }
        }
        //only savings
        if(currentUserAccount == null)
            return null;
        return transactionRepository.save(new Transaction(machineTransfer.getTransferType().toString(), LocalDateTime.now(), currentUserAccount, currentUserAccount, machineTransfer.getAmount(), userId));
    }




    public List<Transaction> getAllTransactions(Integer limit, Integer offset) throws Exception {

        List<Transaction> allTransaction = (List<Transaction>) transactionRepository.findAll();

        if(allTransaction.size() == 0){   	return allTransaction;       } //No transactions found

        //apply pagination with respect to offset and limit
        allTransaction = createPageable(offset, limit, allTransaction);

        return allTransaction;

    }

    public List<Transaction> getAllTransactionOfAccount(String iban, String token, Integer offset, Integer limit) throws Exception {
        //User id must be provided
        if(iban == null){
            throw new Exception("The iban you entered is wrong");
        }
        //Find performing user
        //User user = getUserById(userId);

        //Find logged-in user
        User loggedInUser = getLoggedInUser(token);

        List<Transaction>  allTransaction = transactionRepository.findByIban(iban);
        Set<Transaction> allTransactionSet = new HashSet<Transaction>();
        if(loggedInUser.getUserType()== User.UserTypeEnum.CUSTOMER){
            List<Account> accounts = accountRepository.findAccountByOwner(loggedInUser.getUserId());
            for(Account account: accounts){
                for(Transaction tans:allTransaction){
                    if(tans.getAccountFrom().equals(account.getIban())){
                        allTransactionSet.add(tans);
                    }
                }
            }
            allTransaction = new ArrayList<Transaction>(allTransactionSet);
        }

        if(allTransaction.size() == 0){   	return allTransaction;       }

        //apply pagination with respect to offset and limit
        allTransaction = createPageable(offset, limit, allTransaction);

        return allTransaction;
    }

    public List<Transaction> getAllTransactionsOfUser(Integer userId, String token, Integer offset, Integer limit) throws Exception {
        //User id must be provided
        if(userId == null){
            throw new Exception("The user id you entered is wrong");
        }
        //Find performing user
        User user = getUserById(userId);

        //Find logged-in user
        User loggedInUser = getLoggedInUser(token);

        validateUserDetails(loggedInUser, user);

        List<Account> accounts = accountRepository.findAccountByOwner(user.getUserId());

        //Retrieve all transaction details of the user
        Set<Transaction> allTransactionSet = new HashSet<Transaction>();
        for(Account account : accounts){
            allTransactionSet.addAll(transactionRepository.findByIban(account.getIban()));
        }

        List<Transaction> allTransaction = new ArrayList<Transaction>(allTransactionSet);

        if(allTransaction.size() == 0){   	return allTransaction;       }

        //apply pagination with respect to offset and limit
        allTransaction = createPageable(offset, limit, allTransaction);

        return allTransaction;
    }


    /**
     * Apply Pagination
     * @param offset
     * @param limit
     * @param allTransaction
     * @return
     * @throws Exception
     */
    private List<Transaction> createPageable(Integer offset, Integer limit, List<Transaction> allTransaction) throws Exception{

        if(limit == null && offset == null){
            // No pagination
            return allTransaction;
        }

        int size = allTransaction.size();
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = size;
        }
        if (limit <= 0) {
            throw new Exception("limit can't be zero or negative");
        }

        if (offset < 0) {
            throw new Exception("offset can't be  negative");
        }

        limit = offset + limit ;

        if(limit > size) { limit  = size;}
        if(offset > size){ offset = size;}

        allTransaction= allTransaction.subList(offset, limit);

        return allTransaction;
    }

    private void validateUserDetails(User loggedInUser, User user)throws Exception {

        // no user found for give userId
        if (user == null) {
            throw new Exception("The user id you entered is wrong");
        }

        // customer is only able to check it's own transaction
        if (loggedInUser.getUserType() == User.UserTypeEnum.CUSTOMER) {
            if (loggedInUser.getUserId().intValue() != user.getUserId().intValue()) {
                throw new Exception("You cannot search for all transactions");
            }
        }
    }



    //to do geld ook echt overschrijven
    public Transaction createTransactionForUser(Transaction transaction, String token) throws Exception {
        //Find loggedIn user
        User loggedInUser = getLoggedInUser(token);

        //use current time
        transaction.setTimestamp(LocalDateTime.now());
        //use logged-in userId as performing user
        transaction.setUserPerforming(loggedInUser.getUserId());
        //use default transaction if not provided in the input attribute
        if(transaction.getTransactionType() == null){
            transaction.setTransactionType(Transaction.TransactionTypeEnum.TRANSACTION);
        }

        //if bank account transaction performed bij bank account owner
        if(loggedInUser.getEmail().equals(bankAccount.getBankAccountOwnersEmail()) && transaction.getAccountFrom().equals(bankAccount.getBankAccountIban()))
        {
            bankAccount.peformBankTransfer(transaction.getAmount());
            return transactionRepository.save(transaction);
        }
        else if(transaction.getAccountFrom().equals(bankAccount.getBankAccountIban()))
            throw new Exception("You cannot transfer from the bank's own account");

        //Validate the provided input before creating transaction
        validateInput(transaction, loggedInUser);

        //deduct amount from account
        updateAccountFromBalance(transaction);
        //add amount to account
        updateAccountToBalance(transaction);

        return transactionRepository.save(transaction);
    }

    /**
     *   Add amount to account
     * @param transaction
     */
    private void updateAccountToBalance(Transaction transaction) {
        if(accountRepository.findById(transaction.getAccountTo()).isPresent()){
            //This is internal bank account
            Account account = accountRepository.findById(transaction.getAccountTo()).get();
            double newBalance = account.getBalance()+transaction.getAmount();
            account.setBalance(newBalance);
            accountRepository.save(account);
        }else{
            //money will be removed from the bank own account
            bankAccount.removeAmountFromBankBalance(transaction.getAmount());
        }

    }

    /**
     * deduct amount from account
     * @param transaction
     */
    private void updateAccountFromBalance(Transaction transaction) {
        Account account = null;
        if(accountRepository.findById(transaction.getAccountFrom()).isPresent()){
            //This is internal bank account
            account = accountRepository.findById(transaction.getAccountFrom()).get();
        }else{
            //This is external bank account do nothing
        }
        double newBalance = account.getBalance()-transaction.getAmount();
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    /**
     * Validate the provided input to perform transaction
     * @param transaction
     * @param loggedInUser
     * @throws Exception
     */
    private void validateInput(Transaction transaction, User loggedInUser) throws Exception {

        User user = getUserById(transaction.getUserPerforming());

        Account accountFrom = getAccountById(transaction.getAccountFrom());

        //Check if account from is a valid account
        if(accountRepository.findById(transaction.getAccountFrom()).isPresent()){

            //Customer can transfer from there account only
            if(loggedInUser.getUserType() == User.UserTypeEnum.CUSTOMER && (!accountFrom.getOwner().equals(loggedInUser.getUserId()))){
                throw new Exception("You cannot transfer from other's account");
            }
            //Unable to transfer to own account (Account to is the same as account from)
            if(transaction.getAccountFrom().equals(transaction.getAccountTo())){
                throw new Exception("You cannot transfer to your own account!");
            }
            //Unable to transfer funds to another savings (besides your own)
            else if (accountFrom.getAccountType() == Account.AccountTypeEnum.SAVINGS && user.getUserType() != User.UserTypeEnum.CUSTOMER){
                throw new Exception("You cannot transfer the funds to a savings account.");
            }
            //Check if amount is higher than 0
            if(transaction.getAmount() < 0){
                throw new Exception("You cannot transfer a negative number.");
            }
            //Customer cannot transfer 0 (nothing)
            else if(transaction.getAmount() == 0){
                throw new Exception("You cannot transfer nothing.");
            }
            //Needs to be changed to the absolute limit
            else if(accountFrom.getBalanceLimit() < transaction.getAmount()){
                throw new Exception("Your have extended your absolute limit, please deposit money first.");
            }
            //Check is transfer is higher than balance
            else if(accountFrom.getBalance() < transaction.getAmount()){
                throw new Exception("You do not have enough balance to transfer this amount!");
            }
            //A user has a maximum of transactions per day
            else if(accountFrom.getTransactionDayLimit() <= getTodaysTransactionCount(user)){
                throw new Exception(String.format("You have reached your day limit of %d transactions.",accountFrom.getTransactionDayLimit()));
            }
            //Amount of transactions per day
            else if((getTodaysTransactionAmount(user) + transaction.getAmount()) >= accountFrom.getTransactionAmountLimit()){
                throw new Exception("You have reached your transaction limit, please wait until tomorrow.");
            }
        }else{
            //account_from is an external
            if(accountRepository.findById(transaction.getAccountTo()).isPresent()){
                //account_to is an internal account

                //Customer can transfer from there account only, they can't transfer from external account
                if(loggedInUser.getUserType() == User.UserTypeEnum.CUSTOMER ){
                    throw new Exception("You cannot transfer from other's bank account");
                }
                //Check if amount is higher than 0
                if(transaction.getAmount() < 0){
                    throw new Exception("You cannot transfer a negative number.");
                }
                //Customer cannot transfer 0 (nothing)
                else if(transaction.getAmount() == 0){
                    throw new Exception("You cannot transfer nothing.");
                }

            }else{
                //account_to is an external account too
                throw new Exception("You cannot make transaction as both accounts are external");
            }

        }
    }


    /**Get all transaction done by a User on same day
     * @param user
     * @return
     */
    List<Transaction> getAllTodaysTransactions(User user) {

        List<Account> accounts = accountRepository.findAccountByOwner(user.getUserId());

        // Retrieve all transaction details of the user
        Set<Transaction> allTransactionSet = new HashSet<Transaction>();
        for (Account account : accounts) {
            allTransactionSet.addAll(transactionRepository.findByIban(account.getIban()));
        }

        List<Transaction> todaysTransaction = new ArrayList<Transaction>();
        LocalDateTime timeNow = LocalDateTime.now();
        for (Transaction trans : allTransactionSet) {
            if (trans.getTimestamp().getYear() == timeNow.getYear()
                    && trans.getTimestamp().getMonth() == timeNow.getMonth()
                    && trans.getTimestamp().getDayOfMonth() == timeNow.getDayOfMonth()) {

                todaysTransaction.add(trans);
            }
        }

        return todaysTransaction;
    }

    /**returns total amount of transaction done today for a user
     * @param user
     * @return
     */
    private Double getTodaysTransactionAmount(User user) {

        Double totalTransactionAmount = new Double(0);
        List<Transaction> todaysTransaction = getAllTodaysTransactions(user);
        for (Transaction trans: todaysTransaction){

            totalTransactionAmount += trans.getAmount();
        }
        return totalTransactionAmount;
    }

    /**returns number of transaction done today for a user
     * @param user
     * @return
     */
    private Integer getTodaysTransactionCount(User user) {

        List<Transaction> todaysTransaction = getAllTodaysTransactions(user);

        return todaysTransaction.size();
    }

    /**
     * get logged-in user from authentication token
     * @param token
     * @return
     */

    private User getLoggedInUser(String token) {
        AuthToken authToken = authTokenRepository.findById(token).get();
        Integer loggedInUserId = authToken.getUserId();
        User loggedInUser= getUserById(loggedInUserId);
        return loggedInUser;
    }

    /**
     * Find a User by it's id
     * @param userId
     * @return user
     */
    private User getUserById(Integer userId) {
        //Check if user exist or not
        if (!userRepository.findById(userId).isPresent()) { return null; }

        return userRepository.findById(userId).get();
    }
    /**
     * Find a Account by it's id
     *
     * @param accountId
     * @return account
     */
    private Account getAccountById(String accountId) {
        //Check if account exist or not
        if (!accountRepository.findById(accountId).isPresent()) {  return null;  }

        return accountRepository.findById(accountId).get();
    }
}
