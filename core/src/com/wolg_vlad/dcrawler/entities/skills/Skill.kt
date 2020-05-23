package com.wolg_vlad.dcrawler.entities.skills

import com.badlogic.gdx.graphics.Texture
import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.AIUtils
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.entities.skills.patterns.TargetPattern
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetDisplayer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TargetRenderer
import com.wolg_vlad.dcrawler.entities.skills.targeting.TilePos
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.event.EventController
import com.wolg_vlad.dcrawler.math.DiceAction
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.utils.Utils
import java.util.*

/**
 * Created by Voyager on 29.06.2017.
 */
abstract class Skill(val doer: Entity) {
    var plays = mutableListOf<Play>()
    var targets = mutableListOf<Target>()
    var isCooldown = false
    /** just an iterators  */
    var targetCount = 0
    var cooldownCount = 0
    val renderer: TargetRenderer = TargetRenderer(this)
    abstract val displayers: Array<TargetDisplayer>
    abstract val pattern: TargetPattern
    abstract val icon: Texture
    abstract val name: String
    abstract val description: String
    abstract val skillAccuracyBonus: Int
    abstract val range: Int
    abstract val distanceMin: Int
    abstract val distanceMax: Int
    abstract val targetCountMax: Int
    abstract val cooldownMax: Int
    abstract val type: Int
    abstract val targetType: Int
    abstract val isCheckAllTargets: Boolean
    abstract val isMarkEverything: Boolean
    abstract val isMark: Boolean
    abstract val isObstruct: Boolean
    abstract val isWallTargets: Boolean
    //TODO add some sort of action independent from targets
    open fun use() { // TODO add play for self
        for (target in targets) {
            val mark = FloatingDamageMark(target.x, target.y, "Miss")
            for (play in plays) {
                play.act(target, mark)
            }
            if (isMark && (isMarkEverything || target.entity != null)) {
                mark.show()
            }
        }
        clearTargets()
        undrawTargets()
        startCooldown()
    }

    fun getAccuracyBonus(target: Entity): Int {
        return doer.getAccuracyStat(target) + skillAccuracyBonus
    }

    fun countAttackAction(action: MathAction): MathAction {
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.ATTACK_BONUS_ARG_KEY] = action
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = doer
        return EventController.callEvent(EntityEvent.ATTACK_BONUS_EVENT, map)[EntityEvent.ATTACK_BONUS_ARG_KEY] as MathAction
    }

    fun countHealAction(action: MathAction): MathAction {
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.HEAL_BONUS_ARG_KEY] = action
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = doer
        return EventController.callEvent(EntityEvent.HEAL_BONUS_EVENT, map)[EntityEvent.HEAL_BONUS_ARG_KEY] as MathAction
    }

    fun addPlayContainer(): PlayContainer {
        val playContainer = PlayContainer(this)
        plays.add(playContainer)
        return playContainer
    }

    val typeString: String
        get() {
            when (type) {
                SKILL_TYPE_AT_WILL -> return "At will"
                SKILL_TYPE_COOLDOWN -> return "Cooldown: $cooldownMax turns"
                SKILL_TYPE_COOLDOWN_DICE -> return "Cooldown on $dices"
                SKILL_TYPE_ENCOUNTER -> return "Encounter"
                SKILL_TYPE_DAILY -> return "Daily"
            }
            return ""
        }

    private val dices: String
        private get() {
            var dices = ""
            if (type == SKILL_TYPE_COOLDOWN_DICE) {
                for (i in 0 until cooldownMax) {
                    dices += diceFaces[i]
                }
            }
            return dices
        }

    val targetTypeString: String
        get() {
            when (targetType) {
                SKILL_TARGET_TYPE_ENTITY -> return "Entity"
                SKILL_TARGET_TYPE_FLOOR_SPLASH -> return "Splash"
                SKILL_TARGET_TYPE_FLOOR_WAVE -> return "Wave"
                SKILL_TARGET_TYPE_FLOOR_SWING -> return "Swing"
                SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE -> return "Controllable wave"
                SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER -> return "Splash without center"
                SKILL_TARGET_TYPE_ENEMY -> return "Enemy"
                SKILL_TARGET_TYPE_CHARACTER -> return "Character"
                SKILL_TARGET_TYPE_FLOOR_SINGLE -> return "Single cell"
                SKILL_TARGET_TYPE_SELF -> return "Self"
            }
            return ""
        }

    fun addTarget(target: Target) {
        var target = target
        target = target.main
        if (pattern.isValid(target) && Utils.isTargetInDistance(target.checkX, target.checkY, doer.tileX, doer.tileY, distanceMin, distanceMax) && targetCount < targetCountMax) {
            target = pattern.createTarget(target)
            if (target != null && targetObstructionCheck(target, false)) {
                var added = false
                if (targetWallCheck(target) && (!isCheckAllTargets || targetObstructionCheck(target, true))) {
                    targets.add(target)
                    added = true
                }
                for (linked in target.linkedTargets) {
                    if (targetWallCheck(linked) && (!isCheckAllTargets || targetObstructionCheck(linked, true))) {
                        targets.add(linked)
                        added = true
                    }
                }
                if (added) {
                    targetCount++
                }
                drawTargets()
            }
        }
    }

    fun clearTargets() {
        targets.clear()
        targetCount = 0
    }

    fun hasTarget(target: Target?): Boolean {
        return targets.contains(target)
    }

    fun removeTarget(target: Target?) {
        var target = target
        for (skillTarget in targets) {
            if (skillTarget == target) {
                target = skillTarget
            }
        }
        val main: Target?
        main = if (target!!.isLinked) {
            target.main
        } else {
            target
        }
        for (linked in main.linkedTargets) {
            targets.remove(linked)
        }
        targets.remove(main)
        targetCount = targetCount - 1
    }

    fun drawTargets() {
        DungeonMap.drawTargets(targets)
    }

    fun undrawTargets() {
        DungeonMap.clearTargetLayer()
    }

    /*public void setTypeDisplayer(int targetType){
        clearDisplayers();
        addDisplayer(new RaytraceDisplayer());
        switch (targetType){
            case SKILL_TARGET_TYPE_ENTITY:
                addDisplayer(new EntityDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SPLASH:
                addDisplayer(new SplashDisplayer(true));
                break;
            case SKILL_TARGET_TYPE_FLOOR_WAVE:
                addDisplayer(new WaveDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SWING:
                addDisplayer(new SwingDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE:
                break;
            case SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER:
                addDisplayer(new SplashDisplayer(false));
                break;
            case SKILL_TARGET_TYPE_ENEMY:
                addDisplayer(new EnemyDisplayer());
                break;
            case SKILL_TARGET_TYPE_CHARACTER:
                addDisplayer(new CharacterDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SINGLE:
                addDisplayer(new FloorSingleDisplayer());
                break;
            case SKILL_TARGET_TYPE_SELF:
                break;
        }
    }

    protected void clearDisplayers() {
        renderer.clearDisplayers();
    }

    protected void addDisplayer(TargetDisplayer targetDisplayer) {
        renderer.addDisplayer(targetDisplayer);
    }*/
    fun displayTarget(x: Int, y: Int): List<TilePos>? {
        if (renderer.displayers.size == 0) {
            renderer.addDisplayers(displayers)
        }
        return renderer.displayTarget(x, y)
    }

    private fun targetWallCheck(target: Target): Boolean {
        return if (isWallTargets) {
            true
        } else {
            val node = GraphStorage.getNodeBottom(target.x, target.y)
            node != null && node.cell.tile.groundTile
        }
    }

    /** can check real X and Y or checkX and checkY
     * return true if path not obstructed */
    private fun targetObstructionCheck(target: Target, realCoords: Boolean): Boolean {
        val x = if (realCoords) target.x else target.checkX
        val y = if (realCoords) target.y else target.checkY
        return if (!isObstruct) {
            true
        } else {
            !AIUtils.isPathObstructed(doer.tileX, doer.tileY, x, y)
        }
    }

    fun startCooldown() {
        if (type != SKILL_TYPE_AT_WILL) {
            isCooldown = true
            if (type == SKILL_TYPE_COOLDOWN) {
                cooldownCount = cooldownMax
            }
        }
    }

    fun cooldown() {
        if (isCooldown && type == SKILL_TYPE_COOLDOWN) {
            cooldownCount = cooldownCount - 1
            if (cooldownCount == 0) {
                isCooldown = false
            }
        } else if (isCooldown && type == SKILL_TYPE_COOLDOWN_DICE) {
            if (standardDice.act() <= cooldownMax) {
                isCooldown = false
            }
        }
    }

    fun endEncounter() {
        if (type == SKILL_TYPE_ENCOUNTER) {
            isCooldown = false
        }
    }

    fun endDay() {
        if (type == SKILL_TYPE_DAILY) {
            isCooldown = false
        }
    }

    companion object {
        const val SKILL_TYPE_AT_WILL = 0
        const val SKILL_TYPE_COOLDOWN = 1
        const val SKILL_TYPE_ENCOUNTER = 2
        const val SKILL_TYPE_DAILY = 3
        const val SKILL_TYPE_COOLDOWN_DICE = 4
        const val SKILL_TARGET_TYPE_ENTITY = 0
        const val SKILL_TARGET_TYPE_FLOOR_SPLASH = 1
        /**area centered in chosen point  */
        const val SKILL_TARGET_TYPE_FLOOR_WAVE = 2
        /** line that starts in chosen point  */
        const val SKILL_TARGET_TYPE_FLOOR_SWING = 3
        const val SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE = 4
        const val SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER = 5
        const val SKILL_TARGET_TYPE_ENEMY = 6
        const val SKILL_TARGET_TYPE_CHARACTER = 7
        const val SKILL_TARGET_TYPE_FLOOR_SINGLE = 8
        const val SKILL_TARGET_TYPE_SELF = 9
        private val standardDice = DiceAction(6)
        private val diceFaces = charArrayOf('⚅', '⚄', '⚃', '⚂', '⚁', '⚀')
    }

    init {
    }
}