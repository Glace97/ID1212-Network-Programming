package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


// Server recieves messages from one client and forwards messages to the other clients
// Has one thread for the listening socket, and a unique thread for each client
public class ChatServer {
    // Iterate through list to send message to other clients, sent by another client
    ArrayList<ClientTask> clients = new ArrayList<>();


    //ClientTask is the class implementing the Runnable interface
    //The ClientTask class includes everything that a client on a separate thread is able to do
    //Object is to be passed to thread constructor
    private class ClientTask implements Runnable {
        final int clientID;
        final Socket clientSocket; // reading/writing for this specific client through this specific socket
        final BufferedReader in;
        final PrintWriter out;

        ClientTask(Socket clientSocket, int clientID) throws IOException {
            this.clientID = clientID;
            this.clientSocket = clientSocket;
            this.out = new PrintWriter(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                String message;
                while (clientSocket.isConnected()) {

                    //message is recieved by user input through to the clientSocket
                    message = in.readLine();
                    //  System.out.println("DEBUG: Inside of run: Sent by " + message);
                    forwardMessage(message); //forward the message to other clients
                }

            } catch (IOException e) {
                System.out.println(e);
                removeClient();
            }


        }

        public void forwardMessage(String message) {
            for (int i = 0; i < clients.size(); i++) {
                ClientTask client = clients.get(i);

                if (client.clientID != clientID) {
                    System.out.println("DEBUG: inside of forwardmessage: " + clientID + " to " + client.clientID);
                    //write the message to the other user
                    try {
                        client.out.write(message + '\n');
                        client.out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void removeClient() {
            //remove self from clients and close all streams
            try {
                System.out.println("User left the chat");
                clients.remove(this);
                out.close();
                in.close();
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void startChatServer(int portNum) throws IOException {
        // The serverSocket is used to listen for client request for connection to server
        ServerSocket serverSocket = new ServerSocket(portNum);

        //keep track of client to differentiate between other clients
        int clientID = 0;

        while (!serverSocket.isClosed()) {
            // Accept client request and send/recieve data through this socket
            Socket clientSocket = serverSocket.accept();
            System.out.println("New incoming client. Client:" + clientSocket.getInetAddress()); //will be the same adress as running from this computer

            // One thread for each client currently connected to server
            // Runnable task to be run in separate thread for client
            // The task contains whatever a client is able to do in a separate thread
            ClientTask clientTask = new ClientTask(clientSocket, clientID);
            clients.add(clientTask); //add the client to list of clients
            clientID++;

            //start the client in a separate thread
            Thread thread = new Thread(clientTask);
            thread.start();

        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.startChatServer(8080);
    }
}

