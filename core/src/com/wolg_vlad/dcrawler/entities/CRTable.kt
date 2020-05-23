package com.wolg_vlad.dcrawler.entities

import com.wolg_vlad.dcrawler.utils.AdvancedArray
import com.wolg_vlad.dcrawler.utils.SeededRandom
import java.util.*

/**
 * Created by Voyager on 25.01.2018.
 */
class CRTable {
    private val standardTable = TreeMap<Float, AdvancedArray<Enemy>?>()
    private val soloTable = TreeMap<Float, AdvancedArray<Enemy>?>()
    fun add(enemy: Enemy) {
        val challengeRatingTable: TreeMap<Float, AdvancedArray<Enemy>?>
        challengeRatingTable = if (enemy.isSolo) {
            soloTable
        } else {
            standardTable
        }
        val has = challengeRatingTable.containsKey(enemy.challengeRating)
        if (!has) {
            challengeRatingTable[enemy.challengeRating] = AdvancedArray()
        }
        challengeRatingTable[enemy.challengeRating]!!.add(enemy)
    }

    fun getEnemy(cr: Float, type: Int): Enemy {
        var max = 0
        var challengeRatingTable = standardTable
        if (type == ANY_TYPE) {
            if (!standardTable.containsKey(cr)) {
                challengeRatingTable = soloTable
                max = soloTable[cr]!!.size
            } else if (!soloTable.containsKey(cr)) {
                challengeRatingTable = standardTable
                max = standardTable[cr]!!.size
            } else {
                challengeRatingTable = standardTable
                max = standardTable[cr]!!.size + soloTable[cr]!!.size
            }
        } else if (type == STANDARD_TYPE) {
            challengeRatingTable = standardTable
            max = standardTable[cr]!!.size
        } else if (type == SOLO_TYPE) {
            challengeRatingTable = soloTable
            max = soloTable[cr]!!.size
        }
        var random = SeededRandom.getInstance()!!.nextInt(max)
        if (type == ANY_TYPE) {
            if (soloTable.containsKey(cr) && standardTable.containsKey(cr) && random - standardTable[cr]!!.size >= 0) {
                challengeRatingTable = soloTable
                random -= standardTable[cr]!!.size
            }
        }
        return challengeRatingTable[cr]!![random].clone()
    }

    fun getEnemyByFormula(type: Int): Enemy {
        var array: Array<Float>? = null
        var crs: Set<Float> = standardTable.keys
        if (type == ANY_TYPE) {
            crs = standardTable.keys
        } else if (type == STANDARD_TYPE) {
            crs = standardTable.keys
        } else if (type == SOLO_TYPE) {
            crs = soloTable.keys
        }
        val iterator = crs.iterator()
        array = Array(crs.size) {
            return@Array iterator.next()
        }
        var pos = 0
        for (fl in crs) {
            array[pos] = fl
            pos++
        }
        val max = 1 / array[0]
        val min = 1 / array[array.size - 1]
        val random = 1 / (min + (Math.random() * (max - min)).toFloat())
        var index = 0
        var key = 0f
        var biggerKey = 0f
        if (random < array[index]) {
            key = array[index]
        } else {
            for (i in array.indices) {
                if (random < array[i]) {
                    index = i
                    biggerKey = array[i]
                    key = array[i - 1]
                    break
                }
            }
            if (index == 0) {
                key = array[array.size - 1]
            } else {
                if (biggerKey - random < random - key) {
                    key = biggerKey
                }
            }
        }
        return getEnemy(key, type)
    }

    companion object {
        const val ANY_TYPE = 0
        const val STANDARD_TYPE = 1
        const val SOLO_TYPE = 2
    }
}