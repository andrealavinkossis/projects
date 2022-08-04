package com.techelevator.tenmo;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserThatIsntATrap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests {


    // SET FAKE DATA, per UserThatIsntATrap model, requires String username, long userId
    private static final UserThatIsntATrap USER_THAT_ISNT_A_TRAP_1 = new UserThatIsntATrap("User 1", 101);
    private static final UserThatIsntATrap USER_THAT_ISNT_A_TRAP_2 = new UserThatIsntATrap("User 2", 102); //NOTE:
    private static final UserThatIsntATrap USER_THAT_ISNT_A_TRAP_4 = new UserThatIsntATrap("User 4", 104);

    private UserThatIsntATrap testUser; // instantiate a test user
    private JdbcUserDao sut; // instantiate a JdbcUserDao, system under test

    @Before
    // before every test, create a new JdbcUserDao called sut that passes in dataSource AND a new user called testUser
    public void setup() {
        sut = new JdbcUserDao(new JdbcTemplate(dataSource)); //Do we need a UserThatIsntATrap DAO?
        testUser = new UserThatIsntATrap("Test User", (long)313);

        // this.userId = userId;
        //      this.username = username;
        //      this.password = password;
        //      this.activated = true;
    }
    @Test // THIS DOES NOT WORK, WHY?
    public void findAllForAPI_returns_all_records() {
        List<UserThatIsntATrap> users = sut.findAllForAPI();
        Assert.assertEquals(3, users.size());
    }

     @Test
    public void findByUsername_returns_correct_record_for_username() {
        User user = sut.findByUsername("User 1");
         Assert.assertEquals("bob", user.getPassword());
     }



    @Test
     public void create_user_actually_creates_a_user(){
        Boolean newUser = sut.create("Bob", "bob");
         Assert.assertTrue(newUser);
         User userBob = sut.findByUsername("Bob");
         Assert.assertNotNull(userBob);
     }

     @Test
    public void create_user_without_name_returns_false(){
        Boolean newUser = sut.create(null, "bob");
        Assert.assertFalse(newUser);
     }


}


