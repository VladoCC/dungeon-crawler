package com.wolg_vlad.dcrawler.event

import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 19.05.2017.
 */
interface EntityEvent {
    fun startTurn()
    fun endTurn()
    fun startMove()
    fun endMove()
    fun countMp(withMovement: Boolean): Int
    fun canUseSkill(): Boolean
    fun startSkill()
    fun endSkill()
    fun attackBonus(action: MathAction): MathAction
    fun healBonus(action: MathAction): MathAction
    fun accuracyBonus(accuracy: Int, target: Entity): Int
    fun onDamage(damage: Int): Int
    fun onHeal(heal: Int): Int
    fun onEncounter(encounter: Encounter)
    fun onDeath()

    companion object {
        const val START_TURN_EVENT = "main.dcrawler.entity.start_turn"
        const val END_TURN_EVENT = "main.dcrawler.entity.end_turn"
        const val START_MOVE_EVENT = "main.dcrawler.entity.start_move"
        const val END_MOVE_EVENT = "main.dcrawler.entity.end_move"
        const val COUNT_MP_EVENT = "main.dcrawler.entity.count_mp"
        const val CAN_USE_SKILL_EVENT = "main.dcrawler.entity.can_use_skill"
        const val START_SKILL_EVENT = "main.dcrawler.entity.start_skill"
        const val END_SKILL_EVENT = "main.dcrawler.entity.end_skill"
        const val ATTACK_BONUS_EVENT = "main.dcrawler.entity.attack_bonus"
        const val HEAL_BONUS_EVENT = "main.dcrawler.entity.heal_bonus"
        const val ACCURACY_BONUS_EVENT = "main.dcrawler.entity.accuracy_bonus"
        const val ON_DAMAGE_EVENT = "main.dcrawler.entity.on_damage"
        const val ON_HEAL_EVENT = "main.dcrawler.entity.on_heal"
        const val ON_ENCOUNTER_EVENT = "main.dcrawler.entity.on_encounter"
        const val ON_DEATH_EVENT = "main.dcrawler.entity.on_death"
        const val ENTITY_EXECUTOR_ARG_KEY = "entity_executor"
        const val WITH_MOVEMENT_ARG_KEY = "with_movement"
        const val MP_ARG_KEY = "mp"
        const val CAN_USE_SKILL_ARG_KEY = "can_use_skill"
        const val ATTACK_BONUS_ARG_KEY = "attack_bonus"
        const val HEAL_BONUS_ARG_KEY = "heal_bonus"
        const val ACCURACY_BONUS_ARG_KEY = "accuracy_bonus"
        const val ENTITY_TARGET_ARG_KEY = "entity_target"
        const val DAMAGE_ARG_KEY = "damage"
        const val HEAL_ARG_KEY = "heal"
        const val ENCOUNTER_ARG_KEY = "encounter"

        val eventCodes = arrayOf(START_TURN_EVENT, END_TURN_EVENT, START_MOVE_EVENT,
                END_MOVE_EVENT, COUNT_MP_EVENT, CAN_USE_SKILL_EVENT,
                START_SKILL_EVENT, END_SKILL_EVENT, ATTACK_BONUS_EVENT,
                HEAL_BONUS_EVENT, ACCURACY_BONUS_EVENT, ON_DAMAGE_EVENT,
                ON_HEAL_EVENT, ON_ENCOUNTER_EVENT, ON_DEATH_EVENT)
    }
}