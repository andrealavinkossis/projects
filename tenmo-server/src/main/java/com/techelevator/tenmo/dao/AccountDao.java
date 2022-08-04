package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.BalanceTransferException;
import com.techelevator.tenmo.exceptions.IdentificationException;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getAccountBalance(String username) throws BalanceTransferException; // only for Admin or for the particular user this method must be used in jdbcAccountDao and we can give specific instructions about how to do so there, using @Override

//    boolean adjustAccountBalance(BigDecimal amount, long getAccount_from, long getAccount_to) throws BalanceTransferException; // getAccount_from can't equal getAccount_to, the userId attached to both accounts can't match

    String getUserName(Long accountId) throws IdentificationException;

    Long getAccountNumber(Long userId) throws IdentificationException;

    Long getAccountNumberByUserName(String username) throws IdentificationException;

    String adjustAccountBalanceTest();

}
