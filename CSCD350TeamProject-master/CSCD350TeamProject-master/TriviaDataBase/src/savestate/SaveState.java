package savestate;

import mazedriver.*;
import java.io.*;
import java.util.ArrayList;
/*
Author: Damien Rodriguez
Class: CSCD350
version 1.0
Revisor: Kevin Underwood
 */
public class SaveState {


    public void saveState(Room [][] savedRooms, int[] playerPos, int cursorLocation) {
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(savedRooms);
        data.add(playerPos);
        data.add(cursorLocation);

        try {
            FileOutputStream  fileOut = new FileOutputStream("data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
            System.out.println("Data saved.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Object> loadState() {

        ArrayList<Object> myList = new ArrayList<Object>();

        try {
            FileInputStream fin = new FileInputStream("data.ser");
            ObjectInputStream oin = new ObjectInputStream(fin);
            myList = (ArrayList<Object>)oin.readObject();
            System.out.println("Object successfully loaded.");
            oin.close();
            fin.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return myList;
    }
}
