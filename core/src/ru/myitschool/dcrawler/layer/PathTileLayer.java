package ru.myitschool.dcrawler.layer;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.Node;
import ru.myitschool.dcrawler.ai.pathfinding.NodePath;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.tiles.DungeonTile;

/**
 * Created by Voyager on 03.05.2017.
 */
public class PathTileLayer extends DynamicTileLayer {

    public PathTileLayer(int width, int height, int tileWidth, int tileHeight) {
        super(width, height, tileWidth, tileHeight);
    }

    public void drawPath(NodePath path){
        clearLayer();
        Array<Node> nodes = path.getNodes();
        System.out.println("Nodes size: " + nodes.size);
        for (int i = 1; i < nodes.size; i++) {
            Node node = nodes.get(i);
            int x = node.getX();
            int y = node.getY();
            System.out.println("Node: " + x + "; " + y);
            DungeonCell cell = new DungeonCell();
            cell.setTile(DungeonTile.pathTile);
            setCell(x, y, cell);
        }
    }

    /*public void drawTargets(Array<Target> targets){
        clearLayer();
        for (Target target : targets){
            DungeonCell cell = new DungeonCell(DungeonTile.targetTile);
            setCell(target.getX(), target.getY(), cell);
        }
    }*/
}
