package com.wolg_vlad.dcrawler.entities

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.wolg_vlad.dcrawler.dungeon.DungeonMap
import com.wolg_vlad.dcrawler.effects.AttackEffect
import com.wolg_vlad.dcrawler.effects.Effect
import com.wolg_vlad.dcrawler.effects.EffectArray
import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.NodePath
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.entities.skills.Target
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.event.EntityEventAdapter
import com.wolg_vlad.dcrawler.event.EventController
import com.wolg_vlad.dcrawler.math.MathAction
import com.wolg_vlad.dcrawler.ui.screens.DungeonScreen
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import java.util.*
import java.util.stream.Collectors

/**
 * Created by Voyager on 24.04.2017.
 */
open class Entity(var model: Texture, portrait: Texture, private var x: Float, private var y: Float, hp: Int, hpMax: Int, mp: Int, mpMax: Int, armor: Int) : EntityEventAdapter() {
    private var movementDirection = -1
    var isMovement = false
        private set
    var isThrowing = false
    var isMoved = false
        private set
    var isSkillUse = false
    var isControlled = false
    var isImmobilized = false
    var isRoomOpened = false
    var isAlive = true
        private set
    private var throwXDirection = false
    private var throwYDirection = false
    private var throwXEnd = false
    private var throwYEnd = false
    val portrait: Texture
    private var sprite: Sprite? = null
    private var animations: Array<Animation<*>>? = null
    var path: NodePath? = null
    var usedSkill: Skill? = null
    private set
    private var effects: EffectArray
    var skills: com.badlogic.gdx.utils.Array<Skill>
    private var movementSpeed = 0f
    private var movementLength = 0f
    private var throwXSpeed = 0f
    private var throwYSpeed = 0f
    private var throwXDistance = 0f
    private var throwYDistance = 0f
    var animTime = 0f
    /**
     * this func changes hp without calling listeners. Use addHp() to call with it
     */
    var hp: Int
    var hpMax: Int
    var rawMp: Int
        private set
    var speed: Int
        get() = field
        set
    var detailedEffect = -1
    var detailedSkill = -1
    var armor: Int
    var accuracyBonus = 0

    fun add() {
        add(this)
    }

    fun add(pos: Int) {
        add(pos, this)
    }

    protected fun setSprite(texture: Texture) {
        sprite = Sprite(texture, ENTITY_WIDTH, ENTITY_HEIGHT)
        sprite!!.setPosition(x, y)
        sprite!!.flip(false, true)
    }

    private fun setTexture(texture: Texture) {
        val anims = texture.height / ENTITY_HEIGHT
        val sprites = texture.width / ENTITY_WIDTH
        val textures = TextureRegion.split(texture, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
        animations = Array(anims) {
            val regions = com.badlogic.gdx.utils.Array<TextureRegion?>()
            for (j in 0 until sprites) {
                val region = textures[it][j]
                region.flip(false, true)
                regions.add(region)
            }
            val animation: Animation<*> = Animation<Any?>(MOVEMENT_TIME / regions.size, regions)
            animation.playMode = Animation.PlayMode.LOOP
            return@Array animation
        }
    }

    fun setMovement() {
        if (path != null) {
            isMovement = true
            updateSkills()
            addMp(-(path!!.nodeCount - 1))
            DungeonMap.updateEntityPos(this)
            val mapStart: HashMap<String, Any> = HashMap()
            mapStart[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
            EventController.callEvent(EntityEvent.START_MOVE_EVENT, mapStart)
            planMovement()
        }
    }

    private fun planMovement() {
        if (path == null || path!!.nodeCount == 0) {
            movementDirection = -1
            isMovement = false
        } else if (path!!.nodeCount == 1) {
            path = null
            movementDirection = -1
            isMovement = false
        } else {
            val lastNode = path!![0]
            val nextNode = path!![1]
            if (lastNode.x != nextNode.x || lastNode.y != nextNode.y) {
                if (lastNode.x < nextNode.x) { //System.out.println("MOVING RIGHT!");
                    movementDirection = DIRECTION_RIGHT
                } else if (lastNode.x > nextNode.x) { //System.out.println("MOVING LEST!");
                    movementDirection = DIRECTION_LEFT
                } else if (lastNode.y < nextNode.y) { //System.out.println("MOVING DOWN!");
                    movementDirection = DIRECTION_DOWN
                } else if (lastNode.y > nextNode.y) { //System.out.println("MOVING UP!");
                    movementDirection = DIRECTION_UP
                }
                path!!.remove(0)
                val cellDim: Int
                cellDim = if (movementDirection % 2 == 0) {
                    DungeonTile.TILE_HEIGHT
                } else {
                    DungeonTile.TILE_WIDTH
                }
                movementSpeed = cellDim / MOVEMENT_TIME
                movementLength = cellDim.toFloat()
            }
        }
    }

    fun move(delta: Float) {
        var stepLength = movementSpeed * delta
        var movementEnd = false
        if (movementLength > stepLength) {
            movementLength -= stepLength
        } else {
            movementEnd = true
            stepLength = movementLength
            movementLength = 0f
        }
        println("Direction: $movementDirection")
        if (movementDirection == DIRECTION_UP) { //System.out.println("MOVING UP!");
            y -= stepLength
            sprite!!.y = y
        } else if (movementDirection == DIRECTION_RIGHT) { //System.out.println("MOVING RIGHT!");
            x += stepLength
            sprite!!.x = x
        } else if (movementDirection == DIRECTION_DOWN) { //System.out.println("MOVING DOWN!");
            y += stepLength
            sprite!!.y = y
        } else if (movementDirection == DIRECTION_LEFT) { //System.out.println("MOVING LEFT!");
            x -= stepLength
            sprite!!.x = x
        }
        if (movementEnd) {
            println("Precise")
            x = Math.round(x / DungeonTile.TILE_WIDTH) * DungeonTile.TILE_WIDTH.toFloat()
            y = Math.round(y / DungeonTile.TILE_HEIGHT) * DungeonTile.TILE_HEIGHT.toFloat()
            var lastX = x / DungeonTile.TILE_WIDTH
            var lastY = y / DungeonTile.TILE_HEIGHT
            val thisX = lastX
            val thisY = lastY
            if (movementDirection == DIRECTION_UP) {
                lastY += 1f
            } else if (movementDirection == DIRECTION_RIGHT) {
                lastX -= 1f
            } else if (movementDirection == DIRECTION_DOWN) {
                lastY -= 1f
            } else if (movementDirection == DIRECTION_LEFT) {
                lastX += 1f
            }
            val lastCell = DungeonMap.getCell(lastX.toInt(), lastY.toInt())
            val cell = DungeonMap.getCell(thisX.toInt(), thisY.toInt())
            /*if (lastCell.getEffect() != null){
                CellEffect effect = lastCell.getEffect();
                effect.onStepFrom((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);
            }
            if (cell.getEffect() != null){
                cell.getEffect().onStepTo((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);
            }*/lastCell!!.onStepFrom(lastX.toInt(), lastY.toInt(), thisX.toInt(), thisY.toInt(), this)
            cell!!.onStepTo(lastX.toInt(), lastY.toInt(), thisX.toInt(), thisY.toInt(), this)
            planMovement()
            if (path == null || path!!.nodeCount < 1) {
                val mapEnd: HashMap<String, Any> = HashMap()
                mapEnd[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
                EventController.callEvent(EntityEvent.END_MOVE_EVENT, mapEnd)
                updateSkills()
            }
        }
    }

    fun getSprite(): Sprite? {
        return if (animations == null) {null} else if (isMovement) {
            var animNum = 0
            if (animations!!.size > movementDirection) {
                animNum = movementDirection
            }
            val texture = animations!![animNum].getKeyFrame(animTime) as TextureRegion

            val sprite = Sprite(texture)
            sprite.setPosition(x, y)
            //sprite.flip(false, true);
            sprite
        } else {
            sprite
        }
    }

    fun teleport(tiles: Vector2) {
        x += tiles.x * DungeonTile.TILE_WIDTH
        y += tiles.y * DungeonTile.TILE_HEIGHT
        sprite!!.setPosition(x, y)
    }

    fun setX(x: Float) {
        this.x = x
        sprite!!.setPosition(this.x, y)
    }

    fun setY(y: Float) {
        this.y = y
        sprite!!.setPosition(x, this.y)
    }

    fun setPos(x: Float, y: Float) {
        setX(x)
        setY(y)
    }

    fun setPos(pos: Vector2) {
        setPos(pos.x, pos.y)
    }

    fun setCellPos(x: Int, y: Int) {
        setX(x * DungeonTile.TILE_WIDTH.toFloat())
        setY(y * DungeonTile.TILE_HEIGHT.toFloat())
    }

    fun setCellPos(pos: Vector2) {
        setCellPos(pos.x.toInt(), pos.y.toInt())
    }

    fun throwEntity(tiles: Vector2) {
        println("throw " + tiles.x + " " + tiles.y)
        val x = Math.abs(tiles.x)
        val y = Math.abs(tiles.y)
        if (x > y) {
            throwXSpeed = DungeonTile.TILE_WIDTH / THROW_TIME
            throwYSpeed = throwXSpeed * y / x
        } else {
            throwYSpeed = DungeonTile.TILE_HEIGHT / THROW_TIME
            throwXSpeed = throwYSpeed * x / y
        }
        println("x - $throwXSpeed, y - $throwYSpeed")
        throwXDirection = tiles.x >= 0
        throwYDirection = tiles.y >= 0
        throwXDistance = x * DungeonTile.TILE_WIDTH
        throwYDistance = y * DungeonTile.TILE_HEIGHT
        isThrowing = true
        throwXEnd = false
        throwYEnd = false
        DungeonMap.updateEntityPos(this, tileX, tileY, tileX + tiles.x.toInt(), tileY + tiles.y.toInt())
    }

    fun throwing(delta: Float) {
        println("throwing")
        var stepXLength = throwXSpeed * delta
        var stepYLength = throwYSpeed * delta
        if (throwXDistance > stepXLength) {
            throwXDistance -= stepXLength
        } else {
            throwXEnd = true
            stepXLength = throwXDistance
            throwXDistance = 0f
        }
        if (throwYDistance > stepYLength) {
            throwYDistance -= stepYLength
        } else {
            throwYEnd = true
            stepYLength = throwYDistance
            throwYDistance = 0f
        }
        if (throwXEnd && throwYEnd) {
            isThrowing = false
        }
        if (!throwXDirection) {
            stepXLength = -stepXLength
        }
        if (!throwYDirection) {
            stepYLength = -stepYLength
        }
        x += stepXLength
        y += stepYLength
        if (!isThrowing) {
            x = Math.round(x / DungeonTile.TILE_WIDTH) * DungeonTile.TILE_WIDTH.toFloat()
            y = Math.round(y / DungeonTile.TILE_HEIGHT) * DungeonTile.TILE_HEIGHT.toFloat()
        }
        sprite!!.setPosition(x, y)
    }

    val tileX: Int
        get() = Math.round(x / DungeonTile.TILE_WIDTH)

    val tileY: Int
        get() = Math.round(y / DungeonTile.TILE_HEIGHT)

    fun getX(): Float {
        return x
    }

    fun getY(): Float {
        return y
    }

    fun addTargets(targets: List<Target>) {
        for (target in targets) {
            addOrRemoveSkillTarget(target)
        }
    }

    fun addOrRemoveSkillTarget(target: Target) {
        if (isSkillUse) {
            path = null
            //pathLayer.clearLayer();
            val skill = usedSkill
            if (skill!!.hasTarget(target)) {
                skill.removeTarget(target)
            } else {
                skill.addTarget(target)
            }
            skill.drawTargets()
        }
    }

    fun addOrRemoveSkillTarget(cellX: Int, cellY: Int) {
        addOrRemoveSkillTarget(Target(cellX, cellY))
    }

    fun triggerEncounter() {
        Encounter.randomEncounter.trigger(this)
    }

    open val isCharacter: Boolean
        get() = false

    open val isEnemy: Boolean
        get() = false

    fun addHp(change: Int): Int {
        var change = change
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
        if (change > 0) {
            map[EntityEvent.HEAL_ARG_KEY] = change
            change = EventController.callEvent(EntityEvent.ON_HEAL_EVENT, map)[EntityEvent.HEAL_ARG_KEY] as Int
            if (hp + change > hpMax) {
                hp = hpMax
                change = hpMax - hp
            } else {
                hp += change
            }
        } else if (change < 0) {
            map[EntityEvent.DAMAGE_ARG_KEY] = Math.abs(change)
            change = -(EventController.callEvent(EntityEvent.ON_DAMAGE_EVENT, map)[EntityEvent.DAMAGE_ARG_KEY] as Int)
            if (hp <= change) {
                val lastHp = hp
                hp = 0
                change = lastHp
            } else {
                hp += change
            }
        }
        if (hp < 1) {
            EventController.callEvent(EntityEvent.ON_DEATH_EVENT, map)
        }
        return change
    }

    fun changeHpMax(change: Int) {
        hpMax += change
    }

    fun getMp(withMovement: Boolean): Int {
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
        map[EntityEvent.WITH_MOVEMENT_ARG_KEY] = withMovement
        val mpEffects = EventController.callEvent(EntityEvent.COUNT_MP_EVENT, map)[EntityEvent.MP_ARG_KEY] as Int
        val countedMp: Int
        return if (isImmobilized) {
            0
        } else if (withMovement && path != null && !isMovement) {
            countedMp = rawMp - path!!.nodeCount + 1 + mpEffects
            //System.out.println("Pay attention, please: mp is " + countedMp);
            if (countedMp > 0) {
                countedMp
            } else {
                0
            }
        } else {
            countedMp = rawMp + mpEffects
            //System.out.println("Pay attention, please: mp is " + countedMp);
            if (countedMp > 0) {
                countedMp
            } else {
                0
            }
        }
    }

    fun addMp(mp: Int) {
        rawMp += mp
    }

    fun setMp(mp: Int) {
        rawMp = mp
    }

    fun getAccuracyStat(target: Entity): Int {
        val map: HashMap<String, Any> = HashMap()
        map[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
        map[EntityEvent.ENTITY_TARGET_ARG_KEY] = target
        map[EntityEvent.ACCURACY_BONUS_ARG_KEY] = accuracyBonus
        return EventController.callEvent(EntityEvent.ACCURACY_BONUS_EVENT, map)[EntityEvent.ACCURACY_BONUS_ARG_KEY] as Int
    }

    fun countAccuracy(): Int { //TODO upgrade
        return accuracyBonus
    }

    open fun turnIsEnded(): Boolean {
        return false
    }

    fun addAnimTime(delta: Float) {
        animTime += delta
    }

    fun setEffects(effects: EffectArray) {
        this.effects = effects
    }

    fun addEffect(effect: Effect) {
        effects.add(effect)
    }

    fun addEffect(pos: Int, effect: Effect) {
        effects.add(pos, effect)
    }

    fun addFirstEffectNowPlaying(effect: Effect) {
        nowPlaying!!.addEffect(0, effect)
    }

    fun removeEffect(id: Int) {
        effects.removeId(id)
    }

    fun removeEffect(effect: Effect?) {
        effects.remove(effect)
    }

    fun checkRemoveEffect(index: Int) {
        val effect = effects[index]
        if (effect.delete) {
            val id = effect.checkMarker
            removeEffect(id)
            println("Removing effect: " + id + "EffectArray size: " + effects.size)
        }
    }

    fun checkRemoveEffects() {
        effects.removeIf { it.delete }
    }

    fun clearEffects() {
        effects.clear()
    }

    fun getEffects(): ArrayList<Effect> {
        return effects
    }

    fun addSkill(skill: Skill) {
        skills.add(skill)
    }

    fun removeSkill(index: Int) {
        skills.removeIndex(index)
    }

    fun removeSkill(skill: Skill) {
        skills.removeValue(skill, true)
    }

    fun setUsedSkill(usedSkill: Skill?) {
        isUpdateSkills = true
        this.usedSkill = usedSkill
        if (usedSkill != null) {
            isSkillUse = true
            val mapStart: HashMap<String, Any> = HashMap()
            mapStart[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
            EventController.callEvent(EntityEvent.START_SKILL_EVENT, mapStart)
        } else {
            isSkillUse = false
        }
    }

    fun useSkill() {
        if (isSkillUse && usedSkill != null) {
            usedSkill!!.use()
            usedSkill = null
            isSkillUse = false
            val mapEnd: HashMap<String, Any> = HashMap()
            mapEnd[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = this
            EventController.callEvent(EntityEvent.END_SKILL_EVENT, mapEnd)
            isUpdateSkills = true
        }
    }

    /** Events  */
    override fun endTurn() {
        if (isCharacter && !isRoomOpened) {
            triggerEncounter()
        }
        isRoomOpened = false
        for (i in effects.indices) {
            effects[i].endTurn()
            println("End of turn")
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.endTurn()
        for (i in 0 until skills.size) {
            skills[i].cooldown()
        }
        isMoved = false
        updateEffects()
    }

    override fun startTurn() {
        isMoved = false
        setMp(speed)
        //setMp(0);
        if (isCharacter xor isControlled) {
            addFirstEffectNowPlaying(AttackEffect(this))
        }
        checkRemoveEffects()
        for (i in effects.indices) {
            effects[i].startTurn()
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.startTurn()
        updateEffects()
    }

    override fun startMove() {
        for (i in effects.indices) {
            effects[i].startMove()
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.startMove()
        isMoved = true
        updateEffects()
    }

    override fun endMove() {
        println("MP: " + getMp(false) + " : " + getMp(true))
        for (i in effects.indices) {
            effects[i].endMove()
        }
        checkRemoveEffects()
        println("MP: " + getMp(false) + " : " + getMp(true))
        DungeonMap.getCell(tileX, tileY)!!.endMove()
        updateEffects()
    }

    override fun countMp(withMovement: Boolean): Int {
        var mpEffect = 0
        for (i in effects.indices) {
            mpEffect += effects[i].countMp(withMovement)
        }
        checkRemoveEffects()
        mpEffect += DungeonMap.getCell(tileX, tileY)!!.countMp(withMovement)
        updateEffects()
        return mpEffect
    }

    override fun canUseSkill(): Boolean {
        var use = false
        if (!isMovement) {
            for (i in effects.indices) {
                if (effects[i].skillUse) {
                    use = effects[i].canUseSkill()
                }
            }
            checkRemoveEffects()
            val cell = DungeonMap.getCell(tileX, tileY)
            /** calls to cell.canUseSkill() not working properly */
            for (effect in cell!!.effects) {
                if (effect!!.skillUse) {
                    use = effect.canUseSkill()
                }
            }
            println("Can use: $use")
        }
        updateEffects()
        return use
    }

    override fun startSkill() {
        for (i in effects.indices) {
            effects[i].startSkill()
        }
        checkRemoveEffects()
        DungeonMap.drawTargetingZone(usedSkill!!)
        DungeonMap.getCell(tileX, tileY)!!.startSkill()
        updateEffects()
    }

    override fun endSkill() {
        for (i in effects.indices) {
            effects[i].endSkill()
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.endSkill()
        DungeonMap.clearTargetingZoneLayer()
        updateEffects()
    }

    override fun attackBonus(action: MathAction): MathAction {
        var action: MathAction = action
        for (i in effects.indices) {
            action = effects[i].attackBonus(action)
        }
        checkRemoveEffects()
        action = DungeonMap.getCell(tileX, tileY)!!.attackBonus(action)
        updateEffects()
        return action
    }

    override fun accuracyBonus(accuracy: Int, target: Entity): Int {
        var accuracy = accuracy
        for (i in effects.indices) {
            accuracy = effects[i].accuracyBonus(accuracy, target)
        }
        checkRemoveEffects()
        accuracy = DungeonMap.getCell(tileX, tileY)!!.accuracyBonus(accuracy, target)
        updateEffects()
        return accuracy
    }

    override fun onDamage(damage: Int): Int { //TODO maybe funcs needs to get default damage?
        var damage = damage
        for (i in effects.indices) {
            damage = effects[i].onDamage(damage)
        }
        checkRemoveEffects()
        damage = DungeonMap.getCell(tileX, tileY)!!.onDamage(damage)
        if (damage > 0) {
            return damage
        }
        updateEffects()
        return 0
    }

    override fun onHeal(heal: Int): Int { //TODO maybe funcs needs to get default heal?
        var heal = heal
        for (i in effects.indices) {
            heal = effects[i].onHeal(heal)
        }
        checkRemoveEffects()
        heal = DungeonMap.getCell(tileX, tileY)!!.onHeal(heal)
        if (heal > 0) {
            return heal
        }
        updateEffects()
        return 0
    }

    override fun onEncounter(encounter: Encounter) {
        for (i in effects.indices) {
            effects[i].onEncounter(encounter)
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.onEncounter(encounter)
        updateEffects()
    }

    override fun onDeath() {
        for (i in effects.indices) {
            effects[i].onDeath()
        }
        checkRemoveEffects()
        DungeonMap.getCell(tileX, tileY)!!.onDeath()
        isAlive = false
        updateEffects()
    }

    companion object {
        const val DIRECTION_UP = 2
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_DOWN = 0
        const val DIRECTION_LEFT = 3
        const val ENTITY_WIDTH = 32
        const val ENTITY_HEIGHT = 32
        const val MOVEMENT_TIME = 0.6f
        /** IN SECONDS  */
        const val THROW_TIME = 0.15f
        var isUpdateSkills = false
            private set
        var isUpdateEffects = false
            private set
        var nowPlaying: Entity? = null
        var nowPlayingIndex = 0
            private set
        @JvmStatic
        val playingEntities = ArrayList<Entity>()
        @JvmOverloads
        fun add(entity: Entity) {
            playingEntities.add(entity)
        }

        @JvmOverloads
        fun add(pos: Int, entity: Entity) {
            playingEntities.add(pos, entity)
        }

        fun clearPlayingEntities() {
            playingEntities.clear()
            for (entity in Character.chars) {
                playingEntities.add(entity)
            }
        }

        @JvmStatic
        val aliveEntities: List<Entity>
            get() = playingEntities.stream().filter { entity: Entity -> entity.isAlive }.collect(Collectors.toList())

        fun removePlayingEntity(entity: Entity?) {
            playingEntities.remove(entity)
        }

        @JvmStatic
        fun nextTurn(nowPlayingEntity: Entity) {
            if (nowPlaying === nowPlayingEntity) {
                val mapEnd: HashMap<String, Any> = HashMap()
                mapEnd[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = nowPlayingEntity
                EventController.callEvent(EntityEvent.END_TURN_EVENT, mapEnd)
                nowPlayingIndex++
                if (nowPlayingIndex >= playingEntities.size) {
                    nowPlayingIndex = 0
                }
                nowPlaying = playingEntities[nowPlayingIndex]
                println("!!!")
                val mapStart: HashMap<String, Any> = HashMap()
                mapStart[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = nowPlaying as Any
                EventController.callEvent(EntityEvent.START_TURN_EVENT, mapStart)
                isUpdateSkills = true
                DungeonScreen.clearFrame()
            }
        }

        @JvmStatic
        fun setFirstPlaying() {
            if (nowPlaying != null) {
                val mapEnd: HashMap<String, Any> = HashMap()
                mapEnd[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = nowPlaying as Any
                EventController.callEvent(EntityEvent.END_TURN_EVENT, mapEnd)
            }
            nowPlaying = playingEntities[0]
            val mapStart: HashMap<String, Any> = HashMap()
            mapStart[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = nowPlaying as Any
            EventController.callEvent(EntityEvent.START_TURN_EVENT, mapStart)
            isUpdateSkills = true
        }

        fun updateEffects() {
            isUpdateEffects = true
        }

        @JvmStatic
        fun effectsUpdated() {
            isUpdateSkills = false
        }

        @JvmStatic
        fun updateSkills() {
            isUpdateSkills = true
        }

        @JvmStatic
        fun skillsUpdated() {
            isUpdateSkills = false
        }
    }

    init {
        setSprite(model)
        setTexture(model)
        this.portrait = portrait
        this.hp = hp
        this.hpMax = hpMax
        rawMp = mp
        speed = mpMax
        this.armor = armor
        effects = EffectArray()
        skills = com.badlogic.gdx.utils.Array()
        //playingEntities.add(this);
    }
}