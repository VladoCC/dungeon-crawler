package com.wolg_vlad.dcrawler.entities.ai.task

import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.event.EntityEventAdapter

/**
 * Created by Voyager on 12.02.2018.
 */
abstract class Task : EntityEventAdapter() {
    var entity: Entity? = null
    var isStarted = false
        private set
    var isComplete = false
        private set

    protected fun complete() {
        isComplete = true
    }

    fun start() {
        isStarted = true
        startTask()
    }

    protected abstract fun startTask()
}