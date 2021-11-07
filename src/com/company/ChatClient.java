package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Has two threads, one for listening for incoming messages from the server, and one to send messages to server
public class ChatClient {
    String userName;

    ChatClient(String userName) {this.userName = userName;}

    public void sendMessage(int portNr) throws IOException {

        //Connect to the ChatServer
        Socket socket = new Socket(portNr);
        //Write to the server through outputstream
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        Scanner sc = new Scanner(System.in);
        while(socket.isConnected()) {
            // Take user input
            String message = sc.nextLine();
            out.write(userName + ": " + message);
            out.flush();
        }
    }

    //listens to messages being sent by other clients
    public void receiveMessage() {
        
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        chatClient.sendMessage(8080);
    }

}
