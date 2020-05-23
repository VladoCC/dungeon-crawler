package com.wolg_vlad.dcrawler.dungeon

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.google.gson.Gson
import com.wolg_vlad.dcrawler.effects.FloorEffect
import com.wolg_vlad.dcrawler.entities.CRTable
import com.wolg_vlad.dcrawler.entities.Enemy
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import com.wolg_vlad.dcrawler.utils.AdvancedArray
import com.wolg_vlad.dcrawler.utils.SeededRandom
import java.awt.Point

/**
 * Created by Voyager on 23.04.2017.
 */
class Room {
    var width: Int
        private set
    var height: Int
        private set
    private var openningPoints = 0
    private var exitCount = 0
    var x = 0
        private set
    var y = 0
        private set
    var monsterCount = 0
        private set
    var isEncounter = false
        private set
    private var mobs = true
    private var cells: Array<Array<Int?>>
    var exits: com.badlogic.gdx.utils.Array<Exit>
        private set
    private var entities = com.badlogic.gdx.utils.Array<Entity>()
    var patterns: com.badlogic.gdx.utils.Array<ExitPattern>
        private set
    val effects = com.badlogic.gdx.utils.Array<FloorEffect>()

    //private ArrayList<Integer> exitPositions = new ArrayList<Integer>();
    constructor(width: Int, height: Int, first: Boolean) {
        this.width = width
        this.height = height
        cells = Array(width) { arrayOfNulls<Int>(height) }
        /*
        if (!first) {
            cells[width/2][0] = 0;
            cells[width/2 - 1][0] = 0;
        }*/
//exitPositions.add(1);
//exitPositions.add(2);
//exitPositions.add(3);
        exits = com.badlogic.gdx.utils.Array(4)
        patterns = com.badlogic.gdx.utils.Array()
        /*for (int i = 0; i < exitCount; i++) {
            System.out.println(Exit.canOpenDoor());
            if (Exit.canOpenDoor()) {
                addExit();
            }
        }*/
    }

    constructor(pattern: String?) : this(gson.fromJson<Room>(Gdx.files.internal(pattern).readString(), Room::class.java)) {}
    private constructor(room: Room) {
        cells = room.cells
        patterns = room.patterns
        isEncounter = room.isEncounter
        entities = room.entities
        exitCount = room.exitCount
        exits = room.exits
        width = room.width
        height = room.height
        x = room.x
        y = room.y
        countOpenningPoints()
    }

    private fun countOpenningPoints() {
        openningPoints = Math.max(width * height / DEFAULT_SIZE, 1)
    }

    fun addExits(exitPositions: com.badlogic.gdx.utils.Array<Int>) {
        val random = SeededRandom.getInstance()
        if (Exit.canOpenDoor()) {
            exitCount = Math.max(random.nextInt(3) + 2, Exit.exitsLeft)
        } /*else {
            exitCount = random.nextInt(4);
        }*/
        if (exitCount > exitPositions.size) {
            exitCount = exitPositions.size
        }
        for (i in 0 until exitCount) {
            val index = random.nextInt(exitPositions.size)
            val direction = exitPositions[index]
            exitPositions.removeIndex(index)
            addExit(direction)
        }
    }

    fun addExit(direction: Int) {
        val exit = Exit(EXIT_SIZE, direction)
        addExit(direction, exit)
    }

    fun addExit(direction: Int, exit: Exit) {
        if (direction == Exit.DIRECTION_NORTH) {
            exit.addCell(Point(width / 2, 0))
            exit.addCell(Point(width / 2 - 1, 0))
        } else if (direction == Exit.DIRECTION_EAST) {
            exit.addCell(Point(width - 1, height / 2))
            exit.addCell(Point(width - 1, height / 2 - 1))
        } else if (direction == Exit.DIRECTION_SOUTH) {
            exit.addCell(Point(width / 2, height - 1))
            exit.addCell(Point(width / 2 - 1, height - 1))
        } else if (direction == Exit.DIRECTION_WEST) {
            exit.addCell(Point(0, height / 2))
            exit.addCell(Point(0, height / 2 - 1))
        }
        exits.add(exit)
    }

    fun addDoor(direction: Int) {
        val exit = Exit(EXIT_SIZE, direction)
        exit.isOpened = true
        addExit(direction, exit)
    }

    fun addEffect(effect: FloorEffect) {
        effects.add(effect)
    }

    fun rotate(rotation: Int) {
        for (rot in 0 until rotation) {
            val width = cells.size
            val height: Int = cells[0].size
            val newCells = Array(height) { arrayOfNulls<Int>(width) }
            for (i in 0 until width) {
                for (j in 0 until height) {
                    newCells[height - j - 1][i] = cells[i][j]
                }
            }
            cells = newCells
        }
        //System.out.println(exits.length);
        for (exit in exits) {
            exit?.rotate(rotation, width, height)
        }
    }

    fun complete() {
        val random = SeededRandom.getInstance()
        for (pattern in patterns) {
            var active = true
            for (direction in pattern.statement) {
                if (!hasExit(direction)) {
                    active = false
                    break
                }
            }
            if (active) {
                val cells = pattern.cells
                for (i in cells!!.indices) {
                    for (j in 0 until cells[0].size) {
                        val cell = cells[i]!![j]
                        if (cell != null) {
                            this.cells[i][j] = cell
                        }
                    }
                }
            }
        }
        if (mobs) {
            val points = AdvancedArray<Vector2>()
            for (i in 0 until width) {
                for (j in 0 until height) {
                    val index = cells[i][j]
                    if (index != null) {
                        val tile = DungeonTile.getTile(index)
                        if (tile.groundTile && !tile.door) {
                            points.add(Vector2(i.toFloat(), j.toFloat()))
                        }
                    }
                }
            }
            var percent = random.nextFloat()
            var max = false
            for (i in ENTITIES_PERCENT.indices) {
                percent -= ENTITIES_PERCENT[i]
                if (percent <= 0) {
                    monsterCount = i
                    break
                }
                if (i == ENTITIES_PERCENT.size - 1) {
                    max = true
                }
            }
            if (percent > 0) {
                monsterCount = ENTITIES_PERCENT.size
            }
            var solo = false
            var type = CRTable.STANDARD_TYPE
            if (max) {
                solo = random.nextBoolean()
            }
            if (solo) {
                monsterCount = 1
                type = CRTable.SOLO_TYPE
            }
            for (i in 0 until monsterCount) {
                val enemy = Enemy.getEnemyByFormula(type)
                enemy.activateAI()
                val point = points.random
                points.removeValue(point, true)
                point.x += x * Dungeon.ROOM_WIDTH.toFloat()
                point.y += y * Dungeon.ROOM_HEIGHT.toFloat()
                enemy.teleport(point)
                entities.add(enemy)
            }
            addingArray.addAll(entities)
        }
        isEncounter = random.nextInt(10) < 1 //TODO add reaction to opening room with encounter
        for (exit in exits) {
            if (exit != null) {
                var cellId: Int
                cellId = if (exit.isOpened) {
                    0
                } else {
                    1
                }
                val exitCells = exit.exitCells
                for (cell in exitCells!!) {
                    cells[cell!!.x][cell.y] = cellId
                }
            }
        }
    }

    fun getExit(x: Int, y: Int): Exit? {
        val point = Point(x, y)
        for (exit in exits) {
            for (exitPoint in exit.exitCells) {
                if (point == exitPoint) {
                    return exit
                }
            }
        }
        return null
    }

    fun addExitPattern(pattern: ExitPattern) {
        patterns.add(pattern)
    }

    fun getCell(x: Int, y: Int): Int? {
        return cells[x][y]
    }

    val reachableCells: MutableList<Target>
        get() = getCells { i: Int? -> DungeonTile.getTile(i!!).groundTile }

    fun getCells(predicate: (Int?) -> Boolean): MutableList<Target> {
        val targets = mutableListOf<Target>()
        for (i in cells.indices) {
            for (j in 0 until cells[0].size) {
                if (predicate.invoke(cells[i][j])) {
                    targets.add(Target(i, j))
                }
            }
        }
        return targets
    }

    fun setCell(x: Int, y: Int, cellType: Int) {
        cells[x][y] = cellType
    }

    fun setPos(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun changePos(vector: Vector2) {
        x += vector.x.toInt()
        y += vector.y.toInt()
    }

    fun hasExit(): Boolean {
        return exits.size > 0
    }

    fun getExit(direction: Int): Exit? {
        for (exit in exits) {
            if (exit.direction == direction) {
                return exit
            }
        }
        return null
    }

    fun hasExit(direction: Int): Boolean {
        for (exit in exits) {
            if (exit.direction == direction) {
                return true
            }
        }
        return false
    }

    fun getPattern(index: Int): ExitPattern {
        return patterns[index]
    }

    fun setMobs(mobs: Boolean) {
        this.mobs = mobs
    }

    companion object {
        const val ANGEL_90 = 1
        const val ANGEL_180 = 2
        const val ANGEL_270 = 3
        const val EXIT_SIZE = 2
        const val DEFAULT_SIZE = 64
        val ENTITIES_PERCENT = floatArrayOf(0.35f, 0.6f, 0.05f)
        private val gson = Gson()
        @JvmStatic
        val addingArray = com.badlogic.gdx.utils.Array<Entity>()
    }
}