package ru.myitschool.dcrawler.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.*;
import ru.myitschool.dcrawler.ai.pathfinding.graph.GraphStorage;
import ru.myitschool.dcrawler.ai.pathfinding.graph.Node;
import ru.myitschool.dcrawler.dungeon.Dungeon;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.utils.AdvancedArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * Created by Voyager on 17.08.2017.
 */
public class AIUtils {

    /** lightwieght function to get minimal possible distance from one point to another*/
    public static int countDistanceRaw(int startX, int startY, int endX, int endY){
        int result = Math.abs(startX - endX) + Math.abs(startY - endY);
        return result;
    }

    public static AdvancedArray<Vector2> getCellRaytrace(int xStart, int yStart, int xEnd, int yEnd, int additional){
        AdvancedArray<Vector2> cells = new AdvancedArray<Vector2>();
        int dx = Math.abs(xEnd - xStart);
        int dy = Math.abs(yEnd - yStart);

        int sx = (xStart < xEnd) ? 1 : -1;
        int sy = (yStart < yEnd) ? 1 : -1;

        int err = dx - dy;

        boolean finish = false;
        int count = 0;

        while (!finish || count < additional) {
            cells.add(new Vector2(xStart, yStart));

            if (finish){
                count++;
            }
            if (xStart == xEnd && yStart == yEnd) {
                finish = true;
            }

            int e2 = 2 * err;

            if (e2 > -dy) {
                err = err - dy;
                xStart = xStart + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                yStart = yStart + sy;
            }
        }
        return cells;
    }

    public static boolean isPathObstructed(int xStart, int yStart, int xEnd, int yEnd){
        return isPathObstructed(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0));
    }

    public static boolean isPathObstructed(Array<Vector2> cells){
        for (Vector2 cell : cells){
            if (!DungeonMap.getCell((int) cell.x, (int) cell.y).getTile().isGroundTile()){
                return true;
            }
        }
        return false;
    }

    /** return all cells, that obstructs path*/
    public static Array<Vector2> getPathObstructor(int xStart, int yStart, int xEnd, int yEnd){
        return getPathObstructor(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0));
    }

    /** return all cells, that obstructs path*/
    public static Array<Vector2> getPathObstructor(Array<Vector2> cells){
        AdvancedArray<Vector2> obstructors = new AdvancedArray<Vector2>();
        for (Vector2 cellVect : cells){
            DungeonCell cell = DungeonMap.getCell((int) cellVect.x, (int) cellVect.y);
            if (cell == null || !cell.getTile().isGroundTile()){
                obstructors.add(cellVect);
            }
        }
        return obstructors;
    }

    /** return indexes of obstructors, but without occupied cells */
    public static Array<Integer> getObstructorIndexes(Array<Vector2> cells){
        return getObstructorIndexes(cells, false);
    }

    /** return indexes of obstructors */
    public static Array<Integer> getObstructorIndexes(Array<Vector2> cells, boolean checkOccupied){
        Array<Integer> obstructorsPos = new Array<Integer>();
        for (int i = 0; i < cells.size; i++) {
            Vector2 cellVect = cells.get(i);
            DungeonCell cell = DungeonMap.getCell((int) cellVect.x, (int) cellVect.y);
            if (cell == null || !cell.getTile().isGroundTile() || (checkOccupied && cell.isOccupied())){
                obstructorsPos.add(i);
            }
        }
        return obstructorsPos;
    }

    public static int getLineLength(int xStart, int yStart, int xEnd, int yEnd){
        return getCellRaytrace(xStart, yStart, xEnd, yEnd, 0).size;
    }

    /**
     * search for paths to each entity, which matches criteria
     * @param thisX x coordinate for search to start at
     * @param thisY y coordinate for search to start at
     * @param checkThis if true, entity found at {@param thisX}, {@param thisY} coordinates
     * will be included in search, it will be excluded otherwise
     * @param predicate criteria for entities
     * @return sorted map of paths and entities
     */
    public static Map<NodePath, Entity> getAllEntityPaths(int thisX, int thisY, boolean checkThis, Predicate<Entity> predicate){ //TODO make this function faster
        TreeMap<NodePath, Entity> map = new TreeMap<>();
        for (Entity entity : Entity.getPlayingEntities()){
            boolean valid = predicate.test(entity); // checking predicate function
            // check if valid first then call to search path, because it's heavy operation
            if (valid){
                NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), -1, true);
                // if node count == 1 then it is this this cell
                if (path != null && (path.getNodeCount() > 1 || checkThis)) {
                    map.put(path, entity);
                }
            }
        }
        return map;
    }

    public static Vector2 getDoorCell(int x1, int y1, int x2, int y2, int x3, int y3){ //TODO work only for doors in 2 cells
        int targetX = -1;
        int targetY = -1;
        if (x3 != x2){
            if (x3 > x2) {
                targetX = x3 * Dungeon.ROOM_WIDTH;
            } else if (x3 < x2) {
                targetX = x3 * Dungeon.ROOM_WIDTH + Dungeon.ROOM_WIDTH - 1;
            }
            targetY = y3 * Dungeon.ROOM_HEIGHT + Dungeon.ROOM_HEIGHT / 2;
            if (y1 > y2){
                targetY++;
            }
        } else if (y3 != y2){
            if (y3 > y2){
                targetY = y3 * Dungeon.ROOM_HEIGHT;
            } else if (y3 < y2){
                targetY = y3 * Dungeon.ROOM_HEIGHT + Dungeon.ROOM_HEIGHT - 1;
            }
            targetX = x3 * Dungeon.ROOM_WIDTH + Dungeon.ROOM_WIDTH / 2;
            if (x3 > x2){
                targetX--;
            }
        }
        return new Vector2(targetX, targetY);
    }
}
