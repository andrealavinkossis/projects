package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *  This exception is meant to handle whenever the server encounters a problem with the account balance. It will throw
 *  a bad request and keep the server running
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Unable to access account balance")
public class BalanceTransferException extends Exception{
    public BalanceTransferException()
    {
        super("Could not access account balance");
    }

}
