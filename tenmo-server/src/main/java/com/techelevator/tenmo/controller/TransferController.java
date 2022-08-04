package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.*;
import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private AccountDao jdbcAccountDao;
    private UserDao jdbcUserDao;
    private TransferDao jdbcTransferDao;

    public TransferController(AccountDao jdbcAccountDao, UserDao jdbcUserDao, TransferDao jdbcTransferDao) {
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcTransferDao = jdbcTransferDao;
    }

    /**
     * This method listens for the path name /account, and returns the account balance of user with the associated
     * token. This is meant to secure the account information from anyone who does not have the right authToken.
     * In addition, if the balance can not be retrieved, a BalanceTransferException is thrown
     *
     * @param principal
     * @return
     * @throws BalanceTransferException
     */
    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal principal) throws BalanceTransferException {
        BigDecimal balance = jdbcAccountDao.getAccountBalance(principal.getName());
        if (balance == null) {
            throw new BalanceTransferException();
        }
        return balance;
    }

    /**
     * This returns a list of simple user information that contains only the username and user id. This method allows
     * anyone to view this information. If there is a problem pulling the user information, an IdentificationException
     * is thrown
     *
     * @return
     * @throws IdentificationException
     */
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public List<UserThatIsntATrap> getListOfUsers() throws IdentificationException {
        List<UserThatIsntATrap> listOfUsers = null;
        listOfUsers = jdbcUserDao.findAllForAPI();
        if (listOfUsers == null) {
            throw new IdentificationException();
        }
        return listOfUsers;
    }

    /**
     * This is the method that listens and initiates transfers. It will take in the transfer status, so it can accept
     * all kinds of transfers. If the transfer can not go through, then it will throw one of many kinds of errors
     *
     * @param status
     * @param principal
     * @param transfer
     * @return
     * @throws MinimumValueException
     * @throws BalanceTransferException
     * @throws IdentificationException
     * @throws TransferException
     */
    @RequestMapping(path = "/transfer/{status}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long transfer(@PathVariable String status, Principal principal, @Valid @RequestBody Transfer transfer) throws MinimumValueException, BalanceTransferException, IdentificationException, TransferException {
        long statusID = 2;
        if (status.equalsIgnoreCase("pending")) {
            statusID = 1;
        }
        if (status.equalsIgnoreCase("accepted")) {
            if (transfer.getAmount().compareTo(jdbcAccountDao.getAccountBalance(principal.getName())) > 0) {
                throw new BalanceTransferException();
            }
        }
        if (status.equalsIgnoreCase("accepted") || status.equalsIgnoreCase("pending")) {
            if (transfer.getAccount_to().equals(transfer.getAccount_from())) {
                throw new IdentificationException();
            }
            transfer.setTransferStatusId(statusID);
            transfer.setTransferTypeId((statusID));
            Long transferID = transferToOrFrom(transfer, principal.getName());
            return transferID;
        } else {
            throw new TransferException();
        }
    }

    /**
     * This is a helper method that ensures the account numbers exist for each user, and that the user is not sending
     * money to themselves
     *
     * @param transfer
     * @param username
     * @return
     * @throws IdentificationException
     * @throws BalanceTransferException
     * @throws MinimumValueException
     * @throws TransferException
     */
    private long transferToOrFrom(Transfer transfer, String username) throws IdentificationException, BalanceTransferException, MinimumValueException, TransferException {
        Long transferID = (long) -1;
        Long accountTo = jdbcAccountDao.getAccountNumber(transfer.getAccount_to());
        Long accountFrom = jdbcAccountDao.getAccountNumber(transfer.getAccount_from());
        if (accountFrom == (long) -1 || accountTo == (long) -1) {
            throw new IdentificationException();
        }
        transfer.setAccount_to(accountTo);
        transfer.setAccount_from(accountFrom);
        String name = jdbcAccountDao.getUserName(transfer.getAccount_from());
        if (name.equals("")) {
            throw new IdentificationException();
        }
        if (name.equalsIgnoreCase(username)) {
            transferID = transferBalanceVerification(transfer);
        } else {
            System.out.print("You can't access anyone's balance but your own.");
            throw new TransferException();
        }
        return transferID;
    }

    /**
     * This method checks if the transfer status is set to "accepted". If so, it continues to another helper method, but
     * if not, it initiates the pending transfer and throws exceptions when needed
     *
     * @param transfer
     * @return
     * @throws BalanceTransferException
     * @throws TransferException
     * @throws MinimumValueException
     */
    private long transferBalanceVerification(Transfer transfer) throws BalanceTransferException, TransferException, MinimumValueException {
        long transferID = -1;
        if (transfer.getTransferStatusId() == (long) 2) {
            transferID = transferAccepted(transfer);
            return transferID;
        } else {
            transferID = jdbcTransferDao.addTransfer(transfer);
            if (transferID == -1) {
                throw new TransferException();
            }
            if (transferID == -2) {
                throw new MinimumValueException();
            }
            return transferID;
        }
    }

    /**
     * This is a helper method that actually adjust the account baclances of accepted transfers, then creates a new
     * transfer record for the transaction
     *
     * @param transfer
     * @return
     * @throws BalanceTransferException
     * @throws TransferException
     * @throws MinimumValueException
     */
    private long transferAccepted(Transfer transfer) throws TransferException, MinimumValueException {
        long transferID = -1;
        String adjust = jdbcAccountDao.adjustAccountBalanceTest();
        transferID = jdbcTransferDao.addTransfer(transfer, adjust);
        if (transferID > 0) {
            System.out.println("Your transfer was approved!");
        }
        if (transferID == -1) {
            throw new TransferException();
        }
        if (transferID == -2) {
            throw new MinimumValueException();
        }
        return 1;
    }

    /**
     * This is the method that gets specific transfer data. This checks the username to ensure that they are only
     * receiving transfer data of which they are a part of
     *
     * @param principal
     * @param transferId
     * @return
     * @throws IdentificationException
     * @throws TransferRecordException
     */
    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public DataHolder getSpecificTransfer(Principal principal, @PathVariable Long transferId) throws IdentificationException, TransferRecordException {
        DataHolder soughtTransfer = null;
        soughtTransfer = jdbcTransferDao.findByTransferId(transferId);
        if (soughtTransfer == null) {
            throw new IdentificationException();
        }
        String to = soughtTransfer.getAccountTo();
        String from = soughtTransfer.getAccountFrom();
        if (principal.getName().equalsIgnoreCase(to) || principal.getName().equalsIgnoreCase(from)) {
            return soughtTransfer;
        }
        throw new TransferRecordException();
    }

    /**
     * This method returns a list of transfers of a certain type associated with the user
     *
     * @param statusID
     * @param principal
     * @return
     * @throws IdentificationException
     * @throws TransferRecordException
     */
    @RequestMapping(path = "/transfer/status/{statusID}", method = RequestMethod.GET)
    public List<Request> listOfTransfers(@PathVariable int statusID, Principal principal) throws IdentificationException, TransferRecordException {
        List<Request> listOfTransfers = null;
        Long accountID = jdbcAccountDao.getAccountNumberByUserName(principal.getName());
        if (accountID == (long) -1) {
            throw new IdentificationException();
        }
        listOfTransfers = jdbcTransferDao.findAllTransfersByUser(accountID, statusID);
        if (listOfTransfers == null) {
            throw new IdentificationException();
        }
        if (listOfTransfers.size() == 1) {
            throw new TransferRecordException();
        }
        return listOfTransfers;
    }

    /**
     *
     * Thhis method allows users to accept their pending transfers. It accepts a DataHolder object in the request body
     * and checks it against a record from the database to ensure only the appropriate record is being changed. In addition
     * it looks at the name of the authorized uses, and compares it to the name in the DataHolder accountTo variable
     * to ensure that the person accepting the pending transfer is the same person the transfer was made to. This
     * method also ensures that authorized user has enout money to make the transfer before allowing it.
     * @param principal
     * @param dataHolder
     * @return
     * @throws IdentificationException
     * @throws BalanceTransferException
     * @throws TransferRecordException
     */
    @RequestMapping(path = "transfer/pending_to_accepted", method = RequestMethod.POST)
    public Long changePendingToAccepted(Principal principal, @Valid @RequestBody DataHolder dataHolder) throws IdentificationException, BalanceTransferException, TransferRecordException {
        String authName = principal.getName();
        String givenName = dataHolder.getAccountTo();
        if (givenName.trim().equalsIgnoreCase(authName.trim())) {
            DataHolder check = jdbcTransferDao.findByTransferId(dataHolder.getTransfer_id());
            if (check.toString().equalsIgnoreCase(dataHolder.toString())) {
                BigDecimal accountInfo = jdbcAccountDao.getAccountBalance(dataHolder.getAccountTo());
                if (accountInfo.compareTo(dataHolder.getAmount()) == -1) {
                    throw new BalanceTransferException();
                }
                long to = jdbcAccountDao.getAccountNumberByUserName(dataHolder.getAccountFrom());
                long from = jdbcAccountDao.getAccountNumberByUserName(dataHolder.getAccountTo());
                String bob = jdbcAccountDao.adjustAccountBalanceTest();
                long change = jdbcTransferDao.changePendingToAccepted(dataHolder.getTransfer_id(), bob, dataHolder.getAmount(), from, to);
                if (change == 1) {

                } else {
                    throw new TransferRecordException();
                }
                return (long) 1;
            }
            throw new TransferRecordException();
        }
        throw new IdentificationException();
    }

    /**
     *
     * This method allows users to reject their pending transfers.
     * @param principal
     * @param dataHolder
     * @return
     * @throws IdentificationException
     * @throws BalanceTransferException
     * @throws TransferRecordException
     */
    @RequestMapping(path = "transfer/pending_to_rejected", method = RequestMethod.PUT)
    public Long changePendingToRejected(Principal principal, @Valid @RequestBody DataHolder dataHolder) throws IdentificationException, BalanceTransferException, TransferRecordException {
        String authName = principal.getName();
        String givenName = dataHolder.getAccountTo();
        if (givenName.trim().equalsIgnoreCase(authName.trim())) {
            DataHolder check = jdbcTransferDao.findByTransferId(dataHolder.getTransfer_id());
            if (check.toString().equalsIgnoreCase(dataHolder.toString())) {
                long change = jdbcTransferDao.changePendingToRejected(dataHolder.getTransfer_id());
                if (change == 1) {
                    return (long) 1;
                }
                throw new TransferRecordException();
            }
            throw new TransferRecordException();
        }
        throw new IdentificationException();
    }
}
