let ws;

function newRoom(){
    // calling the ChatServlet to retrieve a new room ID
    let callURL= "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chatservlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(response => enterRoom(response)); // enter the room with the code
    System.out.println("Room ID from newRoom():" + generatingRandomUpperAlphanumericString(5))
}

function generatingRandomUpperAlphanumericString(number) {

}

function enterRoom(){

    let code = document.getElementById("room-code").value;
    let oldHTML = document.getElementById("roomlist").innerHTML;
    document.getElementById("roomlist").innerHTML = oldHTML+ "<tr>" + "<td>" + code + "<br>" + "</td>"+ "</tr>";

    let oldHTML1 = document.getElementById("roomnum").innerHTML;
    document.getElementById("roomnum").innerHTML = oldHTML1 + "<td>" + code + "</td>";

    //console.log(code);

    /*
    if (document.getElementById("room-code").value == "") {

        // refresh the list of rooms
        newRoom();
    }
    else {
        let code = document.getElementById("room-code").value;
        console.log(code);
    }

     */

    // create the web socket
    ws = new WebSocket("ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/"+code);


    // parse messages received from the server and update the UI accordingly
    ws.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);
        document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
        // handle message

    }
}

document.getElementById("input").addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        let request = {"type": "chat", "msg": event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";
    }
});

function timestamp() {
    var d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
}