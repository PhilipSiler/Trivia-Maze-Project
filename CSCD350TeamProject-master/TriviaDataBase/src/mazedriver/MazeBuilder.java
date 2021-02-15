package mazedriver;


/*
Author: Kevin Underwood
Class: CSCD350
version 1.4

 */


public class MazeBuilder {

    public Maze newMaze(String cheats) {

            return new Maze(5,1,cheats);

    }


    public static Maze loadMaze(Room[][] rooms) {
        return new Maze(rooms);
    }

}
