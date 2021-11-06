package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


// Server recieves messages from one client and forwards messages to the other clients
public class ChatServer {
    ServerSocket serverSocket;
    ArrayList<ClientTask> clients;

    public ChatServer(ServerSocket socket) {
        this.serverSocket = socket;
    }

    public void startChatServer() throws IOException {
        // Iterate through list to send message to other clients, sent by another client
        clients = new ArrayList<>();

        //keep track of client to differentiate between other clients
        int clientID = 0;

        while (!serverSocket.isClosed()) {
            // Recieve and send data from and to client through this socket
            Socket clientSocket = serverSocket.accept();

            // One thread for each client currently connected to server
            // task to be run in separate thread for client
            ClientTask clientTask = new ClientTask(clientSocket, clients, clientID);
            clients = clientTask.clients; // Now includes the latest added client
            clientID++;

            Thread thread = new Thread(clientTask);
            thread.start();

        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ChatServer chatServer = new ChatServer(serverSocket);
        chatServer.startChatServer();
    }
}

