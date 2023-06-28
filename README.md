# My Shelfie
![](https://iltopodiludoteca.it/wp-content/uploads/2022/12/wp-1669990262205-scaled.jpg)

## The Project

This project brings My Shelfie, a board game by Cranio Creations, to life as a Java multiplayer videogame. 

It was developed by a group of students for the Software Engineering 2022-2023 at Politecnico di Milano for our third year Computer Science Engineering project. 
The member of the group are: 
- Vlad Alexandru Robu
- Riccardo Speroni
- Letizia Maria Chiara Torre
- Davide Vola

## Overview 

My Shelfie is based on a client-server architecture, where multiple clients can connect to the same server. 

The server is able to handle client disconnecting throughout the game, but it does not allow reconnections. If a player disconnects, the server notifies the other players and ends the match, therefore you need to start another match to play again. 

After running the jars, the user can choose to play the game thorugh a command line interface or through a graphic user interface, implemented with JavaFX. The user can also choose which connection protocol is desired between RMI and Socket and they will need to type the IP and the port of the Server.

## How to play
### Server

The server can be launched using the executable jar Server.jar and writing the following command in the prompt:

```bash
java  -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=yourServerIP -jar ServerExecutable.jar
```
For ensuring the correct funtioning of the RMI connection, it is advised for the server's machine to disable its firewalls.



### Client

The Client can be launched using the executable jar Client.jar:

```bash
java -jar Client.jar
```

After the client being launched, the user is asked to choose which interface (GUI orTUI) they want to play with and then they have to insert the IP address that the server is currently running on.


## Implemented Functionalities

| Functionality  |   |
|---|---|
| Complete rules  | ✅	  |
| Socket  | ✅  |
|  RMI |  ✅  |
|  GUI  | ✅    |
| TUI |  ✅  |
|  Chat| ✅  |


## Tests

Project was tested using JUnit unit tests. Running all written tests with coverage results in 100% coverage for Controller package and 100% coverage for Model package, according to the project requirements. 

## Rules


## Explanation of the json files
For a better understanding of the cards and the board's funcionalities, please check the file under /deliverables named JsonsReadMe.pdf




