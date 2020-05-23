package com.wolg_vlad.dcrawler.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.effects.Effect
import com.wolg_vlad.dcrawler.effects.EffectArray
import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.ai.AI
import com.wolg_vlad.dcrawler.entities.enemies.GoblinWarrior
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.math.MathAction

/**
 * Created by Voyager on 08.08.2017.
 */
abstract class Enemy(model: Texture, portrait: Texture, x: Float, y: Float) : Entity(model, portrait, x, y, 0, 0, 0, 0, 0), Cloneable {
    private var enemyAI: AI? = null
    private var skillTime = 0f
    var challengeRating = 0f
        protected set
    var isSolo = false
    var isActivated = false
        private set

    protected fun addToCRTable() {
        addToCRTable(this)
    }

    @JvmOverloads
    fun activateAI(ai: AI? = createAI()) {
        enemyAI = ai
        isActivated = true
    }

    abstract fun createAI(): AI

    override val isEnemy: Boolean
        get() = true

    fun setEnemyAI(enemyAI: AI?) {
        if (isActivated) {
            this.enemyAI = enemyAI
        } else {
            activateAI(enemyAI)
        }
    }

    fun useAI() {
        enemyAI!!.aiAnalyze()
    }

    fun addSkillTime(delta: Float) {
        if (isSkillUse) {
            skillTime += delta
            if (skillTime >= SKILL_TIME) {
                skillTime = 0f
                useSkill()
            }
            println("Enemy Skill Time: $skillTime")
        }
    }

    public override fun clone(): Enemy {
        val enemy = super.clone() as Enemy
        enemy.setSprite(model)
        val skills = Array<Skill>()
        for (skill in this.skills) {
            skills.add(skill)
        }
        enemy.skills = skills
        enemy.model = model
        val effects = EffectArray()
        for (effect in getEffects()) {
            effects.add(effect.clone() as Effect)
        }
        enemy.setEffects(effects)
        return enemy
    }

    override fun endTurn() {
        super.endTurn()
        if (!isControlled) {
            enemyAI!!.endTurn()
        }
    }

    override fun startTurn() {
        super.startTurn()
        if (!isControlled) {
            enemyAI!!.startTurn()
        }
    }

    override fun startMove() {
        super.startMove()
        if (!isControlled) {
            enemyAI!!.startMove()
        }
    }

    override fun endMove() {
        super.endMove()
        if (!isControlled) {
            enemyAI!!.endMove()
        }
    }

    override fun countMp(withMovement: Boolean): Int {
        var mp = super.countMp(withMovement)
        if (!isControlled) {
            mp += enemyAI!!.countMp(withMovement)
        }
        return mp
    }

    override fun canUseSkill(): Boolean {
        return super.canUseSkill()
    }

    override fun startSkill() {
        super.startSkill()
        if (!isControlled && enemyAI != null) {
            enemyAI!!.startSkill()
        }
    }

    override fun endSkill() {
        super.endSkill()
        if (!isControlled && enemyAI != null) {
            enemyAI!!.endSkill()
        }
    }

    override fun attackBonus(action: MathAction): MathAction {
        var action = action
        action = super.attackBonus(action)
        if (!isControlled && enemyAI != null) {
            action = enemyAI!!.attackBonus(action)!!
        }
        return action
    }

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        var accuracy = accuracy
        accuracy = super.accuracyBonus(accuracy, target)
        if (!isControlled && enemyAI != null) {
            accuracy = enemyAI!!.accuracyBonus(accuracy, target)
        }
        return accuracy
    }

    override fun onDamage(damage: Int): Int {
        var damage = damage
        damage = super.onDamage(damage)
        if (!isControlled && enemyAI != null) {
            damage = enemyAI!!.onDamage(damage)
        }
        return damage
    }

    override fun onHeal(heal: Int): Int {
        var heal = heal
        heal = super.onHeal(heal)
        if (!isControlled && enemyAI != null) {
            heal = enemyAI!!.onHeal(heal)
        }
        return heal
    }

    override fun onEncounter(encounter: Encounter) {
        super.onEncounter(encounter)
        if (!isControlled && enemyAI != null) {
            enemyAI!!.onEncounter(encounter)
        }
    }

    override fun onDeath() {
        super.onDeath()
        if (!isControlled && enemyAI != null) {
            enemyAI!!.onDeath()
        }
    }

    companion object {
        private val crTable = CRTable()
        private const val SKILL_TIME = 1f
        fun createCRTable() {
            GoblinWarrior(0F, 0F).addToCRTable()
        }

        private fun addToCRTable(enemy: Enemy) {
            crTable.add(enemy)
        }

        fun getEnemyByCR(cr: Float, type: Int): Enemy? {
            return crTable.getEnemy(cr, type)
        }

        fun getEnemyByFormula(type: Int): Enemy {
            return crTable.getEnemyByFormula(type)
        }
    }
}