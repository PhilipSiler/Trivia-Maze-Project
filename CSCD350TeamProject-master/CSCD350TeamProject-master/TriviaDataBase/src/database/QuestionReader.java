package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuestionReader {

    private final static DatabaseConnection DB_CONNECTION = DatabaseConnection.getInstance();

    public static void main(String[] args) throws FileNotFoundException {
    System.out.println("In Question Reader.");

    File file = new File("Questions.txt");
    Scanner sc = new Scanner(file);
    String[] question = null;
    int counter = 1;
    while (sc.hasNextLine()){
        question = sc.nextLine().split(" ! ");
        if (question.length == 6)
            addQuestionMC(question, counter);
        else if (question.length == 3){
            if (question[1].equalsIgnoreCase("t") || question[1].equalsIgnoreCase("f"))
                addQuestionTF(question, counter);
            else
                addQuestionSA(question, counter);
        }
        counter ++;
    }//end while loop
    sc.close();
}

	private static void addQuestionSA(String[] question, int counter) {
        //System.out.println("Adding Question SA.");
        int difficulty = 0;
        int roomType = 0;
        int questionType = 2;

        String qid = "";
        if (counter <= 32) {
            difficulty = 0;
        }
        if (counter > 32 && counter <= 64) {
            difficulty = 1;
        }
        if (counter > 64 && counter <= 96) {
            difficulty = 2;
        }

        Question q = new Question(difficulty, roomType, questionType, question[0], question[1], null, null, null, question[2]);
        DB_CONNECTION.addQuestion(q);
        //System.out.println(q);
    }

    private static void addQuestionTF(String[] question, int counter) {
        int difficulty = 0;
        int roomType = 0;
        int questionType = 0;


        if (counter <= 32){
            difficulty = 0;
        }
        if (counter > 32 && counter <= 64){
            difficulty = 1;
        }
        if (counter > 64 && counter <= 96){
            difficulty = 2;
        }

        Question q = new Question(difficulty, roomType, questionType, question[0], question[1], null, null, null, question[2]);
        DB_CONNECTION.addQuestion(q);
        //System.out.println(q);
    }

    private static void addQuestionMC(String[] question, int counter) {
        int difficulty = 0;
        int roomType = 0;
        int questionType = 1;

        if (counter <= 32){
            difficulty = 0;
        }
        if (counter > 32 && counter <= 64){
            difficulty = 1;
        }
        if (counter > 64 && counter <= 96){
            difficulty = 2;
        }


        Question q = new Question(difficulty, roomType, questionType, question[0], question[1], question[2], question[3], question[4], question[5]);
        DB_CONNECTION.addQuestion(q);
        //System.out.println(q);
    }

}
