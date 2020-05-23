package com.wolg_vlad.dcrawler.dungeon

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.Pathfinder
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.Node
import com.wolg_vlad.dcrawler.entities.enemies.GoblinWarrior
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.ui.layers.DynamicTileLayer
import com.wolg_vlad.dcrawler.ui.layers.PathTileLayer
import com.wolg_vlad.dcrawler.ui.tiles.ColorTile
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import com.wolg_vlad.dcrawler.utils.Utils

/**
 * Created by Voyager on 18.04.2017.
 */
class DungeonMap(dungeon: Dungeon, camera: OrthographicCamera, input: InputMultiplexer) : TiledMap() {
    private var lastTileX = -1
    private var lastTileY = -1
    private var spawn = false
    private val input: InputMultiplexer
    private val dungeon: Dungeon
    fun placeRoom(x: Int, y: Int, side: Int): Room? {
        val room = dungeon.placeRoom(x, y, side)
        val width = room.width
        val height = room.height
        for (i in 0 until width) {
            for (j in 0 until height) {
                val cell = DungeonCell()
                val cellId = room!!.getCell(i, j)
                if (cellId != null) {
                    cell.tile = DungeonTile.getTile(cellId)
                    tileLayer!!.setCell(x + i, y + j, cell)
                }
            }
        }
        for (effect in room.effects) {
            for (target in effect!!.nullCells) {
                target.move(Vector2(x.toFloat(), y.toFloat()))
            }
            effect.activate()
        }
        return room
    }

    fun getTile(x: Int, y: Int): DungeonTile? {
        val cell = getCell(x, y)
        return if (cell != null) {
            cell.tile
        } else null
    }

    fun removeDoorTile(x: Int, y: Int) {
        (tileLayer!!.getCell(x, y)?.tile)?.door = false
    }

    fun addUp(count: Int) {
        println("Adding up")
        for (layer in layers) {
            (layer as DynamicTileLayer).addUp(count)
        }
        /**Map moving code */
        FloorEffect.updateEffects(Vector2(0F, Dungeon.ROOM_HEIGHT.toFloat()))
        for (entity in Entity.playingEntities) {
            entity.teleport(Vector2(0F, count.toFloat()))
        }
        dungeon.moveRooms(Vector2(0F, 1F))
        GraphStorage.moveNodesTop(Vector2(0F, 1F))
    }

    fun addDown(count: Int) {
        println("Adding down")
        for (layer in layers) {
            (layer as DynamicTileLayer).addDown(count)
        }
        FloorEffect.updateEffects(Vector2(0F, 0F))
        /*for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(0, -count));
        }*/
    }

    fun addRight(count: Int) {
        println("Adding right")
        for (layer in layers) {
            (layer as DynamicTileLayer).addRight(count)
        }
        FloorEffect.updateEffects(Vector2(0F, 0F))
        //GraphStorage.createBottomGraph(tileLayer);
/*for (Entity entity : Entity.getPlayingEntities()) {
            entity.teleport(new Vector2(count, 0));
        }*/
    }

    fun addLeft(count: Int) {
        println("Adding left")
        for (layer in layers) {
            (layer as DynamicTileLayer).addLeft(count)
        }
        /**Map moving code */
        FloorEffect.updateEffects(Vector2(Dungeon.ROOM_WIDTH.toFloat(), 0F))
        for (entity in Entity.playingEntities) {
            entity.teleport(Vector2(count.toFloat(), 0F))
        }
        dungeon.moveRooms(Vector2(1F, 0F))
        GraphStorage.moveNodesTop(Vector2(1F, 0F))
    }

    val width: Int
        get() = tileLayer!!.layerWidth

    val height: Int
        get() = tileLayer!!.layerHeight

    val layersCount: Int
        get() = layers.count

    companion object {
        var updateCamera = false
        @JvmStatic
        var tileLayer: DynamicTileLayer? = null
        private var pathLayer: PathTileLayer? = null
        private var displayTargetLayer: DynamicTileLayer? = null
        private var effectGroup: Group? = null
        private var targetingZoneGroup: Group? = null
        private var displayTargetGroup: Group? = null
        private var chosenTargetsGroup: Group? = null
        @JvmStatic
        fun clearTargetLayer() {
            chosenTargetsGroup!!.clear()
        }

        @JvmStatic
        fun clearTargetingZoneLayer() {
            targetingZoneGroup!!.clear()
        }

        @JvmStatic
        fun clearSkillLayers() {
            clearTargetLayer()
            clearTargetingZoneLayer()
        }

        @JvmStatic
        fun drawTargetingZone(skill: Skill) {
            if (skill.type != Skill.SKILL_TARGET_TYPE_SELF) {
                val x = skill.doer.tileX
                val y = skill.doer.tileY
                val distanceMax = skill.distanceMax
                val distanceMin = skill.distanceMin
                println("drawing zone")
                for (i in x - distanceMax until x + distanceMax + 1) {
                    for (j in y - distanceMax until y + distanceMax + 1) {
                        if (Utils.isTargetInDistance(x, y, i, j, distanceMin, distanceMax)) {
                            addCellToGroup(i, j, ColorTile(Color.CYAN, 1F, false).textureRegion, targetingZoneGroup)
                        }
                    }
                }
            }
        }

        @JvmStatic
        fun clearPathLayer() {
            pathLayer?.clearLayer()
        }

        @JvmStatic
        fun getCell(x: Int, y: Int): DungeonCell? {
            return tileLayer?.getCell(x, y)
        }

        @JvmStatic
        fun addEntity(entity: Entity) {
            tileLayer?.getCell(entity.tileX, entity.tileY)?.isOccupied = true
            tileLayer?.getCell(entity.tileX, entity.tileY)?.entity = entity
        }

        @JvmStatic
        fun updateEntityPos(entity: Entity) {
            val path = entity.path
            if (path != null && path.nodeCount > 1) {
                pathLayer?.clearLayer()
                val start = path[0]
                val end = path.last
                updateEntityPos(entity, start.x, start.y, end.x, end.y)
            }
        }

        @JvmStatic
        fun updateEntityPos(entity: Entity?, x1: Int, y1: Int, x2: Int, y2: Int) {
            tileLayer?.getCell(x1, y1)?.isOccupied = false
            tileLayer?.getCell(x1, y1)?.entity = null
            tileLayer?.getCell(x2, y2)?.isOccupied = true
            tileLayer?.getCell(x2, y2)?.entity = entity
            GraphStorage.createBottomGraph()
        }

        @JvmStatic
        fun drawTargets(targets: MutableList<Target>) {
            chosenTargetsGroup!!.clear()
            for (target in targets) {
                addCellToGroup(target.x, target.y, DungeonTile.targetTile!!.textureRegion, chosenTargetsGroup)
            }
        }

        @JvmStatic
        fun updateEffects() {
            effectGroup!!.clear()
            for (effect in FloorEffect.effects) {
                if (effect.isShow) {
                    val tile = ColorTile(effect.color)
                    val drawable = TextureRegionDrawable(tile.textureRegion)
                    for (cell in effect.cells) {
                        addCellToGroup(cell.x, cell.y, drawable, effectGroup)
                    }
                    for (target in effect.nullCells) {
                        addCellToGroup(target.x, target.y, drawable, effectGroup)
                    }
                }
            }
            updateCamera()
        }

        private fun addCellToGroup(x: Int, y: Int, drawable: Drawable, group: Group?) {
            val image = Image(drawable)
            image.setPosition(x * DungeonTile.TILE_WIDTH.toFloat(), -(y + 1) * DungeonTile.TILE_HEIGHT.toFloat())
            group!!.addActor(image)
        }

        private fun addCellToGroup(x: Int, y: Int, region: TextureRegion, group: Group?) {
            addCellToGroup(x, y, TextureRegionDrawable(region), group)
        }

        private fun updateCamera() {
            updateCamera = true
        }

        @JvmStatic
        fun setEffectGroup(effectGroup: Group?) {
            Companion.effectGroup = effectGroup
        }

        @JvmStatic
        fun setTargetingZoneGroup(targetingZoneGroup: Group?) {
            Companion.targetingZoneGroup = targetingZoneGroup
        }

        @JvmStatic
        fun setDisplayTargetGroup(displayTargetGroup: Group?) {
            Companion.displayTargetGroup = displayTargetGroup
        }

        @JvmStatic
        fun setChosenTargetsGroup(chosenTargetsGroup: Group?) {
            Companion.chosenTargetsGroup = chosenTargetsGroup
        }
    }

    init {
        tileLayer = DynamicTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
        layers.add(tileLayer)
        displayTargetLayer = DynamicTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
        layers.add(displayTargetLayer)
        pathLayer = PathTileLayer(Dungeon.ROOM_WIDTH, Dungeon.ROOM_HEIGHT, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
        layers.add(pathLayer)
        this.dungeon = dungeon
        placeRoom(0, 0, 0)
        for (entity in Entity.playingEntities) {
            tileLayer!!.getCell(entity.tileX, entity.tileY)?.isOccupied = true
            tileLayer!!.getCell(entity.tileX, entity.tileY)?.entity = entity
        }
        GraphStorage.createBottomGraph()
        this.input = input
        input.addProcessor(object : InputAdapter() {
            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val pos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0F))
                val character = Entity.nowPlaying
                if (!spawn && character != null) {
                    if (character.isCharacter xor character.isControlled) {
                        if (character.isSkillUse) {
                            val cellX = (pos.x / DungeonTile.TILE_WIDTH).toInt()
                            val cellY = (pos.y / DungeonTile.TILE_HEIGHT).toInt()
                            character.addOrRemoveSkillTarget(cellX, cellY)
                        } else if (!character.isMovement) {
                            character.setMovement()
                        }
                    }
                }
                return true
            }

            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                val pos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0F))
                val cellX = Math.floor(pos.x / DungeonTile.TILE_WIDTH.toDouble()).toInt()
                val cellY = Math.floor(pos.y / DungeonTile.TILE_HEIGHT.toDouble()).toInt()
                val cell = tileLayer!!.getCell(cellX, cellY) //todo check safety
                val character = Entity.nowPlaying
                if (character != null) {
                    val charX = Entity.nowPlaying?.tileX?: 0
                    val charY = Entity.nowPlaying?.tileY?: 0
                    println("" + character.isCharacter + " ^ " + character.isControlled)
                    if (character.isCharacter xor character.isControlled) {
                        if (cellX != lastTileX || cellY != lastTileY) {
                            displayTargetGroup!!.clear()
                            lastTileX = cellX
                            lastTileY = cellY
                            if (cell != null && !character.isMovement && !character.isSkillUse) {
                                println("CellX: $cellX, CellY: $cellY, tileX: $lastTileX, tileY: $lastTileY")
                                val startNode: Node? = GraphStorage.getNodeBottom(charX, charY)
                                val endNode: Node? = GraphStorage.getNodeBottom(cellX, cellY)
                                val nodePath = Pathfinder.searchConnectionPath(startNode, endNode, character.getMp(false))
                                if (nodePath != null && nodePath.nodeCount > 1) {
                                    character.path = nodePath
                                    pathLayer!!.drawPath(nodePath)
                                } else {
                                    character.path = null
                                    pathLayer!!.clearLayer()
                                }
                            } else if (character.isSkillUse) {
                                val array = character.usedSkill?.displayTarget(cellX, cellY)
                                if (array != null) {
                                    for (tilePos in array) {
                                        addCellToGroup(tilePos.x, tilePos.y, tilePos.tile.textureRegion, displayTargetGroup)
                                    }
                                }
                            }
                        }
                    }
                }
                return true
            }

            override fun keyUp(keycode: Int): Boolean {
                if (keycode == Input.Keys.C) {
                } else if (keycode == Input.Keys.E) {
                    println("!!!!!!")
                    val entity: Entity = GoblinWarrior((lastTileX * DungeonTile.TILE_WIDTH).toFloat(), (lastTileY * DungeonTile.TILE_HEIGHT).toFloat())
                    entity.add()
                    (entity as GoblinWarrior).activateAI()
                    getCell(lastTileX, lastTileY)?.entity = entity
                } else if (keycode == Input.Keys.G) {
                    spawn = !spawn
                } else if (keycode == Input.Keys.T) {
                    println(effectGroup!!.width.toString() + " " + effectGroup!!.height)
                    println(chosenTargetsGroup!!.width.toString() + " " + chosenTargetsGroup!!.height)
                }
                return false
            }
        })
    }

    override fun dispose() {
        super.dispose()
        //input.removeProcessor(input.size() - 1) //todo temporary logic. We need to remove processor of this map. Not just last one.
    }
}