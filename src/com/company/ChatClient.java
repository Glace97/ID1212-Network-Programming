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
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi " + userName + ". Type a message:");
        while (socket.isConnected()) {
            // Take user input
            try {
                String message = sc.nextLine();
                out.write(userName + ": " + message + '\n'); //include newLine, else readLine in server blocks
                out.flush();
            } catch (Exception e) {
                System.out.println("You just left the chat");
             //   in.close();
                out.close();
               // socket.close();
            }
        }
    }

    //listens to messages being sent by other clients, needs to be done on a separate thread (blocking op)
    public void receiveMessage() throws IOException {
        System.out.println("inside of recieve message");
            String message;
            while (socket.isConnected()) {
                try {
                    message = in.readLine();
                    System.out.println("Recieves message: " + message);
                    System.out.println(message); //print message sent by other clients through server
                } catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                }
            }

    }


    // start the listening thread for incoming messages and the sending thread separately
    public void startThreads() throws IOException {
        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sendMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //start send message on separate thread
        send.start();

        //receive message runs on the main thread
        receiveMessage();
    }


    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
        System.out.println("You just left the chat");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hi, please enter your name:");
        Scanner sc = new Scanner(System.in);
        String userName = sc.nextLine();
        ChatClient chatClient = new ChatClient(userName);

        //Both runs in parallell
        chatClient.startThreads();
    }


}
