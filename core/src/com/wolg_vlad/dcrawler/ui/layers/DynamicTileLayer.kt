package com.wolg_vlad.dcrawler.ui.layers

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.wolg_vlad.dcrawler.dungeon.DungeonCell

/**
 * Created by Voyager on 20.04.2017.
 */
open class DynamicTileLayer(var layerWidth: Int, var layerHeight: Int, tileWidth: Int, tileHeight: Int) : TiledMapTileLayer(layerWidth, layerHeight, tileWidth, tileHeight) {
    var startX = 0
        private set
    var startY = 0
        private set
    private val tileWidth: Float = tileWidth.toFloat()
    private val tileHeight: Float = tileHeight.toFloat()
    var cells: Array<Array<DungeonCell?>>

    override fun getHeight(): Int {
        return layerHeight
    }

    override fun getWidth(): Int {
        return layerWidth
    }

    override fun getTileWidth(): Float {
        return tileWidth
    }

    override fun getTileHeight(): Float {
        return tileHeight
    }

    override fun getCell(x: Int, y: Int): DungeonCell? {
        return if (x < cells.size && x >= 0 && y < cells[0].size && y >= 0) {
            cells[x][y]
        } else null
    }

    /** Better don't use this method */
    override fun setCell(x: Int, y: Int, cell: Cell) {
        setCell(x, y, cell as DungeonCell)
    }

    fun setCell(x: Int, y: Int, cell: DungeonCell) {
        if (x < cells.size && x >= 0 && y < cells[0].size && y >= 0) {
            cells[x][y] = cell
            cell.x = x
            cell.y = y
        }
    }

    fun addLeft(count: Int) {
        layerWidth += count
        startX += count
        val newCells = Array<Array<DungeonCell?>>(layerWidth) { i ->
            return@Array Array(layerHeight) { j ->
                return@Array if (i >= count) { cells[i - count][j]?.x = i
                    cells[i - count][j] }
                else null
            }
        }
        cells = newCells
    }

    fun addRight(count: Int) {
        layerWidth += count
        val newCells = Array<Array<DungeonCell?>>(layerWidth) { i ->
            return@Array Array(layerHeight) { j ->
                return@Array if (i < layerWidth - count) cells[i][j]
                else null
            }
        }
        cells = newCells
    }

    fun addDown(count: Int) {
        layerHeight += count
        val newCells = Array<Array<DungeonCell?>>(layerWidth) { i ->
            return@Array Array(layerHeight) { j ->
                return@Array if (j < layerHeight - count) cells[i][j]
                else null
            }
        }
        cells = newCells
    }

    fun addUp(count: Int) {
        layerHeight += count
        startY += count
        val newCells = Array<Array<DungeonCell?>>(layerWidth) { i ->
            return@Array Array(layerHeight) { j ->
                return@Array if (j >= count) { cells[i][j - count]?.y = j
                    cells[i][j - count] }
                else null
            }
        }
        cells = newCells
    }

    fun clearLayer() {
        cells = Array<Array<DungeonCell?>>(layerWidth) { arrayOfNulls(layerHeight) }
    }

    init {
        this.cells = Array<Array<DungeonCell?>>(layerWidth) { arrayOfNulls(layerHeight) }
    }
}