# FTP

Distributed Computing Systems (Spring 2017 CSCI 6780) - Project 1

Team Members: Prasad Mate, Sharmin Pathan

Technologies Used: Java 1.8 

Problem Statement: To design and implement simple FTP Client and Server

Commands:
---------
We have implemented the following FTP commands. The syntax of the command is indicated in the parenthesis.
- get (get remote_filename) -- Copy file with the name <remote_filename> from remote directory to local directory.
- put (put local_filename) -- Copy file with the name <local_filename> from local directory to remote directory.
- delete (delete remote_filename) – Delete the file with the name <remote_filename> from the remote directory.
- ls (ls) -- List the files and subdirectories in the remote directory.
- cd (cd remote_directory_name or cd ..) – Change to the <remote_direcotry_name > on the remote machine or change to the parent directory of the current directory
- mkdir (mkdir remote_directory_name) – Create directory named <remote_direcotry_name> as the sub-directory of the current working directory on the remote machine.
- pwd (pwd) – Print the current working directory on the remote machine.
- quit (quit) – End the FTP session.

FTP Server (myftpserver):
------------------------
The server program takes a single command line parameter, which is the port number where the server will execute. Once the myftpserver program is invoked, it creates a TCP socket, binds, listens and blocks on accept (for incoming connections). Appropriate error messages are displayed in case any of the above fail. When a client connection arrives, it starts accepting commands and executes them. Appropriate error messages are communicated to the client upon failure of commands. Upon receiving the quit command, the server closes the connections, does all housekeeping and is ready to accept another connection. The directory where the server program resides is the current working directory for each incoming client connection (i.e., if the first command entered by a client is “pwd” it sees the path of the directory where the server program resides).

FTP Client (myftp):
------------------
The ftp client program takes two command line parameters, the machine name where the server resides and the port number. Once the client starts up, it displays a prompt “mytftp>”. It then accepts and executes commands by relaying the commands to the server and displays results and error messages where appropriate. The client terminates when the user enters the “quit” command.

Execution:
---------
The project is developed using Eclipse.
- To execute myftpserver, goto bin directory and run 'java com.ds.server.myftpserver portNumber'
- To execute myftp, goto bin directory and run 'java com.ds.client.myftp ipAddress portNumber'

Note: This project was done in its entirety by Prasad Mate and Sharmin Pathan. We hereby state that we have not received unauthorized help of any form. The project description is taken from the description provided in CSCI 6780.

