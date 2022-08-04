package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


/**
 *
 *   This class is responsible for holding the transfer data, so that it can easily be sent from the client side to
 *   the api and database, and then back again
 *
 */
public class Transfer {

    private Long transferId;
    @Min( value = 1)
    @Max( value = 2)
    private Long transferTypeId;
    @Min( value = 1)
    @Max (value = 3)
    private Long transferStatusId;
    @Min( value = 1)
    @NotNull
    private Long account_from;
    @Min( value = 1)
    @NotNull
    private Long account_to;
    @NotNull
    private BigDecimal amount;

    public Transfer(Long transferStatusId, Long transferTypeId, Long account_from, Long account_to, BigDecimal amount) {
        this.transferStatusId = transferStatusId;
        this.transferTypeId = transferTypeId;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Long getTransferId() {
        return transferId;
    }

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public Long getAccount_from() {
        return account_from;
    }

    public Long getAccount_to() {
        return account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAccount_from(Long account_from) {
        this.account_from = account_from;
    }

    public void setAccount_to(Long account_to) {
        this.account_to = account_to;
    }
}

