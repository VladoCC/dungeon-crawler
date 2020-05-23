package com.wolg_vlad.dcrawler.entities.skills.check

import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target

/**
 * Class for checks. Returns result of attack at this target.
 */
abstract class PlayCheck(var skill: Skill) {
    abstract fun check(target: Target): Int

}