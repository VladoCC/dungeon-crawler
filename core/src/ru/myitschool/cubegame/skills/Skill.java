package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.AITweaks;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.Node;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 29.06.2017.
 */
public class Skill {

    public static final int SKILL_TYPE_AT_WILL = 0;
    public static final int SKILL_TYPE_COOLDOWN = 1;
    public static final int SKILL_TYPE_ENCOUNTER = 2;
    public static final int SKILL_TYPE_DAILY = 3;
    public static final int SKILL_TYPE_COOLDOWN_DICE = 4;

    //public static final int SKILL_TARGET_TYPE_
    public static final int SKILL_TARGET_TYPE_ENTITY = 0;
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH = 1;
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE = 2;
    public static final int SKILL_TARGET_TYPE_FLOOR_SWING = 3;
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE = 4;
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER = 5;
    public static final int SKILL_TARGET_TYPE_ENEMY = 6;
    public static final int SKILL_TARGET_TYPE_CHARACTER = 7;
    public static final int SKILL_TARGET_TYPE_FLOOR_SINGLE = 8;
    public static final int SKILL_TARGET_TYPE_SELF = 9;

    private static final DiceAction dice = new DiceAction(1, 6);

    private Array<Play> plays = new Array<Play>();
    private Array<Target> targets = new Array<Target>();

    private char[] diceFaces = {'⚅', '⚄', '⚃', '⚂', '⚁', '⚀'};

    private boolean cooldown = false;
    private boolean wallTargets = false;
    private boolean obstruct = false;

    private int type;
    private int targetType;
    private int targetCount = 0;
    private int targetCountMax = 0;
    private int cooldownCount = 0;
    private int cooldownMax = 0;
    private int distanceMin = 0;
    private int distanceMax = 0;
    private int range = 1;

    private String name;
    private String description;

    private Texture icon;

    private Entity doer;

    public Skill(Entity doer) {
        this.doer = doer;
    }

    public void use(){
        for (Target target : targets) {
            for (Play play : plays) {
                play.act(target);
            }
        }
        clearTargets();
        undrawTargets();
        startCooldown();
    }

    public Entity getDoer() {
        return doer;
    }

    protected int getAccuracyBonus(){
        return doer.getAccuracyBonus();
    }

    protected MathAction countAttackAction(MathAction action){
        return doer.attackBonus(action);
    }

    public void addPlay(Play play){
        plays.add(play);
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

    public int getType() {
        return type;
    }

    public String getTypeString(){
        switch (type){
            case SKILL_TYPE_AT_WILL:
                return "At will";
            case SKILL_TYPE_COOLDOWN:
                return "Cooldown: " + cooldownMax + " turns";
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
        if (type == SKILL_TYPE_COOLDOWN_DICE){
            for (int i = 0; i < cooldownMax; i++) {
                dices += diceFaces[i];
            }
        }
        return dices;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTargetType() {
        return targetType;
    }

    public String getTargetTypeString(){
        switch (targetType){
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

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getCooldownCount() {
        return cooldownCount;
    }

    public void setCooldownCount(int cooldownCount) {
        this.cooldownCount = cooldownCount;
    }

    public int getCooldownMax() {
        return cooldownMax;
    }

    public void setCooldownMax(int cooldownMax) {
        this.cooldownMax = cooldownMax;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public int getTargetCountMax() {
        return targetCountMax;
    }

    public void setTargetCountMax(int targetCountMax) {
        this.targetCountMax = targetCountMax;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDistanceMin() {
        return distanceMin;
    }

    public void setDistanceMin(int distanceMin) {
        this.distanceMin = distanceMin;
    }

    public int getDistanceMax() {
        return distanceMax;
    }

    public void setDistanceMax(int distanceMax) {
        this.distanceMax = distanceMax;
    }

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addTarget(Target target){
        if (targetWallCheck(target)) {
            targets.add(target);
        }
        for (Target linked : target.getLinkedTargets()){
            if (targetWallCheck(linked)) {
                targets.add(linked);
            }
        }
        drawTargets();
    }

    public Array<Target> getTargets() {
        return targets;
    }

    public void setTargets(Array<Target> targets) {
        this.targets = targets;
    }

    public void clearTargets(){
        targets.clear();;
        targetCount = 0;
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
        targetCount--;
    }

    public void drawTargets(){
        DungeonMap.drawTargets(getTargets());
    }

    public void undrawTargets(){
        DungeonMap.clearTargetLayer();
    }

    public boolean isWallTargets() {
        return wallTargets;
    }

    public void setWallTargets(boolean wallTargets) {
        this.wallTargets = wallTargets;
    }

    public boolean isObstruct() {
        return obstruct;
    }

    public void setObstruct(boolean obstruct) {
        this.obstruct = obstruct;
    }

    private boolean targetWallCheck(Target target){
        boolean targ = false;
        if (wallTargets){
            targ = true;
        } else {
            Node node = GraphStorage.getNodeBottom(target.getX(), target.getY());
            targ = node != null && node.getTile().isReachable();
        }
        boolean obst = false;
        if (!obstruct){
            obst = true;
        } else {
            obst = !AITweaks.isPathObstructed(doer.getTileX(), doer.getTileY(), target.getX(), target.getY());
        }
        return obst && targ;
    }

    public void startCooldown(){
        if (type != SKILL_TYPE_AT_WILL) {
            setCooldown(true);
            if (type == SKILL_TYPE_COOLDOWN) {
                cooldownCount = cooldownMax;
            }
        }
    }

    public void cooldown(){
        if (cooldown && type == SKILL_TYPE_COOLDOWN) {
            cooldownCount--;
            if (cooldownCount == 0) {
                setCooldown(false);
            }
        } else if (cooldown && type == SKILL_TYPE_COOLDOWN_DICE){
            if (dice.act() <= cooldownMax){
                setCooldown(false);
            }
        }
    }

    public void endEncounter(){
        if (type == SKILL_TYPE_ENCOUNTER){
            setCooldown(false);
        }
    }

    public void endDay(){
        if (type == SKILL_TYPE_DAILY){
            setCooldown(false);
        }
    }
}
