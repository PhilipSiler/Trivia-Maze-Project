package mazedriver;

import inputscrubber.Scrubber;
import database.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/*
Author: Kevin Underwood
Class: CSCD350
version 1.5
Revisor: Damien Rodriguez

 */
public class Maze implements Serializable {




    private final int size;
    private Room[][] maze;
    private int[] exitPos;

    public Maze(int size, int dif, String cheats) {
        DatabaseConnection databaseconnection = DatabaseConnection.getInstance();
        this.size =size;
        Room[][] maze = new Room[size][size];

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                maze[row][column] = makeRoom(row, column);
                if(cheats.equalsIgnoreCase("y")) {
                    maze[row][column].setSouthdoor(1);
                    maze[row][column].setNorthdoor(1);
                    maze[row][column].setEastdoor(1);
                    maze[row][column].setWestdoor(1);
                }
            }
        }

        exitPos = new int[]{4, 4};
        setMaze(maze);
    }

    public Maze(Room[][] savedRooms) {
        setMaze(savedRooms);
        this.size = savedRooms.length;
        exitPos = new int[]{4, 4};
    }


    public int[] getExitPos(){
        return this.exitPos;
    }


    public void setMaze(final Room[][] maze) {
        this.maze = maze;
    }


    public Room[][] getMaze() {
        return maze;
    }


    private Room makeRoom(int x, int y) {
        int[] coords = new int[]{x, y};
        return new Room(coords);
    }


    @Override
    public String toString() {
        StringBuilder strTop = new StringBuilder();
        StringBuilder strMid = new StringBuilder();
        StringBuilder strBot = new StringBuilder();
        StringBuilder strFinal = new StringBuilder();

        for (Room[] rooms : maze) {

            for (int y = 0; y < maze.length; y++) {
                String[] temp = rooms[y].toString().split("%");
                strTop.append(temp[0]).append(" ");
                strMid.append(temp[1]).append(" ");
                strBot.append(temp[2]).append(" ");
            }

            strFinal.append(strTop);
            strFinal.append("\n");

            strFinal.append(strMid);
            strFinal.append("\n");

            strFinal.append(strBot);
            strFinal.append("\n");

            strTop = new StringBuilder();
            strMid = new StringBuilder();
            strBot = new StringBuilder();

        }
        return strFinal.toString();
    }


    //code below works as intended.
    public boolean answerQuestion(Scanner kb) {

        String[] answer;
        //Pull a question from the database
        Question question = DatabaseConnection.getInstance().pullQuestion();

        //stubbed for testing waiting for Damiens database.


        if((question.getWrongAnswerOne() == null || question.getWrongAnswerOne().equalsIgnoreCase("null")) && question.getQuestion().length() == 1)
            return askTFQuestion(question);

        if((question.getWrongAnswerOne() == null || question.getWrongAnswerOne().equalsIgnoreCase("null")))
            return askSAQuestion(question);

        //problem determining short answer questions
        if(question.getWrongAnswerOne() != null)
            return askMCQuestion(question);



        System.out.println("answer question method not working as intended and no if statements were entered.");
        return false;
    }


    private String[] makeAnswerArray(final Question question) {
        String[] answerArray;

        if(question.getWrongAnswerTwo() != null) {
            answerArray = new String[4];
            answerArray[0] = question.getAnswer();
            answerArray[1] = question.getWrongAnswerOne();
            answerArray[2] = question.getWrongAnswerTwo();
            answerArray[3] = question.getWrongAnswerThree();
        }

        //If it is a t/f or short answer question
        else {
            answerArray = new String[1];
            answerArray[0] = question.getAnswer();
        }

        return answerArray;
    }


    private int findAnswerInArray(final Question question, final String[] answerArray) {
        for(int i = 0; i < answerArray.length; i++) {
            if(answerArray[i].equals(question.getAnswer()))
                return i;
        }
        return -1;
    }


    private boolean askMCQuestion(final Question question) {

        //Display Question
        System.out.println(question.getQuestion());

        //Get answer Array
        String[] answerArray = makeAnswerArray(question);
        Collections.shuffle(Arrays.asList(answerArray));

        //Display answer options
        for(int i = 0; i < answerArray.length; i++) {
            System.out.println((i + 1) +") " + answerArray[i]);
        }

        System.out.println("Please enter a number between 1 and 4");
        System.out.print("----------->");
        Scanner kb = new Scanner(System.in);
        int answer = Scrubber.intScrubber(kb.nextLine(), kb);

        int indexOfAnswer = findAnswerInArray(question, answerArray);


        return indexOfAnswer == (answer - 1);
    }


    private boolean askTFQuestion(final Question question) {

        //Display Question
        System.out.println(question.getQuestion());

        //Get answer Array
        String[] answerArray = makeAnswerArray(question);

        //Display answer options

        System.out.println("Please enter either a 'T' or 'F'");
        System.out.print("----------->");
        Scanner kb = new Scanner(System.in);

        char input = Scrubber.charScrubber(kb.nextLine(), kb);
        Character.toLowerCase(input);

        char answer = question.getAnswer().charAt(0);
        Character.toLowerCase(answer);

        return answer == input;
    }


    private boolean askSAQuestion(final Question question) {

        //Display Question
        System.out.println(question.getQuestion());

        //Get answer Array
        //Display answer options

        System.out.println("Please enter the answer");
        System.out.print("-----------> ");
        Scanner kb = new Scanner(System.in);

        //Get user Input
        String[] userAnswer = Scrubber.shortScrubber(kb.nextLine(), kb);
        String[] actualAnswer = question.getAnswer().split(" ");

        //make sure all lower case
        for(int i = 0; i < userAnswer.length; i++) {
            userAnswer[i] = userAnswer[i].toLowerCase();
        }

        for(int i = 0; i < actualAnswer.length; i++) {
            actualAnswer[i] = actualAnswer[i].toLowerCase();
        }

        return Arrays.equals(userAnswer, actualAnswer);
    }

//DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE DAMIEN LOOK HERE
//These methods were a bit ambigous, and I didn't understand how to use them, so I expanded the askQuestion method. Hopefully it all works.

    //get the door of that particular direction.
    //returned int provides status of the door.
    // 0 - Untouched door that will ask player question
    // 1 - Question was answered successfuly, and door is now open
    // 2 - Question was answered unsuccessfuly, and door is now locked forever
    // 3 - Wall.

    public int touchDoor(String c, Player player1) {

        int[] temp = player1.getPos();
        if (c.equalsIgnoreCase("w")) {       //getting north Door status
            return maze[temp[0]][temp[1]].getNorthdoor();
        } else if (c.equalsIgnoreCase("s")) {//getting south Door status
            return maze[temp[0]][temp[1]].getSouthdoor();
        } else if (c.equalsIgnoreCase("d")) {//getting east Door status
            return maze[temp[0]][temp[1]].getEastdoor();
        } else if (c.equalsIgnoreCase("a")){ //getting west door status
            return maze[temp[0]][temp[1]].getWestdoor();
        }

        return 3; //wall

    }


    public void lock(String c, Player player1) {
        int[] temp = player1.getPos();
        if (c.equalsIgnoreCase("w")) { //move up
            maze[temp[0]][temp[1]].setNorthdoor(2);
        } else if (c.equalsIgnoreCase("s")) {//move down
            maze[temp[0]][temp[1]].setSouthdoor(2);
        } else if (c.equalsIgnoreCase("d")) {//move right
            maze[temp[0]][temp[1]].setEastdoor(2);
        } else if (c.equalsIgnoreCase("a")) {//move left
            maze[temp[0]][temp[1]].setWestdoor(2);

        }else System.out.println("Problem in lock");

    }

}
