package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

// Has two threads, one for listening for incoming messages from the server, and one to send messages to server
public class ChatClient {
    String userName;
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    ChatClient(String userName ) throws IOException {
        this.userName = userName;
        //Connect to the ChatServer
        socket = new Socket("localhost", 8080);

        //write to the server through outputstream
        out = new PrintWriter(socket.getOutputStream());
    }

    public void sendMessage() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi " + userName + ". Type a message:");
        while(socket.isConnected()) {
            // Take user input
            String message = sc.nextLine();
            out.write(userName + ": " + message);
            out.flush();
        }
    }

    //listens to messages being sent by other clients, needs to be done on a separate thread (blocking op)
    public void receiveMessage() throws IOException {
        Runnable listen = new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        System.out.println(in.readLine()); //print message sent by other clients through server
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        //Start thread with runnable task
        Thread thread = new Thread(listen);
        thread.start();

    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hi, please enter your name:");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        ChatClient chatClient = new ChatClient(userName);

        //Both runs in parallell
        chatClient.sendMessage();
        chatClient.receiveMessage();

    }


}
