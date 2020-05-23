package com.wolg_vlad.dcrawler.entities.skills.targeting

import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile

/**
 * Class for tile with  coordinates. Used by displayers to interact with [DynamicTileLayer]
 */
data class TilePos(val x: Int, val y: Int, val tile: DungeonTile)