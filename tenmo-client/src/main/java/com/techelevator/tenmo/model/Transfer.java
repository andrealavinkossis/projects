package com.techelevator.tenmo.model;

import java.math.BigDecimal;

/**
 *
 *
 *    This class is meant to handle the transfer data. It is used mostly when a user transfer money between accounts
 *
 */

public class Transfer {

    private Long transferId;
    private Long transferTypeId;
    private Long transferStatusId;
    private Long account_from;
    private Long account_to;
    private BigDecimal amount;

    public Transfer(Long account_from, Long account_to, BigDecimal amount) {
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
}

