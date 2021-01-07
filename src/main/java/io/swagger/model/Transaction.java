package io.swagger.model;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import io.swagger.annotations.ApiModelProperty;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transaction
 */
@Entity
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-03T10:32:36.707Z[GMT]")
public class Transaction   {
    public Transaction() {
    }
    public Transaction(String transactionType, LocalDateTime timestamp, String accountFrom, String accountTo, Double amount, Integer userPerforming) {
        this.transactionType = TransactionTypeEnum.fromValue(transactionType);
        this.timestamp = timestamp;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.userPerforming = userPerforming;
    }

    public Transaction(String accountFrom, String accountTo, Double amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    @Id
    @SequenceGenerator(name="seqTransaction", initialValue = 100001)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTransaction")
    @JsonProperty("transaction_id")
    private Long transactionId = null;

    /**
     * Gets or Sets transactionType
     */
    public enum TransactionTypeEnum {
        WITHDRAW("withdraw"),

        DEPOSIT("deposit"),

        TRANSACTION("transaction");

        private String value;

        TransactionTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static TransactionTypeEnum fromValue(String text) {
            for (TransactionTypeEnum b : TransactionTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("transaction_type")
    @Valid
    private TransactionTypeEnum transactionType = null;

    @JsonProperty("timestamp")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp = null;

    @JsonProperty("account_from")
    private String accountFrom = null;

    @JsonProperty("account_to")
    private String accountTo = null;

    @JsonProperty("amount")
    private Double amount = null;

    @JsonProperty("user_performing")
    private Integer userPerforming = null;

    public Transaction transactionId(Long transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    /**
     * Unique transaction id
     * @return transactionId
     **/
    @ApiModelProperty(example = "10034", value = "Unique transaction id")

    public Long getTransactionId() {
        return transactionId;
    }

    public Transaction transactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    /**
     * Get transactionType
     * @return transactionType
     **/
    @ApiModelProperty(value = "")

    public TransactionTypeEnum getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeEnum transactionType) {
        this.transactionType = transactionType;
    }

    public Transaction LocalDateTime(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Date time of transaction creation
     * @return timestamp
     **/
    @ApiModelProperty(example = "995-09-07T10:40:52Z", value = "Date time of transaction creation")

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction accountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
        return this;
    }

    /**
     * Account that is transferring amount
     * @return accountFrom
     **/
    @ApiModelProperty(example = "NL39INGB007801007", required = true, value = "Account that is transferring amount")

    @Pattern(regexp="^\\w{2}\\d{2}\\w{4}\\d{10}$")   public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Transaction accountTo(String accountTo) {
        this.accountTo = accountTo;
        return this;
    }

    /**
     * Account that receives payment
     * @return accountTo
     **/
    @ApiModelProperty(example = "NL39ING008451843", required = true, value = "Account that receives payment")

    @Pattern(regexp="^\\w{2}\\d{2}\\w{4}\\d{10}$")   public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public Transaction amount(Double amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Amount of money that's been transferred
     * @return amount
     **/
    @ApiModelProperty(example = "22.30", required = true, value = "Amount of money that's been transferred")

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Transaction userPerforming(Integer userPerforming) {
        this.userPerforming = userPerforming;
        return this;
    }

    /**
     * id of performing user
     * @return userPerforming
     **/
    @ApiModelProperty(example = "1", value = "id of performing user")

    public Integer getUserPerforming() {
        return userPerforming;
    }

    public void setUserPerforming(Integer userPerforming) {
        this.userPerforming = userPerforming;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        return Objects.equals(this.transactionId, transaction.transactionId) &&
                Objects.equals(this.transactionType, transaction.transactionType) &&
                Objects.equals(this.timestamp, transaction.timestamp) &&
                Objects.equals(this.accountFrom, transaction.accountFrom) &&
                Objects.equals(this.accountTo, transaction.accountTo) &&
                Objects.equals(this.amount, transaction.amount) &&
                Objects.equals(this.userPerforming, transaction.userPerforming);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, transactionType, timestamp, accountFrom, accountTo, amount, userPerforming);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Transaction {\n");

        sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
        sb.append("    transactionType: ").append(toIndentedString(transactionType)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("    accountFrom: ").append(toIndentedString(accountFrom)).append("\n");
        sb.append("    accountTo: ").append(toIndentedString(accountTo)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    userPerforming: ").append(toIndentedString(userPerforming)).append("\n");
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
