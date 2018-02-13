package ru.myitschool.cubegame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.scene.effect.BlendMode;
import ru.myitschool.cubegame.*;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.dungeon.Room;
import ru.myitschool.cubegame.effects.Effect;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.entities.Enemy;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.skills.Skill;
import ru.myitschool.cubegame.tiles.DungeonTile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

/**
 * Created by Voyager on 22.04.2017.
 */
public class DungeonScreen implements Screen {

    OrthographicCamera camera;
    ru.myitschool.cubegame.dungeon.DungeonMap dungeonMap;
    OrthogonalTiledMapRenderer renderer;

    private InputMultiplexer input;

    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean debugInfo = false;
    private boolean restart = false;
    private boolean button = false;

    private float width;
    private float height;
    private float animTime = 0;
    private float restartTimer = 0;

    private int mouseX = 0;
    private int mouseY = 0;

    private float xTooltip;
    private float yTooltip;

    private float h;
    private float w;

    private static Array<String> logs = new Array<String>();

    Stage stage;

    TextButton nextTurnButton;
    TextButton attackButton;
    TextButton cancelButton;

    HorizontalGroup effectGroup;
    HorizontalGroup skillGroup;
    VerticalGroup buttonsGroup;

    Table attackButtonTable;
    Table cancelButtonTable;
    Table turnButtonTable;
    Table guiTable;
    Table tooltipGroup;
    static Table frameTable;

    protected BitmapFont font;
    protected BitmapFont headerFont;

    protected ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected SpriteBatch charBatch = new SpriteBatch();
    protected SpriteBatch interfaceBatch = new SpriteBatch();

    TextureAtlas atlas;
    Skin skin;

    public DungeonScreen(InputMultiplexer input) {
        this.input = input;
    }

    @Override
    public void show() {
        InterfaceElements.init();
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        //final float w = Gdx.graphics.getWidth();
        //final float h = Gdx.graphics.getHeight();
        tooltipGroup = new Table();
        camera = new OrthographicCamera(w, h);
        camera.setToOrtho(true);
        camera.update();
        input.addProcessor(new InputAdapter(){
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                mouseX = screenX;
                mouseY = screenY;
                Vector3 coords = camera.unproject(new Vector3(screenX,screenY, 0));
                System.out.println(coords);
                float wMoveField = w/20;
                float hMoveField = h/20;
                System.out.println("screenMoving");
                if (screenX < wMoveField && !moveLeft){
                    moveLeft = true;
                    width = w;
                } else if (screenX >= wMoveField && moveLeft) {
                    moveLeft = false;
                } else if (screenX > w - wMoveField && !moveRight) {
                    moveRight = true;
                    width = w;
                } else if (screenX <= w - wMoveField && moveRight) {
                    moveRight = false;
                }
                if (screenY < hMoveField && !moveUp){
                    moveUp = true;
                    height = h;
                } else if (screenY >= hMoveField && moveUp) {
                    moveUp = false;
                } else if (screenY > h - hMoveField && !moveDown) {
                    moveDown = true;
                    height = h;
                } else if (screenY <= h - hMoveField && moveDown) {
                    moveDown = false;
                }
                Entity.getNowPlaying().setDetailedEffect(-1); //TODO ЭТО ВООБЩЕ НУЖНО? (СКОРЕЕ ВСЕГО НЕТ) ПРОВЕРИТЬ!
                Entity.getNowPlaying().setDetailedSkill(-1);
                tooltipGroup.remove();
                return false; //TODO true or false? to be or not to be?
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.GRAVE) {
                    System.out.println("cam info");
                    if (debugInfo) {
                        debugInfo = false;
                    } else {
                        debugInfo = true;
                    }
                    return true;
                } else if (keycode == Input.Keys.R){
                    restart = false;
                    if (restartTimer >= 5){
                        camera.position.x = 0;
                        camera.position.y = 0;
                        dungeonMap = new ru.myitschool.cubegame.dungeon.DungeonMap(12, 3, 21, camera, input);
                        renderer = new OrthogonalTiledMapRenderer(dungeonMap);
                    }
                    return true;
                } else if (keycode == Input.Keys.A){
                    nextTurnButton.setText(nextTurnButton.getText() + "A");
                    System.out.println("!!!");
                } else if (keycode == Input.Keys.C){
                    for (Entity entity : Entity.getPlayingEntities()){
                        if (entity.isEnemy()){
                            entity.setControlled(!entity.isControlled());
                        }
                    }
                } else if ((keycode > 6 && keycode < 17) || (keycode > 143 && keycode < 154)){
                    keycode -= 8;
                    if (keycode > 135){
                        keycode -= 137;
                    }
                    if (keycode == -1){
                        keycode = 10;
                    }
                    if (keycode < skillGroup.getChildren().size && keycode > -1){
                        Actor actor = skillGroup.getChildren().get(keycode);
                        InputEvent event1 = new InputEvent();
                        event1.setType(InputEvent.Type.touchDown);
                        actor.fire(event1);

                        InputEvent event2 = new InputEvent();
                        event2.setType(InputEvent.Type.touchUp);
                        actor.fire(event2);
                    }
                } else if (keycode == Input.Keys.J){
                    final GsonBuilder builder = new GsonBuilder();
                    builder.setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getName().equals("ready");
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> checkClass) {
                            return checkClass == TiledMapTile.BlendMode.class || checkClass == float.class || checkClass == TextureRegion.class;
                        }
                    });
                    builder.setPrettyPrinting();
                    Gson gson = builder.create();
                    FileHandle file = Gdx.files.local("tiles/tiles.list");
                    String s = gson.toJson(DungeonTile.tiles.items, DungeonTile[].class);
                    file.writeString(s, false);
                } else if (keycode == Input.Keys.K){
                    System.out.println(Entity.getPlayingEntities().size());
                }
                System.out.println(Input.Keys.toString(keycode));
                return false;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.R){
                    restart = true;
                } else if (keycode == Input.Keys.T){

                }
                return false;
            }
        });
        createFonts();
        stage = new Stage();
        guiTable = new Table();
        guiTable.setPosition(1,1);
        guiTable.setHeight(h/6 - 1);
        guiTable.setWidth(w - 1);
        guiTable.setDebug(true, true);
        guiTable.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("hm...");
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                System.out.println("hm...");
                return true;
            }
        });
        stage.addActor(guiTable);

        atlas = new TextureAtlas("buttons.pack");
        skin = new Skin(atlas);
        placeButton();
        turnButtonTable = new Table();
        turnButtonTable.add(nextTurnButton).width(w / 10).height(h / 12);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        Drawable up = skin.getDrawable("button_off");
        Drawable down = skin.getDrawable("button_on");
        style.up = up;
        style.down = down;
        style.font = font;
        attackButton = new TextButton("Attack", style);
        attackButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Entity.getNowPlaying().useSkill();
                changeButton(false);
            }
        });
        cancelButton = new TextButton("Cancel", style);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Entity.getNowPlaying().setUsedSkill(null);
                changeButton(false);
                DungeonMap.clearTargetLayer();
            }
        });
        attackButtonTable = new Table();
        cancelButtonTable = new Table();
        attackButtonTable.add(attackButton).width(w / 10).height(h / 18).padTop(h / 36).padBottom(h / 72);
        cancelButtonTable.add(cancelButton).width(w / 10).height(h / 18).padTop(h / 72).padBottom(h / 36);

        buttonsGroup = new VerticalGroup();
        buttonsGroup.setWidth(w / 10);
        buttonsGroup.setHeight(h / 6);
        buttonsGroup.setPosition(w * 9 / 10 - w / 100, 0);
        buttonsGroup.addActor(nextTurnButton);
        buttonsGroup.center();
        guiTable.addActor(buttonsGroup);

        effectGroup = new HorizontalGroup();
        effectGroup.setPosition(w / 5,  h / 8);
        effectGroup.setWidth(3 * w / 5);
        effectGroup.setHeight(h / 24);
        effectGroup.wrap(true);
        effectGroup.center();
        guiTable.addActor(effectGroup);

        skillGroup = new HorizontalGroup();
        skillGroup.setPosition(w/7, h / 72);
        skillGroup.setWidth(5 * w / 7);
        skillGroup.setHeight(h / 11);
        skillGroup.center();
        skillGroup.debugAll();
        guiTable.addActor(skillGroup);
        //guiTable.debugAll();

        frameTable = new Table();
        Drawable frameDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("frame.png")));
        frameDrawable.setMinHeight(h / 11);
        frameDrawable.setMinWidth(h / 11);
        Image frame = new Image(frameDrawable);
        frameTable.add(frame);

        input.addProcessor(stage);
        camera.position.x = 0;
        camera.position.y = 0;
        dungeonMap = new ru.myitschool.cubegame.dungeon.DungeonMap(12, 3, 21, camera, input);
        Character.setFirstPlaying();
        renderer = new OrthogonalTiledMapRenderer(dungeonMap);
        camera.zoom = 0.6f;
        shapeRenderer.setAutoShapeType(true);
        //Character character = new Character(new Texture("Char1.png"), 64, 64);
    }

    @Override
    public void render(float delta) {
        if (restart){
            restartTimer += delta;
            System.out.println(restartTimer);
        }
        if (restartTimer >= 5){
            restartTimer = 0;
            input.removeProcessor(input.size()-1);
            camera.position.x = 0;
            camera.position.y = 0;
            dungeonMap = new DungeonMap(12, 3, 21, camera, input);
            renderer = new OrthogonalTiledMapRenderer(dungeonMap);
        }
        if (moveLeft || moveRight || moveUp || moveDown) {
            final float w = Gdx.graphics.getWidth();
            final float h = Gdx.graphics.getHeight();
            if (moveLeft) {
                camera.translate(-500 * delta, 0); //TODO сделать скорость зависящей от расстояния до стенки
                camera.update();
            } else if (moveRight) {
                camera.translate(500 * delta, 0);
                camera.update();
            }
            if (moveUp) {
                camera.translate(0, -500 * delta);
                camera.update();
            } else if (moveDown) {
                camera.translate(0, 500 * delta);
                camera.update();
            }
        }
        camera.update();
        charBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update();

        renderer.setView(camera);
        renderer.render();

        Entity nowPlayingEntity = Entity.getNowPlaying();

        charBatch.begin();
        for (Entity entity : Entity.getPlayingEntities()) {
            act(entity, delta);
            entity.getSprite().draw(charBatch);
        }
        for (Entity entity : Room.getAddingArray()){
            entity.add(Entity.getNowPlayingIndex() + 1); //TODO CHECK adding after now playing entity WORKING CORRECTLY
        }
        Room.getAddingArray().clear();
        charBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        //System.out.println(w + " " + h);
        shapeRenderer.rect(0, 0, w, h / 6);
        shapeRenderer.end();

        String hpBarInfo;
        String mpBarInfo;
        if (nowPlayingEntity.isPlayer() || nowPlayingEntity.isControlled()){
            hpBarInfo = nowPlayingEntity.getHp() + "/" + nowPlayingEntity.getHpMax();
            mpBarInfo = nowPlayingEntity.getMp(true) + "/" + nowPlayingEntity.getMpMax();
        } else {
            hpBarInfo = "-";
            mpBarInfo = "-";
        }
        interfaceBatch.begin();
        interfaceBatch.draw(nowPlayingEntity.getPortrait(), w / 180, h / 120,h / 6 - h / 20, h / 6 - h / 60);
        TextureRegion healthBarIcon = InterfaceElements.getHealth();
        interfaceBatch.draw(healthBarIcon, h / 6 - h / 20 + w / 90, h / 36 * 5 - h / 30, h / 15, h / 15);
        font.draw(interfaceBatch, hpBarInfo, h / 6 - h / 20 + w / 90, h / 36 * 5 - h / 30, h / 15, Align.center, false);
        TextureRegion moveBarIcon = InterfaceElements.getMoves();
        interfaceBatch.draw(moveBarIcon, h / 6 - h / 20 + w / 90, h / 18 - h / 30, h / 15, h / 15);
        font.draw(interfaceBatch, mpBarInfo,h / 6 - h / 20 + w / 90, h / 18 - h / 30, h / 15, Align.center, false);
        final Entity finalEntity = Entity.getNowPlaying();
        if (Entity.isUpdateSkills()){
            System.out.println("UPDATE!");
            skillGroup.clear();
            final Entity entity = Entity.getNowPlaying();
            for (int i = 0; i < entity.getSkills().size; i++){
                final Skill skill = entity.getSkills().get(i);
                Texture texture = skill.getIcon();
                Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
                drawable.setMinHeight(h / 11);
                drawable.setMinWidth(h / 11);
                final Stack stack = new Stack();
                Image image = new Image(drawable);
                Table table = new Table();
                table.add(image).padLeft(h / 110).padRight(h / 110);
                stack.add(table);
                if (skill.isCooldown()){
                    Table cooldownTable = new Table();
                    Drawable cooldownDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("cooldown.png")));
                    cooldownDrawable.setMinHeight(h / 11);
                    cooldownDrawable.setMinWidth(h / 11);
                    Image cooldown = new Image(cooldownDrawable);
                    cooldownTable.add(cooldown);
                    stack.add(cooldownTable);
                }
                if (nowPlayingEntity.getUsedSkill() == skill){
                    frameTable.remove();
                    stack.add(frameTable);
                }
                final int finalI = i;
                stack.addListener(new InputListener(){
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (!skill.isCooldown()) {
                            if (entity.getUsedSkill() != skill) {
                                entity.setUsedSkill(skill);
                                changeButton(true);
                                DungeonMap.clearPathLayer();
                            } else {
                                entity.setUsedSkill(null);
                                changeButton(false);
                            }
                        }
                    }

                    @Override
                    public boolean mouseMoved(InputEvent event, float x, float y) {
                        entity.setDetailedSkill(finalI);
                        return true;
                    }
                });
                skillGroup.addActor(stack);
            }
        }

        int detailedSkill = nowPlayingEntity.getDetailedSkill();
        if (detailedSkill != -1){
            Skill skill = nowPlayingEntity.getSkills().get(detailedSkill);
            tooltipGroup.remove();
            tooltipGroup = new Table(skin);
            tooltipGroup.setWidth(w/6);
            Color color = Color.WHITE;
            Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, color);
            Label headerLabel = new Label(skill.getName(), headerStyle);
            headerLabel.setWidth(w/6);
            headerLabel.setWrap(true);
            headerLabel.setAlignment(Align.top);
            tooltipGroup.add(headerLabel).width(w/6).padLeft(10).padRight(10).padTop(10).row();
            Label.LabelStyle style = new Label.LabelStyle(font, color);
            Label typeLabel = new Label(skill.getTypeString(), style);
            addDefaultTooltipLabel(typeLabel);
            int targetType = skill.getTargetType();
            Label targetLabel = new Label("Target type: " + skill.getTargetTypeString(), style);
            addDefaultTooltipLabel(targetLabel);
            if (targetType != Skill.SKILL_TARGET_TYPE_SELF){
                Label countLabel = new Label("Target count: " + skill.getTargetCountMax(), style);
                addDefaultTooltipLabel(countLabel);
                if (targetType != Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER){
                    Label distanceLabel = new Label("Distance: " + skill.getDistanceMin() + "-" + skill.getDistanceMax(), style);
                    addDefaultTooltipLabel(distanceLabel);
                }
            }
            if (targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SPLASH || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_WAVE || targetType == Skill.SKILL_TARGET_TYPE_FLOOR_SWING){
                Label rangeLabel = new Label("Range: " + skill.getRange(), style);
                addDefaultTooltipLabel(rangeLabel);
            }
            Label descriptionLabel = new Label(skill.getDescription(), style);
            addDefaultTooltipLabel(descriptionLabel);
            float skillWidth = ((Table) ((Stack) skillGroup.getChildren().get(detailedSkill)).getChildren().get(0)).getChildren().get(0).getWidth();
            float fullWidth = skillWidth * 1.2f;
            int count = skillGroup.getChildren().size;
            int half = count/ 2;
            boolean b = count % 2 == 1;
            int move = detailedSkill - half + 1;
            float pos = move * fullWidth;
            if (b){
                pos -= fullWidth / 2;
            }
            System.out.println(pos + " - pos // " + move + " - move");
            pos = w / 2 + pos - skillWidth * 0.1f;
            tooltipGroup.setPosition(pos, skillGroup.getY());
            tooltipGroup.pack();
            Drawable background = skin.getDrawable("tooltip_background");
            tooltipGroup.setBackground(background);
            stage.addActor(tooltipGroup);
        }

        effectGroup.clear();
        for (int i = 0; i < nowPlayingEntity.getEffects().size(); i++) {
            final Effect effect = nowPlayingEntity.getEffects().get(i);
            if (!effect.isHide()) {
                Texture texture = effect.getIcon();
                Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
                drawable.setMinHeight(h / 24);
                drawable.setMinWidth(h / 24);
                Image image = new Image(drawable);
                Table table = new Table();
                table.add(image).padLeft(h / 480).padRight(h / 480);
                final int finalI = i;
                table.addListener(new InputListener(){
                    @Override
                    public boolean mouseMoved(InputEvent event, float x, float y) {
                        finalEntity.setDetailedEffect(finalI);
                        return true;
                    }
                });
                effectGroup.addActor(table);
            }
        }

        int detailedEffect = nowPlayingEntity.getDetailedEffect();
        if (detailedEffect != -1){
            Effect effect = nowPlayingEntity.getEffects().get(detailedEffect);
            tooltipGroup.remove();
            tooltipGroup = new Table(skin);
            tooltipGroup.setWidth(w/6);
            Color color = null;
            if (effect.isPositive()){
                color = Color.GREEN;
            } else {
                color = Color.RED;
            }
            Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, color);
            Label headerLabel = new Label(effect.getName(), headerStyle);
            headerLabel.setWidth(w/6);
            headerLabel.setWrap(true);
            headerLabel.setAlignment(Align.top);
            tooltipGroup.add(headerLabel).width(w/6).padLeft(10).padRight(10).padTop(10).row();
            Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
            Label descriptionLabel = new Label(effect.getDescription(), style);
            descriptionLabel.setWidth(w/6);
            descriptionLabel.setWrap(true);
            descriptionLabel.setAlignment(Align.topLeft);
            descriptionLabel.setPosition(0, -descriptionLabel.getHeight());
            tooltipGroup.add(descriptionLabel).width(w/6).padLeft(10).padRight(10).padBottom(10);
            //System.out.println(image.getX() + " " + image.getY() + " " + image.getImageWidth());
            float effectWidth = ((Table) effectGroup.getChildren().get(detailedEffect)).getChildren().get(0).getWidth() * 1.1f;
            int count = effectGroup.getChildren().size;
            System.out.println(effectWidth + " - effect width");
            int half = count/ 2;
            boolean b = count % 2 == 1;
            int move = detailedEffect - half + 1;
            float pos = move * effectWidth;
            if (b){
                pos -= effectWidth / 2;
            }
            pos = w / 2 + pos;
            tooltipGroup.setPosition(pos, effectGroup.getY());
            tooltipGroup.pack();
            Drawable background = skin.getDrawable("tooltip_background");
            tooltipGroup.setBackground(background);
            stage.addActor(tooltipGroup);
        }

        if (debugInfo) {
            font.draw(interfaceBatch, "camera: " + camera.position + " " + '\u2685', 10, h - 50);
            for (int i = 0; i < logs.size ; i++) {
                font.draw(interfaceBatch, logs.get(i), 10, h - 80 - 30 * i);
            }
        }
        interfaceBatch.end();
        stage.draw();
    }

    private void act(Entity entity, float delta){
        if (entity.isMovement() || entity.isThrowing()) {
            //float delta = Gdx.graphics.getDeltaTime();
            System.out.println("1. " + entity.getAnimTime() + " " + delta);
            if (entity.isMovement()) {
                entity.addAnimTime(delta); //TODO if entity ends movement on his next turn, then it acts buggy
                //animTime += delta;
                entity.move(delta);
            } else if (entity.isThrowing()){
                System.out.println("throwing");
                entity.throwing(delta);
            }
            System.out.println("2. " + entity.getAnimTime() + " " + delta);
            int x = entity.getTileX();
            int y = entity.getTileY();
            if (dungeonMap.getTile(x, y).isDoor() && entity.isPlayer()){
                int index = ru.myitschool.cubegame.dungeon.Door.getDoorIndex(x, y);
                if (index == -1){
                    System.out.println("404 NOT FOUND!");
                    dungeonMap.removeDoorTile(x, y);
                } else {
                    ru.myitschool.cubegame.dungeon.Door door = ru.myitschool.cubegame.dungeon.Door.getDoor(index);
                    int direction = door.getDirection();
                    int newRoomStartX = x / ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH;
                    int newRoomStartY = y / ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT;
                    if (direction == ru.myitschool.cubegame.dungeon.Exit.DIRECTION_NORTH){
                        System.out.println("North: y - " + newRoomStartY + " Critical: 0");
                        if (newRoomStartY == 0){
                            dungeonMap.addUp(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT);
                            ru.myitschool.cubegame.dungeon.Door.moveDoors(new Vector2(0, ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT));
                            camera.translate(0, ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT * DungeonTile.TILE_HEIGHT);
                        } else {
                            newRoomStartY--;
                        }
                    } else if (direction == ru.myitschool.cubegame.dungeon.Exit.DIRECTION_EAST){
                        System.out.println("East: x - " + newRoomStartX + " Critical: " + (dungeonMap.getWidth() - 1));
                        if (newRoomStartX == (dungeonMap.getWidth() - 1) / dungeonMap.ROOM_WIDTH){
                            dungeonMap.addRight(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH);
                        }
                        newRoomStartX++;
                    } else if (direction == ru.myitschool.cubegame.dungeon.Exit.DIRECTION_SOUTH){
                        System.out.println("South: y - " + newRoomStartY + " Critical: " + (dungeonMap.getHeight() - 1));
                        if (newRoomStartY == (dungeonMap.getHeight() - 1) / ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT){
                            dungeonMap.addDown(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT);
                        }
                        newRoomStartY++;
                    } else if (direction == ru.myitschool.cubegame.dungeon.Exit.DIRECTION_WEST){
                        System.out.println("West: x - " + newRoomStartX + " Critical: 0");
                        if (newRoomStartX == 0){
                            dungeonMap.addLeft(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH);
                            ru.myitschool.cubegame.dungeon.Door.moveDoors(new Vector2(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH, 0));
                            camera.translate(ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH * DungeonTile.TILE_WIDTH, 0);
                        } else {
                            newRoomStartX--;
                        }
                    }
                    for (Point cell : door.getDoorCells()) {
                        dungeonMap.removeDoorTile((int) cell.getX(), (int) cell.getY());
                    }
                    System.out.println("Room X: " + newRoomStartX + " Room Y: " + newRoomStartY);
                    ru.myitschool.cubegame.dungeon.Door.removeDoor(index);
                    newRoomStartX *= ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_WIDTH;
                    newRoomStartY *= ru.myitschool.cubegame.dungeon.DungeonMap.ROOM_HEIGHT;
                    int side = direction + 2;
                    if (side >= 4){
                        side -= 4;
                    }
                    System.out.println("Direction: " + direction + " Side: " + side);
                    dungeonMap.placeRoom(newRoomStartX, newRoomStartY, side);
                    GraphStorage.createBottomGraph(dungeonMap.getTileLayer());
                }
            }
        } else {
            entity.setAnimTime(0);
        }

        //System.out.println("Enemy attacks: " + entity.isEnemy() + " "+ entity.isSkillUse());
        if (entity.isEnemy() && entity.isSkillUse()){
            Enemy enemy = (Enemy) entity;
            enemy.addSkillTime(delta);
        }
    }

    private void addDefaultTooltipLabel(Label label){
        label.setWidth(w / 6);
        label.setWrap(true);
        label.setAlignment(Align.left);
        label.setX(0);
        tooltipGroup.add(label).width(w/6).padLeft(10).padRight(10).padBottom(10).row();
    }

    private void changeButton(boolean state){
        buttonsGroup.clear();
        button = state;
        if (!button){
            turnButtonTable.clear();
            turnButtonTable.add(nextTurnButton).width(w / 10).height(h / 12);
            buttonsGroup.addActor(turnButtonTable);
        } else {
            buttonsGroup.addActor(attackButtonTable);
            buttonsGroup.addActor(cancelButtonTable);
        }
    }

    @Override
    public void resize(int width, int height) {
        h = height;
        w = width;
        nextTurnButton.setPosition(w - w / 180 - nextTurnButton.getWidth(), h / 12 - nextTurnButton.getHeight() / 2);
        effectGroup.setPosition(w / 5, 9 * h / 72);
        effectGroup.setWidth(3 * w / 5);
        effectGroup.setHeight(h / 40);
        interfaceBatch = new SpriteBatch();
        charBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        createFonts();
        placeButton();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private void createFonts(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixelart.otf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = FreeTypeFontGenerator.DEFAULT_CHARS + "⚅⚄⚃⚂⚁⚀";
        float mult =  h / 720;
        if (mult == 0){
            mult = 1;
        }
        System.out.println(mult);
        parameter.size = (int) (14 * mult);
        font = generator.generateFont(parameter);
        parameter.size = (int) (16 * mult);
        headerFont = generator.generateFont(parameter);
    }

    private void placeButton(){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        Drawable up = skin.getDrawable("button_off");
        Drawable down = skin.getDrawable("button_on");
        float multHeight =  h / 720;
        float multWidth = w / 1280;
        System.out.println("Button mults: " + multWidth + " " + multHeight);
        up.setMinWidth(up.getMinWidth() * multWidth);
        up.setMinHeight(up.getMinHeight() * multHeight);
        down.setMinWidth(up.getMinWidth() * multWidth);
        down.setMinHeight(up.getMinHeight() * multHeight);
        style.up = up;
        style.down = down;
        style.font = font;
        nextTurnButton = new TextButton("End turn", style);
        //nextTurnButton.setText("test");
        nextTurnButton.setPosition(w - w / 180 - nextTurnButton.getWidth(), h / 12 - nextTurnButton.getHeight() / 2);
        nextTurnButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dungeonMap.clearTargetLayer();
                Entity.nextTurn();
                //nextTurnButton.setDisabled(!Entity.getNowPlaying().isPlayer());
            }
        });
    }

    public static int addLog(String log){
        logs.add(log);
        return logs.size - 1;
    }

    public static void changeLog(String log, int index){
        logs.set(index, log);
    }

    public static void clearFrame(){
        frameTable.remove();
    }
}
