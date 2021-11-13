package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HTTPServer {
    ServerSocket serverSocket;
    BufferedReader request;
    PrintWriter response;
    ArrayList<Guess> sessions;
    static int clientID;

    public HTTPServer(int portNum) throws IOException {
        serverSocket = new ServerSocket(portNum);
        System.out.println("GuessingGame server up and running!");
        clientID = 0;
        sessions = new ArrayList<>();
    }

    public void runServer() throws IOException {

        // Wait for new clients to connect to the game
        // Naive approach, this does not scale and only handles one client a time
        while(!serverSocket.isClosed()) {
            System.out.println("Waiting for client...");
            Socket client = serverSocket.accept(); //client request
            System.out.println("Client connected.");

            // Set I/O streams
            request = new BufferedReader(new InputStreamReader(client.getInputStream()));
            response = new PrintWriter(client.getOutputStream());

            String startLine = request.readLine();  // [<method> <target> <protocol version>]

            //String startLine = "GET /background.png HTTP/1.0"; //example string for testing
            String[] tokens = startLine.split(" ");

            String method = tokens[0]; //should contain GET or POST

            response.write("HTTP/1.1 200 OK\r\n\r\n");
            response.write("hello");
            response.write("\r\n");
            response.flush();

            //close the connection
            request.close();
            response.close();
            client.close();
        }

    }

    public static void main(String[] args) throws IOException {
        HTTPServer guessingGame = new HTTPServer(8080);
        guessingGame.runServer();
    }

}
