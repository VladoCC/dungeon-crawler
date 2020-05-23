package com.wolg_vlad.dcrawler.entities.skills

import com.wolg_vlad.dcrawler.entities.skills.check.EntityPlayCheck

/**
 * Created by Voyager on 19.04.2018.
 */
class PlayContainer(skill: Skill) : Play(skill) {
    val floorPlay: Play = this
    val entityPlay: Play
    val playerPlay: Play
    val enemyPlay: Play
    override fun isValidTarget(target: Target): Boolean {
        return true
    }

    init {
        entityPlay = object : Play(skill) {
            // any entity
            override fun isValidTarget(target: Target): Boolean {
                return target.entity != null
            }
        }
        addPlay(entityPlay)
        playerPlay = object : Play(skill) {
            override fun isValidTarget(target: Target): Boolean {
                return target.entity != null && target.entity?.isCharacter?:false
            }
        }
        playerPlay.setPlayCheck(EntityPlayCheck(skill))
        entityPlay.addPlay(playerPlay)
        enemyPlay = object : Play(skill) {
            override fun isValidTarget(target: Target): Boolean {
                return target.entity != null && target.entity?.isEnemy?:false
            }
        }
        enemyPlay.setPlayCheck(EntityPlayCheck(skill))
        entityPlay.addPlay(enemyPlay)
    }
}