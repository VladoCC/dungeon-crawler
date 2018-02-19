package ru.myitschool.cubegame.entities;

import ru.myitschool.cubegame.math.MathAction;

/**
 * Created by Voyager on 19.05.2017.
 */
public interface Event {

    void startTurn();

    void endTurn();

    void startMove();

    void endMove();

    int countMp(boolean withMovement);

    boolean canUseSkill();

    void startSkill();

    void endSkill();

    MathAction attackBonus(MathAction action);

    int accuracyBonus(int accuracy, Entity target);

    int onDamage(int damage);

    int onHeal(int heal);

    void onAny();

    void onEncounter();
}
