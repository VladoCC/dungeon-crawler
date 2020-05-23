package com.wolg_vlad.dcrawler.event

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.utils.throwable.EventAlreadyRegisteredError
import com.wolg_vlad.dcrawler.utils.throwable.EventNotFoundError
import java.util.*

object EventController {
    private val beforeActions = Array<EventAction>()
    private val afterActions = Array<EventAction>()
    private val events = HashMap<String, EventSlot?>()
    fun callEvent(eventCode: String, args: HashMap<String, Any>): HashMap<String, Any> {
        beforeEvent(eventCode)
        val result = event(eventCode, args)
        afterEvent(eventCode)
        return result
    }

    private fun beforeEvent(eventCode: String) {
        for (action in beforeActions) {
            action.act(eventCode)
        }
    }

    private fun event(eventCode: String, args: HashMap<String, Any>): HashMap<String, Any> {
        if (events[eventCode] == null) {
            throw EventNotFoundError(eventCode)
        }
        return events[eventCode]!!.fire(eventCode, args)
    }

    private fun afterEvent(eventCode: String) {
        for (action in afterActions) {
            action.act(eventCode)
        }
    }

    @Throws(Exception::class)
    fun registerEvent(eventCode: String, listener: EventListener): EventSlot {
        return if (!events.containsKey(eventCode)) {
            val slot = EventSlot(listener)
            events[eventCode] = slot
            slot
        } else {
            throw EventAlreadyRegisteredError(eventCode)
        }
    }

    fun addActionBeforeEvent(action: EventAction) {
        beforeActions.add(action)
    }

    fun addActionAfterEvent(action: EventAction) {
        afterActions.add(action)
    }
}