package mazedriver;


/*
Author: Kevin Underwood
Class: CSCD350
version 1.2

 */


import java.io.Serializable;

public class Player implements Serializable {


    private static int[] pos = new int[]{0,0};




    public void movePlayer(String i, Room[][] tempmaze) {
        int temp;
        if (i.equalsIgnoreCase("w") && pos[0] > 0) { //move up
            tempmaze[pos[0]][pos[1]].setNorthdoor(1);
            temp = pos[0];
            pos[0] = temp - 1;
            tempmaze[pos[0]][pos[1]].setSouthdoor(1);


        }else if (i.equalsIgnoreCase("s") && pos[0] < 4) {//move down
            tempmaze[pos[0]][pos[1]].setSouthdoor(1);
            temp = pos[0];
            pos[0] = temp + 1;
            tempmaze[pos[0]][pos[1]].setNorthdoor(1);


        }else if (i.equalsIgnoreCase("d") && pos[1] < 4) {//move right
            tempmaze[pos[0]][pos[1]].setEastdoor(1);
            temp = pos[1];
            pos[1] = temp + 1;
            tempmaze[pos[0]][pos[1]].setWestdoor(1);


        } else if (i.equalsIgnoreCase("a") && pos[1] > 0) {//move left
            tempmaze[pos[0]][pos[1]].setWestdoor(1);
            temp = pos[1];
            pos[1] = temp - 1;
            tempmaze[pos[0]][pos[1]].setEastdoor(1);


        } else
            System.out.println("invalid, its a wall...");
    }


    public static int[] getPos() {
        return pos;
    }


    public void setPos(int[] pos) {
        Player.pos = pos;
    }
}