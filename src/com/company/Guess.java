package com.company;


import java.util.Random;

//Contains the guessing game logic
public class Guess {
    int numOfGuesses;
    int secretNum;
    int clientID;
    String index; //dynamically decide which page to show user

    public Guess(int cliendID) {
        this.secretNum = generateSecreteNum(); //"unique" for this session
        this.clientID = cliendID;

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
                "    <form>\n" +
                "        <input type=\"submit\">\n" +
                "    </form>\n" +
                "</body>\n" +
                "</html>";

        this.index = newGame;
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
