package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.BalanceTransferException;
import com.techelevator.tenmo.exceptions.IdentificationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     *
     * Returns the account balance for a given username. This is based off the authToken information passed to the
     * server in an attempt to keep the account balance secure.
     * @param username
     * @return
     */
    @Override
    public BigDecimal getAccountBalance(String username) {
        String sql = "SELECT account.balance FROM account JOIN tenmo_user Using (user_id) WHERE tenmo_user.username = ?;";
        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
            return balance;
        }
        catch (Exception e){
            return null;
        }
    }

    /**
     *
     * This method is part of the transfer process. Initially, we made transferring the money between accounts and
     * making a new transfer record separate steps, but felt uncomfortable about the prospect of the database going
     * down before both actions were accomplished. So instead, we decided to move everything into a transaction block
     * in the JdbcTransferDao class. This string simply returns a String query that would update both account records.
     * @return
     */
    @Override
    public String adjustAccountBalanceTest(){
        String from = adjustAccountBalanceFromTest();
        String to = adjustAccountBalanceFromTest();
        return from+" "+to;
    }

    /**
     *
     * This is a private helper method that creates the string query to pass into the transfer dao class
     * @return
     */
    private String adjustAccountBalanceFromTest(){
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        return sql;
    }

    /**
     *
     * This method returns the username associated with a given account id
     * @param accountId
     * @return
     */
    @Override
    public String getUserName(Long accountId) {
        String sql = "SELECT tenmo_user.username FROM tenmo_user JOIN account USING(user_id) WHERE account.account_id = ?;";
        try {
            String results = jdbcTemplate.queryForObject(sql, String.class, accountId);
            System.out.println(results);
            return results;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     * This method returns the account id for a given user id
     * @param userId
     * @return
     */
    @Override
    public Long getAccountNumber(Long userId){
        String sql = "SELECT account.account_id FROM account WHERE user_id = ?;";
        try {
            Long results = jdbcTemplate.queryForObject(sql, Long.class, userId);
            System.out.println(results);
            return results;
        } catch (Exception e) {
            return (long)-1;
        }
    }

    /**
     *
     * This method returns the account number for a given username
     * @param username
     * @return
     */
    @Override
    public Long getAccountNumberByUserName(String username) {
        String sql = "SELECT account.account_id FROM account JOIN tenmo_user USING(user_id) WHERE username = ?;";
        try {
            Long results = jdbcTemplate.queryForObject(sql, Long.class, username);
            System.out.println(results);
            return results;
        } catch (Exception e) {
            return (long)-1;
        }
    }

}
