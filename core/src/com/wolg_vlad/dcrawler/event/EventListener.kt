package com.wolg_vlad.dcrawler.event

import java.util.*

abstract class EventListener {
    abstract fun fire(eventCode: String?, args: HashMap<String, Any>): HashMap<String, Any>
    abstract fun eventCodes(): Array<String>

    companion object {
        fun initListeners() {
            try {
                EntityEventListener()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    init {
        for (code in eventCodes()) {
            EventController.registerEvent(code, this)
        }
    }
}