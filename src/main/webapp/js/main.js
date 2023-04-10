let ws;

function newRoom()
{
// calling the ChatServlet to retrieve a new room ID
    let callURL= "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(response => enterRoom(response)); // enter the room with the code
}

function joinRoom() {
    // Get the room code from the input field
    var roomCode = document.getElementById("room-code").value;

    // Fetch the chat log for the selected room
    var chatLog = fetchChatLog(roomCode);

    // Display the chat log in the text area
    document.getElementById("log").value = chatLog;

    // Update the current room text
    document.getElementById("room-id-literal").textContent = "Room: " + roomCode;

    // Clear the input field
    document.getElementById("input").value = "";

    // Scroll to the bottom of the chat log
    document.getElementById("log").scrollTop = document.getElementById("log").scrollHeight;
}




document.getElementById("input").addEventListener("keyup", function (event)
{
    if (event.keyCode === 13) {
        let request = {"type":"chat", "msg":event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";
    }
});
function enterRoom(code)
{
    // refresh the list of rooms
// create the web socket
    ws = new WebSocket("ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/"+code);

   document.getElementById("room-id-literal").innerHTML = "Current Room: " + code;
    let roomList = document.getElementById("room-list");
    let newRoom = document.createElement("li");
    newRoom.innerText = code;
    roomList.appendChild(newRoom);

    // add click event listener to the new room list item
    newRoom.addEventListener("click", function() {
        // request chat history for this room
        fetch(`http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-history-servlet?room=${code}`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
            },
        })
            .then(response => response.json())
            .then(history => {
                // display chat history for this room
                let log = document.getElementById("log");
                log.value = "";
                history.forEach(message => {
                    log.value += message.message + "\n";
                });
            });
    });

// parse messages received from the server and update the UI accordingly
    ws.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);

        // handle message
        document.getElementById("log").value += message.message + "\n";
    }
}

function refreshChatLog() {
    document.getElementById("log").value = "";
}
















