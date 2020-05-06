package ru.myitschool.dcrawler.ai.pathfinding.graph;

import ru.myitschool.dcrawler.dungeon.Exit;
import ru.myitschool.dcrawler.dungeon.Room;

public class RoomNode extends Node {

    private Room room;

    public RoomNode(int x, int y, Room room) {
        super(x, y);
        this.room = room;
    }

    @Override
    public boolean canConnect(Node node) {
        boolean exit = false;
        if (this.getX() - 1 == node.getX() && this.getY() == node.getY()) {
            exit = room.hasExit(Exit.DIRECTION_WEST);
        }
        if (this.getX() + 1 == node.getX() && this.getY() == node.getY()) {
            exit = room.hasExit(Exit.DIRECTION_EAST);
        }
        if (this.getX() == node.getX() && this.getY() - 1 == node.getY()) {
            exit = room.hasExit(Exit.DIRECTION_NORTH);
        }
        if (this.getX() == node.getX() && this.getY() + 1 == node.getY()) {
            exit = room.hasExit(Exit.DIRECTION_SOUTH);
        }
        return exit;
    }

    @Override
    public int connectionCost(Node to) {
        return 1;
    }

    @Override
    public boolean isReachable() {
        return true;
    }
}
