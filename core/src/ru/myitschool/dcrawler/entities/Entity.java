package ru.myitschool.dcrawler.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.Node;
import ru.myitschool.dcrawler.ai.pathfinding.NodePath;
import ru.myitschool.dcrawler.dungeon.DungeonCell;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.effects.AttackEffect;
import ru.myitschool.dcrawler.effects.Effect;
import ru.myitschool.dcrawler.effects.EffectArray;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.event.EntityEvent;
import ru.myitschool.dcrawler.event.EntityEventAdapter;
import ru.myitschool.dcrawler.event.EventController;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.screens.DungeonScreen;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;
import ru.myitschool.dcrawler.tiles.DungeonTile;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Voyager on 24.04.2017.
 */
public class Entity extends EntityEventAdapter {

    public static final int DIRECTION_UP = 2;
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_LEFT = 3;

    public static final int ENTITY_WIDTH = 32;
    public static final int ENTITY_HEIGHT = 32;

    public static final float MOVEMENT_TIME = 0.6f; /** IN SECONDS */
    public static final float THROW_TIME = 0.15f;

    private int movementDirection = -1;

    private boolean movement = false;
    private boolean throwing = false;
    private boolean moved = false;
    private boolean skillUse = false;
    private boolean controlled = false;
    private boolean immobilized = false;
    private boolean roomOpened = false;
    private boolean alive = true;

    private boolean throwXDirection;
    private boolean throwYDirection;
    private boolean throwXEnd;
    private boolean throwYEnd;

    private static boolean updateSkills;

    private Texture portrait;
    private Texture model;

    private Sprite sprite;

    private Animation[] animations;

    private NodePath path;

    private Skill usedSkill;

    private EffectArray effects;
    private Array<Skill> skills;

    private float x;
    private float y;

    private float movementSpeed = 0;
    private float movementLength = 0;

    private float throwXSpeed;
    private float throwYSpeed;
    private float throwXDistance;
    private float throwYDistance;

    private float animTime = 0;

    private int hp;
    private int hpMax;
    private int mp;
    private int mpMax;
    private int detailedEffect = -1;
    private int detailedSkill = -1;
    private int armor;
    private int accuracyBonus;

    private static Entity nowPlaying;

    private static int index = 0;
    private static ArrayList<Entity> playingEntities = new ArrayList<Entity>();

    public Entity(Texture model, Texture portrait, float x, float y, int hp, int hpMax, int mp, int mpMax, int armor) {
        this.x = x;
        this.y = y;
        if (model != null) {
            setModel(model);
        }
        this.portrait = portrait;
        this.hp = hp;
        this.hpMax = hpMax;
        this.mp = mp;
        this.mpMax = mpMax;
        this.armor = armor;
        effects = new EffectArray();
        skills = new Array<Skill>();
        //playingEntities.add(this);
    }

    public static void add(Entity entity) {
        playingEntities.add(entity);
    }

    public void add(){
        add(this);
    }

    public static void add(int pos, Entity entity){
        playingEntities.add(pos, entity);
    }

    public void add(int pos){
        add(pos, this);
    }

    public static void clearPlayingEntities(){
        playingEntities.clear();
        for (Entity entity : Character.getChars()){
            playingEntities.add(entity);
        }
    }

    private void setSprite(Texture texture) {
        sprite = new Sprite(texture, ENTITY_WIDTH, ENTITY_HEIGHT);
        sprite.setPosition(x, y);
        sprite.flip(false, true);
    }

    private void setTexture(Texture texture){
        int anims = texture.getHeight() / ENTITY_HEIGHT;
        int sprites = texture.getWidth() / ENTITY_WIDTH;
        animations = new Animation[anims];
        TextureRegion[][] textures = TextureRegion.split(texture,DungeonTile.TILE_WIDTH,DungeonTile.TILE_HEIGHT);
        for (int i = 0; i < anims; i++) {
            Array<TextureRegion> regions = new Array<TextureRegion>();
            for (int j = 0; j < sprites; j++) {
                TextureRegion region = textures[i][j];
                region.flip(false, true);
                regions.add(region);
            }
            Animation animation = new Animation(MOVEMENT_TIME/regions.size, regions);
            animation.setPlayMode(Animation.PlayMode.LOOP);
            animations[i] = animation;
        }
    }

    public void setModel(Texture texture){
        setSprite(texture);
        setTexture(texture);
        this.model = texture;
    }

    protected void setPortrait(Texture portrait){
        this.portrait = portrait;
    }

    public void setMovement() {
        if (path != null) {
            movement = true;
            updateSkills();
            mp -= path.getCount() - 1;
            DungeonMap.updateEntityPos(this);
            HashMap mapStart = new HashMap();
            mapStart.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
            EventController.callEvent(EntityEvent.START_MOVE_EVENT, mapStart);
            planMovement();
        }
    }

    public static boolean isUpdateSkills(){
        boolean ret = updateSkills;
        updateSkills = false;
        return ret;
    }

    public void setSkills(Array<Skill> skills) {
        this.skills = skills;
    }

    public Texture getModel() {
        return model;
    }

    private void planMovement(){
        if (path == null || path.getCount() == 0) {
            movementDirection = -1;
            movement = false;
        } else if (path.getCount() == 1){
            path = null;
            movementDirection = -1;
            movement = false;
        } else {
            Node lastNode = path.get(0);
            Node nextNode = path.get(1);
            if (lastNode.getX() != nextNode.getX() || lastNode.getY() != nextNode.getY()) {
                if (lastNode.getX() < nextNode.getX()) {
                    //System.out.println("MOVING RIGHT!");
                    movementDirection = DIRECTION_RIGHT;
                } else if (lastNode.getX() > nextNode.getX()) {
                    //System.out.println("MOVING LEST!");
                    movementDirection = DIRECTION_LEFT;
                } else if (lastNode.getY() < nextNode.getY()) {
                    //System.out.println("MOVING DOWN!");
                    movementDirection = DIRECTION_DOWN;
                } else if (lastNode.getY() > nextNode.getY()) {
                    //System.out.println("MOVING UP!");
                    movementDirection = DIRECTION_UP;
                }
                path.remove(0);
                int cellDim;
                if (movementDirection % 2 == 0) {
                    cellDim = DungeonTile.TILE_HEIGHT;
                } else {
                    cellDim = DungeonTile.TILE_WIDTH;
                }
                movementSpeed = cellDim / MOVEMENT_TIME;
                movementLength = cellDim;
            }
        }
    }

    public void move(float delta) {
        float stepLength = movementSpeed * delta;
        boolean movementEnd = false;
        if (movementLength > stepLength) {
            movementLength -= stepLength;
        } else {
            movementEnd = true;
            stepLength = movementLength;
            movementLength = 0;
        }
        System.out.println("Direction: " + movementDirection);
        if (movementDirection == DIRECTION_UP) {
            //System.out.println("MOVING UP!");
            y -= stepLength;
            sprite.setY(y);
        } else if (movementDirection == DIRECTION_RIGHT){
            //System.out.println("MOVING RIGHT!");
            x += stepLength;
            sprite.setX(x);
        } else if (movementDirection == DIRECTION_DOWN){
            //System.out.println("MOVING DOWN!");
            y += stepLength;
            sprite.setY(y);
        } else if (movementDirection == DIRECTION_LEFT){
            //System.out.println("MOVING LEFT!");
            x -= stepLength;
            sprite.setX(x);
        }
        if (movementEnd){
            System.out.println("Precise");
            x = Math.round(x / DungeonTile.TILE_WIDTH) * DungeonTile.TILE_WIDTH;
            y = Math.round(y / DungeonTile.TILE_HEIGHT) * DungeonTile.TILE_HEIGHT;
            float lastX = x / DungeonTile.TILE_WIDTH;
            float lastY = y / DungeonTile.TILE_HEIGHT;
            float thisX = lastX;
            float thisY = lastY;
            if (movementDirection == DIRECTION_UP) {
                lastY += 1;
            } else if (movementDirection == DIRECTION_RIGHT){
                lastX -= 1;
            } else if (movementDirection == DIRECTION_DOWN){
                lastY -= 1;
            } else if (movementDirection == DIRECTION_LEFT){
                lastX += 1;
            }
            DungeonCell lastCell = DungeonMap.getCell((int) lastX, (int) lastY);
            DungeonCell cell = DungeonMap.getCell((int) thisX, (int) thisY);
            /*if (lastCell.getEffect() != null){
                CellEffect effect = lastCell.getEffect();
                effect.onStepFrom((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);
            }
            if (cell.getEffect() != null){
                cell.getEffect().onStepTo((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);
            }*/
            lastCell.onStepFrom((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);
            cell.onStepTo((int) lastX,(int)  lastY,(int)  thisX,(int)  thisY, this);

            planMovement();
            if (path == null || path.getCount() < 1){
                HashMap mapEnd = new HashMap();
                mapEnd.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
                EventController.callEvent(EntityEvent.END_MOVE_EVENT, mapEnd);
                updateSkills();
            }
        }
    }

    public static ArrayList<Entity> getPlayingEntities() {
        return playingEntities;
    }

    public static void removePlayingEntity(Entity entity){
        playingEntities.remove(entity);
    }

    public Sprite getSprite() {
        if (movement){
            int animNum = 0;
            if (animations.length > movementDirection){
                animNum = movementDirection;
            }
            TextureRegion texture = (TextureRegion) animations[animNum].getKeyFrame(animTime);
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(x, y);
            //sprite.flip(false, true);
            return sprite;
        } else {
            return sprite;
        }
    }

    public void teleport(Vector2 tiles){
        x += tiles.x * DungeonTile.TILE_WIDTH;
        y += tiles.y * DungeonTile.TILE_HEIGHT;
        sprite.setPosition(x, y);
    }

    public void setX(float x) {
        this.x = x;
        sprite.setPosition(this.x, this.y);
    }

    public void setY(float y) {
        this.y = y;
        sprite.setPosition(this.x, this.y);
    }

    public void setPos(float x, float y){
        setX(x);
        setY(y);
    }

    public void setPos(Vector2 pos){
        setPos(pos.x, pos.y);
    }

    public void setCellPos(int x, int y){
        setX(x * DungeonTile.TILE_WIDTH);
        setY(y * DungeonTile.TILE_HEIGHT);
    }

    public void setCellPos(Vector2 pos){
        setCellPos((int) pos.x, (int) pos.y);
    }

    public void throwEntity(Vector2 tiles){
        System.out.println("throw " + tiles.x + " " + tiles.y);
        float x = Math.abs(tiles.x);
        float y = Math.abs(tiles.y);
        if (x > y){
            throwXSpeed = DungeonTile.TILE_WIDTH / THROW_TIME;
            throwYSpeed = throwXSpeed * y / x;
        } else {
            throwYSpeed = DungeonTile.TILE_HEIGHT / THROW_TIME;
            throwXSpeed = throwYSpeed * x / y;
        }
        System.out.println("x - " + throwXSpeed + ", y - " + throwYSpeed);
        throwXDirection = !(tiles.x < 0);
        throwYDirection = !(tiles.y < 0);
        throwXDistance = x * DungeonTile.TILE_WIDTH;
        throwYDistance = y * DungeonTile.TILE_HEIGHT;
        throwing = true;
        throwXEnd = false;
        throwYEnd = false;
        DungeonMap.updateEntityPos(this, getTileX(), getTileY(), getTileX() + (int) tiles.x, getTileY() + (int) tiles.y);
    }

    public void throwing(float delta){
        System.out.println("throwing");
        float stepXLength = throwXSpeed * delta;
        float stepYLength = throwYSpeed * delta;
        if (throwXDistance > stepXLength){
            throwXDistance -= stepXLength;
        } else {
            throwXEnd = true;
            stepXLength = throwXDistance;
            throwXDistance = 0;
        }
        if (throwYDistance > stepYLength){
            throwYDistance -= stepYLength;
        } else {
            throwYEnd = true;
            stepYLength = throwYDistance;
            throwYDistance = 0;
        }
        if (throwXEnd && throwYEnd){
            throwing = false;
        }
        if (!throwXDirection){
            stepXLength = -stepXLength;
        }
        if (!throwYDirection){
            stepYLength = -stepYLength;
        }
        x += stepXLength;
        y += stepYLength;
        if (!throwing){
            x = Math.round(x / DungeonTile.TILE_WIDTH) * DungeonTile.TILE_WIDTH;
            y = Math.round(y / DungeonTile.TILE_HEIGHT) * DungeonTile.TILE_HEIGHT;
        }
        sprite.setPosition(x, y);
    }

    public boolean isThrowing() {
        return throwing;
    }

    public void setThrowing(boolean throwing) {
        this.throwing = throwing;
    }

    public static Entity getNowPlaying() {
        return nowPlaying;
    }

    public static void setNowPlaying(Entity nowPlaying) {
        Entity.nowPlaying = nowPlaying;
    }

    public static void nextTurn(Entity nowPlayingEntity){
        if (nowPlaying == nowPlayingEntity) {
            HashMap mapEnd = new HashMap();
            mapEnd.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, nowPlayingEntity);
            EventController.callEvent(EntityEvent.END_TURN_EVENT, mapEnd);
            index++;
            if (index >= playingEntities.size()) {
                index = 0;
            }
            nowPlaying = playingEntities.get(index);
            System.out.println("!!!");
            HashMap mapStart = new HashMap();
            mapStart.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, nowPlaying);
            EventController.callEvent(EntityEvent.START_TURN_EVENT, mapStart);
            updateSkills = true;
            DungeonScreen.clearFrame();
        }
    }
    
    public static int getNowPlayingIndex(){
        return index;
    }

    public static void setFirstPlaying(){
        if (nowPlaying != null){
            HashMap mapEnd = new HashMap();
            mapEnd.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, nowPlaying);
            EventController.callEvent(EntityEvent.END_TURN_EVENT, mapEnd);
        }
        nowPlaying = playingEntities.get(0);
        HashMap mapStart = new HashMap();
        mapStart.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, nowPlaying);
        EventController.callEvent(EntityEvent.START_TURN_EVENT, mapStart);
        updateSkills = true;
    }

    public int getTileX(){
        return Math.round(x / DungeonTile.TILE_WIDTH);
    }

    public int getTileY(){
        return Math.round(y / DungeonTile.TILE_HEIGHT);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Array<Skill> getSkills() {
        return skills;
    }

    public boolean isControlled() {
        return controlled;
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

    public void addTargets(Array<Target> targets){
        for (Target target : targets) {
            addOrRemoveSkillTarget(target);
        }
    }

    public void addOrRemoveSkillTarget(Target target){
        if (isSkillUse()){
            setPath(null);
            //pathLayer.clearLayer();

            Skill skill = getUsedSkill();

            if (skill.hasTarget(target)){
                skill.removeTarget(target);
            } else {
                skill.addTarget(target);
            }
        }
    }

    public void addOrRemoveSkillTarget(int cellX, int cellY){
        addOrRemoveSkillTarget(new Target(cellX, cellY));
    }

    public boolean isRoomOpened() {
        return roomOpened;
    }

    public void setRoomOpened(boolean roomOpened) {
        this.roomOpened = roomOpened;
    }

    public void triggerEncounter(){
        Encounter.getRandomEncounter().trigger(this);
    }

    public boolean isMovement() {
        return movement;
    }

    public void setPath(NodePath path) {
        this.path = path;
    }

    public NodePath getPath() {
        return path;
    }

    public boolean isPlayer(){
        return false;
    }

    public boolean isEnemy(){
        return false;
    }

    public Texture getPortrait() {
        return portrait;
    }

    public int getHp() {
        return hp;
    }

    /**
     * this func changes hp without calling listeners. Use addHp() to call with it
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    public int addHp(int change){
        HashMap map = new HashMap();
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
        if (change > 0){
            map.put(EntityEvent.HEAL_ARG_KEY, change);
            change = (int) EventController.callEvent(EntityEvent.ON_HEAL_EVENT, map).get(EntityEvent.HEAL_ARG_KEY);
            if (hp + change > hpMax){
                hp = hpMax;
                change = hpMax - hp;
            } else {
                hp += change;
            }
        } else if (change < 0){
            map.put(EntityEvent.DAMAGE_ARG_KEY, Math.abs(change));
            change = -((int) EventController.callEvent(EntityEvent.ON_DAMAGE_EVENT, map).get(EntityEvent.DAMAGE_ARG_KEY));
            if (hp <= change){
                int lastHp = hp;
                hp = 0;
                change = lastHp;
            } else {
                hp += change;
            }
        }
        if (hp < 1){
            EventController.callEvent(EntityEvent.ON_DEATH_EVENT, map);
        }
        return change;
    }

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public void changeHpMax(int change){
        hpMax += change;
    }

    public int getMp(boolean withMovement) {
        HashMap map = new HashMap();
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
        map.put(EntityEvent.WITH_MOVEMENT_ARG_KEY, withMovement);

        int mpEffects = (int) EventController.callEvent(EntityEvent.COUNT_MP_EVENT, map).get(EntityEvent.MP_ARG_KEY);
        int countedMp;
        if (isImmobilized()){
            return 0;
        } else if (withMovement && path != null && !movement){
            countedMp = mp - path.getCount() + 1 + mpEffects;
            //System.out.println("Pay attention, please: mp is " + countedMp);
            if (countedMp > 0) {
                return countedMp;
            } else {
                return 0;
            }
        } else {
            countedMp = mp + mpEffects;
            //System.out.println("Pay attention, please: mp is " + countedMp);
            if (countedMp > 0) {
                return countedMp;
            } else {
                return 0;
            }
        }
    }

    public int getRawMp() {
        return mp;
    }

    public void addMp(int mp){
        this.mp += mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMpMax() {
        return mpMax;
    }

    public void setMpMax(int mpMax) {
        this.mpMax = mpMax;
    }

    public int getSpeed() {
        return mpMax;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getAccuracyBonus() {
        return accuracyBonus;
    }

    public int getAccuracyStat(Entity target) {
        HashMap map = new HashMap();
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
        map.put(EntityEvent.ENTITY_TARGET_ARG_KEY, target);
        map.put(EntityEvent.ACCURACY_BONUS_ARG_KEY, accuracyBonus);
        return (int) EventController.callEvent(EntityEvent.ACCURACY_BONUS_EVENT, map).get(EntityEvent.ACCURACY_BONUS_ARG_KEY);
    }

    public void setAccuracyBonus(int accuracyBonus) {
        this.accuracyBonus = accuracyBonus;
    }

    public int countAccuracy(){ //TODO upgrade
        return accuracyBonus;
    }

    public boolean turnIsEnded(){
        return false;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isImmobilized() {
        return immobilized;
    }

    public void setImmobilized(boolean immobilized) {
        this.immobilized = immobilized;
    }

    public float getAnimTime() {
        return animTime;
    }

    public void setAnimTime(float animTime) {
        this.animTime = animTime;
    }

    public void addAnimTime(float delta) {
        this.animTime += delta;
    }

    public void setEffects(EffectArray effects) {
        this.effects = effects;
    }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    public void addEffect(Effect effect, int pos){
        effects.add(effect, pos);
    }

    public void addFirstEffectNowPlaying(Effect effect){
        nowPlaying.addEffect(effect, 0);
    }

    public void removeEffect(int id){
        effects.removeId(id);
    }

    public void removeEffect(Effect effect){
        effects.remove(effect);
    }

    public void checkRemoveEffect(int index){
        Effect effect = effects.get(index);
        if (effect.isDelete()){
            int id = effect.getId();
            removeEffect(id);
            System.out.println("Removing effect: " + id + "EffectArray size: " + effects.size());
        }
    }

    public void checkRemoveEffects(){
        for (int i = 0; i < effects.size(); i++) {
            checkRemoveEffect(i);
        }
    }

    public void clearEffects(){
        effects.clear();
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

    public int getDetailedEffect() {
        return detailedEffect;
    }

    public void setDetailedEffect(int detailedEffect) {
        this.detailedEffect = detailedEffect;
    }

    public int getDetailedSkill() {
        return detailedSkill;
    }

    public void setDetailedSkill(int detailedSkill) {
        this.detailedSkill = detailedSkill;
    }

    public void addSkill(Skill skill){
        skills.add(skill);
    }

    public void removeSkill(int index){
        skills.removeIndex(index);
    }

    public void removeSkill(Skill skill){
        skills.removeValue(skill, true);
    }

    public static void updateSkills() {
        Entity.updateSkills = true;
    }

    public boolean isSkillUse() {
        return skillUse;
    }

    public void setSkillUse(boolean skillUse) {
        this.skillUse = skillUse;
    }

    public Skill getUsedSkill() {
        return usedSkill;
    }

    public void setUsedSkill(Skill usedSkill) {
        updateSkills = true;
        this.usedSkill = usedSkill;
        if (usedSkill != null) {
            setSkillUse(true);
            HashMap mapStart = new HashMap();
            mapStart.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
            EventController.callEvent(EntityEvent.START_SKILL_EVENT, mapStart);
        } else {
            setSkillUse(false);
        }
    }

    public void useSkill(){
        if (isSkillUse() && usedSkill != null){
            usedSkill.use();
            usedSkill = null;
            setSkillUse(false);
            HashMap mapEnd = new HashMap();
            mapEnd.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, this);
            EventController.callEvent(EntityEvent.END_SKILL_EVENT, mapEnd);
            updateSkills = true;
        }
    }

    /** Events */
    @Override
    public void endTurn(){
        if (isPlayer() && !isRoomOpened()){
            triggerEncounter();
        }
        roomOpened = false;

        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).endTurn();
            System.out.println("End of turn");
        }
        checkRemoveEffects();

        DungeonMap.getCell(getTileX(), getTileY()).endTurn();

        for (int i = 0; i < skills.size; i++) {
            skills.get(i).cooldown();
        }
        moved = false;
    }

    @Override
    public void startTurn(){
        moved = false;
        if (isPlayer() ^ isControlled()) {
            addFirstEffectNowPlaying(new AttackEffect(this));
        }
        checkRemoveEffects();

        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).startTurn();
            checkRemoveEffect(i);
        }

        DungeonMap.getCell(getTileX(), getTileY()).startTurn();
    }

    @Override
    public void startMove() {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).startMove();
        }
        checkRemoveEffects();

        DungeonMap.getCell(getTileX(), getTileY()).startMove();

        moved = true;
    }

    @Override
    public void endMove() {
        System.out.println("MP: " + getMp(false) + " : " + getMp(true));
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).endMove();
        }
        checkRemoveEffects();
        System.out.println("MP: " + getMp(false) + " : " + getMp(true));

        DungeonMap.getCell(getTileX(), getTileY()).endMove();
    }

    @Override
    public int countMp(boolean withMovement) {
        int mpEffect = 0;
        for (int i = 0; i < effects.size(); i++) {
            mpEffect += effects.get(i).countMp(withMovement);
        }
        checkRemoveEffects();

        mpEffect += DungeonMap.getCell(getTileX(), getTileY()).countMp(withMovement);
        return mpEffect;
    }

    @Override
    public boolean canUseSkill() {
        boolean use = false;
        if (!isMovement()) {
            for (int i = 0; i < effects.size(); i++) {
                if (effects.get(i).isSkillUse()) {
                    use = effects.get(i).canUseSkill();
                }
            }
            checkRemoveEffects();

            DungeonCell cell = DungeonMap.getCell(getTileX(), getTileY());/** calls to cell.canUseSkill() not working properly*/
            for (Effect effect : cell.getEffects()) {
                if (effect.isSkillUse()) {
                    use = effect.canUseSkill();
                }
            }
            System.out.println("Can use: " + use);
        }
        return use;
    }

    @Override
    public void startSkill() {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).startSkill();
        }
        checkRemoveEffects();

        DungeonMap.drawTargetingZone(usedSkill);
        DungeonMap.getCell(getTileX(), getTileY()).startSkill();
    }

    @Override
    public void endSkill() {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).endSkill();
        }
        checkRemoveEffects();

        DungeonMap.getCell(getTileX(), getTileY()).endSkill();
        DungeonMap.clearTargetingZoneLayer();
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        for (int i = 0; i < effects.size(); i++) {
            action = effects.get(i).attackBonus(action);
        }
        checkRemoveEffects();

        action = DungeonMap.getCell(getTileX(), getTileY()).attackBonus(action);
        return action;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        for (int i = 0; i < effects.size(); i++) {
            accuracy = effects.get(i).accuracyBonus(accuracy, target);
        }
        checkRemoveEffects();

        accuracy = DungeonMap.getCell(getTileX(), getTileY()).accuracyBonus(accuracy, target);
        return accuracy;
    }

    @Override
    public int onDamage(int damage) {//TODO maybe funcs needs to get default damage?
        for (int i = 0; i < effects.size(); i++) {
            damage = effects.get(i).onDamage(damage);
        }
        checkRemoveEffects();

        damage = DungeonMap.getCell(getTileX(), getTileY()).onDamage(damage);

        if (damage > 0){
            return damage;
        }
        return 0;
    }

    @Override
    public int onHeal(int heal) {//TODO maybe funcs needs to get default heal?
        for (int i = 0; i < effects.size(); i++) {
            heal = effects.get(i).onHeal(heal);
        }
        checkRemoveEffects();

        heal = DungeonMap.getCell(getTileX(), getTileY()).onHeal(heal);

        if (heal > 0){
            return heal;
        }
        return 0;
    }

    @Override
    public void onEncounter(Encounter encounter) {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).onEncounter(encounter);
        }
        checkRemoveEffects();

        DungeonMap.getCell(getTileX(), getTileY()).onEncounter(encounter);
    }

    @Override
    public void onDeath() {
        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).onDeath();
        }
        checkRemoveEffects();

        DungeonMap.getCell(getTileX(), getTileY()).onDeath();

        alive = false;
    }
}
