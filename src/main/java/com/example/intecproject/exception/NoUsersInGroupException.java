package com.example.intecproject.exception;

public class NoUsersInGroupException extends Exception
{
    public NoUsersInGroupException(Long id) {
        super(String.format("There are no users in the group with ID: %d ",id));
    }
}
