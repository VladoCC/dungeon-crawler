package com.wolg_vlad.dcrawler.ui.tiles

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.google.gson.Gson

/**
 * Created by Voyager on 18.04.2017.
 */
open class DungeonTile : StaticTiledMapTile {
    var groundTile: Boolean
    var door = false
    var ready = false
    var hardness: Int
    var pictureIndex = -1

    constructor(id: Int) : this(id, id, false, 0) {}
    constructor(id: Int, groundTile: Boolean, hardness: Int) : this(id, id, groundTile, hardness) {}
    constructor(pictureIndex: Int, id: Int, groundTile: Boolean, hardness: Int) : this(chooseTextureRegion(pictureIndex), id, groundTile, hardness) {
        this.pictureIndex = pictureIndex
    }

    constructor(textureRegion: TextureRegion?, id: Int, groundTile: Boolean, hardness: Int) : super(textureRegion) {
        ready = true
        this.groundTile = groundTile
        this.hardness = hardness
        setId(id)
    }

    private constructor(tile: DungeonTile) : super(tile.textureRegion) {
        ready = tile.ready
        pictureIndex = tile.pictureIndex
        door = tile.door
        hardness = tile.hardness
        groundTile = tile.groundTile
        id = tile.id
    }

    override fun getTextureRegion(): TextureRegion {
        var region = super.getTextureRegion()
        if (!ready) {
            region = chooseTextureRegion(pictureIndex)
            textureRegion = region
            ready = true
        }
        return region
    }

    companion object {
        const val TILE_WIDTH = 32
        const val TILE_HEIGHT = 32
        private const val TEXTURE_PATH = "tiles/tiles.png"
        private val texture = Texture(TEXTURE_PATH)
        var floorTile: DungeonTile? = null
        var doorTile: DungeonTile? = null
        var wallTile: DungeonTile? = null
        var pathTile: DungeonTile? = null
        var targetTile: DungeonTile? = null
        @kotlin.jvm.JvmField
        var tiles = com.badlogic.gdx.utils.Array<DungeonTile>()
        @kotlin.jvm.JvmStatic
        fun initTiles() { /*floorTile = new FloorTile();
        tiles.add(floorTile);
        doorTile = new DoorTile();
        tiles.add(doorTile);
        wallTile = new WallTile();
        tiles.add(wallTile);
        pathTile = new PathTile();
        tiles.add(pathTile);
        targetTile = new TargetTile();
        tiles.add(targetTile);*/
            val file = Gdx.files.local("tiles/tiles.list")
            val gson = Gson()
            tiles = com.badlogic.gdx.utils.Array(gson.fromJson(file.readString(), Array<DungeonTile>::class.java))
            floorTile = tiles[0]
            wallTile = tiles[2]
            pathTile = tiles[3]
            targetTile = tiles[4]
        }

        @kotlin.jvm.JvmStatic
        fun getTile(index: Int): DungeonTile {
            return DungeonTile(tiles[index])
        }

        protected fun chooseTextureRegion(index: Int): TextureRegion {
            val itemsInRow = texture.width / TILE_WIDTH
            val row = index / itemsInRow
            val coloumn = index % itemsInRow
            return TextureRegion(texture, coloumn * TILE_WIDTH, row * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT)
        }
    }
}