package com.wolg_vlad.dcrawler.entities.ai

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.task.MoveTask
import com.wolg_vlad.dcrawler.entities.ai.task.SkillTask
import com.wolg_vlad.dcrawler.entities.skills.ProtectiveStance
import com.wolg_vlad.dcrawler.entities.skills.Scratches
import com.wolg_vlad.dcrawler.entities.skills.Target

/**
 * Created by Voyager on 16.08.2017.
 */
class GoblinAI(controlledEntity: Entity) : AI(controlledEntity) {
    var scratchSkill: Scratches
    var stanceSkill: ProtectiveStance
    override fun aiAnalyze() {
        val paths = AIUtils.getAllEntityPaths(controlledEntity.tileX, controlledEntity.tileY, false, Entity::isCharacter)
        if (paths!!.size > 0) {
            val entityPath = paths.entries.toTypedArray()[0]
            val entity = entityPath.value
            val path = entityPath.key
            path.cutLast()
            val distance = path.cost
            val speed: Int = controlledEntity.speed
            if (distance > 0 && distance <= controlledEntity.speed) {
                addTask(MoveTask(path))
                val targets = mutableListOf<Target>()
                targets.add(Target(entity.tileX, entity.tileY))
                addTask(SkillTask(scratchSkill, targets))
            } else if (distance > speed) {
                if (path.cost > entity.speed * 2 + 1) {
                    path.cut(entity.speed * 2 + 1)
                }
                addTask(MoveTask(path))
            } else if (distance == 0) {
                addTask(SkillTask(stanceSkill, Target(entity.tileX, entity.tileY)))
            }
        }
    }

    override fun startTurn() {
        super.startTurn()
    }

    override fun endMove() {
        super.endMove()
    }

    override fun endSkill() {
        super.endSkill()
    }

    companion object {
        const val LONG_DISTANCE_STATE = 0
        const val NORMALL_DISTANCE_STATE = 1
        const val SHORT_DISTANCE_STATE = 2
    }

    init {
        this.controlledEntity = controlledEntity
        scratchSkill = Scratches(controlledEntity)
        stanceSkill = ProtectiveStance(controlledEntity)
        controlledEntity.addSkill(scratchSkill)
        controlledEntity.addSkill(stanceSkill)
    }
}