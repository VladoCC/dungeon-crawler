package com.wolg_vlad.dcrawler.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.wolg_vlad.dcrawler.dungeon.*
import com.wolg_vlad.dcrawler.encounters.Encounter
import com.wolg_vlad.dcrawler.entities.Enemy
import com.wolg_vlad.dcrawler.entities.Entity
import com.wolg_vlad.dcrawler.entities.ai.pathfinding.graph.GraphStorage
import com.wolg_vlad.dcrawler.entities.skills.FloatingDamageMark
import com.wolg_vlad.dcrawler.entities.skills.Skill
import com.wolg_vlad.dcrawler.event.EntityEvent
import com.wolg_vlad.dcrawler.event.EventController
import com.wolg_vlad.dcrawler.ui.InterfaceElements
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import java.util.*

/**
 * Created by Voyager on 22.04.2017.
 */
class DungeonScreen(private val dungeon: Dungeon) : AdvancedScreen() {
    var mainCamera: OrthographicCamera? = null
    var yUpCamera: OrthographicCamera? = null
    var dungeonMap: DungeonMap? = null
    var renderer: OrthogonalTiledMapRenderer? = null
    private var moveLeft = false
    private var moveRight = false
    private var moveUp = false
    private var moveDown = false
    private var debugInfo = false
    private var restart = false
    private var button = false
    private var width = 0f
    private var height = 0f
    private val animTime = 0f
    private var restartTimer = 0f
    private var mouseX = 0
    private var mouseY = 0
    private val xTooltip = 0f
    private val yTooltip = 0f
    private var h = 0f
    private var w = 0f
    var mainStage: Stage? = null
    var cameraStage: Stage? = null
    var nextTurnButton: TextButton? = null
    var attackButton: TextButton? = null
    var cancelButton: TextButton? = null
    var effectGroup: HorizontalGroup? = null
    var skillGroup: HorizontalGroup? = null
    var buttonsGroup: VerticalGroup? = null
    var encounterGroup: VerticalGroup? = null
    var attackButtonTable: Table? = null
    var cancelButtonTable: Table? = null
    var turnButtonTable: Table? = null
    var guiTable: Table? = null
    var tooltipGroup: Table? = null
    var markTable: Group? = null
    protected var font: BitmapFont? = null
    protected var headerFont: BitmapFont? = null
    protected var shapeRenderer = ShapeRenderer()
    protected var charBatch = SpriteBatch()
    protected var interfaceBatch = SpriteBatch()
    var atlas: TextureAtlas? = null
    var skin: Skin? = null
    override fun show() {
        h = Gdx.graphics.height.toFloat()
        w = Gdx.graphics.width.toFloat()
        //final float w = Gdx.graphics.getWidth();
//final float h = Gdx.graphics.getHeight();
        tooltipGroup = Table()
        yUpCamera = OrthographicCamera(w, h)
        yUpCamera!!.setToOrtho(false)
        yUpCamera!!.update()
        yUpCamera!!.zoom = 0.6f
        yUpCamera!!.position.x = 0f
        yUpCamera!!.position.y = 0f
        mainCamera = OrthographicCamera(w, h)
        mainCamera!!.setToOrtho(true)
        mainCamera!!.update()
        input.addProcessor(object : InputAdapter() {
            override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
                mouseX = screenX
                mouseY = screenY
                val coords = mainCamera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0F))
                println(coords)
                val wMoveField = w / 20
                val hMoveField = h / 20
                println("screenMoving")
                if (screenX < wMoveField && !moveLeft) {
                    moveLeft = true
                    width = w
                } else if (screenX >= wMoveField && moveLeft) {
                    moveLeft = false
                } else if (screenX > w - wMoveField && !moveRight) {
                    moveRight = true
                    width = w
                } else if (screenX <= w - wMoveField && moveRight) {
                    moveRight = false
                }
                if (screenY < hMoveField && !moveUp) {
                    moveUp = true
                    height = h
                } else if (screenY >= hMoveField && moveUp) {
                    moveUp = false
                } else if (screenY > h - hMoveField && !moveDown) {
                    moveDown = true
                    height = h
                } else if (screenY <= h - hMoveField && moveDown) {
                    moveDown = false
                }
                Entity.nowPlaying!!.detailedEffect = -1 //TODO ЭТО ВООБЩЕ НУЖНО? (СКОРЕЕ ВСЕГО НЕТ) ПРОВЕРИТЬ!
                Entity.nowPlaying!!.detailedSkill = -1
                tooltipGroup!!.remove()
                return false //TODO true or false? to be or not to be?
            }

            override fun keyUp(keycode: Int): Boolean {
                var keycode = keycode
                if (keycode == Input.Keys.GRAVE) {
                    println("cam info")
                    debugInfo = !debugInfo
                    return true
                } else if (keycode == Input.Keys.R) {
                    restart = false
                    if (restartTimer >= 5) {
                        mainCamera!!.position.x = 0f
                        mainCamera!!.position.y = 0f
                        dungeonMap = DungeonMap(dungeon, mainCamera!!, input)
                        renderer = OrthogonalTiledMapRenderer(dungeonMap)
                    }
                    return true
                } else if (keycode == Input.Keys.A) {
                    nextTurnButton!!.setText(nextTurnButton!!.text.toString() + "A")
                    println("!!!")
                } else if (keycode == Input.Keys.C) {
                    for (entity in Entity.playingEntities) {
                        if (entity.isEnemy) {
                            entity.isControlled = !entity.isControlled
                        }
                    }
                } else if (keycode > 6 && keycode < 17 || keycode > 143 && keycode < 154) {
                    keycode -= 8
                    if (keycode > 135) {
                        keycode -= 137
                    }
                    if (keycode == -1) {
                        keycode = 10
                    }
                    if (keycode < skillGroup!!.children.size && keycode > -1) {
                        val actor = skillGroup!!.children[keycode]
                        val event1 = InputEvent()
                        event1.type = InputEvent.Type.touchDown
                        actor.fire(event1)
                        val event2 = InputEvent()
                        event2.type = InputEvent.Type.touchUp
                        actor.fire(event2)
                    }
                } else if (keycode == Input.Keys.J) {
                    val builder = GsonBuilder()
                    builder.setExclusionStrategies(object : ExclusionStrategy {
                        override fun shouldSkipField(f: FieldAttributes): Boolean {
                            return f.name == "ready"
                        }

                        override fun shouldSkipClass(checkClass: Class<*>): Boolean {
                            return checkClass == TiledMapTile.BlendMode::class.java || checkClass == Float::class.javaPrimitiveType || checkClass == TextureRegion::class.java
                        }
                    })
                    builder.setPrettyPrinting()
                    val gson = builder.create()
                    val file = Gdx.files.local("tiles/tiles.list")
                    val s = gson.toJson(DungeonTile.tiles.items, Array<DungeonTile>::class.java)
                    file.writeString(s, false)
                } else if (keycode == Input.Keys.K) {
                    Entity.playingEntities[0].addHp(-30)
                } else if (keycode == Input.Keys.M) {
                    mainCamera!!.position.x = 0f
                    mainCamera!!.position.y = 0f
                } else if (keycode == Input.Keys.T) {
                    Entity.nextTurn(Entity.nowPlaying!!)
                }
                println(Input.Keys.toString(keycode))
                return false
            }

            override fun keyDown(keycode: Int): Boolean {
                if (keycode == Input.Keys.R) {
                    restart = true
                } else if (keycode == Input.Keys.T) {
                }
                return false
            }
        })
        createFonts()
        mainStage = Stage()
        guiTable = Table()
        guiTable!!.setPosition(1f, 1f)
        guiTable!!.height = h / 6 - 1
        guiTable!!.width = w - 1
        guiTable!!.setDebug(true, true)
        guiTable!!.addListener(object : InputListener() {
            override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                println("hm...")
                return true
            }

            override fun mouseMoved(event: InputEvent, x: Float, y: Float): Boolean {
                println("hm...")
                return true
            }
        })
        mainStage!!.addActor(guiTable)
        atlas = TextureAtlas("buttons.pack")
        skin = Skin(atlas)
        placeButton()
        turnButtonTable = Table()
        turnButtonTable!!.add(nextTurnButton).width(w / 10).height(h / 12)
        val style = TextButtonStyle()
        val up = skin!!.getDrawable("button_off")
        val down = skin!!.getDrawable("button_on")
        style.up = up
        style.down = down
        style.font = font
        attackButton = TextButton("Attack", style)
        attackButton!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Entity.nowPlaying!!.useSkill()
                changeButton(false)
            }
        })
        cancelButton = TextButton("Cancel", style)
        cancelButton!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Entity.nowPlaying!!.setUsedSkill(null)
                changeButton(false)
                DungeonMap.clearTargetLayer()
                DungeonMap.clearTargetingZoneLayer()
            }
        })
        attackButtonTable = Table()
        cancelButtonTable = Table()
        attackButtonTable!!.add<TextButton>(attackButton).width(w / 10).height(h / 18).padTop(h / 36).padBottom(h / 72)
        cancelButtonTable!!.add<TextButton>(cancelButton).width(w / 10).height(h / 18).padTop(h / 72).padBottom(h / 36)
        markTable = Group()
        markTable!!.setPosition(0f, h / 6)
        markTable!!.setSize(w, h - h / 6)
        mainStage!!.addActor(markTable)
        buttonsGroup = VerticalGroup()
        buttonsGroup!!.width = w / 10
        buttonsGroup!!.height = h / 6
        buttonsGroup!!.setPosition(w * 9 / 10 - w / 100, 0f)
        buttonsGroup!!.addActor(nextTurnButton)
        buttonsGroup!!.center()
        guiTable!!.addActor(buttonsGroup)
        encounterGroup = VerticalGroup()
        encounterGroup!!.width = w / 6
        encounterGroup!!.height = h - h / 6
        encounterGroup!!.setPosition(w - w / 6, h / 6)
        encounterGroup!!.center()
        mainStage!!.addActor(encounterGroup)
        effectGroup = HorizontalGroup()
        effectGroup!!.setPosition(w / 5, h / 8)
        effectGroup!!.width = 3 * w / 5
        effectGroup!!.height = h / 24
        effectGroup!!.wrap(true)
        effectGroup!!.center()
        guiTable!!.addActor(effectGroup)
        skillGroup = HorizontalGroup()
        skillGroup!!.setPosition(w / 7, h / 72)
        skillGroup!!.width = 5 * w / 7
        skillGroup!!.height = h / 11
        skillGroup!!.center()
        skillGroup!!.debugAll()
        guiTable!!.addActor(skillGroup)
        //guiTable.debugAll();
        frameTable = Table()
        val frameDrawable: Drawable = TextureRegionDrawable(TextureRegion(Texture("frame.png")))
        frameDrawable.minHeight = h / 11
        frameDrawable.minWidth = h / 11
        val frame = Image(frameDrawable)
        frameTable!!.add(frame)
        input.addProcessor(mainStage)
        mainCamera!!.position.x = 0f
        mainCamera!!.position.y = 0f
        dungeonMap = DungeonMap(dungeon, mainCamera!!, input)
        cameraStage = Stage()
        cameraStage!!.viewport.camera = yUpCamera
        val stack = Stack()
        val effectGroup = Group()
        effectGroup.setSize(w, h)
        stack.add(effectGroup)
        DungeonMap.setEffectGroup(effectGroup)
        val targetingZoneGroup = Group()
        stack.add(targetingZoneGroup)
        DungeonMap.setTargetingZoneGroup(targetingZoneGroup)
        val displayTargetGroup = Group()
        stack.add(displayTargetGroup)
        DungeonMap.setDisplayTargetGroup(displayTargetGroup)
        val choosenTargetsGroup = Group()
        stack.add(choosenTargetsGroup)
        DungeonMap.setChosenTargetsGroup(choosenTargetsGroup)
        println("Stack: " + stack.minWidth + " " + stack.minHeight + " / " + stack.prefWidth + " " + stack.prefHeight + " / " + stack.maxWidth + " " + stack.maxHeight)
        cameraStage!!.addActor(stack)
        Entity.setFirstPlaying()
        renderer = OrthogonalTiledMapRenderer(dungeonMap)
        mainCamera!!.zoom = 0.6f
        shapeRenderer.setAutoShapeType(true)
        //Character character = new Character(new Texture("Char1.png"), 64, 64);
    }

    override fun render(delta: Float) {
        if (restart) {
            restartTimer += delta
            println(restartTimer)
        }
        if (restartTimer >= 5) {
            restartTimer = 0f
            input.removeProcessor(input.size() - 1)
            mainCamera!!.position.x = 0f
            mainCamera!!.position.y = 0f
            dungeonMap = DungeonMap(dungeon, mainCamera!!, input)
            renderer = OrthogonalTiledMapRenderer(dungeonMap)
        }
        if (moveLeft || moveRight || moveUp || moveDown) { //TODO add method for camera moving
            if (moveLeft) {
                mainCamera!!.translate(-500 * delta, 0f) //TODO сделать скорость зависящей от расстояния до стенки
            } else if (moveRight) {
                mainCamera!!.translate(500 * delta, 0f)
            }
            if (moveUp) {
                mainCamera!!.translate(0f, -500 * delta)
            } else if (moveDown) {
                mainCamera!!.translate(0f, 500 * delta)
            }
            updateCamera()
        }
        if (DungeonMap.updateCamera) {
            updateCamera()
        }
        mainCamera!!.update()
        charBatch.projectionMatrix = mainCamera!!.combined
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        //mainCamera.update();
        renderer!!.setView(mainCamera)
        renderMapLayers(0, 0) //TODO move to the end of method
        cameraStage!!.draw()
        renderMapLayers(1, dungeonMap!!.layersCount - 1)
        Encounter.update(delta)
        FloatingDamageMark.update(delta)
        val nowPlayingEntity = Entity.nowPlaying
        if (!nowPlayingEntity!!.isAlive) {
            Entity.nextTurn(nowPlayingEntity)
        }
        charBatch.begin()
        //Entity.getPlayingEntities().removeIf(entity -> !entity.isAlive());
        if (Room.addingArray.size > 0) {
            for (entity in Room.addingArray) {
                entity.add(Entity.nowPlayingIndex + 1)
                DungeonMap.addEntity(entity)
            }
            GraphStorage.createBottomGraph()
        }
        Room.addingArray.clear()
        for (entity in Entity.aliveEntities) {
            act(entity, delta)
            entity.getSprite()!!.draw(charBatch)
        }
        charBatch.end()
        markTable!!.clear()
        for (mark in FloatingDamageMark.marks) {
            val color = Color.WHITE
            val style = LabelStyle(font, color)
            val textLabel = Label(mark.getText(), style)
            textLabel.setAlignment(Align.center)
            val x: Float = mark.tileX * DungeonTile.TILE_WIDTH + DungeonTile.TILE_WIDTH / 2.toFloat()
            val y: Float = (mark.tileY + 2) * DungeonTile.TILE_HEIGHT + DungeonTile.TILE_HEIGHT / 2 - DungeonTile.TILE_HEIGHT / 2 * mark.time / FloatingDamageMark.MAX_TIME //TODO DON'T KNOW WHY THIS NEEDS TO HAVE +2
            val vector = mainCamera!!.project(Vector3(x, y, 0F))
            textLabel.setPosition(vector.x, vector.y, Align.center)
            textLabel.setWrap(false)
            markTable!!.addActor(textLabel)
        }
        encounterGroup!!.clear()
        if (Encounter.hasNowPlayimg()) {
            val encounters: MutableList<Encounter> = Encounter.nowPlayings
            for (encounter in encounters) {
                val encounterTable = Table()
                /*encounterTable.setPosition(w, (h - h / 6) / 2);
                encounterTable.align(Align.center);*/
                val color = Color.WHITE
                val headerStyle = LabelStyle(headerFont, color)
                val headerLabel = Label(encounter.name, headerStyle)
                headerLabel.width = w / 6
                headerLabel.setWrap(true)
                headerLabel.setAlignment(Align.top)
                encounterTable.add(headerLabel).width(w / 6).padLeft(10f).padRight(10f).padTop(10f).row()
                val style = LabelStyle(font, color)
                val textLabel = Label(encounter.text, style)
                textLabel.width = w / 6
                textLabel.setWrap(true)
                textLabel.setAlignment(Align.left)
                textLabel.x = 0f
                encounterTable.add(textLabel).width(w / 6).padLeft(10f).padRight(10f).padBottom(10f).row()
                val background = skin!!.getDrawable("tooltip_background")
                encounterTable.background = background
                encounterTable.pad(10f, 0f, 0f, 0f)
                encounterGroup!!.addActor(encounterTable)
            }
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.GRAY
        //System.out.println(w + " " + h);
        shapeRenderer.rect(0f, 0f, w, h / 6)
        shapeRenderer.end()
        val hpBarInfo: String
        val mpBarInfo: String
        if (nowPlayingEntity.isCharacter || nowPlayingEntity.isControlled) {
            hpBarInfo = nowPlayingEntity.hp.toString() + "/" + nowPlayingEntity.hpMax
            mpBarInfo = nowPlayingEntity.getMp(true).toString() + "/" + nowPlayingEntity.speed
        } else {
            hpBarInfo = "-"
            mpBarInfo = "-"
        }
        interfaceBatch.begin()
        interfaceBatch.draw(nowPlayingEntity.portrait, w / 180, h / 120, h / 6 - h / 20, h / 6 - h / 60)
        val healthBarIcon = InterfaceElements.getHealth()
        interfaceBatch.draw(healthBarIcon, h / 6 - h / 20 + w / 90, h / 36 * 5 - h / 30, h / 15, h / 15)
        font!!.draw(interfaceBatch, hpBarInfo, h / 6 - h / 20 + w / 90, h / 36 * 5 - h / 30, h / 15, Align.center, false)
        val moveBarIcon = InterfaceElements.getMoves()
        interfaceBatch.draw(moveBarIcon, h / 6 - h / 20 + w / 90, h / 18 - h / 30, h / 15, h / 15)
        font!!.draw(interfaceBatch, mpBarInfo, h / 6 - h / 20 + w / 90, h / 18 - h / 30, h / 15, Align.center, false)
        val finalEntity = Entity.nowPlaying
        if (Entity.isUpdateSkills) {
            println("UPDATE!")
            Entity.skillsUpdated()
            skillGroup!!.clear()
            val entity = Entity.nowPlaying
            val mapStart: HashMap<String, Any> = HashMap()
            mapStart[EntityEvent.ENTITY_EXECUTOR_ARG_KEY] = entity as Any
            val canUse = EventController.callEvent(EntityEvent.CAN_USE_SKILL_EVENT, mapStart)!![EntityEvent.CAN_USE_SKILL_ARG_KEY] as Boolean
            for (i in 0 until entity!!.skills.size) {
                val skill = entity.skills[i]
                val texture = skill.icon
                val drawable: Drawable = TextureRegionDrawable(TextureRegion(texture))
                drawable.minHeight = h / 11
                drawable.minWidth = h / 11
                val stack = Stack()
                val image = Image(drawable)
                val table = Table()
                table.add(image).padLeft(h / 110).padRight(h / 110)
                stack.add(table)
                if (skill.isCooldown || !canUse) {
                    val cooldownTable = Table()
                    val cooldownDrawable: Drawable = TextureRegionDrawable(TextureRegion(Texture("cooldown.png")))
                    cooldownDrawable.minHeight = h / 11
                    cooldownDrawable.minWidth = h / 11
                    val cooldown = Image(cooldownDrawable)
                    cooldownTable.add(cooldown)
                    stack.add(cooldownTable)
                }
                if (nowPlayingEntity.usedSkill === skill) {
                    frameTable!!.remove()
                    stack.add(frameTable)
                }
                stack.addListener(object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        return true
                    }

                    override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                        if (!skill.isCooldown && canUse) {
                            DungeonMap.clearSkillLayers()
                            if (entity.usedSkill !== skill) {
                                entity.setUsedSkill(skill)
                                changeButton(true)
                                DungeonMap.clearPathLayer()
                            } else {
                                entity.setUsedSkill(null)
                                changeButton(false)
                            }
                        }
                    }

                    override fun mouseMoved(event: InputEvent, x: Float, y: Float): Boolean {
                        entity.detailedSkill = i
                        return true
                    }
                })
                println("Stack: " + stack.minWidth + " " + stack.minHeight + " / " + stack.prefWidth + " " + stack.prefHeight + " / " + stack.maxWidth + " " + stack.maxHeight)
                skillGroup!!.addActor(stack)
            }
        }
        val detailedSkill = nowPlayingEntity.detailedSkill
        if (detailedSkill != -1) {
            val skill = nowPlayingEntity.skills[detailedSkill]
            tooltipGroup!!.remove()
            tooltipGroup = Table(skin)
            tooltipGroup!!.width = w / 6
            val color = Color.WHITE
            val headerStyle = LabelStyle(headerFont, color)
            val headerLabel = Label(skill.name, headerStyle)
            headerLabel.width = w / 6
            headerLabel.setWrap(true)
            headerLabel.setAlignment(Align.top)
            tooltipGroup!!.add(headerLabel).width(w / 6).padLeft(10f).padRight(10f).padTop(10f).row()
            val style = LabelStyle(font, color)
            val typeLabel = Label(skill.typeString, style)
            addDefaultTooltipLabel(typeLabel)
            val targetType = skill.targetType
            val targetLabel = Label("Target type: " + skill.targetTypeString, style)
            addDefaultTooltipLabel(targetLabel)
            if (targetType != Skill.SKILL_TARGET_TYPE_SELF) {
                val countLabel = Label("Target count: " + skill.targetCountMax, style)
                addDefaultTooltipLabel(countLabel)
                if (targetType != Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER) {
                    val distanceLabel = Label("Distance: " + skill.distanceMin + "-" + skill.distanceMax, style)
                    addDefaultTooltipLabel(distanceLabel)
                }
            }
            if (targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_WAVE || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SWING) {
                val rangeLabel = Label("Range: " + skill.range, style)
                addDefaultTooltipLabel(rangeLabel)
            }
            val descriptionLabel = Label(skill.description, style)
            addDefaultTooltipLabel(descriptionLabel)
            val skillWidth = ((skillGroup!!.children[detailedSkill] as Stack).children[0] as Table).children[0].width
            val fullWidth = skillWidth * 1.2f
            val count = skillGroup!!.children.size
            val half = count / 2
            val b = count % 2 == 1
            val move = detailedSkill - half + 1
            var pos = move * fullWidth
            if (b) {
                pos -= fullWidth / 2
            }
            println("$pos - pos // $move - move")
            pos = w / 2 + pos - skillWidth * 0.1f
            tooltipGroup!!.setPosition(pos, skillGroup!!.y)
            tooltipGroup!!.pack()
            val background = skin!!.getDrawable("tooltip_background")
            tooltipGroup!!.background = background
            mainStage!!.addActor(tooltipGroup)
        }
        if (Entity.isUpdateEffects) {
            Entity.effectsUpdated()
            effectGroup!!.clear()
            for (i in nowPlayingEntity.getEffects().indices) {
                val effect = nowPlayingEntity.getEffects()[i]
                if (!effect.hide) {
                    val texture = effect.icon
                    val drawable: Drawable = TextureRegionDrawable(TextureRegion(texture))
                    drawable.minHeight = h / 24
                    drawable.minWidth = h / 24
                    val image = Image(drawable)
                    val table = Table()
                    table.add(image).padLeft(h / 480).padRight(h / 480)
                    table.addListener(object : InputListener() {
                        override fun mouseMoved(event: InputEvent, x: Float, y: Float): Boolean {
                            finalEntity!!.detailedEffect = i
                            return true
                        }
                    })
                    effectGroup!!.addActor(table)
                }
            }
        }
        val detailedEffect = nowPlayingEntity.detailedEffect
        if (detailedEffect != -1 && detailedEffect < effectGroup!!.children.size) {
            val effect = nowPlayingEntity.getEffects()[detailedEffect]
            tooltipGroup!!.remove()
            tooltipGroup = Table(skin)
            tooltipGroup!!.width = w / 6
            var color: Color?
            color = if (effect.positive) {
                Color.GREEN
            } else {
                Color.RED
            }
            val headerStyle = LabelStyle(headerFont, color)
            val headerLabel = Label(effect.name, headerStyle)
            headerLabel.width = w / 6
            headerLabel.setWrap(true)
            headerLabel.setAlignment(Align.top)
            tooltipGroup!!.add(headerLabel).width(w / 6).padLeft(10f).padRight(10f).padTop(10f).row()
            val style = LabelStyle(font, Color.WHITE)
            val descriptionLabel = Label(effect.description, style)
            descriptionLabel.width = w / 6
            descriptionLabel.setWrap(true)
            descriptionLabel.setAlignment(Align.topLeft)
            descriptionLabel.setPosition(0f, -descriptionLabel.height)
            tooltipGroup!!.add(descriptionLabel).width(w / 6).padLeft(10f).padRight(10f).padBottom(10f)
            //System.out.println(image.getX() + " " + image.getY() + " " + image.getImageWidth());
            val effectWidth = (effectGroup!!.children[detailedEffect] as Table).children[0].width * 1.1f //TODO constants
            val count = effectGroup!!.children.size
            println("$effectWidth - effect width")
            val half = count / 2
            val b = count % 2 == 1
            val move = detailedEffect - half + 1
            var pos = move * effectWidth
            if (b) {
                pos -= effectWidth / 2
            }
            pos = w / 2 + pos
            tooltipGroup!!.setPosition(pos, effectGroup!!.y)
            tooltipGroup!!.pack()
            val background = skin!!.getDrawable("tooltip_background")
            tooltipGroup!!.background = background
            mainStage!!.addActor(tooltipGroup)
        }
        if (debugInfo) {
            font!!.draw(interfaceBatch, "mainCamera: " + mainCamera!!.position + " " + '\u2685', 10f, h - 50)
            for (i in 0 until logs.size) {
                font!!.draw(interfaceBatch, logs[i], 10f, h - 80 - 30 * i)
            }
        }
        interfaceBatch.end()
        mainStage!!.draw()
    }

    private fun act(entity: Entity, delta: Float) {
        if (entity.isAlive) {
            if (entity.isMovement || entity.isThrowing) { //float delta = Gdx.graphics.getDeltaTime();
                println("1. " + entity.animTime + " " + delta)
                if (entity.isMovement) {
                    entity.addAnimTime(delta) //TODO if entity ends movement on his next turn, then it acts buggy
                    //animTime += delta;
                    entity.move(delta)
                } else if (entity.isThrowing) {
                    println("throwing")
                    entity.throwing(delta)
                }
                println("2. " + entity.animTime + " " + delta)
                val x = entity.tileX
                val y = entity.tileY
                if (dungeonMap!!.getTile(x, y)!!.door) {
                    val index = Door.getDoorIndex(x, y)
                    if (index == -1) {
                        println("Already opened")
                        dungeonMap!!.removeDoorTile(x, y)
                    } else {
                        val door = Door.getDoor(index)
                        val direction = door!!.direction
                        var newRoomStartX = x / Dungeon.ROOM_WIDTH
                        var newRoomStartY = y / Dungeon.ROOM_HEIGHT
                        if (direction == Exit.DIRECTION_NORTH) {
                            println("North: y - $newRoomStartY Critical: 0")
                            if (newRoomStartY == 0) {
                                dungeonMap!!.addUp(Dungeon.ROOM_HEIGHT)
                                Door.moveDoors(Vector2(0F, Dungeon.ROOM_HEIGHT.toFloat()))
                                mainCamera!!.translate(0F, Dungeon.ROOM_HEIGHT * DungeonTile.TILE_HEIGHT.toFloat())
                            } else {
                                newRoomStartY--
                            }
                        } else if (direction == Exit.DIRECTION_EAST) {
                            println("East: x - " + newRoomStartX + " Critical: " + (dungeonMap!!.width - 1))
                            if (newRoomStartX == (dungeonMap!!.width - 1) / Dungeon.ROOM_WIDTH) {
                                dungeonMap!!.addRight(Dungeon.ROOM_WIDTH)
                            }
                            newRoomStartX++
                        } else if (direction == Exit.DIRECTION_SOUTH) {
                            println("South: y - " + newRoomStartY + " Critical: " + (dungeonMap!!.height - 1))
                            if (newRoomStartY == (dungeonMap!!.height - 1) / Dungeon.ROOM_HEIGHT) {
                                dungeonMap!!.addDown(Dungeon.ROOM_HEIGHT)
                            }
                            newRoomStartY++
                        } else if (direction == Exit.DIRECTION_WEST) {
                            println("West: x - $newRoomStartX Critical: 0")
                            if (newRoomStartX == 0) {
                                dungeonMap!!.addLeft(Dungeon.ROOM_WIDTH)
                                Door.moveDoors(Vector2(Dungeon.ROOM_WIDTH.toFloat(), 0F))
                                mainCamera!!.translate(Dungeon.ROOM_WIDTH * DungeonTile.TILE_WIDTH.toFloat(), 0F)
                            } else {
                                newRoomStartX--
                            }
                        }
                        for (cell in door.doorCells!!) {
                            dungeonMap!!.removeDoorTile(cell!!.getX().toInt(), cell.getY().toInt())
                        }
                        println("Room X: $newRoomStartX Room Y: $newRoomStartY")
                        Door.removeDoor(index)
                        newRoomStartX *= Dungeon.ROOM_WIDTH
                        newRoomStartY *= Dungeon.ROOM_HEIGHT
                        var side = direction + 2
                        if (side >= 4) {
                            side -= 4
                        }
                        println("Direction: $direction Side: $side")
                        val room = dungeonMap!!.placeRoom(newRoomStartX, newRoomStartY, side)
                        GraphStorage.createBottomGraph()
                        if (room!!.isEncounter) {
                            entity.triggerEncounter()
                        }
                        Entity.nowPlaying!!.isRoomOpened = true
                    }
                }
            } else {
                entity.animTime = 0F
            }
            //System.out.println("Enemy attacks: " + entity.isEnemy() + " "+ entity.isSkillUse());
            if (entity.isEnemy && entity.isSkillUse) {
                val enemy = entity as Enemy
                enemy.addSkillTime(delta)
            }
        } else {
            if (entity === Entity.nowPlaying) {
                Entity.nextTurn(entity)
            }
        }
    }

    private fun renderMapLayers(startLayer: Int, endLayer: Int) {
        val unvisualized = com.badlogic.gdx.utils.Array<Int>()
        for (i in 0 until dungeonMap!!.layers.count) {
            val layer = dungeonMap!!.layers[i]
            if ((i > endLayer || i < startLayer) && layer.isVisible) {
                unvisualized.add(i)
                layer.isVisible = false
            }
        }
        renderer!!.render()
        for (visualize in unvisualized) {
            dungeonMap!!.layers[visualize].isVisible = true
        }
    }

    private fun addDefaultTooltipLabel(label: Label) {
        label.width = w / 6
        label.setWrap(true)
        label.setAlignment(Align.left)
        label.x = 0f
        tooltipGroup!!.add(label).width(w / 6).padLeft(10f).padRight(10f).padBottom(10f).row()
    }

    private fun changeButton(state: Boolean) {
        buttonsGroup!!.clear()
        button = state
        if (!button) {
            turnButtonTable!!.clear()
            turnButtonTable!!.add(nextTurnButton).width(w / 10).height(h / 12)
            buttonsGroup!!.addActor(turnButtonTable)
        } else {
            buttonsGroup!!.addActor(attackButtonTable)
            buttonsGroup!!.addActor(cancelButtonTable)
        }
    }

    private fun updateCamera() {
        mainCamera!!.update()
        yUpCamera!!.position.x = mainCamera!!.position.x
        yUpCamera!!.position.y = -mainCamera!!.position.y
        yUpCamera!!.update()
    }

    override fun resize(width: Int, height: Int) {
        h = height.toFloat()
        w = width.toFloat()
        nextTurnButton!!.setPosition(w - w / 180 - nextTurnButton!!.width, h / 12 - nextTurnButton!!.height / 2)
        effectGroup!!.setPosition(w / 5, 9 * h / 72)
        effectGroup!!.width = 3 * w / 5
        effectGroup!!.height = h / 40
        interfaceBatch = SpriteBatch()
        charBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        createFonts()
        placeButton()
    }

    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {
        Entity.playingEntities.removeIf { entity: Entity -> entity.isEnemy }
    }

    private fun createFonts() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("pixelart.otf"))
        val parameter = FreeTypeFontParameter()
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "⚅⚄⚃⚂⚁⚀"
        var mult = h / 720
        if (mult == 0f) {
            mult = 1f
        }
        println(mult)
        parameter.size = (14 * mult).toInt()
        font = generator.generateFont(parameter)
        parameter.size = (16 * mult).toInt()
        headerFont = generator.generateFont(parameter)
    }

    private fun placeButton() {
        val style = TextButtonStyle()
        val up = skin!!.getDrawable("button_off")
        val down = skin!!.getDrawable("button_on")
        val multHeight = h / 720
        val multWidth = w / 1280
        println("Button mults: $multWidth $multHeight")
        up.minWidth = up.minWidth * multWidth
        up.minHeight = up.minHeight * multHeight
        down.minWidth = up.minWidth * multWidth
        down.minHeight = up.minHeight * multHeight
        style.up = up
        style.down = down
        style.font = font
        nextTurnButton = TextButton("End turn", style)
        //nextTurnButton.setText("test");
        nextTurnButton!!.setPosition(w - w / 180 - nextTurnButton!!.width, h / 12 - nextTurnButton!!.height / 2)
        nextTurnButton!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                DungeonMap.clearTargetLayer()
                Entity.nextTurn(Entity.nowPlaying!!)
                //nextTurnButton.setDisabled(!Entity.getNowPlaying().isPlayer());
            }
        })
    }

    companion object {
        private val logs = com.badlogic.gdx.utils.Array<String>()
        var frameTable: Table? = null
        fun addLog(log: String): Int {
            logs.add(log)
            return logs.size - 1
        }

        fun changeLog(log: String, index: Int) {
            logs[index] = log
        }

        fun clearFrame() {
            frameTable!!.remove()
        }
    }

}