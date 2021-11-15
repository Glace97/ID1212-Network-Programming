package com.company;


import java.util.Random;

//Contains the guessing game logic
public class Guess {
    int attempts = 0;
    int secretNum;
    int sessionID;
    String index; //dynamically decide which page to show user
    boolean finishedGame = false;

    public Guess(int cliendID) {
        this.secretNum = generateSecreteNum(); //"unique" for this session
        //this.secretNum = 50;
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
                "       <text>Welcome to the number guess game. Im thinking of a number between 1 and 100. What's your guess?</text>\n" +
                "       <form action=\"http://localhost:8080/\" method=\"post\" >\n" +
                "           <input type=\"text\" name=\"Guess\">\n" +
                "           <input type=\"submit\" value=\"send\">\n" +
                "       </form>\n" +
                "</body>\n" +
                "</html>";

        this.index = newGame;
    }

    /*TODO:
    *   1. en indexformat om man gissar fel --> boolean om man gissat för lågt/högt
    *   2. indexformat om man gissar rätt --> ska printa ut numOfGuesses
    * */


    public void guess(int guess) {
        attempts++;
        String hint = ""; // hint the user if they should guess higher or lower

        if(guess < secretNum) {
            hint = "higher";
        } else if (guess > secretNum){
            hint = "lower";
        }

        if(guess != secretNum) {
            //Nope guess <hiny> You have made <attempts> guess(es)
            this.index =
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Guessing Game</title>\n" +
                    "\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "       <text>Nope, guess " + hint +  ". You have made " + attempts + "guess(es). What's your guess?</text>\n" +
                    "       <form action=\"http://localhost:8080/\" method=\"post\" >\n" +
                    "           <input type=\"text\" name=\"Guess\">\n" +
                    "           <input type=\"submit\" value=\"send\">\n" +
                    "       </form>\n" +
                    "</body>\n" +
                    "</html>";
        }
        if (guess == secretNum) {
            finishedGame = true;
            this.index =
                    "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Guessing Game</title>\n" +
                    "\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "       <text>You made it in " + attempts + "guess(es). Press button to try again.</text>\n" +
                    "       <form>\n" +
                    "           <input type=\"submit\" value=\"New game\">\n" +
                    "       </form>\n" +
                    "</body>\n" +
                    "</html>";
        }


    }

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
