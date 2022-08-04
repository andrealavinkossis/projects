package com.techelevator.tenmo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 *   This is the exception that will the thrown whenever the user tries to transfer a balance that is below 0
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "could not initiate transfer due to a problem with the given amount")
public class MinimumValueException extends Exception{
    public MinimumValueException()
    {
        super("Must transfer a balance greater than 0");
    }
}
