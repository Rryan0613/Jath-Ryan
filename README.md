# Assignment 02 - Web Chat Server (Instructions)
> Course: CSCI 2020U: Software Systems Development and Integration

# ReadMe File

## Project Information:

We are creating our own web chat server using a java web application project.
Our chat server will use websockets and HTML/JS/CSS. Our web chat server
has multiple chat room, messages can only be sent to those on the same room.
Upon joining a different room, users will automatically leave their previous
room. A user needs to in a room in order to be able to send chat messages.

## Improvements:

The improvements that we made to the interface is that we displayed the unique code that was generated to help users 
see which room they were in. We also changed the way the buttons looked to help with visibility. Everything else 
was just changes to the appearance of the website, for example we changed the background to a grey and white, with white color text, and for the about
page we changed the background to all grey with orange color text. We kept the chat rooms and everything the same position, but we made the chat room text boxed, so it would look cleaner.
I have also added a refresh button that will clear the chat box in-case it becomes too clustered. It will also display the 
previous room codes in-case you would like to check the history of that chat room. 

## How to run:
To get git clone on windows open git bash if not already installed. Then on git bash input $ cd
and the folder location. Then get the git clone link, and do $ git clone and then the link. For mac,
you would open up terminal type cd and the location to the folder. Then do git clone and paste the copy
of the link. In order to fully access the website of its full functionality, once you have git cloned it,
you must create a glassfish local, the url will be http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-servlet
then once you have created local glassfish, you must create your remote glassfish, this time with a war: artifact.
once both glassfish are created, first run the local glassfish, then proceed to run the remote glassfish.
When the user first accesses the web page, the user would first need to create a new chat room. In order to create a new chat room
the user must click the "create and join new room" button to generate a unique room code.
After the user will be asked to be asked to enter a username, and then a message would pop up
to greet the new user. The user can now send messages and to send the messages the user would need
to press the enter key button. To check the history of the page you can either generate another room code
and simply re-enter the previous room code to see the chat history. Or you would first type in the text box, then
click on the about page, and then back to the chat room page, type in the same room code, it will then display
the history of the chat. To get to our about page you would click the about button on the top
of the page, where it would list information about the web application, and the contributors.
I have also added a refresh button, it will clear the chat box in-case it gets to clustered.


## Other resources:
We used lecture notes, such as WSChatServerDemo that was used for reference. And a couple of reference code from stackoverflow and Codepedia

## Members:
Ryan Liu - 100817964
Jathushan Vishnukaran - 100819506