package com.techelevator.tenmo.model;

import java.math.BigDecimal;

/**
 *
 *    This class is meant to handle the data for viewing a list of past transfers. It allows the user to
 *    view their own transfer history, then search for a more specific transfer which would be stored in
 *    DataHolder.
 *
 */

public class Request {

    private long transferStatusId;
    private String username;
    private BigDecimal amount;

    public long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}