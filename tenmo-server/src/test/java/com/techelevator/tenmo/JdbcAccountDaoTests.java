package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    // SET FAKE DATA, but I don't know if I can since there is not an Account model.
    //INSERT INTO account (user_id, balance) VALUES (101, 100.00);
    //INSERT INTO account (user_id, balance) VALUES (102, 100.00);
    //INSERT INTO account (user_id, balance) VALUES (103, 100.00);

    //INSERT INTO tenmo_user (username, password_hash) VALUES ('User 1', 'bob');
    //INSERT INTO tenmo_user (username, password_hash) VALUES ('User 2', 'bob');
    //INSERT INTO tenmo_user (username, password_hash) VALUES ('User 4', 'bob');

    private JdbcAccountDao sut; // instantiate a JdbcAccountDao, system under test

    @Before
    // before every test, create a new JdbcUserDao called sut that passes in dataSource AND a new user called testUser
    public void setup() {
        sut = new JdbcAccountDao(new JdbcTemplate(dataSource)); //Do we need a UserThatIsntATrap DAO?

    }

    @Test
    public void getAccountBalance_real_username_returns_balance() {
        BigDecimal balance = sut.getAccountBalance("User 1");
        Assert.assertEquals(new BigDecimal("100.00"), balance);
    }
    @Test
    public void getAccountBalance_fake_username_returns_null(){
        BigDecimal balance = sut.getAccountBalance("User 100");
        Assert.assertNull(balance);
    }
}