package com.wolg_vlad.dcrawler.entities.enemies

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.entities.Enemy
import com.wolg_vlad.dcrawler.entities.ai.AI
import com.wolg_vlad.dcrawler.entities.ai.DummyAI
import com.wolg_vlad.dcrawler.ui.screens.DungeonScreen

/**
 * Created by Voyager on 11.12.2017.
 */
class Dummy(x: Float, y: Float) : Enemy(Texture("sprites/goblinwarrior.png"), Texture("goblin.png"), x, y) {
    var damageLog: Int
    var damageTurnLog: Int
    var healLog: Int
    var healTurnLog: Int
    var damageTurns: Int
    var healTurns: Int
    var damage: Float
    var heal: Float
    override fun createAI(): AI {
        return DummyAI(this)
    }

    override fun onDamage(damage: Int): Int {
        damageTurns++
        this.damage += damage.toFloat()
        DungeonScreen.changeLog("Damage: $damage", damageLog)
        DungeonScreen.changeLog("Damage per turn: " + this.damage / damageTurns, damageTurnLog)
        return damage
    }

    override fun onHeal(heal: Int): Int {
        healTurns++
        this.heal += heal.toFloat()
        DungeonScreen.changeLog("Damage: $heal", healLog)
        DungeonScreen.changeLog("Damage per turn: " + this.heal / healTurns, healTurnLog)
        return heal
    }

    init {
        challengeRating = 0f
        hp = 1000
        hpMax = 2000
        setMp(6)
        speed = 6
        armor = 0
        damageLog = DungeonScreen.addLog("")
        damageTurnLog = DungeonScreen.addLog("")
        healLog = DungeonScreen.addLog("")
        healTurnLog = DungeonScreen.addLog("")
        damageTurns = 0
        healTurns = 0
        damage = 0f
        heal = 0f
    }
}