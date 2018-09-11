package ru.myitschool.dcrawler.event;

import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 27.11.2017.
 */

/**
 * After every update of this class and Event interface, Enemy, DungeonCell and AI is need to be updated
 */
public class EntityEventAdapter implements EntityEvent {
    @Override
    public void startTurn() {
    }

    @Override
    public void endTurn() {

    }

    @Override
    public void startMove() {
    }

    @Override
    public void endMove() {
    }

    @Override
    public int countMp(boolean withMovement) {
        return 0;
    }

    @Override
    public boolean canUseSkill() {
        return false;
    }

    @Override
    public void startSkill() {
    }

    @Override
    public void endSkill() {
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        return action;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        return accuracy;
    }

    @Override
    public int onDamage(int damage) {
        return damage;
    }

    @Override
    public int onHeal(int heal) {
        return heal;
    }

    @Override
    public void onEncounter(Encounter encounter) {
    }

    @Override
    public void onDeath() {

    }
}
