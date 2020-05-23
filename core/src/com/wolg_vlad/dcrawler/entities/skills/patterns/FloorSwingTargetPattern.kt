package com.wolg_vlad.dcrawler.entities.skills.patterns

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

class FloorSwingTargetPattern(skill: Skill) : ValidatedTargetPattern(skill) {
    override fun createTarget(target: Target): Target {
        val range = skill.range
        val cellX = target.x
        val cellY = target.y
        val charX = skill.doer.tileX
        val charY = skill.doer.tileY
        if (cellY == charY && cellX > charX) {
            for (i in 0 until range) {
                val linked1 = Target(cellX, cellY + 1 + i)
                linked1.main = target
                linked1.isLinked = true
                val linked2 = Target(cellX - 1, cellY + 1 + i)
                linked2.main = target
                linked2.isLinked = true
                val linked3 = Target(cellX, cellY - 1 - i)
                linked3.main = target
                linked3.isLinked = true
                val linked4 = Target(cellX - 1, cellY - 1 - i)
                linked4.main = target
                linked4.isLinked = true
                target.addLinkedTarget(linked1)
                target.addLinkedTarget(linked2)
                target.addLinkedTarget(linked3)
                target.addLinkedTarget(linked4)
            }
            for (i in 1 until range) {
                for (j in 0 until 2 * range + 1) {
                    val linked = Target(cellX + i, cellY - range + j)
                    linked.main = target
                    linked.isLinked = true
                    target.addLinkedTarget(linked)
                }
            }
        } else if (cellY == charY && cellX < charX) {
            for (i in 0 until range) {
                val linked1 = Target(cellX, cellY + 1 + i)
                linked1.main = target
                linked1.isLinked = true
                val linked2 = Target(cellX + 1, cellY + 1 + i)
                linked2.main = target
                linked2.isLinked = true
                val linked3 = Target(cellX, cellY - 1 - i)
                linked3.main = target
                linked3.isLinked = true
                val linked4 = Target(cellX + 1, cellY - 1 - i)
                linked4.main = target
                linked4.isLinked = true
                target.addLinkedTarget(linked1)
                target.addLinkedTarget(linked2)
                target.addLinkedTarget(linked3)
                target.addLinkedTarget(linked4)
            }
            for (i in 1 until range) {
                for (j in 0 until 2 * range + 1) {
                    val linked = Target(cellX - i, cellY - range + j)
                    linked.main = target
                    linked.isLinked = true
                    target.addLinkedTarget(linked)
                }
            }
        } else if (cellX == charX && cellY > charY) {
            for (i in 0 until range) {
                val linked1 = Target(cellX + 1 + i, cellY)
                linked1.main = target
                linked1.isLinked = true
                val linked2 = Target(cellX + 1 + i, cellY - 1)
                linked2.main = target
                linked2.isLinked = true
                val linked3 = Target(cellX - 1 - i, cellY)
                linked3.main = target
                linked3.isLinked = true
                val linked4 = Target(cellX - 1 - i, cellY - 1)
                linked4.main = target
                linked4.isLinked = true
                target.addLinkedTarget(linked1)
                target.addLinkedTarget(linked2)
                target.addLinkedTarget(linked3)
                target.addLinkedTarget(linked4)
            }
            for (i in 1 until range) {
                for (j in 0 until 2 * range + 1) {
                    val linked = Target(cellX - range + j, cellY + i)
                    linked.main = target
                    linked.isLinked = true
                    target.addLinkedTarget(linked)
                }
            }
        } else if (cellX == charX && cellY < charY) {
            for (i in 0 until range) {
                val linked1 = Target(cellX + 1 + i, cellY)
                linked1.main = target
                linked1.isLinked = true
                val linked2 = Target(cellX + 1 + i, cellY + 1)
                linked2.main = target
                linked2.isLinked = true
                val linked3 = Target(cellX - 1 - i, cellY)
                linked3.main = target
                linked3.isLinked = true
                val linked4 = Target(cellX - 1 - i, cellY + 1)
                linked4.main = target
                linked4.isLinked = true
                target.addLinkedTarget(linked1)
                target.addLinkedTarget(linked2)
                target.addLinkedTarget(linked3)
                target.addLinkedTarget(linked4)
            }
            for (i in 1 until range) {
                for (j in 0 until 2 * range + 1) {
                    val linked = Target(cellX - range + j, cellY - i)
                    linked.main = target
                    linked.isLinked = true
                    target.addLinkedTarget(linked)
                }
            }
        }
        return target
    }
}