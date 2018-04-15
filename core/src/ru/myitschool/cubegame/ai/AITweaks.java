package ru.myitschool.cubegame.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.pathfinding.*;
import ru.myitschool.cubegame.dungeon.Dungeon;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.utils.AdvancedArray;

import java.util.Comparator;

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

    /** return all cells, that obstructs path*/
    public static Array<Vector2> getPathObstructor(Array<Vector2> cells){
        AdvancedArray<Vector2> obstructors = new AdvancedArray<Vector2>();
        for (Vector2 cell : cells){
            Node node = GraphStorage.getNodeBottom((int) cell.x, (int) cell.y);
            if (node == null || !node.getTile().isReachable()){
                obstructors.add(cell);
            }
        }
        return obstructors;
    }

    public static Array<Vector2> getPathObstructor(int xStart, int yStart, int xEnd, int yEnd){
        return getPathObstructor(getCellRaytrace(xStart, yStart, xEnd, yEnd, 0));
    }

    /** return indexes of obstructors */
    public static Array<Integer> getObstructorIndexes(Array<Vector2> cells, boolean checkOccupied){
        Array<Integer> obstructorsPos = new Array<Integer>();
        for (int i = 0; i < cells.size; i++) {
            Vector2 cell = cells.get(i);
            Node node = GraphStorage.getNodeBottom((int) cell.x, (int) cell.y);
            if (node == null || !node.getTile().isReachable() || (checkOccupied && DungeonMap.getCell(node.getX(), node.getY()).isOccupied())){
                obstructorsPos.add(i);
            }
        }
        return obstructorsPos;
    }

    public static Array<Integer> getObstructorIndexes(Array<Vector2> cells){
        return getObstructorIndexes(cells, false);
    }

    public static int getLineLength(int xStart, int yStart, int xEnd, int yEnd){
        return getCellRaytrace(xStart, yStart, xEnd, yEnd, 0).size;
    }

    public static Array<EntityPath> getAllEntityPaths(int thisX, int thisY, int entityType, boolean checkThis){ //TODO make this function faster
        Array<EntityPath> array = new Array<>();
        for (Entity entity : Entity.getPlayingEntities()){
            boolean valid = true;
            switch (entityType){
                case TYPE_CHARACTER:
                    valid = entity.isPlayer();
                    break;
                case TYPE_ENEMY:
                    valid = entity.isEnemy();
                    break;
            }
            if (valid && (checkThis || entity.getTileX() != thisX || entity.getTileY() != thisY)){
                NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(thisX, thisY), GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), false, 0, true);
                array.add(new EntityPath(entity, path, true));
            }
        }
        array.sort(new Comparator<EntityPath>() {
            @Override
            public int compare(EntityPath o1, EntityPath o2) {
                if (o1.getPath() == null){
                    return 1;
                } else if (o2.getPath() == null){
                    return -1;
                }
                return o1.getPath().getCost() - o2.getPath().getCost();
            }
        });
        return array;
    }

    public static Array<Entity> getNearestEntities(int thisX, int thisY, int entityType){
        int distance = Pathfinder.PATH_MAX_LENGTH_ROOM;
        Array<Entity> entities = new Array<Entity>();
        Array<Entity> lastEntities = new Array<Entity>();
        for (Entity entity : Entity.getPlayingEntities()){
            boolean valid = true;
            switch (entityType){ //TODO check for thisEntity == entity
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
                    if (thisDist + 1 == distance){
                        lastEntities.clear();
                        lastEntities.addAll(entities);
                    } else if (thisDist + 1 < distance){
                        lastEntities.clear();
                    }
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
        entities.addAll(lastEntities);

        return entities;
    }

    public static NodePath getNearestEntityPath(int thisX, int thisY, int entityType){
        Array<Entity> entities = getNearestEntities(thisX, thisY, entityType);
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
        Array<Entity> entities = getNearestEntities(thisX, thisY, entityType);
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
        Array<Entity> entities = getNearestEntities(thisX, thisY, entityType);
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
