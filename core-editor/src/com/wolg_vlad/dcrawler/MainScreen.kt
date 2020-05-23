package com.wolg_vlad.dcrawler

import com.badlogic.gdx.*
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.input.GestureDetector.GestureListener
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.google.gson.Gson
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener
import com.wolg_vlad.dcrawler.dungeon.DungeonCell
import com.wolg_vlad.dcrawler.dungeon.Exit
import com.wolg_vlad.dcrawler.dungeon.ExitPattern
import com.wolg_vlad.dcrawler.dungeon.Room
import com.wolg_vlad.dcrawler.ui.tiles.DungeonTile
import java.util.*

class MainScreen(original: Room?, private val width: Int, private val height: Int) : Screen, GestureListener {
    private var map: TiledMap? = null
    private var renderer: OrthogonalTiledMapRenderer? = null
    private var camera: OrthographicCamera? = null
    private val shapeRenderer = ShapeRenderer()
    protected var mainFont: BitmapFont? = null
    protected var actionFont: BitmapFont? = null
    protected var panelFont: BitmapFont? = null
    protected var stage = Stage()
    private var skin: Skin? = null
    private var checkboxFalseTexture: Texture? = null
    private var checkboxTrueTexture: Texture? = null
    private var room: Room? = null
    private val original: Room?
    private var frameImage: Image? = null
    private val logLabel: Label? = null
    private val actionLabel: Label? = null
    private var button: TextButton? = null
    private var layersGroup: VerticalGroup? = null
    private var shownLayers: ArrayList<Boolean>? = null
    private var layerIndex = 0
    override fun show() {
        DungeonTile.initTiles()
        shapeRenderer.setAutoShapeType(true)
        room = Room(width, height, false)
        val layer = TiledMapTileLayer(width, height, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
        val w = Gdx.graphics.width
        val h = Gdx.graphics.height
        checkboxFalseTexture = Texture(Gdx.files.internal("ch_no.png"))
        checkboxTrueTexture = Texture(Gdx.files.internal("ch_yes.png"))
        frameImage = Image()
        val mainGuiGroup = HorizontalGroup()
        mainGuiGroup.height = h / 6.toFloat()
        mainGuiGroup.width = w.toFloat()
        mainGuiGroup.setPosition(0f, 0f)
        val generator = FreeTypeFontGenerator(Gdx.files.internal("unifont-12.1.02.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 16
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}↷↶"
        mainFont = generator.generateFont(parameter)
        parameter.size = 20
        actionFont = generator.generateFont(parameter)
        parameter.size = 27
        panelFont = generator.generateFont(parameter)
        val atlas = TextureAtlas("uiskin.atlas")
        skin = Skin(Gdx.files.internal("uiskin.json"), atlas)
        skin!!.add("default-font", BitmapFont(), BitmapFont::class.java)
        val style = TextButtonStyle()
        //Drawable up = skin.getDrawable("button_off");
//Drawable down = skin.getDrawable("button_on");
//style.up = up;
//style.down = down;
        style.font = panelFont
        val leftGroup = VerticalGroup()
        FileChooser.setDefaultPrefsName("ru.myitschool.dcrawler.filechooser")
        val chooser = FileChooser(FileChooser.Mode.SAVE)
        chooser.setListener(object : SingleFileChooserListener() {
            override fun selected(file: FileHandle) {
                for (k in 0 until map!!.layers.count) {
                    val layer = map!!.layers[k] as TiledMapTileLayer
                    for (i in 0 until width) {
                        for (j in 0 until height) {
                            val cell = layer.getCell(i, j)
                            if (cell != null) {
                                if (k == 0) {
                                    room?.setCell(i, j, cell.tile.id)
                                } else {
                                    room?.getPattern(k - 1)?.cells?.get(i)?.set(j, cell.tile.id)
                                }
                            }
                        }
                    }
                }
                val gson = Gson()
                file.writeString(gson.toJson(room, Room::class.java), false)
            }
        })
        button = TextButton("Save", skin)
        //button.setStyle(style);
        button!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                stage.addActor(chooser)
            }
        })
        button!!.height = h / 21.toFloat()
        //button.pack();
        val saveTable = Table()
        saveTable.add<TextButton>(button).height(button!!.height)
        leftGroup.addActor(saveTable)
        tile = DungeonTile.getTile(0)
        val middleGroup = HorizontalGroup()
        middleGroup.fill()
        System.out.println(DungeonTile.tiles.size)
        for (tile in DungeonTile.tiles) {
            if (tile != null) {
                val region: TextureRegion = tile.getTextureRegion()
                val drawable = TextureRegionDrawable(region)
                val image = Image(drawable)
                image.addListener(object : InputListener() {
                    override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        Companion.tile = tile
                        return true
                    }
                })
                middleGroup.addActor(image)
            }
        }
        val textStyle = TextFieldStyle()
        textStyle.font = LevelManager.Companion.mainFont
        textStyle.fontColor = Color.WHITE
        val rightGroup = VerticalGroup()
        val rotateClockwiseButton = TextButton("↷", skin)
        rotateClockwiseButton.setStyle(style)
        rotateClockwiseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                for (k in 0 until map!!.layers.count) {
                    val layer = map!!.layers[k] as TiledMapTileLayer
                    val cells = Array(room!!.height) { arrayOfNulls<TiledMapTileLayer.Cell>(room!!.width) }
                    for (i in 0 until room!!.width) {
                        for (j in 0 until room!!.height) {
                            cells[cells.size - j - 1][i] = layer.getCell(i, j)
                        }
                    }
                    for (i in cells.indices) {
                        for (j in 0 until cells[0].size) {
                            layer.setCell(i, j, cells[i][j])
                        }
                    }
                }
                for (pattern in room!!.patterns) {
                    for (i in 0 until pattern.statement.size) {
                        pattern.statement[i]++
                        pattern.statement[i] %= Exit.EXIT_SIDES.size
                    }
                }
                showLayers()
            }
        })
        val newButton = TextButton("New", skin) //TODO move this button somewhere more convenient
        newButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val nameArea = TextArea("Exit Pattern", skin)
                val checkBoxArray = ArrayList<CheckBox>()
                for (name in Exit.EXIT_SIDES) {
                    checkBoxArray.add(CheckBox(name, skin))
                }
                val dialog: EditorDialog = object : EditorDialog("Statements", skin) {
                    override fun result(`object`: Any) {
                        val check = `object` as Int > 0
                        if (check) {
                            val statements = com.badlogic.gdx.utils.Array<Int>(checkBoxArray.size)
                            for (i in checkBoxArray.indices) {
                                if (checkBoxArray[i].isChecked) {
                                    statements.add(i)
                                }
                            }
                            val states = IntArray(statements.size)
                            for (i in states.indices) {
                                states[i] = statements[i]
                            }
                            val pattern = ExitPattern(states, width.toInt(), height.toInt(), nameArea.text)
                            room?.addExitPattern(pattern)
                            layerIndex = map!!.layers.count
                            //field.setText(layerIndex + "");
                            map!!.layers.add(TiledMapTileLayer(width.toInt(), height.toInt(), 32, 32))
                            shownLayers!!.add(true)
                            updateLayers()
                            showLayers()
                        }
                    }
                }
                val group = VerticalGroup()
                group.setOrigin(Align.left)
                group.addActor(nameArea)
                for (checkBox in checkBoxArray) {
                    group.addActor(checkBox)
                }
                group.pack()
                dialog.contentTable.add(group)
                //dialog.pack();
                val createButton = TextButton("Create", skin)
                dialog.button(createButton, 1)
                val cancelButton = TextButton("Cancel", skin)
                dialog.button(cancelButton, -1)
                dialog.align(Align.top)
                dialog.show(stage)
            }
        })
        val rotateAnticlockwiseButton = TextButton("↶", skin)
        rotateAnticlockwiseButton.setStyle(style)
        rotateAnticlockwiseButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                for (k in 0 until map!!.layers.count) {
                    val layer = map!!.layers[k] as TiledMapTileLayer
                    val cells = Array(room!!.height) { arrayOfNulls<TiledMapTileLayer.Cell>(room!!.width) }
                    for (i in 0 until room!!.width) {
                        for (j in 0 until room!!.height) {
                            cells[j][cells[0].size - i - 1] = layer.getCell(i, j)
                        }
                    }
                    for (i in cells.indices) {
                        for (j in 0 until cells[0].size) {
                            layer.setCell(i, j, cells[i][j])
                        }
                    }
                }
                for (pattern in room!!.patterns) {
                    for (i in 0 until pattern.statement.size) {
                        pattern.statement[i]--
                        pattern.statement[i] += Exit.EXIT_SIDES.size
                        pattern.statement[i] %= Exit.EXIT_SIDES.size
                        print(pattern.statement[i].toString() + " ")
                    }
                    println()
                }
                showLayers()
            }
        })
        rightGroup.addActor(rotateClockwiseButton)
        rightGroup.addActor(newButton)
        rightGroup.addActor(rotateAnticlockwiseButton)
        mainGuiGroup.addActor(leftGroup)
        mainGuiGroup.addActor(middleGroup)
        mainGuiGroup.addActor(rightGroup)
        println(mainGuiGroup.width.toString() + " " + mainGuiGroup.height)
        shownLayers = ArrayList()
        shownLayers!!.add(true)
        if (original != null) {
            for (i in 0 until original.patterns.size) {
                shownLayers!!.add(false)
            }
        }
        //stage.addActor(testTable);
/*TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tile);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                layer.setCell(i, j, cell);
            }
        }*/
        val gd = GestureDetector(this)
        val inputMultiplexer = InputMultiplexer(stage)
        inputMultiplexer.addProcessor(object : InputAdapter() {
            var button = 0
            override fun keyUp(keycode: Int): Boolean {
                if (keycode == Input.Keys.M) {
                }
                return false
            }

            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                if (button == Input.Buttons.RIGHT) {
                    this.button = Input.Buttons.RIGHT
                } else {
                    this.button = Input.Buttons.LEFT
                }
                return false
            }

            override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
                return act(screenX, screenY)
            }

            override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
                val result = act(screenX, screenY)
                this.button = -1
                return result
            }

            private val zoomMult = 0.03f
            private val xTransMult = 597.5f * zoomMult
            private val yTransMult = 337.5f * zoomMult
            override fun scrolled(amount: Int): Boolean {
                if (camera!!.zoom + amount * zoomMult > 0) {
                    camera!!.zoom += amount * zoomMult
                    camera!!.translate(amount * xTransMult, amount * yTransMult)
                    camera!!.update()
                }
                return super.scrolled(amount)
            }

            private fun act(screenX: Int, screenY: Int): Boolean {
                val pos = camera!!.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0F))
                val cellX = (pos.x / DungeonTile.TILE_WIDTH).toInt()
                val cellY = (pos.y / DungeonTile.TILE_HEIGHT).toInt()
                var cell: TiledMapTileLayer.Cell? = TiledMapTileLayer.Cell()
                if (button == Input.Buttons.LEFT) {
                    cell!!.tile = tile
                } else if (button == Input.Buttons.RIGHT) {
                    cell = null
                }
                (map!!.layers[layerIndex] as TiledMapTileLayer).setCell(cellX, cellY, cell)
                return cellX > -1 && cellX < room!!.width && cellY > -1 && cellY < room!!.height
            }
        })
        Gdx.input.inputProcessor = inputMultiplexer
        camera = OrthographicCamera(w.toFloat(), h.toFloat())
        camera!!.setToOrtho(true)
        camera!!.translate(-2f, -2f)
        camera!!.update()
        map = TiledMap()
        map!!.layers.add(layer)
        if (original != null) {
            for (i in 0 until original.width) {
                for (j in 0 until original.height) {
                    val index: Int? = original.getCell(i, j)
                    if (index != null) {
                        layer.setCell(i, j, DungeonCell(DungeonTile.getTile(index)))
                    }
                }
            }
            for (pattern in original.patterns) {
                room!!.addExitPattern(pattern)
                val tileLayer = TiledMapTileLayer(width, height, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT)
                for (i in 0 until original.width) {
                    for (j in 0 until original.height) {
                        val index: Int? = pattern.cells.get(i).get(j)
                        if (index != null) {
                            tileLayer.setCell(i, j, DungeonCell(DungeonTile.getTile(index)))
                        }
                    }
                }
                map!!.layers.add(tileLayer)
            }
        }
        layersGroup = VerticalGroup()
        layersGroup!!.setPosition(w / 7 * 6.toFloat(), 40f)
        layersGroup!!.width = w / 7.toFloat()
        layersGroup!!.height = h - 60.toFloat()
        layersGroup!!.align(Align.left)
        updateLayers()
        showLayers()
        stage.addActor(mainGuiGroup)
        stage.addActor(layersGroup)
        renderer = OrthogonalTiledMapRenderer(map)
    }

    private fun updateLayers() {
        layersGroup!!.clear()
        for (i in shownLayers!!.indices) {
            val stack = Stack()
            //stack.debugAll();
            val group = HorizontalGroup()
            group.setFillParent(false)
            group.align(Align.left)
            var name = ""
            name = if (i == 0) {
                "Base"
            } else {
                room!!.getPattern(i - 1).name?:"placeholder"
            }
            val checkBox = CheckBox("", skin)
            checkBox.isChecked = shownLayers!![i]
            checkBox.padLeft(5f)
            checkBox.padRight(10f)
            checkBox.align(Align.left)
            checkBox.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    shownLayers!![i] = checkBox.isChecked
                    showLayers()
                }
            })
            group.addActor(checkBox)
            val textField = Label("$i. $name", skin)
            textField.addListener(object : InputListener() {
                private var button = -1
                override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    if (button == Input.Buttons.RIGHT) {
                        this.button = Input.Buttons.RIGHT
                    } else {
                        this.button = Input.Buttons.LEFT
                    }
                    return true
                }

                override fun touchUp(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int) {
                    super.touchUp(event, x, y, pointer, button)
                    if (button == Input.Buttons.LEFT && i != layerIndex) {
                        changeFocusedLayer(i)
                    } else if (button == Input.Buttons.RIGHT && i != 0) {
                        val nameLabel = TextArea(room!!.getPattern(i - 1).name, skin)
                        val dialog: EditorDialog = object : EditorDialog("Rename", skin) {
                            override fun result(`object`: Any) {
                                val check = `object` as Int > 0
                                if (check) {
                                    room!!.getPattern(i - 1).name = nameLabel.text
                                    updateLayers()
                                }
                            }
                        }
                        dialog.contentTable.add(nameLabel)
                        //dialog.pack();
                        val createButton = TextButton("OK", skin)
                        dialog.button(createButton, 1)
                        val cancelButton = TextButton("Cancel", skin)
                        dialog.button(cancelButton, -1)
                        dialog.align(Align.top)
                        dialog.show(stage)
                    }
                }
            })
            group.addActor(textField)
            val button = TextButton("x", skin)
            button.addListener(object : ChangeListener() {
                override fun changed(event: ChangeEvent, actor: Actor) {
                    shownLayers!!.removeAt(i)
                    room!!.patterns.removeIndex(i - 1)
                    updateLayers()
                    map!!.layers.remove(i)
                    if (i == layerIndex) {
                        changeFocusedLayer(layerIndex - 1)
                    }
                }
            })
            if (i != 0) {
                group.addActor(button)
            }
            val widget: Widget = object : Widget() {
                override fun getPrefWidth(): Float {
                    return Math.max(super.getPrefWidth(), 5f)
                }
            }
            widget.width = 10f
            widget.height = 1f
            group.addActor(widget)
            if (layerIndex == i) {
                println(checkBox.width.toInt().toString() + " " + checkBox.height.toInt())
                val pixmap = Pixmap(checkBox.width.toInt() + 20, checkBox.height.toInt(), Pixmap.Format.RGBA8888)
                pixmap.setColor(Color.WHITE)
                pixmap.drawRectangle(0, 0, checkBox.width.toInt() + 20, checkBox.height.toInt())
                frameImage!!.drawable = TextureRegionDrawable(TextureRegion(Texture(pixmap)))
                stack.addActor(frameImage)
            }
            stack.addActor(group)
            //stack.setFillParent(true);
            layersGroup!!.addActorAt(0, stack)
        }
        layersGroup!!.pack()
    }

    private fun changeFocusedLayer(index: Int) {
        layerIndex = index
        if (!shownLayers!![index]) {
            shownLayers!![index] = true
        }
        updateLayers()
    }

    var defaultStatesCount = 1f
    fun showLayers() {
        for (i in 0 until map!!.layers.count) {
            if (shownLayers!![i]) {
                map!!.layers[i].isVisible = true
            } else {
                map!!.layers[i].isVisible = false
            }
        }
    }

    internal inner abstract class EditorDialog(title: String?, skin: Skin?) : Dialog(title, skin) {
        override fun key(keycode: Int, `object`: Any): Dialog {
            return super.key(keycode, `object`)
        }

        abstract override fun result(`object`: Any)
    }

    fun reset() {
        button!!.setText("Start")
    }

    override fun render(delta: Float) //кажый кадр
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        renderer!!.setView(camera)
        renderer!!.render()
        stage.draw()
        stage.act()
        shapeRenderer.begin()
        shapeRenderer.rect(1f, Gdx.graphics.height - DungeonTile.TILE_HEIGHT * height / camera!!.zoom, DungeonTile.TILE_WIDTH * width / camera!!.zoom, DungeonTile.TILE_HEIGHT * height / camera!!.zoom)
        shapeRenderer.end()
    }

    var normallStateCount = 1f
    var logger = false
    var time = 0f
    val ANIM_TIME = 0.5f
    var logTime = 0f
    var checkInfo = false
    fun setAction(action: String?) {
        actionLabel!!.setText(action)
    }

    fun addLog(log: String) {
        val text = log + '\n' + logLabel!!.text.toString()
        logLabel.setText(text)
    }

    //---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------
    override fun resize(width: Int, height: Int) {}

    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        return false
    }

    fun initRandoms() {}
    override fun longPress(x: Float, y: Float): Boolean {
        return false
    }

    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean {
        return false
    }

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        return true
    }

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        return false
    }

    override fun pinch(initialPointer1: Vector2, initialPointer2: Vector2, pointer1: Vector2, pointer2: Vector2): Boolean {
        return false
    }

    override fun pinchStop() {}

    companion object {
        private var tile: DungeonTile? = null
    }

    init {
        this.original = original
    }
}