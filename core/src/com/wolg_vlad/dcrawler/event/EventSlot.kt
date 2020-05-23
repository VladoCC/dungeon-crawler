package com.wolg_vlad.dcrawler.event

import java.util.*

class EventSlot(private val listener: EventListener) {

    fun fire(eventCode: String, args: HashMap<String, Any>): HashMap<String, Any> {
        return listener.fire(eventCode, args)
    }
}