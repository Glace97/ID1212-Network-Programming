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
    static int nextSessionID;

    public HTTPServer(int portNum) throws IOException {
        serverSocket = new ServerSocket(portNum);
        System.out.println("GuessingGame server up and running!");
        nextSessionID = 1;
        sessions = new ArrayList<>();
    }

    public void runServer() throws IOException {

        // Wait for new clients to connect to the game
        // Naive approach, this does not scale and only handles one client a time
        while (!serverSocket.isClosed()) {
            System.out.println("Waiting for client...");
            Socket client = serverSocket.accept(); //client request
            System.out.println("Client connected.");

            // Set I/O streams
            request = new BufferedReader(new InputStreamReader(client.getInputStream()));
            response = new PrintWriter(client.getOutputStream());


            // Check if the client already has a session
            // Extract the sessionID for session
            String line;
            String[] cookieHeader;
            String[] keyVal;
            int sessionID = 0;
            while (!(line = request.readLine()).isEmpty()) {

                if(line.contains("Guess:"))

                if (line.contains("Cookie:")) {
                    cookieHeader = line.split(" ");
                    keyVal = cookieHeader[1].split("=");
                    sessionID = Integer.parseInt(keyVal[1]);
                }
            }

            //hämta gamelogik isf


            //TODO: skippa favicon requesten (den ska inte renderera någonting nytt;



            // might be overwritten with a existing session, might be a new one
            Guess session;

            response.println("HTTP/1.1 200 OK");
            response.println("Content-Type: text/html");
            response.println("Content-Length: " + index.length());

            if (sessionID == 0) {
                // The first session for the client. Set cookie with next available sessionID
                response.println("Set-Cookie: sessionID=" + nextSessionID);

                //Generate a new session
                session = new Guess(nextSessionID);
                sessions.add(session);

                nextSessionID++;
            } else {
                // Assign the original cookie
                response.println("Set-Cookie: sessionID=" + sessionID);

                //plocka ut gissningen härifån?
                for (Guess session : sessions) {
                    if (session.sessionID == sessionID) {
                        //Vi har rätt session, kolla om vi har rätt gissnig



                        String index = session.index;
                    }
                }
            }
        }

        response.println("\r\n");
        //TODO: printa Set-Cookie headern

        //TODO: printa strängar nödvändiga för gamelogiken

        //TODO: printa form
        response.println(index);
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
