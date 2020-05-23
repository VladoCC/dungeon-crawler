package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.utils.Array
import com.wolg_vlad.dcrawler.entities.skills.action.Action
import com.wolg_vlad.dcrawler.entities.skills.check.PlayCheck

/**
 * Created by Voyager on 28.06.2017.
 */
abstract class Play(private val skill: Skill) {
    private var playCheck: PlayCheck
    private val actions = Array<Action>()
    private val plays = Array<Play>()
    abstract fun isValidTarget(target: Target): Boolean
    fun check(target: Target): Int {
        return playCheck.check(target)
    }

    fun addAction(action: Action): Play {
        actions.add(action)
        return this
    }

    fun addPlay(play: Play): Play {
        plays.add(play)
        return this
    }

    fun act(target: Target, mark: FloatingDamageMark) {
        if (isValidTarget(target)) {
            val success = check(target)
            for (action in actions) {
                action.act(target, success, mark)
            }
            for (play in plays) {
                play.act(target, mark)
            }
        }
    }

    fun setPlayCheck(playCheck: PlayCheck): Play {
        this.playCheck = playCheck
        return this
    }

    companion object {
        const val TARGETING_CRIT_MISS = 0
        const val TARGETING_MISS = 1
        const val TARGETING_HIT = 2
        const val TARGETING_CRIT_HIT = 3
    }

    init {
        playCheck = object : PlayCheck(skill) {
            override fun check(target: Target): Int {
                return TARGETING_MISS
            }
        }
    }
}