package com.wolg_vlad.dcrawler.entities.ai

import com.wolg_vlad.dcrawler.entities.Entity

/**
 * Created by Voyager on 11.12.2017.
 */
class DummyAI(controlledEntity: Entity) : AI(controlledEntity) {
    override fun aiAnalyze() {}
}