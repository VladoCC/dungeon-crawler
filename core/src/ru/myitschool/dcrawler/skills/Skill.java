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
import ru.myitschool.dcrawler.skills.targeting.*;

import java.util.HashMap;

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
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH = 1; /**area centered in chosen point */
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE = 2; /** line that starts in chosen point */
    public static final int SKILL_TARGET_TYPE_FLOOR_SWING = 3;
    public static final int SKILL_TARGET_TYPE_FLOOR_WAVE_CONTROLLABLE = 4;
    public static final int SKILL_TARGET_TYPE_FLOOR_SPLASH_NOCENTER = 5;
    public static final int SKILL_TARGET_TYPE_ENEMY = 6;
    public static final int SKILL_TARGET_TYPE_CHARACTER = 7;
    public static final int SKILL_TARGET_TYPE_FLOOR_SINGLE = 8;
    public static final int SKILL_TARGET_TYPE_SELF = 9;

    private static final DiceAction dice = new DiceAction(6);

    private Array<Play> plays = new Array<Play>();
    private Array<Target> targets = new Array<Target>();

    private static final char[] diceFaces = {'⚅', '⚄', '⚃', '⚂', '⚁', '⚀'};

    private boolean cooldown = false;
    private boolean wallTargets = false;
    private boolean obstruct = false;
    private boolean mark = true;
    private boolean markEverything = false;
    private boolean checkAllTargets = false;

    private int type;
    private int targetType;
    private int targetCount = 0;
    private int targetCountMax = 0;
    private int cooldownCount = 0;
    private int cooldownMax = 0;
    private int distanceMin = 0;
    private int distanceMax = 0;
    private int range = 1;
    private int skillAccuracyBonus = 0;

    private String name;
    private String description;

    private Texture icon;

    private Entity doer;

    private TargetRenderer renderer;

    public Skill(Entity doer) {
        this.doer = doer;
        renderer = new TargetRenderer(this);
        renderer.addDisplayer(new RaytraceDisplayer());
    }

    public void use(){
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

    public int getSkillAccuracyBonus() {
        return skillAccuracyBonus;
    }

    public void setSkillAccuracyBonus(int skillAccuracyBonus) {
        this.skillAccuracyBonus = skillAccuracyBonus;
    }

    public int getAccuracyBonus(Entity target){
        return doer.getAccuracyStat(target) + getSkillAccuracyBonus();
    }

    protected MathAction countAttackAction(MathAction action){
        HashMap map = new HashMap();
        map.put(EntityEvent.ATTACK_BONUS_ARG_KEY, action);
        map.put(EntityEvent.ENTITY_EXECUTOR_ARG_KEY, doer);
        return (MathAction) EventController.callEvent(EntityEvent.ATTACK_BONUS_EVENT, map).get(EntityEvent.ATTACK_BONUS_ARG_KEY);
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

    public boolean isCheckAllTargets() {
        return checkAllTargets;
    }

    public void setCheckAllTargets(boolean checkAllTargets) {
        this.checkAllTargets = checkAllTargets;
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

    public void setDistance(int distanceMin, int distanceMax){
        this.distanceMin = distanceMin;
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

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public boolean isMarkEverything() {
        return markEverything;
    }

    public void setMarkEverything(boolean markEverything) {
        this.markEverything = markEverything;
    }

    public void addTarget(Target target){
        target = target.getMain();
        if (targetObstructionCheck(target, false)) {
            if (targetWallCheck(target) && (!checkAllTargets || targetObstructionCheck(target, true))) {
                 targets.add(target);
            }
            for (Target linked : target.getLinkedTargets()) {
                if (targetWallCheck(linked) && (!checkAllTargets || targetObstructionCheck(linked, true))) {
                    targets.add(linked);
                }
            }
            drawTargets();
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

    public void setRenderer(TargetRenderer renderer) {
        this.renderer = renderer;
    }

    public void setTypeDisplayer(int targetType){
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

    public Array<TilePos> displayTarget(int x, int y){
        return renderer.displayTarget(x, y);
    }

    public void addDisplayer(TargetDisplayer displayer){
        renderer.addDisplayer(displayer);
    }

    public void clearDisplayers(){
        renderer.clearDisplayers();
    }

    private boolean targetWallCheck(Target target){
        boolean targ = false;
        if (wallTargets){
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
        if (!obstruct){
            notObstructed = true;
        } else {
            notObstructed = !AITweaks.isPathObstructed(doer.getTileX(), doer.getTileY(), x, y);
        }
        return notObstructed;
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
