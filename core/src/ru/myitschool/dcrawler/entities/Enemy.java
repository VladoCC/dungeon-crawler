package ru.myitschool.dcrawler.entities;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AI;
import ru.myitschool.dcrawler.effects.Effect;
import ru.myitschool.dcrawler.effects.EffectArray;
import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.enemies.GoblinWarrior;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.skills.Skill;

/**
 * Created by Voyager on 08.08.2017.
 */
public abstract class Enemy extends Entity implements Cloneable {

    private AI enemyAI;
    private float skillTime;
    private float challengeRating;
    private boolean solo;
    private boolean activated = false;

    private static CRTable crTable = new CRTable();

    private static final float SKILL_TIME = 1;

    public Enemy(float x, float y) {
        super(null, null, x, y, 0, 0, 0, 0, 0);
        //DungeonMap.addEntity(this);
    }

    public static void createCRTable(){
        (new GoblinWarrior(0, 0)).addToCRTable();
    }

    private static void addToCRTable(Enemy enemy){
        crTable.add(enemy);
    }

    protected void addToCRTable(){
        addToCRTable(this);
    }

    public static Enemy getEnemyByCR(float cr, int type){
        return crTable.getEnemy(cr, type);
    }

    public static Enemy getEnemyByFormula(int type){
        return crTable.getEnemyByFormula(type);
    }

    public void activateAI(AI ai){
        enemyAI = ai;
        activated = true;
    }

    public void activateAI(){
        activateAI(createAI());
    }

    public abstract AI createAI();

    public boolean isActivated() {
        return activated;
    }

    @Override
    public boolean isEnemy() {
        return true;
    }

    public void setEnemyAI(AI enemyAI) {
        if (activated) {
            this.enemyAI = enemyAI;
        } else {
            activateAI(enemyAI);
        }
    }

    protected void setChallengeRating(float challengeRating) {
        this.challengeRating = challengeRating;
    }

    public float getChallengeRating() {
        return challengeRating;
    }

    public boolean isSolo() {
        return solo;
    }

    public void setSolo(boolean solo) {
        this.solo = solo;
    }

    public void useAI(){
        enemyAI.aiAnalyze();
    }

    public void addSkillTime(float delta){
        if (isSkillUse()) {
            skillTime += delta;
            if (skillTime >= SKILL_TIME) {
                skillTime = 0;
                useSkill();
            }
            System.out.println("Enemy Skill Time: " + skillTime);
        }
    }

    @Override
    public Enemy clone(){
        try {
            Enemy enemy = (Enemy) super.clone();
            Array<Skill> skills = new Array<>();
            for (Skill skill : this.getSkills()){
                skills.add(skill);
            }
            enemy.setSkills(skills);
            enemy.setModel(this.getModel());
            EffectArray effects = new EffectArray();
            for (Effect effect : this.getEffects()){
                effects.add((Effect) effect.clone());
            }
            enemy.setEffects(effects);
            return enemy;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void endTurn() {
        super.endTurn();
        if (!isControlled()) {
            enemyAI.endTurn();
        }
    }

    @Override
    public void startTurn() {
        super.startTurn();
        if (!isControlled()) {
            enemyAI.startTurn();
        }
    }

    @Override
    public void startMove() {
        super.startMove();
        if (!isControlled()) {
            enemyAI.startMove();
        }
    }

    @Override
    public void endMove() {
        super.endMove();
        if (!isControlled()) {
            enemyAI.endMove();
        }
    }

    @Override
    public int countMp(boolean withMovement) {
        int mp = super.countMp(withMovement);
        if (!isControlled()) {
            mp += enemyAI.countMp(withMovement);
        }
        return mp;
    }

    @Override
    public boolean canUseSkill() {
        return super.canUseSkill();
    }

    @Override
    public void startSkill() {
        super.startSkill();
        if (!isControlled() && enemyAI != null) {
            enemyAI.startSkill();
        }
    }

    @Override
    public void endSkill() {
        super.endSkill();
        if (!isControlled() && enemyAI != null) {
            enemyAI.endSkill();
        }
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        action = super.attackBonus(action);
        if (!isControlled() && enemyAI != null){
            action = enemyAI.attackBonus(action);
        }
        return action;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        accuracy = super.accuracyBonus(accuracy, target);
        if (!isControlled() && enemyAI != null){
            accuracy = enemyAI.accuracyBonus(accuracy, target);
        }
        return accuracy;
    }

    @Override
    public int onDamage(int damage) {
        damage = super.onDamage(damage);
        if (!isControlled() && enemyAI != null){
            damage = enemyAI.onDamage(damage);
        }
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        heal = super.onHeal(heal);
        if (!isControlled() && enemyAI != null){
            heal = enemyAI.onHeal(heal);
        }
        return heal;
    }

    @Override
    public void onEncounter(Encounter encounter) {
        super.onEncounter(encounter);
        if (!isControlled() && enemyAI != null) {
            enemyAI.onEncounter(encounter);
        }
    }

    @Override
    public void onDeath() {
        super.onDeath();
        if (!isControlled() && enemyAI != null) {
            enemyAI.onDeath();
        }
    }
}
