package ru.myitschool.cubegame.dungeon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.Node;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.story.quest.Quest;

import java.awt.*;

/**
 * Created by Voyager on 25.03.2018.
 */
public class Dungeon {


    public static final int ROOM_WIDTH = 8;
    public static final int ROOM_HEIGHT = 8;

    private Quest quest;

    int placedRoomsCount = 0;

    private Array<Vector2> charPoses = new Array<>();

    private Array<Room> roomsPool = new Array<Room>();
    private Array<Room> roomsPlaced = new Array<Room>();

    public Dungeon(Quest quest) {
        this(quest, getDefaultCharPoses());
    }

    public Dungeon(Quest quest, Array<Vector2> charPoses) {
        this.quest = quest;
        this.charPoses = charPoses;
        createRooms(quest.getRoomsMax());
        placeChars();
        Exit.resetExitsCount();
        Exit.setExitsMax(quest.getRoomsMax() - 1);
    }

    private static Array<Vector2> getDefaultCharPoses(){
        Array<Vector2> array = new Array<>();
        array.add(new Vector2(3, 3));
        array.add(new Vector2(4, 3));
        array.add(new Vector2(4, 4));
        array.add(new Vector2(3, 4));
        return array;
    }

    public Quest getQuest() {
        return quest;
    }

    public int getRoomPos() {
        return getQuest().getRoomPos();
    }

    public int getRoomCountMax() {
        return getQuest().getRoomsMax();
    }

    public Array<Room> getRoomsPool() {
        return roomsPool;
    }

    public Array<Room> getRoomsPlaced() {
        return roomsPlaced;
    }

    public void moveRooms(Vector2 move){
        for (Room room : roomsPlaced){
            room.changePos(move);
        }
    }

    public Room getPlacedRoom(int roomX, int roomY){
        for (Room room : roomsPlaced){
            if (room.getX() == roomX && room.getY() == roomY){
                return room;
            }
        }
        return null;
    }

    public Room popRoom(){
        Room room = roomsPool.get(0);
        roomsPool.removeIndex(0);
        roomsPlaced.add(room);
        placedRoomsCount++;
        return room;
    }

    public Room placeRoom(int x, int y, int side){
        Room room = popRoom();
        room.rotate(side);

        int roomX = x / ROOM_WIDTH;
        int roomY = y / ROOM_HEIGHT;
        System.out.println(roomX + ",ggnbgn " + roomY);
        room.setPos(roomX, roomY);
        Array<Integer> sides = new Array<Integer>();

        Room left = getPlacedRoom(roomX - 1, roomY);
        Room right = getPlacedRoom(roomX + 1, roomY);
        Room up = getPlacedRoom(roomX, roomY - 1);
        Room down = getPlacedRoom(roomX, roomY + 1);

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

        return room;
    }

    private void createRooms(int count){
        int exitRoom = getRoomPos();
        System.out.println(exitRoom + " - exit room");
        for (int i = 0; i < count; i++) {
            Room room;
            if (i == 0) {
                room = createRoom(true);
            } else {
                room = createRoom(false);
                if (i == exitRoom - 1) {
                    room = quest.exitRoomConstruction(room);
                }
            }
            roomsPool.add(room);
        }
    }

    private Room createRoom(boolean first){
        Room room;
        if (first){
            room = new Room("rooms/default.room");
            room.setMobs(false);
        } else {
            room = new Room("rooms/corridor.room");
        }
        return room;
    }

    public void placeChars(){
        for (int i = 0; i < Character.getChars().size; i++) {
            Character character = Character.getChar(i);
            Vector2 pos = charPoses.get(i);
            character.setCellPos(pos);
        }
    }
}
