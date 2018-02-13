package ru.myitschool.cubegame.entities;

import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 27.11.2017.
 */

/**
 * After every update of this class and Event interface, Enemy and AI is need to be updated
 */
public class EventAdapter implements Event {
    @Override
    public void startTurn() {
        onAny();
    }

    @Override
    public void endTurn() {

    }

    @Override
    public void startMove() {
        onAny();
    }

    @Override
    public void endMove() {
        onAny();
    }

    @Override
    public int countMp(boolean withMovement) {
        onAny();
        return 0;
    }

    @Override
    public boolean canUseSkill() {
        onAny();
        return false;
    }

    @Override
    public void startSkill() {
        onAny();
    }

    @Override
    public void endSkill() {
        onAny();
    }

    @Override
    public MathAction attackBonus(MathAction action) {
        onAny();
        return null;
    }

    @Override
    public int accuracyBonus(int accuracy, Entity target) {
        onAny();
        return accuracy;
    }

    @Override
    public int onDamage(int damage) {
        onAny();
        return 0;
    }

    @Override
    public int onHeal(int heal) {
        onAny();
        return 0;
    }

    @Override
    public void onAny() {}
}
