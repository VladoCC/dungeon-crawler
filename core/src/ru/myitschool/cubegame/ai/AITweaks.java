package ru.myitschool.cubegame.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.NodePath;
import ru.myitschool.cubegame.ai.pathfinding.Pathfinder;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.utils.AdvancedArray;

/**
 * Created by Voyager on 17.08.2017.
 */
public class AITweaks {

    public static final int TYPE_ENTITY = 0;
    public static final int TYPE_ENEMY = 1;
    public static final int TYPE_CHARACTER = 2;

    public static int countDistanceRaw(int startX, int startY, int endX, int endY, boolean neighbourCell){
        int result = Math.abs(startX - endX) + Math.abs(startY - endY);
        System.out.println(result + " - res");
        if (neighbourCell){
            result--;
        }
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

        while (count < additional) {
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

    public static boolean isPathObstructed(Array<Vector2> cells){
        for (Vector2 cell : cells){
            if (!GraphStorage.getNodeBottom((int) cell.x, (int) cell.y).getTile().isReachable()){
                return true;
            }
        }
        return false;
    }

    public static boolean isPathObstructed(int xStart, int yStart, int xEnd, int yEnd){
        return isPathObstructed(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0));
    }

    public static Array<Vector2> getPathObstructor(Array<Vector2> cells){
        AdvancedArray<Vector2> obstructors = new AdvancedArray<Vector2>();
        for (Vector2 cell : cells){
            if (!GraphStorage.getNodeBottom((int) cell.x, (int) cell.y).getTile().isReachable()){
                obstructors.add(cell);
            }
        }
        return obstructors;
    }

    public static Array<Vector2> getPathObstructor(int xStart, int yStart, int xEnd, int yEnd){
        return getPathObstructor(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0));
    }

    public static Array<Integer> getObstructorsPos(Array<Vector2> cells){
        Array<Integer> obstructorsPos = new Array<Integer>();
        for (int i = 0; i < cells.size; i++) {
            Vector2 cell = cells.get(i);
            if (!GraphStorage.getNodeBottom((int) cell.x, (int) cell.y).getTile().isReachable()){
                obstructorsPos.add(i);
            }
        }
        return obstructorsPos;
    }

    public static int getLineLength(int xStart, int yStart, int xEnd, int yEnd){
        return getCellRaytrace(xStart, yStart, xEnd, yEnd, 0).size;
    }

    public static Array<Entity> getEntityPaths(int thisX, int thisY, int entityType){
        int distance = Pathfinder.PATH_MAX_LENGTH_ROOM;
        Array<Entity> entities = new Array<Entity>();
        for (Entity entity : Entity.getPlayingEntities()){
            boolean valid = true;
            switch (entityType){ //TODO check for thisEntity === entity
                case TYPE_CHARACTER:
                    valid = entity.isPlayer();
                    break;
                case TYPE_ENEMY:
                    valid = entity.isEnemy();
                    break;
            }
            if (valid) {
                NodePath path = Pathfinder.searchRoomPath(thisX, thisY, entity.getTileX(), entity.getTileY());
                if (path != null) {
                    int thisDist = path.getCost();
                    if (thisDist < distance) {
                        distance = thisDist;
                        entities.clear();
                        entities.add(entity);
                    } else if (thisDist == distance){
                        entities.add(entity);
                    }
                }
            }
        }

        return entities;
    }

    public static NodePath getNearestEntityPath(int thisX, int thisY, int entityType){
        Array<Entity> entities = getEntityPaths(thisX, thisY, entityType);
        if (entities.size > 0) {
            NodePath nearestPath = null;
            int distance = Pathfinder.PATH_MAX_LENGTH_CELL;
            for (Entity entity : entities) {
                NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), false, 0, true);
                if (path != null) {
                    if (path.getCost() < distance) {
                        distance = path.getCost();
                        nearestPath = path;
                    }
                }
            }
            return nearestPath;
        }
        return null;
    }

    public static Entity getNearestEntity(int thisX, int thisY, int entityType){
        Array<Entity> entities = getEntityPaths(thisX, thisY, entityType);
        if (entities.size > 0) {
            Entity nearest = null;
            int distance = Pathfinder.PATH_MAX_LENGTH_CELL;
            for (Entity entity : entities) {
                NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), false, 0, true);
                if (path != null) {
                    if (path.getCost() < distance) {
                        distance = path.getCost();
                        nearest = entity;
                    }
                }
            }
            return nearest;
        }
        return null;
    }

    public static Object[] getNearestEntityAndPath(int thisX, int thisY, int entityType){
        Array<Entity> entities = getEntityPaths(thisX, thisY, entityType);
        if (entities.size > 0) {
            Object[] nearest = new Object[2];
            int distance = Pathfinder.PATH_MAX_LENGTH_CELL;
            for (Entity entity : entities) {
                NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), false, 0, true);
                if (path != null) {
                    if (path.getCost() < distance) {
                        distance = path.getCost();
                        nearest[0] = entity;
                        nearest[1] = path;
                    }
                }
            }
            return nearest;
        }
        return null;
    }

    public static Vector2 getDoorCell(int x1, int y1, int x2, int y2, int x3, int y3){ //TODO work only for doors in 2 cells
        int targetX = -1;
        int targetY = -1;
        if (x3 != x2){
            if (x3 > x2) {
                targetX = x3 * DungeonMap.ROOM_WIDTH;
            } else if (x3 < x2) {
                targetX = x3 * DungeonMap.ROOM_WIDTH + DungeonMap.ROOM_WIDTH - 1;
            }
            targetY = y3 * DungeonMap.ROOM_HEIGHT + DungeonMap.ROOM_HEIGHT / 2;
            if (y1 > y2){
                targetY++;
            }
        } else if (y3 != y2){
            if (y3 > y2){
                targetY = y3 * DungeonMap.ROOM_HEIGHT;
            } else if (y3 < y2){
                targetY = y3 * DungeonMap.ROOM_HEIGHT + DungeonMap.ROOM_HEIGHT - 1;
            }
            targetX = x3 * DungeonMap.ROOM_WIDTH + DungeonMap.ROOM_WIDTH / 2;
            if (x3 > x2){
                targetX--;
            }
        }
        return new Vector2(targetX, targetY);
    }
}
