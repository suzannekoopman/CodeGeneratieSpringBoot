package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-05-12T14:55:03.233Z[GMT]")
public class MachineTransfer {


    public MachineTransfer(double amount, String transferType) {
        this.amount = amount;
        this.transferType = TransferTypeEnum.fromValue(transferType);
    }

    @JsonProperty("amount")
        private double amount;

        /**
         * Gets or Sets transferType
         */
        public enum TransferTypeEnum {
            DEPOSIT("deposit"),

            WITHDRAW("withdraw");

            private String value;

            TransferTypeEnum(String value) {
                this.value = value;
            }

            @Override
            @JsonValue
            public String toString() {
                return String.valueOf(value);
            }

            @JsonCreator
            public static TransferTypeEnum fromValue(String text) {
                for (TransferTypeEnum b : TransferTypeEnum.values()) {
                    if (String.valueOf(b.value).equals(text)) {
                        return b;
                    }
                }
                return null;
            }
        }
        @JsonProperty("transfer_type")
        private TransferTypeEnum transferType = null;

        public MachineTransfer amount(double amount) {
            this.amount = amount;
            return this;
        }

        /**
         * Get amount
         * @return amount
         **/
        @ApiModelProperty(required = true, value = "")
        @NotNull

        @Valid
        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public MachineTransfer transferType(TransferTypeEnum transferType) {
            this.transferType = transferType;
            return this;
        }

        /**
         * Get transferType
         * @return transferType
         **/
        @ApiModelProperty(required = true, value = "")
        @NotNull

        public TransferTypeEnum getTransferType() {
            return transferType;
        }

        public void setTransferType(TransferTypeEnum transferType) {
            this.transferType = transferType;
        }




}
