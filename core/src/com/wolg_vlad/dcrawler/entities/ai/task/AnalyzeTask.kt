package com.wolg_vlad.dcrawler.entities.ai.task

import com.wolg_vlad.dcrawler.entities.ai.AI

abstract class AnalyzeTask(private val ai: AI) : Task() {
    override fun startTask() {
        analyze()
        complete()
    }

    protected abstract fun analyze()

}