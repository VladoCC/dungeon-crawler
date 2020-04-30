package ru.myitschool.dcrawler.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
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
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.myitschool.dcrawler.InterfaceElements;
import ru.myitschool.dcrawler.ai.pathfinding.GraphStorage;
import ru.myitschool.dcrawler.dungeon.Dungeon;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.effects.Effect;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Character;
import ru.myitschool.dcrawler.entities.Enemy;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEvent;
import ru.myitschool.dcrawler.event.EventController;
import ru.myitschool.dcrawler.skills.FloatingDamageMark;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.tiles.DungeonTile;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by Voyager on 22.04.2017.
 */
public class DungeonScreen extends AdvancedScreen {

    OrthographicCamera mainCamera;
    OrthographicCamera yUpCamera;
    ru.myitschool.dcrawler.dungeon.DungeonMap dungeonMap;
    OrthogonalTiledMapRenderer renderer;

    private Dungeon dungeon;

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

    Stage mainStage;
    Stage cameraStage;

    TextButton nextTurnButton;
    TextButton attackButton;
    TextButton cancelButton;

    HorizontalGroup effectGroup;
    HorizontalGroup skillGroup;
    VerticalGroup buttonsGroup;
    VerticalGroup encounterGroup;

    Table attackButtonTable;
    Table cancelButtonTable;
    Table turnButtonTable;
    Table guiTable;
    Table tooltipGroup;
    Group markTable;
    static Table frameTable;

    protected BitmapFont font;
    protected BitmapFont headerFont;

    protected ShapeRenderer shapeRenderer = new ShapeRenderer();
    protected SpriteBatch charBatch = new SpriteBatch();
    protected SpriteBatch interfaceBatch = new SpriteBatch();

    TextureAtlas atlas;
    Skin skin;

    public DungeonScreen(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    @Override
    public void show() {
        InterfaceElements.init();
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        //final float w = Gdx.graphics.getWidth();
        //final float h = Gdx.graphics.getHeight();
        tooltipGroup = new Table();
        yUpCamera = new OrthographicCamera(w, h);
        yUpCamera.setToOrtho(false);
        yUpCamera.update();
        yUpCamera.zoom = 0.6f;
        yUpCamera.position.x = 0;
        yUpCamera.position.y = 0;
        mainCamera = new OrthographicCamera(w, h);
        mainCamera.setToOrtho(true);
        mainCamera.update();
        getInput().addProcessor(new InputAdapter(){
            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                mouseX = screenX;
                mouseY = screenY;
                Vector3 coords = mainCamera.unproject(new Vector3(screenX,screenY, 0));
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
                    debugInfo = !debugInfo;
                    return true;
                } else if (keycode == Input.Keys.R){
                    restart = false;
                    if (restartTimer >= 5){
                        mainCamera.position.x = 0;
                        mainCamera.position.y = 0;
                        dungeonMap = new ru.myitschool.dcrawler.dungeon.DungeonMap(dungeon, mainCamera, getInput());
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
                    Entity.getPlayingEntities().get(0).addHp(-30);
                } else if (keycode == Input.Keys.M){
                    mainCamera.position.x = 0;
                    mainCamera.position.y = 0;
                } else if (keycode == Input.Keys.T){
                    Entity.nextTurn(Entity.getNowPlaying());
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
        mainStage = new Stage();
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
        mainStage.addActor(guiTable);

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
                DungeonMap.clearTargetingZoneLayer();
            }
        });
        attackButtonTable = new Table();
        cancelButtonTable = new Table();
        attackButtonTable.add(attackButton).width(w / 10).height(h / 18).padTop(h / 36).padBottom(h / 72);
        cancelButtonTable.add(cancelButton).width(w / 10).height(h / 18).padTop(h / 72).padBottom(h / 36);

        markTable = new Group();
        markTable.setPosition(0, h / 6);
        markTable.setSize(w, h - h / 6);
        mainStage.addActor(markTable);

        buttonsGroup = new VerticalGroup();
        buttonsGroup.setWidth(w / 10);
        buttonsGroup.setHeight(h / 6);
        buttonsGroup.setPosition(w * 9 / 10 - w / 100, 0);
        buttonsGroup.addActor(nextTurnButton);
        buttonsGroup.center();
        guiTable.addActor(buttonsGroup);

        encounterGroup = new VerticalGroup();
        encounterGroup.setWidth(w / 6);
        encounterGroup.setHeight(h - h / 6);
        encounterGroup.setPosition(w - w / 6, h / 6);
        encounterGroup.center();
        mainStage.addActor(encounterGroup);

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

        getInput().addProcessor(mainStage);
        mainCamera.position.x = 0;
        mainCamera.position.y = 0;
        dungeonMap = new ru.myitschool.dcrawler.dungeon.DungeonMap(dungeon, mainCamera, getInput());

        cameraStage = new Stage();
        cameraStage.getViewport().setCamera(yUpCamera);
        Stack stack = new Stack();
        Group effectGroup = new Group();
        effectGroup.setSize(w, h);
        stack.add(effectGroup);
        DungeonMap.setEffectGroup(effectGroup);
        Group targetingZoneGroup = new Group();
        stack.add(targetingZoneGroup);
        DungeonMap.setTargetingZoneGroup(targetingZoneGroup);
        Group displayTargetGroup = new Group();
        stack.add(displayTargetGroup);
        DungeonMap.setDisplayTargetGroup(displayTargetGroup);
        Group choosenTargetsGroup = new Group();
        stack.add(choosenTargetsGroup);
        DungeonMap.setChoosenTargetsGroup(choosenTargetsGroup);
        System.out.println("Stack: " + stack.getMinWidth() + " " + stack.getMinHeight() + " / " + stack.getPrefWidth() + " " + stack.getPrefHeight() + " / " + stack.getMaxWidth() + " " + stack.getMaxHeight());
        cameraStage.addActor(stack);
        Character.setFirstPlaying();

        renderer = new OrthogonalTiledMapRenderer(dungeonMap);
        mainCamera.zoom = 0.6f;
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
            getInput().removeProcessor(getInput().size()-1);
            mainCamera.position.x = 0;
            mainCamera.position.y = 0;
            dungeonMap = new DungeonMap(dungeon, mainCamera, getInput());
            renderer = new OrthogonalTiledMapRenderer(dungeonMap);
        }
        if (moveLeft || moveRight || moveUp || moveDown) { //TODO add method for camera moving
            if (moveLeft) {
                mainCamera.translate(-500 * delta, 0); //TODO сделать скорость зависящей от расстояния до стенки
            } else if (moveRight) {
                mainCamera.translate(500 * delta, 0);
            }
            if (moveUp) {
                mainCamera.translate(0, -500 * delta);
            } else if (moveDown) {
                mainCamera.translate(0, 500 * delta);
            }
            updateCamera();
        }
        if (DungeonMap.isUpdateCamera()){
            updateCamera();
        }
        mainCamera.update();
        charBatch.setProjectionMatrix(mainCamera.combined);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //mainCamera.update();

        renderer.setView(mainCamera);
        renderMapLayers(0, 0);//TODO move to the end of method
        cameraStage.draw();
        renderMapLayers(1, dungeonMap.getLayersCount() - 1);

        Encounter.update(delta);
        FloatingDamageMark.update(delta);

        Entity nowPlayingEntity = Entity.getNowPlaying();
        if (!nowPlayingEntity.isAlive()){
            Entity.nextTurn(nowPlayingEntity);
        }
        charBatch.begin();
        //Entity.getPlayingEntities().removeIf(entity -> !entity.isAlive());
        if (Room.getAddingArray().size > 0) {
            for (Entity entity : Room.getAddingArray()) {
                entity.add(Entity.getNowPlayingIndex() + 1);
                DungeonMap.addEntity(entity);
            }
            GraphStorage.createBottomGraph();
        }
        Room.getAddingArray().clear();

        for (Entity entity : Entity.getAliveEntities()) {
            act(entity, delta);
            entity.getSprite().draw(charBatch);
        }
        charBatch.end();

        markTable.clear();
        for (FloatingDamageMark mark : FloatingDamageMark.getMarks()){
            Color color = Color.WHITE;
            Label.LabelStyle style = new Label.LabelStyle(font, color);
            Label textLabel = new Label(mark.getText(), style);
            textLabel.setAlignment(Align.center);
            float x = mark.getTileX() * DungeonTile.TILE_WIDTH + DungeonTile.TILE_WIDTH / 2;
            float y = (mark.getTileY() + 2) * DungeonTile.TILE_HEIGHT + DungeonTile.TILE_HEIGHT / 2 - DungeonTile.TILE_HEIGHT / 2 * mark.getTime() / FloatingDamageMark.MAX_TIME; //TODO DON'T KNOW WHY THIS NEEDS TO HAVE +2
            Vector3 vector = mainCamera.project(new Vector3(x, y, 0));
            textLabel.setPosition(vector.x, vector.y, Align.center);
            textLabel.setWrap(false);
            markTable.addActor(textLabel);
        }

        encounterGroup.clear();
        if (Encounter.hasNowPlayimg()){
            Array<Encounter> encounters = Encounter.getNowPlayings();
            for (Encounter encounter : encounters) {
                Table encounterTable = new Table();
                /*encounterTable.setPosition(w, (h - h / 6) / 2);
                encounterTable.align(Align.center);*/
                Color color = Color.WHITE;
                Label.LabelStyle headerStyle = new Label.LabelStyle(headerFont, color);
                Label headerLabel = new Label(encounter.getName(), headerStyle);
                headerLabel.setWidth(w / 6);
                headerLabel.setWrap(true);
                headerLabel.setAlignment(Align.top);
                encounterTable.add(headerLabel).width(w / 6).padLeft(10).padRight(10).padTop(10).row();
                Label.LabelStyle style = new Label.LabelStyle(font, color);
                Label textLabel = new Label(encounter.getText(), style);
                textLabel.setWidth(w / 6);
                textLabel.setWrap(true);
                textLabel.setAlignment(Align.left);
                textLabel.setX(0);
                encounterTable.add(textLabel).width(w / 6).padLeft(10).padRight(10).padBottom(10).row();
                Drawable background = skin.getDrawable("tooltip_background");
                encounterTable.setBackground(background);
                encounterTable.pad(10, 0, 0, 0);
                encounterGroup.addActor(encounterTable);
            }
        }

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
            Entity.skillsUpdated();
            skillGroup.clear();
            final Entity entity = Entity.getNowPlaying();
            HashMap mapStart = new HashMap();
            mapStart.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, entity);
            final boolean canUse = (boolean) EventController.callEvent(EntityEvent.CAN_USE_SKILL_EVENT, mapStart).get(EntityEvent.CAN_USE_SKILL_ARG_KEY);
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
                if (skill.isCooldown() || !canUse){
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
                        if (!skill.isCooldown() && canUse) {
                            DungeonMap.clearSkillLayers();
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
                System.out.println("Stack: " + stack.getMinWidth() + " " + stack.getMinHeight() + " / " + stack.getPrefWidth() + " " + stack.getPrefHeight() + " / " + stack.getMaxWidth() + " " + stack.getMaxHeight());
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
            mainStage.addActor(tooltipGroup);
        }

        if (Entity.isUpdateEffects()) {
            Entity.effectsUpdated();
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
                    table.addListener(new InputListener() {
                        @Override
                        public boolean mouseMoved(InputEvent event, float x, float y) {
                            finalEntity.setDetailedEffect(finalI);
                            return true;
                        }
                    });
                    effectGroup.addActor(table);
                }
            }
        }

        int detailedEffect = nowPlayingEntity.getDetailedEffect();
        if (detailedEffect != -1 && detailedEffect < effectGroup.getChildren().size){
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
            float effectWidth = ((Table) effectGroup.getChildren().get(detailedEffect)).getChildren().get(0).getWidth() * 1.1f; //TODO constants
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
            mainStage.addActor(tooltipGroup);
        }

        if (debugInfo) {
            font.draw(interfaceBatch, "mainCamera: " + mainCamera.position + " " + '\u2685', 10, h - 50);
            for (int i = 0; i < logs.size ; i++) {
                font.draw(interfaceBatch, logs.get(i), 10, h - 80 - 30 * i);
            }
        }
        interfaceBatch.end();
        mainStage.draw();
    }

    private void act(Entity entity, float delta){
        if (entity.isAlive()) {
            if (entity.isMovement() || entity.isThrowing()) {
                //float delta = Gdx.graphics.getDeltaTime();
                System.out.println("1. " + entity.getAnimTime() + " " + delta);
                if (entity.isMovement()) {
                    entity.addAnimTime(delta); //TODO if entity ends movement on his next turn, then it acts buggy
                    //animTime += delta;
                    entity.move(delta);
                } else if (entity.isThrowing()) {
                    System.out.println("throwing");
                    entity.throwing(delta);
                }
                System.out.println("2. " + entity.getAnimTime() + " " + delta);
                int x = entity.getTileX();
                int y = entity.getTileY();
                if (dungeonMap.getTile(x, y).isDoor()) {
                    int index = ru.myitschool.dcrawler.dungeon.Door.getDoorIndex(x, y);
                    if (index == -1) {
                        System.out.println("Already opened");
                        dungeonMap.removeDoorTile(x, y);
                    } else {
                        ru.myitschool.dcrawler.dungeon.Door door = ru.myitschool.dcrawler.dungeon.Door.getDoor(index);
                        int direction = door.getDirection();
                        int newRoomStartX = x / ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH;
                        int newRoomStartY = y / ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT;
                        if (direction == ru.myitschool.dcrawler.dungeon.Exit.DIRECTION_NORTH) {
                            System.out.println("North: y - " + newRoomStartY + " Critical: 0");
                            if (newRoomStartY == 0) {
                                dungeonMap.addUp(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT);
                                ru.myitschool.dcrawler.dungeon.Door.moveDoors(new Vector2(0, ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT));
                                mainCamera.translate(0, ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT * DungeonTile.TILE_HEIGHT);
                            } else {
                                newRoomStartY--;
                            }
                        } else if (direction == ru.myitschool.dcrawler.dungeon.Exit.DIRECTION_EAST) {
                            System.out.println("East: x - " + newRoomStartX + " Critical: " + (dungeonMap.getWidth() - 1));
                            if (newRoomStartX == (dungeonMap.getWidth() - 1) / Dungeon.ROOM_WIDTH) {
                                dungeonMap.addRight(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH);
                            }
                            newRoomStartX++;
                        } else if (direction == ru.myitschool.dcrawler.dungeon.Exit.DIRECTION_SOUTH) {
                            System.out.println("South: y - " + newRoomStartY + " Critical: " + (dungeonMap.getHeight() - 1));
                            if (newRoomStartY == (dungeonMap.getHeight() - 1) / ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT) {
                                dungeonMap.addDown(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT);
                            }
                            newRoomStartY++;
                        } else if (direction == ru.myitschool.dcrawler.dungeon.Exit.DIRECTION_WEST) {
                            System.out.println("West: x - " + newRoomStartX + " Critical: 0");
                            if (newRoomStartX == 0) {
                                dungeonMap.addLeft(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH);
                                ru.myitschool.dcrawler.dungeon.Door.moveDoors(new Vector2(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH, 0));
                                mainCamera.translate(ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH * DungeonTile.TILE_WIDTH, 0);
                            } else {
                                newRoomStartX--;
                            }
                        }
                        for (Point cell : door.getDoorCells()) {
                            dungeonMap.removeDoorTile((int) cell.getX(), (int) cell.getY());
                        }
                        System.out.println("Room X: " + newRoomStartX + " Room Y: " + newRoomStartY);
                        ru.myitschool.dcrawler.dungeon.Door.removeDoor(index);
                        newRoomStartX *= ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_WIDTH;
                        newRoomStartY *= ru.myitschool.dcrawler.dungeon.Dungeon.ROOM_HEIGHT;
                        int side = direction + 2;
                        if (side >= 4) {
                            side -= 4;
                        }
                        System.out.println("Direction: " + direction + " Side: " + side);
                        Room room = dungeonMap.placeRoom(newRoomStartX, newRoomStartY, side);
                        GraphStorage.createBottomGraph();
                        if (room.isEncounter()) {
                            entity.triggerEncounter();
                        }
                        Entity.getNowPlaying().setRoomOpened(true);
                    }
                }
            } else {
                entity.setAnimTime(0);
            }

            //System.out.println("Enemy attacks: " + entity.isEnemy() + " "+ entity.isSkillUse());
            if (entity.isEnemy() && entity.isSkillUse()) {
                Enemy enemy = (Enemy) entity;
                enemy.addSkillTime(delta);
            }
        } else {
            if (entity == Entity.getNowPlaying()) {
                Entity.nextTurn(entity);
            }
        }
    }

    private void renderMapLayers(int startLayer, int endLayer){
        Array<Integer> unvisualized = new Array<Integer>();
        for (int i = 0; i < dungeonMap.getLayers().getCount(); i++) {
            MapLayer layer = dungeonMap.getLayers().get(i);
            if ((i > endLayer || i < startLayer) && layer.isVisible()){
                unvisualized.add(i);
                layer.setVisible(false);
            }
        }
        renderer.render();
        for (Integer visualize : unvisualized){
            dungeonMap.getLayers().get(visualize).setVisible(true);
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

    private void updateCamera(){
        mainCamera.update();
        yUpCamera.position.x = mainCamera.position.x;
        yUpCamera.position.y = -mainCamera.position.y;
        yUpCamera.update();
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
        Entity.getPlayingEntities().removeIf(entity -> entity.isEnemy());
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
                DungeonMap.clearTargetLayer();
                Entity.nextTurn(Entity.getNowPlaying());
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
