package ru.myitschool.dcrawler.dungeon;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Predicate;
import com.google.gson.Gson;
import ru.myitschool.dcrawler.effects.FloorEffect;
import ru.myitschool.dcrawler.entities.CRTable;
import ru.myitschool.dcrawler.entities.Enemy;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.Target;
import ru.myitschool.dcrawler.tiles.DungeonTile;
import ru.myitschool.dcrawler.utils.AdvancedArray;
import ru.myitschool.dcrawler.utils.SeededRandom;

import java.awt.*;
import java.util.Random;

/**
 * Created by Voyager on 23.04.2017.
 */
public class Room {

    public static final int ANGEL_90 = 1;
    public static final int ANGEL_180 = 2;
    public static final int ANGEL_270 = 3;

    public static final int EXIT_SIZE = 2;

    public static final int DEFAULT_SIZE = 64;

    public static final float[] ENTITIES_PERCENT = {0.35f, 0.6f, 0.05f};

    private int width;
    private int height;
    private int openningPoints;
    private int exitCount = 0;
    private int x;
    private int y;
    private int monsterCount = 0;

    private boolean encounter;
    private boolean mobs = true;

    private Integer[][] cells;

    private static Gson gson = new Gson();
    private static Array<Entity> addingArray = new Array<Entity>();

    private Array<Exit> exits;
    private Array<Entity> entities = new Array<Entity>();
    private Array<ExitPattern> patterns;
    private Array<FloorEffect> effects = new Array<>();

    //private ArrayList<Integer> exitPositions = new ArrayList<Integer>();

    public Room(int width, int height, boolean first) {
        this.width = width;
        this.height = height;
        cells = new Integer[width][height];
        /*
        if (!first) {
            cells[width/2][0] = 0;
            cells[width/2 - 1][0] = 0;
        }*/
        //exitPositions.add(1);
        //exitPositions.add(2);
        //exitPositions.add(3);
        exits = new Array<Exit>(4);
        patterns = new Array<ExitPattern>();

        /*for (int i = 0; i < exitCount; i++) {
            System.out.println(Exit.canOpenDoor());
            if (Exit.canOpenDoor()) {
                addExit();
            }
        }*/

    }

    public Room(String pattern){
        this(gson.fromJson(new FileHandle(pattern).readString(), Room.class));
    }

    private Room(Room room){
        this.cells = room.cells;
        this.patterns = room.patterns;
        this.encounter = room.encounter;
        this.entities = room.entities;
        this.exitCount = room.exitCount;
        this.exits = room.exits;
        this.width = room.width;
        this.height = room.height;
        this.x = room.x;
        this.y = room.y;
        countOpenningPoints();
    }

    private void countOpenningPoints(){
        openningPoints = Math.max(width * height / DEFAULT_SIZE, 1);
    }

    public void addExits(Array<Integer> exitPositions){
        Random random = SeededRandom.getInstance();
        if (Exit.canOpenDoor()){
            exitCount = Math.max(random.nextInt(3) + 2, Exit.getExitsLeft());
        } /*else {
            exitCount = random.nextInt(4);
        }*/
        if (exitCount >  exitPositions.size){
            exitCount = exitPositions.size;
        }
        for (int i = 0; i < exitCount; i++) {
            int index = random.nextInt(exitPositions.size);
            int direction = exitPositions.get(index);
            exitPositions.removeIndex(index);
            addExit(direction);
        }
    }

    public void addExit(int direction) {
        Exit exit = new Exit(EXIT_SIZE, direction);
        addExit(direction, exit);
    }

   public void addExit(int direction, Exit exit) {
       if (direction == Exit.DIRECTION_NORTH){
           exit.addCell(new Point(width / 2, 0));
           exit.addCell(new Point(width / 2 - 1, 0));
       } else if (direction == Exit.DIRECTION_EAST){
           exit.addCell(new Point(width - 1, height / 2));
           exit.addCell(new Point(width - 1, height / 2 - 1));
       } else if (direction == Exit.DIRECTION_SOUTH) {
           exit.addCell(new Point(width / 2, height - 1));
           exit.addCell(new Point(width / 2 - 1, height - 1));
       } else if (direction == Exit.DIRECTION_WEST) {
           exit.addCell(new Point(0, height / 2));
           exit.addCell(new Point(0, height / 2 - 1));
       }
       exits.add(exit);
   }

   public void addDoor(int direction){
       Exit exit = new Exit(EXIT_SIZE, direction);
       exit.setOpened(true);
       addExit(direction, exit);
   }

   public void addEffect(FloorEffect effect){
       effects.add(effect);
   }

   public void rotate(int rotation){
       for (int rot = 0; rot < rotation; rot++) {
           int width = cells.length;
           int height = cells[0].length;
           Integer[][] newCells = new Integer[height][width];
           for (int i = 0; i < width; i++) {
               for (int j = 0; j < height; j++) {
                   newCells[height - j - 1][i] = cells[i][j];
               }
           }
           cells = newCells;
       }
       //System.out.println(exits.length);
       for (Exit exit : exits) {
           if (exit != null) {
               exit.rotate(rotation, width, height);
           }
       }
   }

   public void complete(){
        Random random = SeededRandom.getInstance();

       for (ExitPattern pattern : patterns){
           boolean active = true;
           for (int direction : pattern.getStatement()){
               if (!hasExit(direction)){
                   active = false;
                   break;
               }
           }
           if (active){
               Integer[][] cells = pattern.getCells();
               for (int i = 0; i < cells.length; i++) {
                   for (int j = 0; j < cells[0].length; j++) {
                       Integer cell = cells[i][j];
                       if (cell != null){
                           this.cells[i][j] = cell;
                       }
                   }
               }
           }
       }

       if (mobs) {
           AdvancedArray<Vector2> points = new AdvancedArray<Vector2>();
           for (int i = 0; i < width; i++) {
               for (int j = 0; j < height; j++) {
                   Integer index = cells[i][j];
                   if (index != null) {
                       DungeonTile tile = DungeonTile.getTile(index);
                       if (tile.isReachable() && !tile.isDoor()) {
                           points.add(new Vector2(i, j));
                       }
                   }
               }
           }
           float percent = random.nextFloat();
           boolean max = false;
           for (int i = 0; i < ENTITIES_PERCENT.length; i++) {
               percent -= ENTITIES_PERCENT[i];
               if (percent <= 0) {
                   monsterCount = i;
                   break;
               }
               if (i == ENTITIES_PERCENT.length - 1) {
                   max = true;
               }
           }
           if (percent > 0) {
               monsterCount = ENTITIES_PERCENT.length;
           }
           boolean solo = false;
           int type = CRTable.STANDARD_TYPE;
           if (max) {
               solo = random.nextBoolean();
           }
           if (solo) {
               monsterCount = 1;
               type = CRTable.SOLO_TYPE;
           }
           for (int i = 0; i < monsterCount; i++) {
               Enemy enemy = Enemy.getEnemyByFormula(type);
               enemy.activateAI();
               Vector2 point = points.getRandom();
               points.removeValue(point, true);
               point.x += x * Dungeon.ROOM_WIDTH;
               point.y += y * Dungeon.ROOM_HEIGHT;
               enemy.teleport(point);
               entities.add(enemy);
           }

           addingArray.addAll(entities);
       }

       encounter = random.nextInt(10) < 1; //TODO add reaction to opening room with encounter

       for (Exit exit : exits) {
           if (exit != null) {
               int cellId;
               if (exit.isOpened()) {
                   cellId = 0;
               } else {
                   cellId = 1;
               }
               Point[] exitCells = exit.getExitCells();
               for (Point cell : exitCells) {
                   cells[cell.x][cell.y] = cellId;
               }
           }
       }
   }

   public Exit getExit(int x, int y){
       Point point = new Point(x, y);
       for (Exit exit : exits) {
           for (Point exitPoint : exit.getExitCells()) {
               if (point.equals(exitPoint)) {
                   return exit;
               }
           }
       }
       return null;
   }

    public void addExitPattern(ExitPattern pattern){
        patterns.add(pattern);
    }

    public int getMonsterCount() {
        return monsterCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Integer getCell(int x, int y){
       return cells[x][y];
    }

    public Array<Target> getReachableCells(){
        return getCells(i -> DungeonTile.getTile(i).isReachable());
    }

    public Array<Target> getCells(Predicate<Integer> predicate){
        Array<Target> targets = new Array<>();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (predicate.evaluate(cells[i][j])){
                    targets.add(new Target(i, j));
                }
            }
        }
        return targets;
    }

    public void setCell(int x, int y, int cellType){
        cells[x][y] = cellType;
    }

    public Array<Exit> getExits() {
        return exits;
    }

    public static Array<Entity> getAddingArray() {
        return addingArray;
    }

    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void changePos(Vector2 vector){
        this.x += vector.x;
        this.y += vector.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Array<FloorEffect> getEffects() {
        return effects;
    }

    public boolean hasExit(){
        return exits.size > 0;
    }

    public Exit getExit(int direction){
        for (Exit exit : exits){
            if (exit.getDirection() == direction){
                return exit;
            }
        }
        return null;
    }

    public boolean hasExit(int direction){
        for (Exit exit : exits){
            if (exit.getDirection() == direction){
                return true;
            }
        }
        return false;
    }

    public Array<ExitPattern> getPatterns() {
        return patterns;
    }

    public ExitPattern getPattern(int index){
        return patterns.get(index);
    }

    public void setMobs(boolean mobs) {
        this.mobs = mobs;
    }

    public boolean isEncounter() {
        return encounter;
    }
}
