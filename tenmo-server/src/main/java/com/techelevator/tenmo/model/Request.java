package com.techelevator.tenmo.model;

import java.math.BigDecimal;

/**
 *
 * This class is meant to hold simple transfer data. If a user has any questions about a specific transfer, then the ID
 * will be taken and another class will be responsible for holding the detailed transfer requests
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
