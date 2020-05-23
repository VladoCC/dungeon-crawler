package com.wolg_vlad.dcrawler.encounters

import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.event.EventController
import com.wolg_vlad.dcrawler.utils.AdvancedArray
import java.util.*

/**
 * Created by Voyager on 17.02.2018.
 */
abstract class Encounter : Cloneable {
    protected var entity: Entity? = null
    var text = ""
    var name = ""
    private var time = 0f
    fun trigger(entity: Entity) {
        addNowPlaying(this)
        this.entity = entity
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = entity
        map[EntityEvent.ENCOUNTER_ARG_KEY] = this
        EventController.callEvent(EntityEvent.ON_ENCOUNTER_EVENT, map)
    }

    protected abstract fun activate()

    override fun clone(): Encounter {
        return super.clone() as Encounter
    }

    companion object {
        const val FULL_TIME = 3f
        private val encounters = AdvancedArray<Encounter?>()
        var nowPlayings = mutableListOf<Encounter>()
        fun update(delta: Float) {
            nowPlayings = nowPlayings.filter { encounter ->
                encounter.time += delta
                if (encounter.time >= FULL_TIME) {
                    encounter.activate()
                    return@filter false
                }
                return@filter true
            }.toMutableList()
        }

        fun initEncouters() {
            DartEncounter()
        }

        fun getEncounter(index: Int): Encounter {
            return encounters[index]!!.clone()
        }

        val randomEncounter: Encounter
            get() = (encounters.random as Encounter).clone()

        fun getNowPlaying(index: Int): Encounter {
            return nowPlayings[index]
        }

        fun addNowPlaying(encounter: Encounter) {
            nowPlayings.add(encounter)
        }

        fun hasNowPlayimg(): Boolean {
            return nowPlayings.size > 0
        }
    }

    init {
        encounters.add(this)
    }
}