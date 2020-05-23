package com.wolg_vlad.dcrawler.dungeon

/**
 * Created by Voyager on 18.01.2018.
 */
class ExitPattern(statement: IntArray, roomWidth: Int, roomHeight: Int, name: String?) {
    var statement: IntArray
        private set
    private var roomWidth: Int
    private var roomHeight: Int
    lateinit var cells: Array<Array<Int?>>
        private set
    var name: String? = null

     init {
        this.statement = statement
        this.roomWidth = roomWidth
        this.roomHeight = roomHeight
        this.name = name
    }

}