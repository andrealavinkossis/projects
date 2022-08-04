package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *  This is the exception that will be thrown whenever a user initiates a transfer, or views transfer information
 *  from an account number that is not valid
 *
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "ID info could not be found or was not accepted")
public class IdentificationException extends Exception{

    public IdentificationException()
    {
        super("Could not access identification info");
    }

}
