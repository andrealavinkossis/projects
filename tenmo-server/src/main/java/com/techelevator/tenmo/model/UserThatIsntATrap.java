package com.techelevator.tenmo.model;

/**
 *
 *  This is the class we needed to ensure user's tokens and hashed passwords are not visible to everyone
 *
 */
public class UserThatIsntATrap {

    private String username;
    private long userId;

    public UserThatIsntATrap() {
    }

    public UserThatIsntATrap(String username, long userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
