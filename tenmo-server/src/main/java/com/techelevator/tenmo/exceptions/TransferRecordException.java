package com.techelevator.tenmo.exceptions;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *  This is the exception that will be thrown whenever there are not any transfer records to view
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "No transfer records found")
public class TransferRecordException extends Exception{
    public TransferRecordException()
    {
        super("Could not find the requested transfer records");
    }
}
