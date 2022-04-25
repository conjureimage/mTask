package com.conjureimage.mtask.exception;

public class UserNotAuthenticated extends Exception{
    public UserNotAuthenticated() {
        super("User not authenticated");
    }
}
