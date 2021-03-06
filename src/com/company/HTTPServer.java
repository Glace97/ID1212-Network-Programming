package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;

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
            Socket client = serverSocket.accept(); //client request

            // Set I/O streams
            request = new BufferedReader(new InputStreamReader(client.getInputStream()));
            response = new PrintWriter(client.getOutputStream());

            // Check if the client already has a session
            // Extract the sessionID for session
            StringBuilder sb = new StringBuilder();
            String line = request.readLine();
            sb.append(line);

            if(line.contains("GET /favicon.ico HTTP/1.1")){
                //Skip the favicon requests
                continue;
            }

            String[] cookieHeader;
            String[] keyVal;
            String guess = "0";
            //sessionID is overwritten if the request if from an existing guessing game
            int sessionID = 0;
            int contentLength = 0;

           //only reads headers
            while(!line.isEmpty()) {
                System.out.println(line);

                if (line.contains("Cookie: ")) {
                    cookieHeader = line.split(" ");
                    keyVal = cookieHeader[1].split("=");
                    sessionID = Integer.parseInt(keyVal[1]);
                }

                //if header indicates there are more bytes to read
                if (line.contains("Content-Length:")){
                    contentLength = Integer.parseInt(line.split(" ")[1]); //length of body
                    System.out.println("The content length: " + contentLength);
                }

                line = request.readLine();;
            };

            // read post body
            if(contentLength != 0) {
                char[] body = new char[contentLength];
               // System.out.println("before read body");
                request.read(body, 0, contentLength);
                String postBody = new String(body); //Guess=<gissning>
               // System.out.println(postBody);
                guess = postBody.split("=")[1];
                System.out.println(guess);
            }

            response.println("HTTP/1.1 200 OK");
            response.println("Content-Type: text/html");

            //Either creates a new session or returns the existing one
            Guess session = handleSession(sessionID, guess);

            response.println("Content-Length: " + session.index.length());
            response.println("\r\n");

            response.println(session.index);
            response.flush();

            //close the connection
            request.close();
            response.close();
            client.close();
        }
    }


    // returns a an existing session or a completely new one
    public Guess handleSession(int sessionID, String stringGuess) {
        Guess session;
        int guess = Integer.parseInt(stringGuess); //parse to int

        // if the sessionID is overwritten, the request must be coming from an existing guessing game
        if (sessionID != 0) {
            //plocka ut gissningen h??rif??n?
            for (Guess existingSession : sessions) {
                if (existingSession.sessionID == sessionID) {
                    session = existingSession;

                    //test the guess. Guess class updates html to be shown
                    session.guess(guess);

                    //check if the user won the game
                    if(session.finishedGame) {
                        System.out.println("finished game remove cookie");
                        response.println("Set-Cookie: sessionsID=deleted; path=/; domain=localhost; Max-Age=0;");  //old expiration date on cookie
                        sessions.remove(session);
                        //response.println("Set-Cookie: testcookie=2");
                    } else {
                        response.println("Set-Cookie: sessionID=" + sessionID);
                    }

                    return session;
                }
            }
        }

        //Else, this must be a completey new hame
        response.println("Set-Cookie: sessionID=" + nextSessionID);

        //Generate a new session
        session = new Guess(nextSessionID);
        sessions.add(session);
        nextSessionID++;
        return session;

    }

    public static void main(String[] args) throws IOException {
        HTTPServer guessingGame = new HTTPServer(8080);
        guessingGame.runServer();
    }

}
