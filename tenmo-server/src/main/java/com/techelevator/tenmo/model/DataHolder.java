package com.techelevator.tenmo.model;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 *   This class is meant to hold the information on a specific transfer so it can easily be displayed to the screen
 *
 */
public class DataHolder {
    @NotNull
    String accountFrom;
    @NotNull
    String accountTo;
    @NotNull
    String typeDescription;
    @NotNull
    String statusDescription;
    @NotNull
    Long transfer_id;
    @NotNull
    BigDecimal amount;

    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Long getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(Long transfer_id) {
        this.transfer_id = transfer_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DataHolder{" +
                "accountFrom='" + accountFrom + '\'' +
                ", accountTo='" + accountTo + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", statusDescription='" + statusDescription + '\'' +
                ", transfer_id=" + transfer_id +
                ", amount=" + amount +
                '}';
    }
}
