package com.company;


import java.util.Random;

//Contains the guessing game logic
public class Guess {
    int numOfGuesses;
    int secretNum;
    int sessionID;
    String index; //dynamically decide which page to show user

    public Guess(int cliendID) {
        this.secretNum = generateSecreteNum(); //"unique" for this session
        this.sessionID = cliendID;

        //test HTML
        //NewGame
        String newGame =
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Guessing Game</title>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "       <text>Welcome to the number guess game.Im thinking of a number between 1 and 100. What's your guess?</text>\n" +
                "       <form method=\"post\" action=\"HTTPServer.java\">\n" +
                "           <input type=\"text\" name=\"Guess\">\n" +
                "       </form>\n" +
                "       <form>\n" +
                "       <input type=\"submit\" value=\"send\">\n" +
                "       </form>\n" +
                "</body>\n" +
                "</html>";

        this.index = newGame;
    }

    /*TODO:
    *   1. en indexformat om man gissar fel --> boolean om man gissat för lågt/högt
    *   2. indexformat om man gissar rätt --> ska printa ut numOfGuesses
    * */

    public int generateSecreteNum() {
        Random random = new Random();
        int secretNum = random.nextInt(100);

        //random is 0 inclusive, our range should be 1-100
        if(secretNum == 0) {
            secretNum = 1;
        }
        return secretNum;
    }
}
