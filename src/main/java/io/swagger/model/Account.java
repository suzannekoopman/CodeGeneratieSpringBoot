package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Account
 */
@Entity
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-03T10:32:36.707Z[GMT]")
public class Account   {
    public Account()
    {
    }
  public Account(String iban, String accountType, Integer transactionDayLimit, double transactionAmountLimit, double balanceLimit, Integer owner, double balance){
    this.iban = iban;
    this.accountType = AccountTypeEnum.fromValue(accountType);
    this.transactionDayLimit = transactionDayLimit;
    this.transactionAmountLimit = transactionAmountLimit;
    this.balanceLimit = balanceLimit;
    this.owner = owner;
    this.balance = balance;
  }

  public Account(Integer transactionDayLimit, double transactionAmountLimit, double balanceLimit){
    this.transactionDayLimit = transactionDayLimit;
    this.transactionAmountLimit = transactionAmountLimit;
    this.balanceLimit = balanceLimit;
  }
  public Account(String accountType, double balance, Integer transactionDayLimit, double transactionAmountLimit, double balanceLimit){
    this.accountType = AccountTypeEnum.fromValue(accountType);
    this.balance = balance;
    this.transactionDayLimit = transactionDayLimit;
    this.transactionAmountLimit = transactionAmountLimit;
    this.balanceLimit = balanceLimit;
  }

  @Id
  @JsonProperty("iban")
  private String iban = null;

  /**
   * Gets or Sets accountType
   */
  public enum AccountTypeEnum {
    CURRENT("current"),

    SAVINGS("savings");

    private String value;

    AccountTypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static AccountTypeEnum fromValue(String text) {
      for (AccountTypeEnum b : AccountTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("account_type")
  @Valid
  private AccountTypeEnum accountType = null;

  @JsonProperty("balance")
  private double balance = 0.00;

  @JsonProperty("transactionDayLimit")
  private Integer transactionDayLimit = 0;

  @JsonProperty("transactionAmountLimit")
  private double transactionAmountLimit = 0.00;

  @JsonProperty("balanceLimit")
  private double balanceLimit = 0.00;

  @JsonProperty("owner")
  private Integer owner = null;

  public Account iban(String iban) {
    this.iban = iban;
    return this;
  }

  /**
   * Get iban
   * @return iban
   **/
  @ApiModelProperty(example = "NL11INHO0123456789", value = "")

  @Pattern(regexp="^\\w{2}\\d{2}\\w{4}\\d{10}$")   public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public Account accountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
    return this;
  }


  /**
   * Get accountType
   * @return accountType
   **/
  @ApiModelProperty(value = "")


  public AccountTypeEnum getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountTypeEnum accountType) {
    this.accountType = accountType;
  }

  public Account balance(double balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
   * @return balance
   **/
  @ApiModelProperty(example = "200", value = "")


  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public Account transactionDayLimit(Integer transactionDayLimit) {
    this.transactionDayLimit = transactionDayLimit;
    return this;
  }

  /**
   * Get transactionDayLimit
   * @return transactionDayLimit
   **/
  @ApiModelProperty(example = "100", required = true, value = "")
  @NotNull

  public Integer getTransactionDayLimit() {
    return transactionDayLimit;
  }

  public void setTransactionDayLimit(Integer transactionDayLimit) {
    this.transactionDayLimit = transactionDayLimit;
  }

  public Account transactionAmountLimit(double transactionAmountLimit) {
    this.transactionAmountLimit = transactionAmountLimit;
    return this;
  }

  /**
   * Get transactionAmountLimit
   * @return transactionAmountLimit
   **/
  @ApiModelProperty(example = "200", value = "")

  @Valid
  public double getTransactionAmountLimit() {
    return transactionAmountLimit;
  }

  public void setTransactionAmountLimit(double transactionAmountLimit) {
    this.transactionAmountLimit = transactionAmountLimit;
  }

  public Account balanceLimit(double balanceLimit) {
    this.balanceLimit = balanceLimit;
    return this;
  }

  /**
   * Get balanceLimit
   * @return balanceLimit
   **/
  @ApiModelProperty(example = "-1200", required = true, value = "")
  @NotNull

  @Valid
  public double getBalanceLimit() {
    return balanceLimit;
  }

  public void setBalanceLimit(double balanceLimit) {
    this.balanceLimit = balanceLimit;
  }

  public Account owner(Integer owner) {
    this.owner = owner;
    return this;
  }

  /**
   * id of owning user
   * @return owner
   **/
  @ApiModelProperty(value = "id of owning user")

  public Integer getOwner() {
    return owner;
  }

  public void setOwner(Integer owner) {
    this.owner = owner;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.iban, account.iban) &&
            Objects.equals(this.accountType, account.accountType) &&
            Objects.equals(this.balance, account.balance) &&
            Objects.equals(this.transactionDayLimit, account.transactionDayLimit) &&
            Objects.equals(this.transactionAmountLimit, account.transactionAmountLimit) &&
            Objects.equals(this.balanceLimit, account.balanceLimit) &&
            Objects.equals(this.owner, account.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, accountType, balance, transactionDayLimit, transactionAmountLimit, balanceLimit, owner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");

    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    transactionDayLimit: ").append(toIndentedString(transactionDayLimit)).append("\n");
    sb.append("    transactionAmountLimit: ").append(toIndentedString(transactionAmountLimit)).append("\n");
    sb.append("    balanceLimit: ").append(toIndentedString(balanceLimit)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
