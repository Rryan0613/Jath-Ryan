package com.example.webchatserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the data you may need to store about a Chat room.
 * You may add more methods or attributes as needed.
 **/
public class ChatRoom
{
    private String code;

    // Stores each message in the server.
    private List<String> chatHistory = new ArrayList<>();

    // Each user has a unique ID associated with their WebSocket session and their username.
    private Map<String, String> users = new HashMap<>();

    // When created, the chat room has at least one user.
    public ChatRoom(String code, String user) {
        this.code = code;
        // When created, the user has not entered their username yet.
        this.users.put(user, "");
    }

    public void DeclareCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public Map<String, String> getUsers()
    {
        return users;
    }

    public List<String> getChatHistory()
    {
        return chatHistory;
    }

    /**
     * This method will add the new userID to the room if it does not exist, or it will add a new userID, name pair.
     **/
    public void declareUserName(String userID, String name)
    {
        // Update the name.
        if (users.containsKey(userID)) {
            users.remove(userID);
            users.put(userID, name);
        } else { // Add new user.
            users.put(userID, name);
        }
    }

    /**
     * This method will remove a user from this room.
     **/
    public void removeUser(String userID)
    {
        if (users.containsKey(userID))
        {
            users.remove(userID);
        }
    }

    public boolean inRoom(String userID)
    {
        return users.containsKey(userID);
    }
}
