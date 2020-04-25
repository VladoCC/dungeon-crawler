package ru.myitschool.dcrawler.dungeon;

/**
 * Created by Voyager on 18.01.2018.
 */
public class ExitPattern {

    private int[] statement;
    private int roomWidth;
    private int roomHeight;
    private Integer[][] cells;
    private String name;

    public ExitPattern(int[] statement, int roomWidth, int roomHeight, Integer[][] cells) {
        this.statement = statement;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.cells = cells;
    }

    public ExitPattern(int[] statement, int roomWidth, int roomHeight, Integer[][] cells, String name) {
        this.statement = statement;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.cells = cells;
        this.name = name;
    }

    public ExitPattern(int[] statement, int roomWidth, int roomHeight, String name) {
        this.statement = statement;
        this.roomWidth = roomWidth;
        this.roomHeight = roomHeight;
        this.name = name;
    }

    public ExitPattern(int[] statement, int roomWidth, int roomHeight) {
        this(statement, roomWidth, roomHeight, new Integer[roomWidth][roomHeight]);
    }

    public int[] getStatement() {
        return statement;
    }

    public Integer[][] getCells() {
        return cells;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
