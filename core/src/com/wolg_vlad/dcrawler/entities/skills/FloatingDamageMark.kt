package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.utils.Array

/**
 * Created by Voyager on 21.02.2018.
 */
class FloatingDamageMark(val tileX: Int, val tileY: Int, private val defaultText: String) {
    private var text: String? = ""
    private var textsCount = 0
    var time = 0f
        private set

    fun addText(text: String?) {
        if (textsCount > 0) {
            this.text += "/"
        }
        this.text += text
        textsCount++
    }

    fun show() {
        marks.add(this)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val mark = o as FloatingDamageMark
        if (tileX != mark.tileX) return false
        if (tileY != mark.tileY) return false
        return if (text != null) text == mark.text else mark.text == null
    }

    fun getText(): String? {
        return if (textsCount > 0) {
            text
        } else defaultText
    }

    companion object {
        const val MAX_TIME = 2.5f
        val marks = Array<FloatingDamageMark>()
        fun update(delta: Float) {
            for (mark in marks) {
                mark.time += delta
                if (mark.time >= MAX_TIME) {
                    mark.time = 0f
                    marks.removeValue(mark, false)
                }
            }
        }

    }

}