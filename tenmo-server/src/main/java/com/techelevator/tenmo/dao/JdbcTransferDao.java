package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.*;
import com.techelevator.tenmo.model.DataHolder;
import com.techelevator.tenmo.model.Request;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     *
     * This method takes in a transfer status id and an account id and shows all of the records that correspond to both
     * of those parameters. This allows a user to show both pending and accepted transfers. In addition, since this
     * list contains both the transfers to the user and the transfers from the user, it adds an item into the list to
     * reflect when that change happens since the data object does not have a variable to track that.
     * @param accountId
     * @param transferStatusId
     * @return
     */
    @Override
    public List<Request> findAllTransfersByUser(Long accountId, int transferStatusId)  { //set transfer to Request
        List<Request> allTransfers = new ArrayList<>();
        SqlRowSet result = null;
        String sql = "SELECT transfer.transfer_id, tenmo_user.username, transfer.amount FROM transfer JOIN account ON(transfer.account_from = account.account_id) JOIN tenmo_user USING(user_id) WHERE transfer.transfer_status_id = ? AND transfer.account_to = ? Order By transfer_id;";
        try {
            result = jdbcTemplate.queryForRowSet(sql, transferStatusId, accountId);
            while (result.next()) {
                allTransfers.add(mapRowToRequest(result));
            }
            Request request = new Request();
            request.setUsername("---FROM---");
            allTransfers.add(request);
            sql = "SELECT transfer.transfer_id, tenmo_user.username, transfer.amount FROM transfer JOIN account ON(transfer.account_to = account.account_id) JOIN tenmo_user USING(user_id) WHERE transfer.transfer_status_id = ? AND transfer.account_from = ? Order By transfer_id;";
            result = jdbcTemplate.queryForRowSet(sql, transferStatusId, accountId);
            while (result.next()) {
                allTransfers.add(mapRowToRequest(result));
            }
        }catch (Exception e)
        {
            return null;
        }
        return allTransfers;
    }

    /**
     *
     * This method is meant to return a list of all transfers by type, so this would return a list of transfers based
     * on whether the transfer was sent or requested
     * @param transferStatusDescription
     * @return
     */
    @Override
    public List<Request> findAllTransfersByType(String transferStatusDescription) {
        List<Request> transfersByType = new ArrayList<>();
        String sql = "SELECT transfer.transfer_id, tenmo_user.username, transfer.amount FROM transfer_status JOIN transfer USING(transfer_status_id) JOIN account ON(transfer.account_from = account.account_id) JOIN tenmo_user USING(user_id) WHERE transfer_status_desc = ? Order By transfer_id;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferStatusDescription);
            while (results.next()) {
                Request request = mapRowToRequest(results);
                transfersByType.add(request);
            }
        }catch (Exception e)
        {
            return null;
        }
        return transfersByType;
    }

    /**
     *
     * This method is meant to get more detailed data on a specific transfer. This will generally be called after the
     * user gets a list of their past transfers
     * @param transferId
     * @return
     */
    @Override
    public DataHolder findByTransferId(Long transferId) {
        String sql_from = "SELECT username FROM tenmo_user JOIN account ON(tenmo_user.user_id = account.user_id) JOIN transfer ON (account.account_id = transfer.account_from) WHERE transfer.transfer_id = ?;";
        String sql_to = "SELECT username FROM tenmo_user JOIN account ON(tenmo_user.user_id = account.user_id) JOIN transfer ON (account.account_id = transfer.account_to) WHERE transfer.transfer_id = ?;";
        String sql = "SELECT transfer.transfer_id, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, transfer.amount " +
                "FROM transfer_type JOIN transfer ON(transfer_type.transfer_type_id = transfer.transfer_type_id) " +
                "JOIN transfer_status ON(transfer_status.transfer_status_id = transfer.transfer_status_id) " +
                "WHERE transfer.transfer_id = ?;";
        try {
            String resultsFrom = jdbcTemplate.queryForObject(sql_from, String.class, transferId);
            String resultsTo = jdbcTemplate.queryForObject(sql_to, String.class, transferId);
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            DataHolder dataHolder = null;
            if (results.next()) {
                dataHolder = mapToDataHolder(results, resultsFrom, resultsTo);
            }
            return dataHolder;
        }catch (Exception e)
        {
            return null;
        }
    }

    /**
     *
     * This is the method that adds a pending transfer record to the transfer table. If the amount is 0 or below, then it
     * throws a minimum value exception
     * @param transfer
     * @return
     */
    @Override
    public Long addTransfer(Transfer transfer) {
        Long transferId = null;
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        if (transfer.getAmount() == null || transfer.getAmount().compareTo(new BigDecimal("0.0")) <= 0) {
            return (long)-2;
        }
        try {
            transferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
            if (transferId != null) {
                return transferId;
            }
        }catch (Exception e){
        }
        return (long)-1;
    }

    /**
     *
     * This is a method that adds an accepted transfer to the transfer table as well as changing the account balance
     * on both accounts
     * @param transfer
     * @param adjust
     * @return
     */
    @Override
    public Long addTransfer(Transfer transfer, String adjust) {
        Long transferId = null;
        String sql = "Start Transaction; " + adjust + " ";
        sql = sql + "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?); Commit;";
        if (transfer.getAmount().compareTo(new BigDecimal("0.0")) <= 0) {
            System.out.println("There wasn't enough $$ in your balance. Punk.");
            return (long) -2;
        }
        try {
            jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_from(), transfer.getAmount().negate(), transfer.getAccount_to(), transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
            System.out.println("It worked!!");
            return (long)1;
        } catch (Exception e) {
            System.out.println(sql);
            System.out.println(e.getMessage());
        }
        System.out.println("Meh, there was an error.");
        return (long)-1;
    }
    /**
     *
     * This is a helper method that takes the SQL data in the rowset, and puts it into a Request object
     * @param srs
     * @return
     */
    private Request mapRowToRequest(SqlRowSet srs) {
        Request request = new Request();
        request.setAmount(srs.getBigDecimal("amount"));
        request.setTransferStatusId(srs.getLong("transfer_id"));
        request.setUsername(srs.getString("username"));
        return request;

    }

    /**
     *
     * This is a helper method that takes the SQL data in the rowset, as well as two Strings that contain the usernames
     * of those involved with the transfers, and puts it into a DataHolder object
     * @param srs
     * @param resultsFrom
     * @param resultsTo
     * @return
     */
    private DataHolder mapToDataHolder(SqlRowSet srs, String resultsFrom, String resultsTo) {
        DataHolder dataHolder = new DataHolder();
        dataHolder.setAmount(srs.getBigDecimal("amount"));
        dataHolder.setTransfer_id(srs.getLong("transfer_id"));
        dataHolder.setStatusDescription(srs.getString("transfer_status_desc"));
        dataHolder.setTypeDescription(srs.getString("transfer_type_desc"));
        dataHolder.setAccountTo(resultsTo);
        dataHolder.setAccountFrom(resultsFrom);
        return dataHolder;
    }

    /**
     *
     * This is a method that changes pending requests to accepted, and changes the account balances to reflect the changes
     * @param transferID
     * @param adjust
     * @param balance
     * @param from
     * @param to
     * @return
     */
    @Override
    public Long changePendingToAccepted(long transferID, String adjust, BigDecimal balance, long from, long to)
    {
        String sql = "Start Transaction; "+adjust+" ";
        sql = sql + "Update transfer " +
                "Set transfer_type_id = 2, transfer_status_id = 2 " +
                "Where transfer_id = ?; Commit;";
        try{
            jdbcTemplate.update(sql, balance, from, balance.negate(), to, transferID);
        }catch (Exception e)
        {
            return (long)-1;
        }
        return (long)1;
    }

    /**
     *
     * This method changes pending request to rejected
     * @param transferID
     * @return
     */@Override
    public Long changePendingToRejected(long transferID)
    {
        String sql = "Update transfer " +
                "Set transfer_status_id = 3 " +
                "Where transfer_id = ?;";
        try{
            jdbcTemplate.update(sql, transferID);
        }catch (Exception e)
        {
            return (long)-1;
        }
        return (long)1;
    }
}
