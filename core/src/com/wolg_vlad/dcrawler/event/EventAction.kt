package com.wolg_vlad.dcrawler.event

abstract class EventAction {
    abstract fun act(eventCode: String)
}