package ru.myitschool.dcrawler.ai.pathfinding.graph;

import ru.myitschool.dcrawler.dungeon.DungeonCell;

public class CellNode extends Node {

    private DungeonCell cell;

    public CellNode(int x, int y, DungeonCell cell) {
        super(x, y);
        this.cell = cell;
    }

    public DungeonCell getCell() {
        return cell;
    }

    @Override
    public boolean canConnect(Node node) {
        if (!(node instanceof CellNode)) {
            return false;
        }
        int distance = Math.abs(node.getX() - getX()) + Math.abs(node.getY() - getY());
        // this and other one should be ground tiles and they should be neighbours
        return getCell().getTile().isGroundTile() && ((CellNode) node).getCell().getTile().isGroundTile() && distance == 1;
    }

    @Override
    public int connectionCost(Node to) {
        return ((CellNode) to).getCell().getTile().getHardness();
    }

    @Override
    public boolean isReachable() {
        return cell.isOccupied();
    }
}
