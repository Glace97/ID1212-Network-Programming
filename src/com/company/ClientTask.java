package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//ClientTask is the class implementing the Runnable interface
//The ClientTask class includes everything that a client on a separate thread is able to do
//Object is to be passed to thread constructor
public class ClientTask implements Runnable{
    PrintWriter output;
    BufferedReader input;
    Socket clientSocket;
    ArrayList<ClientTask> clients;
    int clientID;

    public ClientTask(Socket clientSocket, ArrayList<ClientTask>clients, int clientID) throws IOException {
        this.clientSocket = clientSocket;
        this.clientID = clientID;
        // OutputStream prints bytes, PrintWriter wrapper for writing text-output
        this.output = new PrintWriter(clientSocket.getOutputStream());
        this.input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    }

    @Override
    public void run() {
        String message;
        while(clientSocket.isConnected()) {
            try {
                message = input.readLine();
                forwardMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }
    }

    //loops through all clients and sends message
    public void forwardMessage(String message) {
        for(ClientTask client : clients) {
            if(client.clientID != clientID){
                client.output.write(message); //write the message through the stream to this client
                client.output.flush();
            }
        }
    }


}
