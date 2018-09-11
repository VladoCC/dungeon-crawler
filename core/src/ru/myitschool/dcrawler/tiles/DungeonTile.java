package ru.myitschool.dcrawler.tiles;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;

/**
 * Created by Voyager on 18.04.2017.
 */
public class DungeonTile extends StaticTiledMapTile {

    static public final int TILE_WIDTH = 32;
    static public final int TILE_HEIGHT = 32;
    final static private String TEXTURE_PATH = "tiles/tiles.png";
    final static  private Texture texture = new Texture(TEXTURE_PATH);

    static public DungeonTile floorTile;
    static public DungeonTile doorTile;
    static public DungeonTile wallTile;
    static public DungeonTile pathTile;
    static public DungeonTile targetTile;

    boolean reachable;
    boolean door;
    boolean ready = false;

    int hardness;
    int pictureIndex = -1;

    public static Array<DungeonTile> tiles = new Array<DungeonTile>();

    public DungeonTile(int id){
        this(id, id, false, 0);
    }

    public DungeonTile(int id, boolean reacheable, int hardness){
        this(id, id, reacheable, hardness);
    }

    public DungeonTile(int pictureIndex, int id, boolean reacheable, int hardness) {
        this(chooseTextureRegion(pictureIndex), id, reacheable, hardness);
        this.pictureIndex = pictureIndex;
    }

    public DungeonTile(TextureRegion textureRegion, int id, boolean reacheable, int hardness) {
        super(textureRegion);
        ready = true;
        this.reachable = reacheable;
        this.hardness = hardness;
        setId(id);
    }

    private DungeonTile(DungeonTile tile){
        super(tile.getTextureRegion());
        this.ready = tile.ready;
        this.pictureIndex = tile.pictureIndex;
        this.door = tile.door;
        this.hardness = tile.hardness;
        this.reachable = tile.reachable;
        this.setId(tile.getId());
    }

    public static void initTiles() {
        /*floorTile = new FloorTile();
        tiles.add(floorTile);
        doorTile = new DoorTile();
        tiles.add(doorTile);
        wallTile = new WallTile();
        tiles.add(wallTile);
        pathTile = new PathTile();
        tiles.add(pathTile);
        targetTile = new TargetTile();
        tiles.add(targetTile);*/
        FileHandle file = new FileHandle("tiles/tiles.list");
        Gson gson = new Gson();
        tiles = new Array<DungeonTile>(gson.fromJson(file.readString(), DungeonTile[].class));
        floorTile = tiles.get(0);
        wallTile = tiles.get(2);
        pathTile = tiles.get(3);
        targetTile = tiles.get(4);
    }

    public static DungeonTile getTile(int index){
        return new DungeonTile(tiles.get(index));
    }

    public int getPictureIndex() {
        return pictureIndex;
    }

    public void setPictureIndex(int pictureIndex) {
        this.pictureIndex = pictureIndex;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public boolean isDoor() {
        return door;
    }

    public void setDoor(boolean door) {
        this.door = door;
    }

    protected static TextureRegion chooseTextureRegion(int index){
        int itemsInRow = texture.getWidth() / TILE_WIDTH;
        int row = index / itemsInRow;
        int coloumn = index % itemsInRow;
        TextureRegion textureRegion = new TextureRegion(texture, coloumn * TILE_WIDTH, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        return textureRegion;
    }

    public int getHardness() {
        return hardness;
    }

    @Override
    public TextureRegion getTextureRegion() {
        TextureRegion region = super.getTextureRegion();
        if (!ready){
            region = chooseTextureRegion(getPictureIndex());
            setTextureRegion(region);
            ready = true;
        }
        return region;
    }
}
