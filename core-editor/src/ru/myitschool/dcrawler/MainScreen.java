package ru.myitschool.dcrawler;

import com.badlogic.gdx.*;
import ru.myitschool.dcrawler.dungeon.ExitPattern;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.dungeon.Exit;
import ru.myitschool.dcrawler.tiles.DungeonTile;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.SingleFileChooserListener;

/**
 * Created by КАРАТ on 21.03.2017.
 */

public class MainScreen implements Screen, GestureDetector.GestureListener {

    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    static DungeonTile tile;

    protected BitmapFont mainFont;
    protected BitmapFont actionFont;
    protected BitmapFont panelFont;
    protected Stage stage;

    Texture checkboxFalseTexture;
    Texture checkboxTrueTexture;

    Room room;

    Label logLabel;
    Label actionLabel;
    TextButton button;

    int width;
    int height;
    int layerIndex = 0;

    public MainScreen(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void show()
    {
        DungeonTile.initTiles();
        room = new Room(width, height, false);
        final TiledMapTileLayer layer = new TiledMapTileLayer(width, height, 32, 32);
        int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();
        checkboxFalseTexture = new Texture(Gdx.files.internal("ch_no.png"));
        checkboxTrueTexture = new Texture(Gdx.files.internal("ch_yes.png"));

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
        TextureAtlas atlas = new TextureAtlas("uiskin.atlas");
        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"),atlas);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        //Drawable up = skin.getDrawable("button_off");
        //Drawable down = skin.getDrawable("button_on");
        //style.up = up;
        //style.down = down;
        style.font = LevelManager.mainFont;
        VerticalGroup leftGroup = new VerticalGroup();

        FileChooser.setDefaultPrefsName("com.your.package.here.filechooser");
        final FileChooser chooser = new FileChooser(FileChooser.Mode.SAVE);
        chooser.setListener(new SingleFileChooserListener() {
            @Override
            protected void selected(FileHandle file) {
                for (int k = 0; k < map.getLayers().getCount(); k++) {
                    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(k);
                    for (int i = 0; i < width; i++) {
                        for (int j = 0; j < height; j++) {
                            TiledMapTileLayer.Cell cell = layer.getCell(i, j);
                            if (cell != null) {
                                if (k == 0) {
                                    room.setCell(i, j, cell.getTile().getId());
                                } else {
                                    room.getPattern(k - 1).getCells()[i][j] = cell.getTile().getId();
                                }
                            }
                        }
                    }
                }
                Gson gson = new Gson();
                file.writeString(gson.toJson(room, Room.class), false);
            }
        });

        button = new TextButton("Save", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.addActor(chooser);
            }
        });
        button.setHeight(h / 21);
        Table tableStart = new Table();
        tableStart.add(button).height(button.getHeight());
        leftGroup.addActor(tableStart);
        Table tableReset = new Table();
        leftGroup.addActor(tableReset);
        leftGroup.center();
        //button.pack();

        tile = DungeonTile.getTile(0);
        HorizontalGroup middleGroup = new HorizontalGroup();
        middleGroup.fill();
        System.out.println(DungeonTile.tiles.size);
        for (final DungeonTile tile : DungeonTile.tiles){
            if (tile != null) {
                TextureRegion region = tile.getTextureRegion();
                TextureRegionDrawable drawable = new TextureRegionDrawable(region);
                Image image = new Image(drawable);
                image.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        MainScreen.tile = tile;
                        return true;
                    }
                });
                middleGroup.addActor(image);
            }
        }

        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = LevelManager.mainFont;
        textStyle.fontColor = Color.WHITE;
        final TextArea area = new TextArea("1.0", textStyle);
        TextureRegionDrawable regionDrawable = new TextureRegionDrawable(new TextureRegion(checkboxFalseTexture));
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
        });
        Table table = new Table();
        table.add(area).height(h / 36).center();
        //table.center();
        table.pack();
        Table checkboxTable = new Table();
        checkboxTable.add(checkBox).padTop(h/42);
        checkboxTable.pack();
        //middleGroup.addActor(table);
        //middleGroup.addActor(checkboxTable);

        VerticalGroup rightGroup = new VerticalGroup();
        final TextField field = new TextField(layerIndex + "", skin);
        TextButton upButton = new TextButton("/\\", skin);
        upButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                layerIndex++;
                if (layerIndex == map.getLayers().getCount()){
                    layerIndex--;
                }
                field.setText(layerIndex + "");
                showLayers();
            }
        });
        TextButton newButton = new TextButton("New", skin);
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String[] sides = {"", "North", "East", "South", "West"};
                final SelectBox<String> area1 = new SelectBox(skin);
                area1.setItems(sides);
                final SelectBox<String> area2 = new SelectBox(skin);
                area2.setItems(sides);
                final SelectBox<String> area3 = new SelectBox<String>(skin);
                area3.setItems(sides);
                final SelectBox<String> area4 = new SelectBox<String>(skin);
                area4.setItems(sides);
                final LayerDialog dialog = new LayerDialog("Statements", skin) {
                    @Override
                    protected void result(Object object) {
                        boolean check = ((int) object) > 0;
                        if (check) {
                            Array<Integer> statements = new Array<>(4);
                            if (area1.getSelectedIndex() != 0) {
                                statements.add(area1.getSelectedIndex() - 1);
                            }
                            if (area2.getSelectedIndex() != 0) {
                                statements.add(area2.getSelectedIndex() - 1);
                            }
                            if (area3.getSelectedIndex() != 0) {
                                statements.add(area3.getSelectedIndex() - 1);
                            }
                            if (area4.getSelectedIndex() != 0) {
                                statements.add(area4.getSelectedIndex() - 1);
                            }
                            int[] states = new int[statements.size];
                            for (int i = 0; i < states.length; i++) {
                                states[i] = statements.get(i);
                            }
                            ExitPattern pattern = new ExitPattern(states, width, height);
                            room.addExitPattern(pattern);
                            layerIndex = map.getLayers().getCount();
                            field.setText(layerIndex + "");
                            map.getLayers().add(new TiledMapTileLayer(width, height, 32, 32));
                            showLayers();
                        }
                    }
                };
                dialog.add(area1);
                dialog.add(area2);
                dialog.add(area3);
                dialog.add(area4);
                TextButton createButton = new TextButton("Create", skin);
                dialog.button(createButton, 1);
                TextButton cancelButton = new TextButton("Cancel", skin);
                dialog.button(cancelButton, -1);
                dialog.show(stage);
            }
        });
        TextButton removeButton = new TextButton("x", skin);
        removeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                map.getLayers().remove(layerIndex);
                layerIndex--;
                if (layerIndex < 0){
                    layerIndex = 0;
                }
                field.setText(layerIndex + "");
                showLayers();
            }
        });
        TextButton downButton = new TextButton("\\/", skin);
        downButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                layerIndex--;
                if (layerIndex < 0){
                    layerIndex = 0;
                }
                field.setText(layerIndex + "");
                showLayers();
            }
        });
        rightGroup.addActor(field);
        rightGroup.addActor(upButton);
        rightGroup.addActor(newButton);
        rightGroup.addActor(removeButton);
        rightGroup.addActor(downButton);

        mainGuiGroup.addActor(leftGroup);
        mainGuiGroup.addActor(middleGroup);
        mainGuiGroup.addActor(rightGroup);

        System.out.println(mainGuiGroup.getWidth() + " " + mainGuiGroup.getHeight());

        //actionLabel.debug();

        Table testTable = new Table();
        testTable.debug();
        final TextArea pathArea = new TextArea("pattern.room", textStyle);
        final TextArea widthArea = new TextArea("8", textStyle);
        final TextArea heightArea = new TextArea("8", textStyle);
        Button button = new TextButton("Create", style);
        testTable.add(pathArea).row();
        testTable.add(widthArea).row();
        testTable.add(heightArea).row();
        testTable.add(button).row();
        testTable.setFillParent(true);

        stage = new Stage();
        stage.addActor(mainGuiGroup);
        //stage.addActor(testTable);

        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tile);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                layer.setCell(i, j, cell);
            }
        }

        GestureDetector gd = new GestureDetector(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage);
        inputMultiplexer.addProcessor(new InputAdapter(){
            int button = 0;

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.M){

                }
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.RIGHT){
                    this.button = Input.Buttons.RIGHT;
                } else {
                    this.button = Input.Buttons.LEFT;
                }
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                act(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                act(screenX, screenY);
                this.button = -1;
                return true;
            }

            private void act(int screenX, int screenY){
                Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
                int cellX = (int) (pos.x / DungeonTile.TILE_WIDTH);
                int cellY = (int) (pos.y / DungeonTile.TILE_HEIGHT);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                if (button == Input.Buttons.LEFT){
                    cell.setTile(tile);
                } else if (button == Input.Buttons.RIGHT){
                    cell = null;
                }
                ((TiledMapTileLayer) map.getLayers().get(layerIndex)).setCell(cellX, cellY, cell);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(true);
        camera.translate(- w / 2, - h / 2);
        map = new TiledMap();

        map.getLayers().add(layer);
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    float defaultStatesCount = 1;

    public void showLayers(){
        for (int i = 1; i < map.getLayers().getCount(); i++) {
            if (i != layerIndex){
                map.getLayers().get(i).setVisible(false);
            } else {
                map.getLayers().get(i).setVisible(true);
            }
        }
    }

    abstract class LayerDialog extends Dialog {

        public LayerDialog(String title, Skin skin) {
            super(title, skin);
        }

        @Override
        public Dialog key(int keycode, Object object) {
            return super.key(keycode, object);
        }

        @Override
        protected abstract void result(Object object);
    }

    public void reset(){
        button.setText("Start");
    }

    //---------------------------------------------------------------
//=====                                   =======================
//------------------------------------------------------------------------
    @Override
    public void render(float delta)//кажый кадр
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();

        stage.draw();
        stage.act();
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        return false;
    }

    public void initRandoms() {

    }

    @Override
    public boolean longPress(float x, float y)
    {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {return false;}

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {

        return true;
    }



    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
    {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
