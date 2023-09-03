# My Shelfie
![](https://iltopodiludoteca.it/wp-content/uploads/2022/12/wp-1669990262205-scaled.jpg)

## The Project

This project brings My Shelfie, a board game by Cranio Creations, to life as a Java multiplayer video game. 

It was developed by a group of students for the Software Engineering 2022-2023 course at Politecnico di Milano for our third year Computer Science Engineering project. 
The member of the group are: 
- Vlad Alexandru Robu
- Riccardo Speroni
- Letizia Maria Chiara Torre
- Davide Vola

The game runs correctly on Windows, Linux and MacOS.

## Overview 

My Shelfie is based on a client-server architecture, where multiple clients can connect to the same server. 

The server is able to handle client disconnecting throughout the game, but it does not allow reconnections. If a player disconnects, the server notifies the other players and ends the match, therefore you need to start another match to play again. 

After running the jars, the user can choose to play the game through a command line interface or through a graphic user interface, implemented with JavaFX. The user can also choose which connection protocol is desired between RMI and Socket, and they will need to type the IP and the port of the Server.

## How to play
### Server

The server can be launched using the executable jar Server.jar and writing the following command in the prompt:

```bash
java  -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=yourServerIP -jar ServerExecutable.jar
```
For ensuring the correct functioning of the RMI connection, it is advised for the server's machine to disable its firewalls.



### Client

The client can be launched using the executable jar ClientExecutable.jar:

```bash
java -jar ClientExecutable.jar
```

After the client is launched, the user is asked to choose which interface (GUI or TUI) they want to play with. Then, they have to insert the IP address that the server is currently running on.

### Using the GUI
Here are a few tips on how to use the graphical interface:
- To pick a tile, simply click on it. To deselect it, click on it again;
- When you're satisfied with your choice, click on the 'OK' button on the upper right corner of the Living Room board;
- To re-arrange your choice, drag the tiles in the desired position on the column between the Living Room and your Shelfie;
- When satisfied with the re-arrange, click on the column to place the tiles on your Shelfie. Note: in order to choose the column or re-arrange the tiles, you need to confirm your choice by clicking on the 'OK' button;
- To see the description of the Common Goal Cards, click on their icons on the upper left side of the screen;
- To chat with your opponents, you may use the box on the right side of the screen. You can choose to send a message to a specific user or to all the users by clicking on the name of the receiver at the top of the chat box.



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
For a complete explanation of the rules, please check the file under /etc directory named MyShelfie_Rulebook_ENG.pdf

## Explanation of the json files
For a better understanding of the cards and the board's functionalities, please check the file under /deliverables named JsonsReadMe.pdf
Common Goal Card are customizable. However, in order to add a new card, it is mandatory to add its graphic to the code.

## Additional functionalities
We decided to implement a logger to keep track of the server and client activities. If you want to use the logger, then you need to add the following command when launching a .jar:

```bash
java -Dlog4j.configurationFile=path\log4j2-client.xml -jar ClientExecutable.jar
```

or

```bash
java -Dlog4j.configurationFile=path\log4j2-server.xml -jar ServerExecutable.jar
```

The configuration files for the logger are inside the /etc folder. Please, be mindful of the path from which you are launching the .jar file.

## Final notes
It could be possible that Windows terminals are not able to display correctly the colors of the title screen. 
We suggest to write this in the command line beforehand:
```bash
reg add hkcu\console /f /v VirtualTerminalLevel /t REG_DWORD /d 1
```
and to start a new terminal after that. This should grant the best experience with the TUI.

NOTA: My Shelfie è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.
