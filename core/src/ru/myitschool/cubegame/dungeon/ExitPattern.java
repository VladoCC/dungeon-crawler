package ru.myitschool.cubegame.dungeon;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 18.01.2018.
 */
public class ExitPattern {

    int[] statement;
    int roomWidth;
    int roomHeight;
    Integer[][] cells;

    public ExitPattern(int[] statement, int roomWidth, int roomHeight, Integer[][] cells) {
        this.statement = statement;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.cells = cells;
    }

    public int[] getStatement() {
        return statement;
    }

    public Integer[][] getCells() {
        return cells;
    }
}
