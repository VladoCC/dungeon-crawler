package com.wolg_vlad.dcrawler.utils.throwable

import java.lang.Error

class EventAlreadyRegisteredError(code: String): Error("Event with code $code already registered") {
}

class EventNotFoundError(code: String): Error("Event with code: $code not found. You should register it first.") {
}