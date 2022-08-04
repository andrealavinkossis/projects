package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.IdentificationException;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserThatIsntATrap;

import java.util.List;

public interface UserDao {

//    List<User> findAll();

    User findByUsername(String username);

//    int findIdByUsername(String username);

    boolean create(String username, String password);

    List<UserThatIsntATrap> findAllForAPI();
}
