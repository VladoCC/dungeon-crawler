package com.wolg_vlad.dcrawler.event

import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction
import java.util.*

class EntityEventListener : EventListener() {
    override fun fire(eventCode: String?, args: HashMap<String, Any>): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        val entity = args[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] as Entity?
        when (eventCode) {
            EntityEvent.START_TURN_EVENT -> entity!!.startTurn()
            EntityEvent.END_TURN_EVENT -> entity!!.endTurn()
            EntityEvent.START_MOVE_EVENT -> entity!!.startMove()
            EntityEvent.END_MOVE_EVENT -> entity!!.endMove()
            EntityEvent.COUNT_MP_EVENT -> {
                val withMovement = args[EntityEvent.WITH_MOVEMENT_ARG_KEY] as Boolean
                val mp = entity!!.countMp(withMovement)
                result[EntityEvent.MP_ARG_KEY] = mp
            }
            EntityEvent.CAN_USE_SKILL_EVENT -> {
                val canUseSkill = entity!!.canUseSkill()
                result[EntityEvent.CAN_USE_SKILL_ARG_KEY] = canUseSkill
            }
            EntityEvent.START_SKILL_EVENT -> entity!!.startSkill()
            EntityEvent.END_SKILL_EVENT -> entity!!.endSkill()
            EntityEvent.ATTACK_BONUS_EVENT -> {
                var attackAction = args[EntityEvent.ATTACK_BONUS_ARG_KEY] as MathAction
                attackAction = entity!!.attackBonus(attackAction)
                result[EntityEvent.ATTACK_BONUS_ARG_KEY] = attackAction
            }
            EntityEvent.HEAL_BONUS_ARG_KEY -> {
                var healAction = args[EntityEvent.HEAL_BONUS_ARG_KEY] as MathAction?
                healAction = entity!!.healBonus(healAction!!)
                result[EntityEvent.HEAL_BONUS_ARG_KEY] = healAction
            }
            EntityEvent.ACCURACY_BONUS_EVENT -> {
                var accuracy = args[EntityEvent.ACCURACY_BONUS_ARG_KEY] as Int
                val target = args[EntityEvent.ENTITY_TARGET_ARG_KEY] as Entity?
                accuracy = entity!!.accuracyBonus(accuracy, target!!)
                result[EntityEvent.ACCURACY_BONUS_ARG_KEY] = accuracy
            }
            EntityEvent.ON_DAMAGE_EVENT -> {
                var damage = args[EntityEvent.DAMAGE_ARG_KEY] as Int
                damage = entity!!.onDamage(damage)
                result[EntityEvent.DAMAGE_ARG_KEY] = damage
            }
            EntityEvent.ON_HEAL_EVENT -> {
                var heal = args[EntityEvent.HEAL_ARG_KEY] as Int
                heal = entity!!.onHeal(heal)
                result[EntityEvent.HEAL_ARG_KEY] = heal
            }
            EntityEvent.ON_ENCOUNTER_EVENT -> {
                val encounter = args[EntityEvent.ENCOUNTER_ARG_KEY] as Encounter
                entity!!.onEncounter(encounter)
            }
            EntityEvent.ON_DEATH_EVENT -> entity!!.onDeath()
        }
        return result
    }

    override fun eventCodes(): Array<String> {
        return arrayOf(EntityEvent.START_TURN_EVENT, EntityEvent.END_TURN_EVENT, EntityEvent.START_MOVE_EVENT,
                EntityEvent.END_MOVE_EVENT, EntityEvent.COUNT_MP_EVENT, EntityEvent.CAN_USE_SKILL_EVENT,
                EntityEvent.START_SKILL_EVENT, EntityEvent.END_SKILL_EVENT, EntityEvent.ATTACK_BONUS_EVENT,
                EntityEvent.ACCURACY_BONUS_EVENT, EntityEvent.ON_DAMAGE_EVENT, EntityEvent.ON_HEAL_EVENT,
                EntityEvent.ON_ENCOUNTER_EVENT, EntityEvent.ON_DEATH_EVENT)
    }
}