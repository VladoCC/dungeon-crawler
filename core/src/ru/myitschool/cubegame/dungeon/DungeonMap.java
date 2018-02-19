package ru.myitschool.cubegame.dungeon;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.Node;
import ru.myitschool.cubegame.ai.pathfinding.NodePath;
import ru.myitschool.cubegame.ai.pathfinding.Pathfinder;
import ru.myitschool.cubegame.effects.BloodiedEffect;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.entities.Enemy;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.entities.enemies.Dummy;
import ru.myitschool.cubegame.entities.enemies.GoblinWarrior;
import ru.myitschool.cubegame.layer.DynamicTileLayer;
import ru.myitschool.cubegame.layer.PathTileLayer;
import ru.myitschool.cubegame.skills.Target;
import ru.myitschool.cubegame.tiles.ColorTile;
import ru.myitschool.cubegame.tiles.DungeonTile;

import java.awt.*;

/**
 * Created by Voyager on 18.04.2017.
 */
public class DungeonMap extends TiledMap {

    public static final int ROOM_WIDTH = 8;
    public static final int ROOM_HEIGHT = 8;

    private int lastTileX = -1;
    private int lastTileY = -1;

    private boolean spawn = false;

    static private DynamicTileLayer tileLayer;
    static private PathTileLayer pathLayer;
    static private DynamicTileLayer targetLayer;
    static private DynamicTileLayer effectLayer;

    private InputMultiplexer input;
    private Pathfinder pathfinder;
    private NodePath playerPath;

    private Array<Room> roomsPool = new Array<Room>();
    private Array<Room> roomsPlaced = new Array<Room>();

    public DungeonMap(int roomCountMin, int roomCountDelta, int roomCountMax, final OrthographicCamera camera, final InputMultiplexer input) {
        Exit.setExitsMax(roomCountMax - 1);
        GraphStorage.createTopGraph(new Node(0, 0));
        //final float camXStart = camera.position.x;
        //final float camYStart = camera.position.y;
        tileLayer = new DynamicTileLayer(ROOM_WIDTH, ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(tileLayer);
        effectLayer = new DynamicTileLayer(ROOM_WIDTH, ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(effectLayer);
        targetLayer = new DynamicTileLayer(ROOM_WIDTH, ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(targetLayer);
        pathLayer = new PathTileLayer(ROOM_WIDTH, ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(pathLayer);
        createRooms(roomCountMax);
        int monsterCount = 0;
        for (Room room : roomsPool) {
            monsterCount += room.getMonsterCount();
        }
        placeRoom(0,0, 0);
        for (Entity entity : Entity.getPlayingEntities()){
            tileLayer.getCell(entity.getTileX(), entity.getTileY()).setOccupied(true);
            tileLayer.getCell(entity.getTileX(), entity.getTileY()).setEntity(entity);
        }
        GraphStorage.createBottomGraph(tileLayer);
        pathfinder = new Pathfinder();
        this.input = input;
        input.addProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                Entity character = Entity.getNowPlaying();
                if (!spawn) {
                    if (character.isPlayer() ^ character.isControlled()) {
                        if (character.isSkillUse()) {
                            int cellX = (int) (pos.x / DungeonTile.TILE_WIDTH);
                            int cellY = (int) (pos.y / DungeonTile.TILE_HEIGHT);
                            character.addTarget(cellX, cellY);
                            character.getUsedSkill().drawTargets();
                        } else if (!character.isMovement()) {
                            moveChar();
                        }
                    }
                } else {
                    int cellX = (int) (pos.x / DungeonTile.TILE_WIDTH);
                    int cellY = (int) (pos.y / DungeonTile.TILE_HEIGHT);
                    final Dummy dummy = new Dummy(cellX * DungeonTile.TILE_WIDTH, cellY * DungeonTile.TILE_HEIGHT);
                    Class class1 = dummy.getClass();
                    Dummy second = (Dummy) dummy.clone();
                    Enemy.add(second);
                    /*try {
                        Enemy spawnedDummy = (Enemy) class1.getConstructors()[0].newInstance(cellX * DungeonTile.TILE_WIDTH, cellY * DungeonTile.TILE_HEIGHT);
                        Enemy.add(spawnedDummy);
                    } catch (Exception e) {

                    }*/
                    boolean b = dummy.isSkillUse();
                }
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
                int cellX = (int) (pos.x  / DungeonTile.TILE_WIDTH);
                int cellY = (int) (pos.y / DungeonTile.TILE_HEIGHT);
                DungeonCell cell = tileLayer.getCell(cellX, cellY);
                Entity character = Entity.getNowPlaying();
                int charX = Character.getNowPlaying().getTileX();
                int charY = Character.getNowPlaying().getTileY();
                System.out.println(character.isPlayer() + " ^ " + character.isControlled());
                if (character.isPlayer() ^ character.isControlled()){
                    if (cellX != lastTileX || cellY != lastTileY) {
                        if (cell != null && !character.isMovement() && !character.isSkillUse()) {
                            System.out.println("CellX: " + cellX + ", CellY: " + cellY + ", tileX: " + lastTileX + ", tileY: " + lastTileY);
                            lastTileX = cellX;
                            lastTileY = cellY;
                            Node startNode = GraphStorage.getNodeBottom(charX, charY);
                            Node endNode = GraphStorage.getNodeBottom(cellX, cellY);
                            NodePath nodePath = Pathfinder.searchConnectionPath(startNode, endNode, true, character.getMp(false));
                            System.out.println("Path: " + nodePath != null);
                            if (nodePath != null) {
                                character.setPath(nodePath);
                                pathLayer.drawPath(nodePath);
                            } else {
                               character.setPath(null);
                               pathLayer.clearLayer();
                            }
                        }
                    }
                }
                return true;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.C){

                } else if (keycode == Input.Keys.E){
                    new GoblinWarrior(20 * DungeonTile.TILE_WIDTH, 4 * DungeonTile.TILE_HEIGHT);
                } else if (keycode == Input.Keys.G){
                    spawn = !spawn;
                } else if (keycode == Input.Keys.T){
                    Entity.getNowPlaying().addEffect(new BloodiedEffect(Entity.getNowPlaying(), 0, 0));
                }
                return false;
            }
        });
    }

    public static void clearTargetLayer(){
        targetLayer.clearLayer();
    }

    public static void clearPathLayer(){
        pathLayer.clearLayer();
    }

    public Room placeRoom(int x, int y, int side){
        Room room = roomsPool.get(0);
        room.rotate(side);

        int roomX = x / ROOM_WIDTH;
        int roomY = y / ROOM_HEIGHT;
        System.out.println(roomX + ",ggnbgn " + roomY);
        room.setPos(roomX, roomY);
        Array<Integer> sides = new Array<Integer>();

        Room left = getRoom(roomX - 1, roomY);
        Room right = getRoom(roomX + 1, roomY);
        Room up = getRoom(roomX, roomY - 1);
        Room down = getRoom(roomX, roomY + 1);

        boolean leftExit = false;
        boolean rightExit = false;
        boolean topExit = false;
        boolean bottomExit = false;

        if (left != null) {
            if (left.hasExit(Exit.DIRECTION_EAST)) {
                System.out.println("Left: Yes");
                leftExit = true;
                room.addDoor(Exit.DIRECTION_WEST);
            }
        } else {
            sides.add(Exit.DIRECTION_WEST);
        }
        if (right != null){
            if (right.hasExit(Exit.DIRECTION_WEST)){
                System.out.println("Right: Yes");
                rightExit = true;
                room.addDoor(Exit.DIRECTION_EAST);
            }
        } else {
            sides.add(Exit.DIRECTION_EAST);
        }
        if (up != null){
            if (up.hasExit(Exit.DIRECTION_SOUTH)){
                System.out.println("Up: Yes");
                topExit = true;
                room.addDoor(Exit.DIRECTION_NORTH);
            }
        } else {
            sides.add(Exit.DIRECTION_NORTH);
        }
        if (down != null){
            if (down.hasExit(Exit.DIRECTION_NORTH)){
                System.out.println("Down: Yes");
                bottomExit = true;
                room.addDoor(Exit.DIRECTION_SOUTH);
            }
        } else {
            sides.add(Exit.DIRECTION_SOUTH);
        }
        room.addExits(sides);

        GraphStorage.addTopNode(new Node(roomX, roomY), leftExit, rightExit, topExit, bottomExit);
        room.complete();

        for (Exit exit : room.getExits()){
            Point[] cells = exit.getExitCells();
            for (Point cell : cells){
                cell.x += x;
                cell.y += y;
            }
            new Door(exit.getDirection(), cells);
        }

        int width = room.getWidth();
        int height = room.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                DungeonCell cell = new DungeonCell();
                Integer cellId = room.getCell(i, j);
                /*if (cellId == DungeonTile.floorTile.getId()){
                    cell.setTile(DungeonTile.floorTile);
                } else if (cellId == DungeonTile.doorTile.getId()){
                    cell.setTile(DungeonTile.doorTile);
                } else if (cellId == DungeonTile.wallTile.getId()){
                    cell.setTile(DungeonTile.wallTile);
                }*/
                if (cellId != null) {
                    cell.setTile(DungeonTile.getTile(cellId));
                    tileLayer.setCell(x + i, y + j, cell);
                }
            }
        }

        roomsPool.removeIndex(0);
        roomsPlaced.add(room);
        return room;
    }

    private void createRooms(int count){
        for (int i = 0; i < count; i++) {
            if (i == 0) {
                createRoom(true);
            } else {
                createRoom(false);
            }
        }
    }

    private void createRoom(boolean first){
        int width = ROOM_WIDTH;
        int height = ROOM_HEIGHT;
        Room room;
        if (first){
            room = new Room("rooms/default.room");
            room.setMobs(false);
        } else {
            room = new Room("rooms/corridor.room");
        }
        //room = new Room(width, height, first);
        roomsPool.add(room);
    }

    public static DungeonCell getCell(int x, int y){
        return tileLayer.getCell(x, y);
    }

    public DungeonTile getTile(int x, int y){
        DungeonCell cell = getCell(x, y);
        if (cell != null) {
            return (DungeonTile) cell.getTile();
        }
        return null;
    }

    public void removeDoorTile(int x, int y){
        ((DungeonTile) tileLayer.getCell(x, y).getTile()).setDoor(false);
    }

    public void addUp(int count){
        System.out.println("Adding up");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addUp(count);
        }
        /**Map moving code*/
        for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(0, count));
        }
        for (Room room : roomsPlaced){
            room.changePos(new Vector2(0, 1));
        }
        GraphStorage.moveNodesTop(new Vector2(0, 1));
    }

    public void addDown(int count){
        System.out.println("Adding down");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addDown(count);
        }
        /*for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(0, -count));
        }*/
    }

    public void addRight(int count){
        System.out.println("Adding right");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addRight(count);
        }
        //GraphStorage.createBottomGraph(tileLayer);
        /*for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(count, 0));
        }*/
    }

    public void addLeft(int count){
        System.out.println("Adding left");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addLeft(count);
        }
        /**Map moving code*/
        for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(count, 0));
        }
        for (Room room : roomsPlaced){
            room.changePos(new Vector2(1, 0));
        }
        GraphStorage.moveNodesTop(new Vector2(1, 0));
    }

    public Room getRoom(int roomX, int roomY){
        for (Room room : roomsPlaced){
            if (room.getX() == roomX && room.getY() == roomY){
                return room;
            }
        }
        return null;
    }

    public static void addEntity(Entity entity){
        tileLayer.getCell(entity.getTileX(), entity.getTileY()).setOccupied(true);
        tileLayer.getCell(entity.getTileX(), entity.getTileY()).setEntity(entity);
    }

    public static void moveChar(){
        Entity entity = Entity.getNowPlaying();
        NodePath path = entity.getPath();
        if (path != null && path.getCount() > 1){
            pathLayer.clearLayer();
            Node start = path.get(0);
            Node end = path.getLast();
            tileLayer.getCell(start.getX(), start.getY()).setOccupied(false);
            tileLayer.getCell(start.getX(), start.getY()).setEntity(null);
            tileLayer.getCell(end.getX(), end.getY()).setOccupied(true);
            tileLayer.getCell(end.getX(), end.getY()).setEntity(entity);
            GraphStorage.createBottomGraph(tileLayer);
        }
        Entity.getNowPlaying().setMovement();
    }

    public static void drawTargets(Array<Target> targets){
        targetLayer.clearLayer();
        for (Target target : targets){
            DungeonCell cell = new DungeonCell(DungeonTile.targetTile);
            targetLayer.setCell(target.getX(), target.getY(), cell);
        }
    }

    public static void updateEffects(){
        effectLayer.clearLayer();
        for (FloorEffect effect : FloorEffect.getEffects()) {
            if (effect.isShow()) {
                ColorTile tile = new ColorTile(effect.getColor());
                for (DungeonCell cell : effect.getCells()){
                    DungeonCell effectCell = new DungeonCell(tile);
                    effectLayer.setCell(cell.getX(), cell.getY(), effectCell);
                }
            }
        }
    }

    public int getWidth(){
        return tileLayer.getWidth();
    }

    public int getHeight(){
        return tileLayer.getHeight();
    }

    public DynamicTileLayer getTileLayer() {
        return tileLayer;
    }
}
