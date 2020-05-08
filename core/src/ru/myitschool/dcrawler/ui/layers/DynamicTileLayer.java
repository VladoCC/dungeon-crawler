package ru.myitschool.dcrawler.ui.layers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.myitschool.dcrawler.dungeon.DungeonCell;


/**
 * Created by Voyager on 20.04.2017.
 */
public class DynamicTileLayer extends TiledMapTileLayer {

    protected int width;
    protected int height;
    private int startX;
    private int startY;

    private float tileWidth;
    private float tileHeight;

    protected DungeonCell[][] cells;

    public DynamicTileLayer(int width, int height, int tileWidth, int tileHeight) {
        super(width, height, tileWidth, tileHeight);
        startX = 0;
        startY = 0;
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.cells = new DungeonCell[width][height];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float getTileWidth() {
        return tileWidth;
    }

    @Override
    public float getTileHeight() {
        return tileHeight;
    }

    @Override
    public DungeonCell getCell(int x, int y) {
        if (x < cells.length && x >= 0 && y < cells[0].length && y >= 0){
            return cells[x][y];
        }
        return null;
    }

    @Override /** Better don't use this method*/
    public void setCell(int x, int y, Cell cell) {
        setCell(x, y, (DungeonCell) cell);
    }

    public void setCell(int x, int y, DungeonCell cell){
        if (x < cells.length && x >= 0 && y < cells[0].length && y >= 0) {
            cells[x][y] = cell;
            cell.setX(x);
            cell.setY(y);
        }
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public void addLeft(int count){
        width += count;
        startX += count;
        DungeonCell[][] newCells = new DungeonCell[width][height];
        for (int i = count; i < width; i++){
            for (int j = 0; j < height; j++) {
                newCells[i][j] = cells[i - count][j];
                if (newCells[i][j] != null) {
                    newCells[i][j].setX(i);
                }
            }
        }
        cells = newCells;
    }

    public void addRight(int count){
        DungeonCell[][] newCells = new DungeonCell[width + count][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++) {
                newCells[i][j] = cells[i][j];
            }
        }
        width += count;
        cells = newCells;
    }

    public void addDown(int count){
        DungeonCell[][] newCells = new DungeonCell[width][height + count];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++) {
                newCells[i][j] = cells[i][j];
            }
        }
        height += count;
        cells = newCells;
    }

    public void addUp(int count){
        height += count;
        startY +=  count;
        DungeonCell[][] newCells = new DungeonCell[width][height];
        for (int i = 0; i < width; i++){
            for (int j = count; j < height; j++) {
                newCells[i][j] = cells[i][j - count];
                if (newCells[i][j] != null) {
                    newCells[i][j].setY(j);
                }
            }
        }
        cells = newCells;
    }

    public DungeonCell[][] getCells() {
        return cells;
    }

    public void setCells(DungeonCell[][] cells) {
        this.cells = cells;
    }

    public void clearLayer(){
        setCells(new DungeonCell[width][height]);
    }
}

