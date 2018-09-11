package ru.myitschool.dcrawler.story.quest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.screens.FinalScreen;

import java.util.Random;

/**
 * Created by Voyager on 21.03.2018.
 */
public abstract class Quest {

    private boolean changeRoom;

    private int roomPos;
    private int deltaPos;
    private int roomsMax;

    private static Color exitColor = new Color(0xffcc2288);

    private Room room;

    public Quest(int roomPos, int deltaPos, int roomsMax, Room room) {
        this(roomPos, deltaPos, roomsMax);
        this.room = room;
        this.changeRoom = true;
    }

    public Quest(int roomPos, int deltaPos, int roomsMax) {
        this.roomPos = roomPos;
        this.deltaPos = deltaPos;
        if (roomsMax >= roomPos + deltaPos){
            this.roomsMax = roomsMax;
        } else {
            this.roomsMax = roomPos + deltaPos;
        }
        this.room = null;
        this.changeRoom = false;
    }

    public boolean isChangeRoom() {
        return changeRoom;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        changeRoom = room != null;
    }

    public int getRoomPos() {
        return roomPos + new Random().nextInt(deltaPos + 1);
    }

    public int getRoomsMax() {
        return roomsMax;
    }

    public static Color getExitColor() {
        return exitColor;
    }

    public Room exitRoomConstruction(Room room){
        if (isChangeRoom()){
            room = getRoom();
        }
        room.setMobs(false);
        room = modifyRoom(room);
        return room;
    }

    public abstract Room modifyRoom(Room room);

    public abstract void exitRoomOpened();

    public abstract void targetReached();

    public void complete(){
        ((Game) Gdx.app.getApplicationListener()).setScreen(new FinalScreen("Complete!"));
    }
}
