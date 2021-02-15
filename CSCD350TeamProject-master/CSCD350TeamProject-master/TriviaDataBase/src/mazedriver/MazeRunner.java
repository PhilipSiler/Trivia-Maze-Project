package mazedriver;


/*
Author: Kevin Underwood
Class: CSCD350
version 1.6
attributions: took BFS from an old homework assignment and retrofitted it for this purpose
Revisor 1.6: Kevin Underwood
Revisor 1.4
Notes: Cheats are simple allows the user to just run through and test the movement and function of the maze.
        Its activated on creation of the maze on creation
 */


import database.DatabaseConnection;
import database.QuestionMenu;
import savestate.SaveState;

import java.util.*;

public class MazeRunner {


    public static void main(String[] args) throws Exception {
        boolean gameOver;
        String t = "y";
        Scanner kb = new Scanner(System.in);
        do {
            Maze maze = menu(kb);

            playGame(maze);
            kb.nextLine();
            System.out.println("keep playing? y/n");
            String temp = kb.nextLine();
            gameOver = t.equalsIgnoreCase(temp);
        } while (gameOver);
    }


    //I believe that this is where the issue happens
    private static void playGame(Maze myMaze) throws Exception {
        Scanner kb = new Scanner(System.in);
        Player player1 = new Player();
        Room[][] tempmaze = myMaze.getMaze();
        SaveState state = new SaveState();

        int doorStatus; //default value

        do {
            System.out.println(myMaze.toString());
            String choice = gameMenu(kb);

            if(choice.equalsIgnoreCase("e")) {
                state.saveState(myMaze.getMaze(), player1.getPos(), DatabaseConnection.getInstance().getHashTableCursor());
            }else if(choice.equalsIgnoreCase("q")) {
                ArrayList<Object> dataList = state.loadState();

                myMaze = MazeBuilder.loadMaze((Room [][]) dataList.get(0)); //first item is the room array
                player1.setPos((int [])dataList.get(1));
                DatabaseConnection.getInstance().setHashTableCursor((int)dataList.get(2));
                tempmaze = myMaze.getMaze();

            }else if(choice.equalsIgnoreCase("r")) {
                QuestionMenu.menu();

            }else {
                doorStatus = myMaze.touchDoor(choice, player1);
                if (doorStatus < 3) {
                    //door ask stuff
                    if (doorStatus <= 1) {
                        if (doorStatus == 0) {
                            if (myMaze.answerQuestion(kb)) { //correct answer, door updated in player
                                player1.movePlayer(choice, tempmaze);
                                myMaze.setMaze(tempmaze);
                            } else {
                                myMaze.lock(choice, player1);
                                System.out.println("The lock clicks...\n" + "You know that door will never open again");
                            }
                        } else if (doorStatus == 1) {
                            player1.movePlayer(choice, tempmaze);
                            myMaze.setMaze(tempmaze);
                        }
                    } else{
                        System.out.println("The doors locked!\n" + "I better find another way round!");
                    }
                } else
                    System.out.println("That is a wall humble adventurer.");
            }
        } while(!Arrays.equals(player1.getPos(), myMaze.getExitPos()) && BFS(tempmaze,player1));

        System.out.println(myMaze.toString());
        int[] temp = player1.getPos();
        if (temp[0] == 4 && temp[1] == 4){
            System.out.println("You win!");
        }else
            System.out.println("You lose.");
    }


    //updated game menu
    private static String gameMenu(Scanner kb) {
        String game_choice;
        boolean bool = true;
        do {
            System.out.println("            [Q] " + " [W] " + " [E]  [R]\n" +
                    "            [A] " + " [S] " + " [D]\n" +
                    "  load      | " + " move up " + "  | save  |  admin tool\n" +
                    "  move left |" + " move down " + " | move right\n");
            game_choice = kb.nextLine();

            if (game_choice.equalsIgnoreCase("a") || game_choice.equalsIgnoreCase("w") || game_choice.equalsIgnoreCase("s") || game_choice.equalsIgnoreCase("d") || game_choice.equalsIgnoreCase("e") || game_choice.equalsIgnoreCase("q")|| game_choice.equalsIgnoreCase("r")) {
                bool = false;
            }
        } while (bool);
        return game_choice;
    }


    private static Maze menu(Scanner kb) {
        MazeBuilder factory = new MazeBuilder();
        System.out.println("Welcome to Maze Tester!\n");
        System.out.println("play with cheats?\n"+"(y/n)");
        String cheats = kb.nextLine();

        return factory.newMaze(cheats);
    }


    public static boolean BFS(Room[][] tempmaze, Player player1) {
        int xValue = tempmaze.length;
        int yvalue = tempmaze[0].length;
        int[] temp =player1.getPos();
        boolean[][] visited = new boolean[xValue][yvalue];

        Queue queue = new LinkedList<>();
        queue.add(temp[0] + "/" + temp[1]);

        while (!queue.isEmpty()) {

            String x = (String) queue.remove();
            int row = Integer.parseInt(x.split("/")[0]);
            int col = Integer.parseInt(x.split("/")[1]);

            if (row >= 0 && col >= 0 && row < xValue && col < yvalue && !visited[row][col]) {
                visited[row][col] = true;
                if (tempmaze[row][col].getWestdoor() < 2)
                    queue.add(row + "/" + (col - 1)); //go west
                if (tempmaze[row][col].getEastdoor() < 2)
                    queue.add(row + "/" + (col + 1)); //go east
                if (tempmaze[row][col].getNorthdoor() < 2)
                    queue.add((row - 1) + "/" + col); //go north
                if (tempmaze[row][col].getSouthdoor() < 2)
                    queue.add((row + 1) + "/" + col); //go south
                if (visited[4][4])
                    return true;
            }
        }
        return false;
    }
}
