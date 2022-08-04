package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.*;
import com.techelevator.tenmo.model.DataHolder;
import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

        List<Request> findAllTransfersByUser(Long accountId, int transferStatusId) throws IdentificationException, TransferRecordException; // have to do a join back to Account table, control so that only the user can access

        DataHolder findByTransferId(Long transferId) throws IdentificationException;  // id search specific, control so only admin OR the particular user

        Long addTransfer(Transfer transfer) throws BalanceTransferException, MinimumValueException, TransferException; // this makes a new transfer object so that it will auto-generate a new transferId.

        List<Request> findAllTransfersByType(String transferStatusDescription);

        Long changePendingToAccepted(long transferID, String adjust, BigDecimal balance, long from, long to);

        Long changePendingToRejected(long transferID);

        Long addTransfer(Transfer transfer, String adjust);
}
