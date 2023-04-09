package com.example.webchatserver;


import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import static com.example.util.ResourceAPI.loadChatRoomHistory;
//import static com.example.util.ResourceAPI.saveChatRoomHistory;


/**
 * This class represents a web socket server, a new connection is created and it receives a roomID as a parameter
 * **/
@ServerEndpoint(value="/ws/{roomID}")
public class ChatServer {

    // contains a static List of ChatRoom used to control the existing rooms and their users
    private static Map<String, String> roomList = new HashMap<String, String>();
    private Map<String, String> usernames = new HashMap<String, String>();
    private static Map<String, String> roomHistoryList = new HashMap<String, String>();
    private String userID;
    // you may add other attributes as you see fit



    @OnOpen
    public void open(@PathParam("roomID") String roomID, Session session) throws IOException, EncodeException {

        session.getBasicRemote().sendText("Join a room to start playing/chatting.");
        session.getBasicRemote().sendText("Alphanumeric code:" + roomID);
//        accessing the roomID parameter
        System.out.println(roomID);

        roomList.put(session.getId(), roomID); // adding userID to a room
        // loading the history chat
        String history = loadChatRoomHistory(roomID);
        System.out.println("Room joined ");
        if (history!=null && !(history.isBlank())){
            System.out.println(history);
            history = history.replaceAll(System.lineSeparator(), "\\\n");
            System.out.println(history);
            session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\""+history+" \\n Chat room history loaded\"}");
            roomHistoryList.put(roomID, history+" \\n "+roomID + " room resumed.");
        }
        if(!roomHistoryList.containsKey(roomID)) { // only if this room has no history yet
            roomHistoryList.put(roomID, roomID + " room Created."); //initiating the room history
        }

        session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome to the chat room. Please state your username to begin.\"}");

    }

    public static void saveChatRoomHistory(String roomID, String log) throws IOException {
        //String uriAPI = "http://localhost:8080/ChatResourceAPI-1.0-SNAPSHOT/api/history/"+roomID;
        String uriAPI = "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/api/history/"+roomID;
        URL url = new URL(uriAPI);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        // allows us to write content to the outputStream
        con.setDoOutput(true);

        // sending the data with the POST request
        String jsonInputString = "{\"room\":\""+roomID+"\",\"log\":\""+log+"\"}";
        System.out.println(jsonInputString);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //reading and printing response
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private String loadChatRoomHistory(String roomID) throws IOException {
        //String uriAPI = "http://localhost:8080/ChatResourceAPI-1.0-SNAPSHOT/api/history/"+roomID;
        String uriAPI = "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/api/history/"+roomID;
        URL url = new URL(uriAPI);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        // we dont need to write content to the outputStream
        con.setDoOutput(false);
        // allows us to read inputstream
        con.setDoInput(true);

        // getting the inputStream
        InputStream inStream = con.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        String jsonData = buffer.toString();

        System.out.println("load the data");
        System.out.println(jsonData);

        // transforming the string into objects using org.json library
        JSONObject data = new JSONObject(jsonData);
        Map<String, Object> mapData = data.toMap();
        String content = (String) mapData.get("log");

        return content;

    }

    @OnClose
    public void close(Session session) throws IOException, EncodeException {
        String userId = session.getId();
        // do things for when the connection closes
        if (usernames.containsKey(userId)) {
            String username = usernames.get(userId);
            String roomID = roomList.get(userId);
            usernames.remove(userId);
            // remove this user from the roomList
            roomList.remove(roomID);

            // adding event to the history of the room
            String logHistory = roomHistoryList.get(roomID);
            roomHistoryList.put(roomID, logHistory+" \\n " + username + " left the chat room.");

            // broadcasting it to peers in the same room
            int countPeers = 0;
            for (Session peer : session.getOpenSessions()){ //broadcast this person left the server
                if(roomList.get(peer.getId()).equals(roomID)) { // broadcast only to those in the same room
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + username + " left the chat room.\"}");
                    countPeers++; // count how many peers are left in the room
                }
            }

            // if everyone in the room left, save history
            if(!(countPeers >0)){
                saveChatRoomHistory(roomID, roomHistoryList.get(roomID));
            }
        }

    }

    @OnMessage
    public void handleMessage(String comm, Session session) throws IOException, EncodeException {
//        example getting unique userID that sent this message
        String userId = session.getId();
        String roomID = roomList.get(userID); // my room
        JSONObject jsonmsg = new JSONObject(comm);
        String type = (String) jsonmsg.get("type");
        String message = (String) jsonmsg.get("msg");

//        Example conversion of json messages from the client
        //        JSONObject jsonmsg = new JSONObject(comm);
//        String val1 = (String) jsonmsg.get("attribute1");
//        String val2 = (String) jsonmsg.get("attribute2");

        // handle the messages
        if(usernames.containsKey(userID)){ // not their first message
            String username = usernames.get(userID);
            System.out.println(username);

            // adding event to the history of the room
            String logHistory = roomHistoryList.get(roomID);
            roomHistoryList.put(roomID, logHistory+" \\n " +"(" + username + "): " + message);

            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only send my messages to those in the same room
                if(roomList.get(peer.getId()).equals(roomID)) {
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(" + username + "): " + message + "\"}");
                }
            }
        }else{ //first message is their username
            usernames.put(userID, message);
            session.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server ): Welcome, " + message + "!\"}");

            // adding event to the history of the room
            String logHistory = roomHistoryList.get(roomID);
            roomHistoryList.put(roomID, logHistory+" \\n " + message + " joined the chat room.");

            // broadcasting it to peers in the same room
            for(Session peer: session.getOpenSessions()){
                // only announce to those in the same room as me, excluding myself
                if((!peer.getId().equals(userID)) && (roomList.get(peer.getId()).equals(roomID))){
                    peer.getBasicRemote().sendText("{\"type\": \"chat\", \"message\":\"(Server): " + message + " joined the chat room.\"}");
                }
            }
        }

    }


}