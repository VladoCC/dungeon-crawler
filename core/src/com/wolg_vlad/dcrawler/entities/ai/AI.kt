package com.wolg_vlad.dcrawler.entities.ai

import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.task.Task
import com.wolg_vlad.dcrawler.event.EntityEventAdapter
import com.wolg_vlad.dcrawler.math.MathAction
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

/**
 * Created by Voyager on 08.08.2017.
 */
abstract class AI(var controlledEntity: Entity) : EntityEventAdapter(), Cloneable {
    private val tasks = LinkedList<Task>()
    private var control = false

    private fun <R> handleTask(event: (Task) -> R): R? {
        if (control) {
            val task = tasks.peek()
            // task is always started before this moment,
            // so we shouldn't worry about activation or null checks
            val result = event.invoke(task)
            if (task.isComplete) {
                tasks.removeFirst()
                // we want to start new task or finish turn if there is no tasks
                activateTask()
            }
            return result
        } else {
            return null
        }
    }

    private fun activateTask() {
        val task = tasks.peek()
        if (task == null) {
            control = false
            Entity.nextTurn(controlledEntity)
            return
        }
        if (!task.isStarted) {
            task.start()
        }
    }

    fun addTask(task: Task) {
        task.entity = controlledEntity
        tasks.add(task)
    }

    abstract fun aiAnalyze()
    public override fun clone(): AI {
        return super.clone() as AI
    }

    override fun startTurn() {
        super.startTurn()
        aiAnalyze()
        control = true
        activateTask()
        if (tasks.peek() != null) {
            handleTask { t -> t.startTurn() }
        }
    }

    override fun startMove() {
        super.startMove()
        handleTask { t -> t.startMove() }
    }

    override fun endMove() {
        super.endMove()
        handleTask { t -> t.endMove() }
    }

    override fun countMp(withMovement: Boolean): Int {
        super.countMp(withMovement)
        return (handleTask { t ->  t.countMp(withMovement) })?: 0
    }

    override fun canUseSkill(): Boolean {
        super.canUseSkill()
        return handleTask { t: Task -> t.canUseSkill() }?: false
    }

    override fun startSkill() {
        super.startSkill()
        handleTask { t: Task -> t.startSkill() }
    }

    override fun endSkill() {
        super.endSkill()
        handleTask { t: Task -> t.endSkill() }
    }

    override fun attackBonus(action: MathAction): MathAction {
        super.attackBonus(action)
        return handleTask { t: Task -> t.attackBonus(action) }?: action
    }

    override fun onDamage(damage: Int): Int {
        super.onDamage(damage)
        return handleTask { t: Task -> t.onDamage(damage) }?: damage
    }

    override fun onHeal(heal: Int): Int {
        super.onHeal(heal)
        return handleTask { t: Task -> t.onHeal(heal) }?: heal
    }

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        super.accuracyBonus(accuracy, target)
        return handleTask { t: Task -> t.accuracyBonus(accuracy, target) }?: accuracy
    }

    override fun onEncounter(encounter: Encounter) {
        super.onEncounter(encounter)
        handleTask { t: Task -> t.onEncounter(encounter) }
    }

    override fun onDeath() {
        super.onDeath()
        handleTask { obj: Task -> obj.onDeath() }
    }

}