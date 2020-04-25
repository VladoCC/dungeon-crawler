package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AITweaks;
import ru.myitschool.dcrawler.ai.pathfinding.GraphStorage;
import ru.myitschool.dcrawler.ai.pathfinding.Node;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEvent;
import ru.myitschool.dcrawler.event.EventController;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.patterns.TargetPattern;
import ru.myitschool.dcrawler.skills.targeting.*;
import ru.myitschool.dcrawler.utils.Utils;

import java.util.HashMap;

/**
 * Created by Voyager on 29.06.2017.
 */
public abstract class Skill {

    public static final int SKILL_TYPE_AT_WILL = 0;
    public static final int SKILL_TYPE_COOLDOWN = 1;
    public static final int SKILL_TYPE_ENCOUNTER = 2;
    public static final int SKILL_TYPE_DAILY = 3;
    public static final int SKILL_TYPE_COOLDOWN_DICE = 4;

    public static final int SKILL_TARGET_TYPE_ENTITY = 0;
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH = 1; /**area centered in chosen point */
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE = 2; /** line that starts in chosen point */
    public static final int SKILL_TARGET_TYPE_FLOOR_SWING = 3;
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE = 4;
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER = 5;
    public static final int SKILL_TARGET_TYPE_ENEMY = 6;
    public static final int SKILL_TARGET_TYPE_CHARACTER = 7;
    public static final int SKILL_TARGET_TYPE_FLOOR_SINGLE = 8;
    public static final int SKILL_TARGET_TYPE_SELF = 9;

    private static final DiceAction standardDice = new DiceAction(6);

    private Array<Play> plays = new Array<Play>();
    private Array<Target> targets = new Array<Target>();

    private static final char[] diceFaces = {'⚅', '⚄', '⚃', '⚂', '⚁', '⚀'};

    private boolean cooldown = false;

    /** just an iterators */
    private int targetCount = 0;
    private int cooldownCount = 0;
    //private int range = 1;

    private Entity doer;

    private TargetRenderer renderer;

    public Skill(Entity doer) {
        this.doer = doer;
        renderer = new TargetRenderer(this);
        renderer.addDisplayer(new RaytraceDisplayer());
        maintainDisplayers(renderer.getDisplayers());
    }

    public abstract void maintainDisplayers(Array<TargetDisplayer> displayers);

    public abstract TargetPattern getPattern();

    public abstract Texture getIcon();

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getSkillAccuracyBonus();

    public abstract int getRange();

    public abstract int getDistanceMin();

    public abstract int getDistanceMax();

    public abstract int getTargetCountMax();

    public abstract int getCooldownMax();

    public abstract int getType();

    public abstract int getTargetType();

    public abstract boolean isCheckAllTargets();

    public abstract boolean isMarkEverything();

    public abstract boolean isMark();

    public abstract boolean isObstruct();

    public abstract boolean isWallTargets();

    //TODO add some sort of action independent from targets
    public void use(){ // TODO add play for self
        for (Target target : targets) {
            FloatingDamageMark mark = new FloatingDamageMark(target.getX(), target.getY(), "Miss");
            for (Play play : plays) {
                play.act(target, mark);
            }
            if (isMark() && (isMarkEverything() || target.getEntity() != null)) {
                mark.show();
            }
        }
        clearTargets();
        undrawTargets();
        startCooldown();
    }

    public Entity getDoer() {
        return doer;
    }

    public int getAccuracyBonus(Entity target){
        return doer.getAccuracyStat(target) + getSkillAccuracyBonus();
    }

    public MathAction countAttackAction(MathAction action){
        HashMap map = new HashMap();
        map.put(EntityEvent.ATTACK_BONUS_ARG_KEY, action);
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, doer);
        return (MathAction) EventController.callEvent(EntityEvent.ATTACK_BONUS_EVENT, map).get(EntityEvent.ATTACK_BONUS_ARG_KEY);
    }

    public MathAction countHealAction(MathAction action){
        HashMap map = new HashMap();
        map.put(EntityEvent.HEAL_BONUS_ARG_KEY, action);
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, doer);
        return (MathAction) EventController.callEvent(EntityEvent.HEAL_BONUS_EVENT, map).get(EntityEvent.HEAL_BONUS_ARG_KEY);
    }

    public PlayContainer addPlayContainer(){
        PlayContainer playContainer = new PlayContainer(this);
        plays.add(playContainer);
        return playContainer;
    }

    public Array<Play> getPlays() {
        return plays;
    }

    public void setPlays(Array<Play> plays) {
        this.plays = plays;
    }

    public boolean isCooldown() {
        return cooldown;
    }

    public void setCooldown(boolean cooldown) {
        this.cooldown = cooldown;
    }

    public String getTypeString(){
        switch (getType()){
            case SKILL_TYPE_AT_WILL:
                return "At will";
            case SKILL_TYPE_COOLDOWN:
                return "Cooldown: " + getCooldownMax() + " turns";
            case SKILL_TYPE_COOLDOWN_DICE:
                return "Cooldown on " + getDices();
            case SKILL_TYPE_ENCOUNTER:
                return "Encounter";
            case SKILL_TYPE_DAILY:
                return "Daily";
        }
        return "";
    }

    private String getDices(){
        String dices = "";
        if (getType() == SKILL_TYPE_COOLDOWN_DICE){
            for (int i = 0; i < getCooldownMax(); i++) {
                dices += diceFaces[i];
            }
        }
        return dices;
    }

    public String getTargetTypeString(){
        switch (getTargetType()){
            case SKILL_TARGET_TYPE_ENTITY:
                return "Entity";
            case SKILL_TARGET_TYPE_FLOOR_SPLASH:
                return "Splash";
            case SKILL_TARGET_TYPE_FLOOR_WAVE:
                return "Wave";
            case SKILL_TARGET_TYPE_FLOOR_SWING:
                return "Swing";
            case SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE:
                return "Controllable wave";
            case SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER:
                return "Splash without center";
            case SKILL_TARGET_TYPE_ENEMY:
                return "Enemy";
            case SKILL_TARGET_TYPE_CHARACTER:
                return "Character";
            case SKILL_TARGET_TYPE_FLOOR_SINGLE:
                return "Single cell";
            case SKILL_TARGET_TYPE_SELF:
                return "Self";
        }
        return "";
    }

    public int getCooldownCount() {
        return cooldownCount;
    }

    public void setCooldownCount(int cooldownCount) {
        this.cooldownCount = cooldownCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public void addTarget(Target target){
        target = target.getMain();
        if (Utils.isTargetInDistance(target.getX(), target.getY(), getDoer().getTileX(), getDoer().getTileY(), getDistanceMin(), getDistanceMax()) && getTargetCount() < getTargetCountMax()) {
            target = getPattern().createTarget(target);
            if (target != null && targetObstructionCheck(target, false)) {
                boolean added = false;
                if (targetWallCheck(target) && (!isCheckAllTargets() || targetObstructionCheck(target, true))) {
                    targets.add(target);
                    added = true;
                }
                for (Target linked : target.getLinkedTargets()) {
                    if (targetWallCheck(linked) && (!isCheckAllTargets() || targetObstructionCheck(linked, true))) {
                        targets.add(linked);
                        added = true;
                    }
                }
                if (added){
                    setTargetCount(getTargetCount() + 1);
                }
                drawTargets();
            }
        }
    }

    public Array<Target> getTargets() {
        return targets;
    }

    public void setTargets(Array<Target> targets) {
        this.targets = targets;
    }

    public void clearTargets(){
        targets.clear();
        setTargetCount(0);
    }

    public boolean hasTarget(Target target){
        return targets.contains(target, false);
    }

    public void removeTarget(Target target){
        for (Target skillTarget : targets){
            if (skillTarget.equals(target)){
                target = skillTarget;
            }
        }
        Target main;
        if (target.isLinked()){
            main = target.getMain();
        } else {
            main = target;
        }
        for (Target linked : main.getLinkedTargets()){
            targets.removeValue(linked, false);
        }
        targets.removeValue(main, false);
        setTargetCount(getTargetCount() - 1);
    }

    public void drawTargets(){
        DungeonMap.drawTargets(getTargets());
    }

    public void undrawTargets(){
        DungeonMap.clearTargetLayer();
    }

    public TargetRenderer getRenderer() {
        return renderer;
    }

    /*public void setTypeDisplayer(int targetType){
        clearDisplayers();
        addDisplayer(new RaytraceDisplayer());
        switch (targetType){
            case SKILL_TARGET_TYPE_ENTITY:
                addDisplayer(new EntityDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SPLASH:
                addDisplayer(new SplashDisplayer(true));
                break;
            case SKILL_TARGET_TYPE_FLOOR_WAVE:
                addDisplayer(new WaveDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SWING:
                addDisplayer(new SwingDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE:
                break;
            case SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER:
                addDisplayer(new SplashDisplayer(false));
                break;
            case SKILL_TARGET_TYPE_ENEMY:
                addDisplayer(new EnemyDisplayer());
                break;
            case SKILL_TARGET_TYPE_CHARACTER:
                addDisplayer(new CharacterDisplayer());
                break;
            case SKILL_TARGET_TYPE_FLOOR_SINGLE:
                addDisplayer(new FloorSingleDisplayer());
                break;
            case SKILL_TARGET_TYPE_SELF:
                break;
        }
    }

    protected void clearDisplayers() {
        renderer.clearDisplayers();
    }

    protected void addDisplayer(TargetDisplayer targetDisplayer) {
        renderer.addDisplayer(targetDisplayer);
    }*/

    public Array<TilePos> displayTarget(int x, int y){
        return getRenderer().displayTarget(x, y);
    }

    private boolean targetWallCheck(Target target){
        boolean targ = false;
        if (isWallTargets()){
            targ = true;
        } else {
            Node node = GraphStorage.getNodeBottom(target.getX(), target.getY());
            targ = node != null && node.getTile().isReachable();
        }
        /*boolean obst = false;
        if (!obstruct){
            obst = true;
        } else {
            obst = !AITweaks.isPathObstructed(doer.getTileX(), doer.getTileY(), target.getX(), target.getY());
        }*/
        return /*obst && */targ;
    }

    /** can check real X and Y or checkX and checkY
     * return true if path not obstructed*/
    private boolean targetObstructionCheck(Target target, boolean realCoords){
        int x = realCoords? target.getX() : target.getCheckX();
        int y = realCoords? target.getY() : target.getCheckY();
        boolean notObstructed = false;
        if (!isObstruct()){
            notObstructed = true;
        } else {
            notObstructed = !AITweaks.isPathObstructed(doer.getTileX(), doer.getTileY(), x, y);
        }
        return notObstructed;
    }

    public void startCooldown(){
        if (getType() != SKILL_TYPE_AT_WILL) {
            setCooldown(true);
            if (getType() == SKILL_TYPE_COOLDOWN) {
                setCooldownCount(getCooldownMax());
            }
        }
    }

    public void cooldown(){
        if (isCooldown() && getType() == SKILL_TYPE_COOLDOWN) {
            setCooldownCount(getCooldownCount() - 1);
            if (getCooldownCount() == 0) {
                setCooldown(false);
            }
        } else if (isCooldown() && getType() == SKILL_TYPE_COOLDOWN_DICE){
            if (standardDice.act() <= getCooldownMax()){
                setCooldown(false);
            }
        }
    }

    public void endEncounter(){
        if (getType() == SKILL_TYPE_ENCOUNTER){
            setCooldown(false);
        }
    }

    public void endDay(){
        if (getType() == SKILL_TYPE_DAILY){
            setCooldown(false);
        }
    }
}
