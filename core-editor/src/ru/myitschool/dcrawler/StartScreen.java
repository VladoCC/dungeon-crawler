package ru.myitschool.dcrawler;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;
import ru.myitschool.dcrawler.dungeon.Room;

/**
 * Created by КАРАТ on 21.03.2017.
 */

public class StartScreen implements Screen
{
    FileChooser chooser;

    Texture t_bar_back, t_background, checkboxFalseTexture, checkboxTrueTexture;
    Sprite s_bar_back, s_background;

    Texture t_bar_knob;
    Sprite s_bar_knob;

    protected BitmapFont mainFont;
    protected BitmapFont actionFont;
    protected BitmapFont panelFont;
    protected Stage stage;
    SpriteBatch batch;

    Label logLabel;
    Label actionLabel;
    Label aliceLabel;
    Label bobLabel;
    Label evaLabel;
    TextButton button;

    String path;
    int width;
    int height;
    boolean render = false;

    @Override
    public void show()
    {
        //VisUI.load("uiskin.json");
        VisUI.load();
        int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();

        final HorizontalGroup mainGuiGroup = new HorizontalGroup();
        mainGuiGroup.setHeight(h/6);
        mainGuiGroup.setWidth(w);
        mainGuiGroup.setPosition(0, 0);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("10771.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}";
        mainFont = generator.generateFont(parameter);
        parameter.size = 20;
        actionFont = generator.generateFont(parameter);
        parameter.size = 27;
        panelFont = generator.generateFont(parameter);
        TextureAtlas atlas = new TextureAtlas("buttons.pack");
        Skin skin = new Skin(atlas);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        Drawable up = skin.getDrawable("button_off");
        Drawable down = skin.getDrawable("button_on");
        style.up = up;
        style.down = down;
        style.font = LevelManager.mainFont;
        VerticalGroup leftGroup = new VerticalGroup();
        button = new TextButton("Start", style);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                play();
            }
        });
        button.setHeight(h / 21);
        Table tableStart = new Table();
        tableStart.add(button).height(button.getHeight());
        leftGroup.addActor(tableStart);
        Table tableReset = new Table();
        leftGroup.addActor(tableReset);
        leftGroup.center();
        //loadButton.pack();

        VerticalGroup middleGroup = new VerticalGroup();
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
        TextArea.TextFieldStyle textStyle = new TextArea.TextFieldStyle();
        textStyle.font = LevelManager.mainFont;
        textStyle.fontColor = Color.WHITE;
        textStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture("bar_horizontal2.png")));
        //textStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture("barRed_horizontalMid.png")));
        final TextArea widthArea = new TextArea("8", textStyle);
        final TextArea heightArea = new TextArea("8", textStyle);
        Table table = new Table();
        //table.add(area).height(h / 36).width(w / 9).center().row();
        //table.add(pathArea).height(h / 36).width(w / 9).center().row();
        table.add(widthArea).height(h / 36).width(w / 9).center().row();
        table.add(heightArea).height(h / 36).width(w / 9).center().row();
        table.setFillParent(true);
        //table.debug();
        //table.center();
        table.pack();
        Table checkboxTable = new Table(); //for future purposes
        //checkboxTable.debugAll();
        //checkboxTable.add(checkBox).padTop(h/42);
        checkboxTable.pack();
        middleGroup.addActor(table);
        middleGroup.addActor(checkboxTable);

        mainGuiGroup.addActor(leftGroup);
        mainGuiGroup.addActor(middleGroup);

        System.out.println(mainGuiGroup.getWidth() + " " + mainGuiGroup.getHeight());

        //actionLabel.debug();

        VerticalGroup group = new VerticalGroup();
        group.center();
        Button newButton = new TextButton("New", style);

        FileChooser.setDefaultPrefsName("ru.myitschool.dcrawler.filechooser");
        final FileChooser chooser = new FileChooser(FileChooser.Mode.OPEN);
        chooser.setListener(new SingleFileChooserListener() {
            @Override
            protected void selected(FileHandle file) {
                Room room = new Room(file.path());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ru.myitschool.dcrawler.MainScreen(room, room.getWidth(), room.getHeight()));
            }
        });

        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final int width = Integer.parseInt(widthArea.getText());
                final int height = Integer.parseInt(heightArea.getText());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ru.myitschool.dcrawler.MainScreen(null, width, height));
            }
        });

        table.add(newButton).center().row();

        Button loadButton = new TextButton("Load", style);

        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //dialog.show(stage);
                stage.addActor(chooser);
            }
        });

        table.add(loadButton).center().row();

        batch = new SpriteBatch();
        //group.addActor(area);
        /*group.addActor(pathArea);
        group.addActor(widthArea);
        group.addActor(heightArea);
        group.addActor(loadButton);
        group.setFillParent(true);*/

        stage = new Stage();
        //stage.addActor(mainGuiGroup);
        stage.addActor(table);
        stage.addActor(group);
        //stage.addActor(pathArea);

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage);
        inputMultiplexer.addProcessor(new InputAdapter(){
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.M){
                    System.out.println(widthArea.getText());
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    float defaultStatesCount = 1;

    public void play(){
    }

    public void reset(){
        button.setText("Start");
    }


    public void update(){
    }

    //---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------
    @Override
    public void render(float delta)//кажый кадр
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();
        stage.act();

        if (render){
            chooser.draw(batch, 1f);
        }
    }

    float normallStateCount = 1;
    boolean logger = false;

    float time = 0;
    final float ANIM_TIME = 0.5f;
    float logTime = 0;
    boolean checkInfo = false;


    public void setAction(String action){
        actionLabel.setText(action);
    }

    public void addLog(String log){
        String text = log + '\n' + logLabel.getText().toString();
        logLabel.setText(text);
    }

//---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------


    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
