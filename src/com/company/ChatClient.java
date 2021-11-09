package com.company;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

// Has two threads, one for listening for incoming messages from the server, and one to send messages to server
public class ChatClient {
    String userName;
    Socket socket;
    PrintWriter out;
    BufferedReader in;


    ChatClient(String userName) throws IOException {
        try {
            this.userName = userName;
            //Connect to the ChatServer
            socket = new Socket("localhost", 8080);

            //write to the server through outputstream
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("i konstruktor   error");
            disconnect();
        }
    }

    public void sendMessage() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Hi " + userName + ". Type a message:");
        try {
            while (socket.isConnected()) {
                // Take user input
                String message = sc.nextLine();
                out.write(userName + ": " + message + '\n'); //include newLine, else readLine in server blocks
                out.flush();
            }
        } catch (Exception e) {
            System.out.println("Lost connection to server. Disconnect from chat");
            disconnect();
        }
    }

    //listens to messages being sent by other clients, needs to be done on a separate thread (blocking op)
    public void receiveMessage() throws IOException {
        try {
            String message;
            while (socket.isConnected()) {
                message = in.readLine();
                System.out.println(message); //print message sent by other clients through server
            }
        } catch (IOException e) {
            System.out.println("Lost connection to server. Disconnect from chat");
            disconnect();
        }

    }


    // start the listening thread for incoming messages and the sending thread separately
    public void startThreads() throws IOException {

        try {
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
        } catch (Exception e) {
            System.out.println("Lost connection to server. Disconnect from chat");
            disconnect();
        }
    }


    public void disconnect() throws IOException {
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("You just left the chat");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error when disconnecting");
            System.exit(1);
        }
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
