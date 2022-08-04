package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * This exception is thrown whenever a transfer cannot be made but the account balance is greater than 0.
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The transfer could not be initiated as currently configured")
public class TransferException  extends  Exception{
    public TransferException()
    {
        super("Could not initiate the transfer as requested");
    }
}
