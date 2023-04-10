package com.example.webchatserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the data you may need to store about a Chat room.
 * You may add more methods or attributes as needed.
 **/
public class ChatRoom {
    private String code;
    private List<String> chatHistory = new ArrayList<>();
    private List<String> chatHistoryBeforeRefresh = new ArrayList<>();
    private Map<String, String> users = new HashMap<>();

    public ChatRoom(String code, String user) {
        this.code = code;
        this.users.put(user, "");
        this.chatHistory.add("Welcome to the chat room!");
        this.chatHistoryBeforeRefresh = new ArrayList<>(this.chatHistory);
    }

    public void declareCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public List<String> getChatHistory() {
        return chatHistory;
    }

    public void declareUserName(String userID, String name) {
        if (users.containsKey(userID)) {
            users.remove(userID);
        }
        users.put(userID, name);
    }

    public void removeUser(String userID) {
        if (users.containsKey(userID)) {
            users.remove(userID);
        }
    }

    public boolean inRoom(String userID) {
        return users.containsKey(userID);
    }

//    public void addMessageToChatHistory(String message) {
//        chatHistory.add(message);
//    }

    public void addMessageToChatHistory(String message) {
        chatHistory.add(message);
        chatHistoryBeforeRefresh = new ArrayList<>(this.chatHistory);
    }

    public List<String> getChatHistoryBeforeRefresh() {
        return chatHistoryBeforeRefresh;
    }
}


