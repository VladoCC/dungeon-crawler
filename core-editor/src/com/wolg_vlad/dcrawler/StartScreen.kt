package com.wolg_vlad.dcrawler

import com.badlogic.gdx.*
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener
import com.wolg_vlad.dcrawler.dungeon.Room

/**
 * Created by КАРАТ on 21.03.2017.
 */
class StartScreen : Screen {
    var chooser: FileChooser? = null
    var t_bar_back: Texture? = null
    var t_background: Texture? = null
    var checkboxFalseTexture: Texture? = null
    var checkboxTrueTexture: Texture? = null
    var s_bar_back: Sprite? = null
    var s_background: Sprite? = null
    var t_bar_knob: Texture? = null
    var s_bar_knob: Sprite? = null
    protected var mainFont: BitmapFont? = null
    protected var actionFont: BitmapFont? = null
    protected var panelFont: BitmapFont? = null
    protected var stage: Stage? = null
    var batch: SpriteBatch? = null
    var logLabel: Label? = null
    var actionLabel: Label? = null
    var aliceLabel: Label? = null
    var bobLabel: Label? = null
    var evaLabel: Label? = null
    var button: TextButton? = null
    var path: String? = null
    var width = 0
    var height = 0
    var render = false
    override fun show() { //VisUI.load("uiskin.json");
        VisUI.load()
        val w = Gdx.graphics.width
        val h = Gdx.graphics.height
        val mainGuiGroup = HorizontalGroup()
        mainGuiGroup.height = h / 6.toFloat()
        mainGuiGroup.width = w.toFloat()
        mainGuiGroup.setPosition(0f, 0f)
        val generator = FreeTypeFontGenerator(Gdx.files.internal("10771.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 16
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}"
        mainFont = generator.generateFont(parameter)
        parameter.size = 20
        actionFont = generator.generateFont(parameter)
        parameter.size = 27
        panelFont = generator.generateFont(parameter)
        val atlas = TextureAtlas("buttons.pack")
        val skin = Skin(atlas)
        val style = TextButtonStyle()
        val up = skin.getDrawable("button_off")
        val down = skin.getDrawable("button_on")
        style.up = up
        style.down = down
        style.font = LevelManager.Companion.mainFont
        val leftGroup = VerticalGroup()
        button = TextButton("Start", style)
        button!!.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                play()
            }
        })
        button!!.height = h / 21.toFloat()
        val tableStart = Table()
        tableStart.add<TextButton>(button).height(button!!.height)
        leftGroup.addActor(tableStart)
        val tableReset = Table()
        leftGroup.addActor(tableReset)
        leftGroup.center()
        //loadButton.pack();
        val middleGroup = VerticalGroup()
        /*TextureRegionDrawable regionDrawable = new TextureRegionDrawable(new TextureRegion(checkboxFalseTexture));
        regionDrawable.setMinHeight(h/32);
        regionDrawable.setMinWidth(h / 32);
        TextureRegionDrawable regionDrawable1 = new TextureRegionDrawable(new TextureRegion(checkboxTrueTexture));
        regionDrawable1.setMinHeight(h/32);
        regionDrawable1.setMinWidth(h/32);
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(regionDrawable, regionDrawable1, LevelManager.mainFont, Color.WHITE);
        final CheckBox checkBox = new CheckBox("Encrypter", checkBoxStyle);
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });*/
        val textStyle = TextFieldStyle()
        textStyle.font = LevelManager.Companion.mainFont
        textStyle.fontColor = Color.WHITE
        textStyle.background = TextureRegionDrawable(TextureRegion(Texture("bar_horizontal2.png")))
        //textStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture("barRed_horizontalMid.png")));
        val widthArea = TextArea("8", textStyle)
        val heightArea = TextArea("8", textStyle)
        val table = Table()
        //table.add(area).height(h / 36).width(w / 9).center().row();
//table.add(pathArea).height(h / 36).width(w / 9).center().row();
        table.add(widthArea).height(h / 36.toFloat()).width(w / 9.toFloat()).center().row()
        table.add(heightArea).height(h / 36.toFloat()).width(w / 9.toFloat()).center().row()
        table.setFillParent(true)
        //table.debug();
//table.center();
        table.pack()
        val checkboxTable = Table() //for future purposes
        //checkboxTable.debugAll();
//checkboxTable.add(checkBox).padTop(h/42);
        checkboxTable.pack()
        middleGroup.addActor(table)
        middleGroup.addActor(checkboxTable)
        mainGuiGroup.addActor(leftGroup)
        mainGuiGroup.addActor(middleGroup)
        println(mainGuiGroup.width.toString() + " " + mainGuiGroup.height)
        //actionLabel.debug();
        val group = VerticalGroup()
        group.center()
        val newButton: Button = TextButton("New", style)
        FileChooser.setDefaultPrefsName("ru.myitschool.dcrawler.filechooser")
        val chooser = FileChooser(FileChooser.Mode.OPEN)
        chooser.setListener(object : SingleFileChooserListener() {
            override fun selected(file: FileHandle) {
                val room = Room(file.path())
                (Gdx.app.applicationListener as Game).screen = MainScreen(room, room.width, room.height)
            }
        })
        newButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                val width = widthArea.text.toInt()
                val height = heightArea.text.toInt()
                (Gdx.app.applicationListener as Game).screen = MainScreen(null, width, height)
            }
        })
        table.add(newButton).center().row()
        val loadButton: Button = TextButton("Load", style)
        loadButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) { //dialog.show(stage);
                stage!!.addActor(chooser)
            }
        })
        table.add(loadButton).center().row()
        batch = SpriteBatch()
        //group.addActor(area);
/*group.addActor(pathArea);
        group.addActor(widthArea);
        group.addActor(heightArea);
        group.addActor(loadButton);
        group.setFillParent(true);*/stage = Stage()
        //stage.addActor(mainGuiGroup);
        stage!!.addActor(table)
        stage!!.addActor(group)
        //stage.addActor(pathArea);
        val inputMultiplexer = InputMultiplexer(stage)
        inputMultiplexer.addProcessor(object : InputAdapter() {
            override fun keyUp(keycode: Int): Boolean {
                if (keycode == Input.Keys.M) {
                    println(widthArea.text)
                }
                return false
            }
        })
        Gdx.input.inputProcessor = inputMultiplexer
    }

    var defaultStatesCount = 1f
    fun play() {}
    fun reset() {
        button!!.setText("Start")
    }

    fun update() {}
    //---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------
    override fun render(delta: Float) //кажый кадр
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        stage!!.draw()
        stage!!.act()
        if (render) {
            chooser!!.draw(batch, 1f)
        }
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
        logLabel!!.setText(text)
    }

    //---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------
    override fun resize(width: Int, height: Int) {}

    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}