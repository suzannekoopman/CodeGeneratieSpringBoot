package io.swagger.model;

public class BankAccount{

    private String iban = "NL01INHO0000000001";
    private String bankAccountOwnerEmail = "BankAccountOwner";
    private int ownersId = 100001;
    private double balance = 100000.00;

    private static BankAccount singletonInstance = null;
    public static BankAccount getBankAccount(){
        if(singletonInstance == null)
        {
            singletonInstance = new BankAccount();
        }
        return singletonInstance;
    }
    public void addAmountToBankBalance(double amount)
    {
        this.balance = balance + amount;
    }
    public void removeAmountFromBankBalance(double amount)
    {
        this.balance = balance - amount;
    }
    public void peformBankTransfer(double amount)
    {
        this.balance = balance - amount;
    }
    public String getBankAccountIban()
    {
        return this.iban;
    }
    public String getBankAccountOwnersEmail()
    {
        return this.bankAccountOwnerEmail;
    }
    public double getBankAccountBalance(){return this.balance;}
    public int getBankAccountOwnerId(){return this.ownersId;}
}
