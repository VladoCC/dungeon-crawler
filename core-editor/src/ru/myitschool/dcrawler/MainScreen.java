package ru.myitschool.dcrawler;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.ExitPattern;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.dungeon.Exit;
import ru.myitschool.dcrawler.ui.tiles.DungeonTile;
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

import java.util.ArrayList;

public class MainScreen implements Screen, GestureDetector.GestureListener {

    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private static DungeonTile tile;

    protected BitmapFont mainFont;
    protected BitmapFont actionFont;
    protected BitmapFont panelFont;
    protected Stage stage = new Stage();

    private Skin skin;

    private Texture checkboxFalseTexture;
    private Texture checkboxTrueTexture;

    private Room room;
    private Room original;

    private Image frameImage;
    private Label logLabel;
    private Label actionLabel;
    private TextButton button;
    private VerticalGroup layersGroup;

    private ArrayList<Boolean> shownLayers;

    private int width;
    private int height;
    private int layerIndex = 0;

    public MainScreen(Room original, int width, int height) {
        this.width = width;
        this.height = height;
        this.original = original;
    }

    @Override
    public void show() {
        DungeonTile.initTiles();
        shapeRenderer.setAutoShapeType(true);
        room = new Room(width, height, false);
        final TiledMapTileLayer layer = new TiledMapTileLayer(width, height, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
        int w = Gdx.graphics.getWidth();
        final int h = Gdx.graphics.getHeight();
        checkboxFalseTexture = new Texture(Gdx.files.internal("ch_no.png"));
        checkboxTrueTexture = new Texture(Gdx.files.internal("ch_yes.png"));

        frameImage = new Image();

        final HorizontalGroup mainGuiGroup = new HorizontalGroup();
        mainGuiGroup.setHeight(h/6);
        mainGuiGroup.setWidth(w);
        mainGuiGroup.setPosition(0, 0);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("unifont-12.1.02.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}↷↶";
        mainFont = generator.generateFont(parameter);
        parameter.size = 20;
        actionFont = generator.generateFont(parameter);
        parameter.size = 27;
        panelFont = generator.generateFont(parameter);
        TextureAtlas atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"),atlas);
        skin.add("default-font", new BitmapFont(), BitmapFont.class);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        //Drawable up = skin.getDrawable("button_off");
        //Drawable down = skin.getDrawable("button_on");
        //style.up = up;
        //style.down = down;
        style.font = panelFont;
        VerticalGroup leftGroup = new VerticalGroup();
        FileChooser.setDefaultPrefsName("ru.myitschool.dcrawler.filechooser");
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
        //button.setStyle(style);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.addActor(chooser);
            }
        });
        button.setHeight(h / 21);
        //button.pack();
        Table saveTable = new Table();
        saveTable.add(button).height(button.getHeight());
        leftGroup.addActor(saveTable);

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

        VerticalGroup rightGroup = new VerticalGroup();

        final TextButton rotateClockwiseButton = new TextButton("↷", skin);
        rotateClockwiseButton.setStyle(style);
        rotateClockwiseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (int k = 0; k < map.getLayers().getCount(); k++) {
                    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(k);
                    TiledMapTileLayer.Cell[][] cells = new TiledMapTileLayer.Cell[room.getHeight()][room.getWidth()];
                    for (int i = 0; i < room.getWidth(); i++) {
                        for (int j = 0; j < room.getHeight(); j++) {
                            cells[cells.length - j - 1][i] = layer.getCell(i, j);
                        }
                    }
                    for (int i = 0; i < cells.length; i++) {
                        for (int j = 0; j < cells[0].length; j++) {
                            layer.setCell(i, j, cells[i][j]);
                        }
                    }
                }
                for (ExitPattern pattern : room.getPatterns()){
                    for (int i = 0; i < pattern.getStatement().length; i++) {
                        pattern.getStatement()[i]++;
                        pattern.getStatement()[i] %= Exit.EXIT_SIDES.length;
                    }
                }
                showLayers();
            }
        });
        TextButton newButton = new TextButton("New", skin); //TODO move this button somewhere more convenient
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final TextArea nameArea = new TextArea("Exit Pattern", skin);
                final ArrayList<CheckBox> checkBoxArray = new ArrayList<>();
                for (String name : Exit.EXIT_SIDES){
                    checkBoxArray.add(new CheckBox(name, skin));
                }
                final EditorDialog dialog = new EditorDialog("Statements", skin) {
                    @Override
                    protected void result(Object object) {
                        boolean check = ((int) object) > 0;
                        if (check) {
                            Array<Integer> statements = new Array<>(checkBoxArray.size());
                            for (int i = 0; i < checkBoxArray.size(); i++) {
                                if (checkBoxArray.get(i).isChecked()){
                                    statements.add(i);
                                }
                            }
                            int[] states = new int[statements.size];
                            for (int i = 0; i < states.length; i++) {
                                states[i] = statements.get(i);
                            }
                            ExitPattern pattern = new ExitPattern(states, width, height, nameArea.getText());
                            room.addExitPattern(pattern);
                            layerIndex = map.getLayers().getCount();
                            //field.setText(layerIndex + "");
                            map.getLayers().add(new TiledMapTileLayer(width, height, 32, 32));
                            shownLayers.add(true);
                            updateLayers();
                            showLayers();
                        }
                    }
                };
                VerticalGroup group = new VerticalGroup();
                group.setOrigin(Align.left);
                group.addActor(nameArea);
                for (CheckBox checkBox : checkBoxArray){
                    group.addActor(checkBox);
                }
                group.pack();
                dialog.getContentTable().add(group);
                //dialog.pack();
                TextButton createButton = new TextButton("Create", skin);
                dialog.button(createButton, 1);
                TextButton cancelButton = new TextButton("Cancel", skin);
                dialog.button(cancelButton, -1);
                dialog.align(Align.top);
                dialog.show(stage);
            }
        });
        TextButton rotateAnticlockwiseButton = new TextButton("↶", skin);
        rotateAnticlockwiseButton.setStyle(style);
        rotateAnticlockwiseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (int k = 0; k < map.getLayers().getCount(); k++) {
                    TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(k);
                    TiledMapTileLayer.Cell[][] cells = new TiledMapTileLayer.Cell[room.getHeight()][room.getWidth()];
                    for (int i = 0; i < room.getWidth(); i++) {
                        for (int j = 0; j < room.getHeight(); j++) {
                            cells[j][cells[0].length - i - 1] = layer.getCell(i, j);
                        }
                    }
                    for (int i = 0; i < cells.length; i++) {
                        for (int j = 0; j < cells[0].length; j++) {
                            layer.setCell(i, j, cells[i][j]);
                        }
                    }
                }
                for (ExitPattern pattern : room.getPatterns()){
                    for (int i = 0; i < pattern.getStatement().length; i++) {
                        pattern.getStatement()[i]--;
                        pattern.getStatement()[i] += Exit.EXIT_SIDES.length;
                        pattern.getStatement()[i] %= Exit.EXIT_SIDES.length;
                        System.out.print(pattern.getStatement()[i] + " ");
                    }
                    System.out.println();
                }
                showLayers();
            }
        });
        rightGroup.addActor(rotateClockwiseButton);
        rightGroup.addActor(newButton);
        rightGroup.addActor(rotateAnticlockwiseButton);

        mainGuiGroup.addActor(leftGroup);
        mainGuiGroup.addActor(middleGroup);
        mainGuiGroup.addActor(rightGroup);

        System.out.println(mainGuiGroup.getWidth() + " " + mainGuiGroup.getHeight());

        shownLayers = new ArrayList<>();
        shownLayers.add(true);

        if (original != null){
            for (int i = 0; i < original.getPatterns().size; i++) {
                shownLayers.add(false);
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
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return act(screenX, screenY);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                boolean result = act(screenX, screenY);
                this.button = -1;
                return result;
            }

            private float zoomMult = 0.03f;
            private float xTransMult = 597.5f * zoomMult;
            private float yTransMult = 337.5f * zoomMult;

            @Override
            public boolean scrolled(int amount) {
                if (camera.zoom + (amount * zoomMult) > 0) {
                    camera.zoom += (amount * zoomMult);
                    camera.translate(amount * xTransMult, amount * yTransMult);
                    camera.update();
                }
                return super.scrolled(amount);
            }

            private boolean act(int screenX, int screenY){
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
                return ((cellX > -1) && (cellX < room.getWidth()) && (cellY > -1) && (cellY < room.getHeight()));
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(true);
        camera.translate(-2, -2);
        camera.update();
        map = new TiledMap();
        map.getLayers().add(layer);

        if (original != null) {
            for (int i = 0; i < original.getWidth(); i++) {
                for (int j = 0; j < original.getHeight(); j++) {
                    Integer index = original.getCell(i, j);
                    if (index != null) {
                        layer.setCell(i, j, new DungeonCell(DungeonTile.getTile(index)));
                    }
                }
            }
            for (ExitPattern pattern : original.getPatterns()) {
                room.addExitPattern(pattern);
                TiledMapTileLayer tileLayer = new TiledMapTileLayer(width, height, DungeonTile.TILE_WIDTH, DungeonTile.TILE_HEIGHT);
                for (int i = 0; i < original.getWidth(); i++) {
                    for (int j = 0; j < original.getWidth(); j++) {
                        Integer index = pattern.getCells()[i][j];
                        if (index != null) {
                            tileLayer.setCell(i, j, new DungeonCell(DungeonTile.getTile(index)));
                        }
                    }
                }
                map.getLayers().add(tileLayer);
            }
        }

        layersGroup = new VerticalGroup();
        layersGroup.setPosition(w / 7 * 6, 40);
        layersGroup.setWidth(w / 7);
        layersGroup.setHeight(h - 60);
        layersGroup.align(Align.left);
        updateLayers();
        showLayers();

        stage.addActor(mainGuiGroup);
        stage.addActor(layersGroup);

        renderer = new OrthogonalTiledMapRenderer(map);
    }

    private void updateLayers() {
        layersGroup.clear();
        for (int i = 0; i < shownLayers.size(); i++) {
            final int finalI = i;
            Stack stack = new Stack();
            //stack.debugAll();

            HorizontalGroup group = new HorizontalGroup();
            group.setFillParent(false);
            group.align(Align.left);
            String name = "";
            if (i == 0) {
                name = "Base";
            } else {
                name = room.getPattern(i - 1).getName();
            }
            final CheckBox checkBox = new CheckBox("", skin);
            checkBox.setChecked(shownLayers.get(i));
            checkBox.padLeft(5);
            checkBox.padRight(10);
            checkBox.align(Align.left);
            checkBox.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    shownLayers.set(finalI, checkBox.isChecked());
                    showLayers();
                }
            });
            group.addActor(checkBox);
            Label textField = new Label(i + ". " + name, skin);
            textField.addListener(new InputListener(){

                private int button = -1;

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (button == Input.Buttons.RIGHT){
                        this.button = Input.Buttons.RIGHT;
                    } else {
                        this.button = Input.Buttons.LEFT;
                    }
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (button == Input.Buttons.LEFT && finalI != layerIndex) {
                        changeFocusedLayer(finalI);
                    } else if (button == Input.Buttons.RIGHT && finalI != 0){
                        final TextArea nameLabel = new TextArea(room.getPattern(finalI - 1).getName(), skin);
                        EditorDialog dialog = new EditorDialog("Rename", skin) {
                            @Override
                            protected void result(Object object) {
                                boolean check = ((int) object) > 0;
                                if (check){
                                    room.getPattern(finalI - 1).setName(nameLabel.getText());
                                    updateLayers();
                                }
                            }
                        };
                        dialog.getContentTable().add(nameLabel);
                        //dialog.pack();
                        TextButton createButton = new TextButton("OK", skin);
                        dialog.button(createButton, 1);
                        TextButton cancelButton = new TextButton("Cancel", skin);
                        dialog.button(cancelButton, -1);
                        dialog.align(Align.top);
                        dialog.show(stage);
                    }
                }
            });
            group.addActor(textField);
            TextButton button = new TextButton("x", skin);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    shownLayers.remove(finalI);
                    room.getPatterns().removeIndex(finalI - 1);
                    updateLayers();
                    map.getLayers().remove(finalI);
                    if (finalI == layerIndex){
                        changeFocusedLayer(layerIndex - 1);
                    }
                }
            });
            if (i != 0) {
                group.addActor(button);
            }
            Widget widget = new Widget(){
                @Override
                public float getPrefWidth() {
                    return Math.max(super.getPrefWidth(), 5);
                }
            };
            widget.setWidth(10);
            widget.setHeight(1);
            group.addActor(widget);

            if (layerIndex == i){
                System.out.println(((int) checkBox.getWidth()) + " " + ((int) checkBox.getHeight()));
                Pixmap pixmap = new Pixmap(((int) checkBox.getWidth()) + 20, ((int) checkBox.getHeight()), Pixmap.Format.RGBA8888);
                pixmap.setColor(Color.WHITE);
                pixmap.drawRectangle(0, 0, ((int) checkBox.getWidth()) + 20, ((int) checkBox.getHeight()));
                frameImage.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));
                stack.addActor(frameImage);
            }

            stack.addActor(group);
            //stack.setFillParent(true);
            layersGroup.addActorAt(0, stack);
        }
        layersGroup.pack();
    }

    private void changeFocusedLayer(int index){
        layerIndex = index;
        if (!shownLayers.get(index)){
            shownLayers.set(index, true);
        }
        updateLayers();
    }

    float defaultStatesCount = 1;

    public void showLayers(){
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            if (shownLayers.get(i)){
                map.getLayers().get(i).setVisible(true);
            } else {
                map.getLayers().get(i).setVisible(false);
            }
        }
    }

    abstract class EditorDialog extends Dialog {

        public EditorDialog(String title, Skin skin) {
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

    @Override
    public void render(float delta)//кажый кадр
    {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();

        stage.draw();
        stage.act();

        shapeRenderer.begin();
        shapeRenderer.rect(1, Gdx.graphics.getHeight() - (DungeonTile.TILE_HEIGHT * height) / camera.zoom, (DungeonTile.TILE_WIDTH * width) / camera.zoom, (DungeonTile.TILE_HEIGHT * height) / camera.zoom);
        shapeRenderer.end();
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
