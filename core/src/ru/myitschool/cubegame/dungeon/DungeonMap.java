package ru.myitschool.cubegame.dungeon;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.Node;
import ru.myitschool.cubegame.ai.pathfinding.NodePath;
import ru.myitschool.cubegame.ai.pathfinding.Pathfinder;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.entities.Enemy;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.entities.enemies.Dummy;
import ru.myitschool.cubegame.entities.enemies.GoblinWarrior;
import ru.myitschool.cubegame.layer.DynamicTileLayer;
import ru.myitschool.cubegame.layer.PathTileLayer;
import ru.myitschool.cubegame.skills.Skill;
import ru.myitschool.cubegame.skills.Target;
import ru.myitschool.cubegame.skills.targeting.TilePos;
import ru.myitschool.cubegame.tiles.ColorTile;
import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 18.04.2017.
 */
public class DungeonMap extends TiledMap {

    private int lastTileX = -1;
    private int lastTileY = -1;

    private boolean spawn = false;
    private static boolean updateCamera = false;

    private static DynamicTileLayer tileLayer;
    private static PathTileLayer pathLayer;
    private static DynamicTileLayer displayTargetLayer;

    private static Group effectGroup;
    private static Group targetingZoneGroup;
    private static Group displayTargetGroup;
    private static Group choosenTargetsGroup;

    private InputMultiplexer input;
    private Pathfinder pathfinder;
    private NodePath playerPath;

    private Dungeon dungeon;

    public DungeonMap(Dungeon dungeon, final OrthographicCamera camera, final InputMultiplexer input) {
        GraphStorage.createTopGraph(new Node(0, 0));
        this.dungeon = dungeon;

        tileLayer = new DynamicTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(tileLayer);
        displayTargetLayer = new DynamicTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(displayTargetLayer);
        pathLayer = new PathTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        getLayers().add(pathLayer);
        placeRoom(0,0, 0);
        for (Entity entity : Entity.getPlayingEntities()){
            tileLayer.getCell(entity.getTileX(), entity.getTileY()).setOccupied(true);
            tileLayer.getCell(entity.getTileX(), entity.getTileY()).setEntity(entity);
        }
        GraphStorage.createBottomGraph();
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
                            character.setMovement();
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
                int cellX = (int) Math.floor(pos.x  / DungeonTile.TILE_WIDTH);
                int cellY = (int) Math.floor(pos.y / DungeonTile.TILE_HEIGHT);
                DungeonCell cell = tileLayer.getCell(cellX, cellY);
                Entity character = Entity.getNowPlaying();
                int charX = Character.getNowPlaying().getTileX();
                int charY = Character.getNowPlaying().getTileY();
                System.out.println(character.isPlayer() + " ^ " + character.isControlled());
                if (character.isPlayer() ^ character.isControlled()){
                    if (cellX != lastTileX || cellY != lastTileY) {
                        displayTargetGroup.clear();
                        lastTileX = cellX;
                        lastTileY = cellY;
                        if (cell != null && !character.isMovement() && !character.isSkillUse()) {
                            System.out.println("CellX: " + cellX + ", CellY: " + cellY + ", tileX: " + lastTileX + ", tileY: " + lastTileY);
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
                        } else if (character.isSkillUse()){
                            Array<TilePos> array = character.getUsedSkill().displayTarget(cellX, cellY);
                            for (TilePos tilePos : array){
                                addCellToGroup(tilePos.getX(), tilePos.getY(), tilePos.getTile().getTextureRegion(), displayTargetGroup);
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
                    System.out.println(effectGroup.getWidth() + " " + effectGroup.getHeight());
                    System.out.println(choosenTargetsGroup.getWidth() + " " + choosenTargetsGroup.getHeight());
                }
                return false;
            }
        });
    }

    public static void clearTargetLayer(){
        choosenTargetsGroup.clear();
    }

    public static void clearTargetingZoneLayer(){
        targetingZoneGroup.clear();
    }

    public static void clearSkillLayers(){
        DungeonMap.clearTargetLayer();
        DungeonMap.clearTargetingZoneLayer();
    }

    public static void drawTargetingZone(Skill skill){
        if (skill.getType() != Skill.SKILL_TARGET_TYPE_SELF) {
            int x = skill.getDoer().getTileX();
            int y = skill.getDoer().getTileY();
            int distanceMax = skill.getDistanceMax();
            int distanceMin = skill.getDistanceMin();
            System.out.println("drawing zone");
            for (int i = x - distanceMax; i < x + distanceMax + 1; i++) {
                for (int j = y - distanceMax; j < y + distanceMax + 1; j++) {
                    int distance = Math.abs(x - i) + Math.abs(y - j);
                    if (distanceMin <= distance && distance <= distanceMax) {
                        addCellToGroup(i, j, new ColorTile(Color.CYAN, 1, false).getTextureRegion(), targetingZoneGroup);
                    }
                }
            }
        }
    }

    public static void clearPathLayer(){
        pathLayer.clearLayer();
    }

    public Room placeRoom(int x, int y, int side){
        Room room = dungeon.placeRoom(x, y, side);

        int width = room.getWidth();
        int height = room.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                DungeonCell cell = new DungeonCell();
                Integer cellId = room.getCell(i, j);
                if (cellId != null) {
                    cell.setTile(DungeonTile.getTile(cellId));
                    tileLayer.setCell(x + i, y + j, cell);
                }
            }
        }

        for (FloorEffect effect : room.getEffects()){
            for (Target target : effect.getNullCells()){
                target.move(new Vector2(x, y));
            }
            effect.activate();
        }

        return room;
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
        FloorEffect.updateEffects(new Vector2(0, Dungeon.ROOM_HEIGHT));
        for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(0, count));
        }
        dungeon.moveRooms(new Vector2(0, 1));
        GraphStorage.moveNodesTop(new Vector2(0, 1));
    }

    public void addDown(int count){
        System.out.println("Adding down");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addDown(count);
        }
        FloorEffect.updateEffects(new Vector2(0, 0));
        /*for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(0, -count));
        }*/
    }

    public void addRight(int count){
        System.out.println("Adding right");
        for (MapLayer layer : getLayers()){
            ((DynamicTileLayer) layer).addRight(count);
        }
        FloorEffect.updateEffects(new Vector2(0, 0));
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
        FloorEffect.updateEffects(new Vector2(Dungeon.ROOM_WIDTH, 0));
        for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(count, 0));
        }
        dungeon.moveRooms(new Vector2(1, 0));
        GraphStorage.moveNodesTop(new Vector2(1, 0));
    }

    public static void addEntity(Entity entity){
        tileLayer.getCell(entity.getTileX(), entity.getTileY()).setOccupied(true);
        tileLayer.getCell(entity.getTileX(), entity.getTileY()).setEntity(entity);
    }

    public static void updateEntityPos(Entity entity){
        NodePath path = entity.getPath();
        if (path != null && path.getCount() > 1){
            pathLayer.clearLayer();
            Node start = path.get(0);
            Node end = path.getLast();
            updateEntityPos(entity, start.getX(), start.getY(), end.getX(), end.getY());
        }
    }

    public static void updateEntityPos(Entity entity, int x1, int y1, int x2, int y2){
        tileLayer.getCell(x1, y1).setOccupied(false);
        tileLayer.getCell(x1, y1).setEntity(null);
        tileLayer.getCell(x2, y2).setOccupied(true);
        tileLayer.getCell(x2, y2).setEntity(entity);
        GraphStorage.createBottomGraph();
    }

    public static void drawTargets(Array<Target> targets){
        choosenTargetsGroup.clear();
        for (Target target : targets){
            addCellToGroup(target.getX(), target.getY(), DungeonTile.targetTile.getTextureRegion(), choosenTargetsGroup);
        }
    }

    public static void updateEffects(){
        effectGroup.clear();
        for (FloorEffect effect : FloorEffect.getEffects()) {
            if (effect.isShow()) {
                ColorTile tile = new ColorTile(effect.getColor());
                TextureRegionDrawable drawable = new TextureRegionDrawable(tile.getTextureRegion());
                for (DungeonCell cell : effect.getCells()){
                    addCellToGroup(cell.getX(), cell.getY(), drawable, effectGroup);
                }
                for (Target target : effect.getNullCells()){
                    addCellToGroup(target.getX(), target.getY(), drawable, effectGroup);
                }
            }
        }
        updateCamera();
    }

    private static void addCellToGroup(int x, int y, Drawable drawable, Group group){
        Image image = new Image(drawable);
        image.setPosition(x * DungeonTile.TILE_WIDTH, - (y + 1) * DungeonTile.TILE_HEIGHT);
        group.addActor(image);
    }

    private static void addCellToGroup(int x, int y, TextureRegion region, Group group){
        addCellToGroup(x, y, new TextureRegionDrawable(region), group);
    }

    public int getWidth(){
        return tileLayer.getWidth();
    }

    public int getHeight(){
        return tileLayer.getHeight();
    }

    public static boolean isUpdateCamera() {
        return updateCamera;
    }

    public static void setUpdateCamera(boolean updateCamera) {
        DungeonMap.updateCamera = updateCamera;
    }

    private static void updateCamera(){
        setUpdateCamera(true);
    }

    public static DynamicTileLayer getTileLayer() {
        return tileLayer;
    }

    public int getLayersCount(){
        return getLayers().getCount();
    }

    public static void setEffectGroup(Group effectGroup) {
        DungeonMap.effectGroup = effectGroup;
    }

    public static void setTargetingZoneGroup(Group targetingZoneGroup) {
        DungeonMap.targetingZoneGroup = targetingZoneGroup;
    }

    public static void setDisplayTargetGroup(Group displayTargetGroup) {
        DungeonMap.displayTargetGroup = displayTargetGroup;
    }

    public static void setChoosenTargetsGroup(Group choosenTargetsGroup) {
        DungeonMap.choosenTargetsGroup = choosenTargetsGroup;
    }
}
