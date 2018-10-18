package ru.myitschool.dcrawler.event;

import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 19.05.2017.
 */
public interface EntityEvent {

    String[] eventCodes = {EntityEvent.START_TURN_EVENT, EntityEvent.END_TURN_EVENT, EntityEvent.START_MOVE_EVENT,
            EntityEvent.END_MOVE_EVENT, EntityEvent.COUNT_MP_EVENT, EntityEvent.CAN_USE_SKILL_EVENT,
            EntityEvent.START_SKILL_EVENT, EntityEvent.END_SKILL_EVENT, EntityEvent.ATTACK_BONUS_EVENT,
            EntityEvent.ACCURACY_BONUS_EVENT, EntityEvent.ON_DAMAGE_EVENT, EntityEvent.ON_HEAL_EVENT,
            EntityEvent.ON_ENCOUNTER_EVENT, EntityEvent.ON_DEATH_EVENT};

    String START_TURN_EVENT = "main.dcrawler.entity.start_turn";
    String END_TURN_EVENT = "main.dcrawler.entity.end_turn";
    String START_MOVE_EVENT = "main.dcrawler.entity.start_move";
    String END_MOVE_EVENT = "main.dcrawler.entity.end_move";
    String COUNT_MP_EVENT = "main.dcrawler.entity.count_mp";
    String CAN_USE_SKILL_EVENT = "main.dcrawler.entity.can_use_skill";
    String START_SKILL_EVENT = "main.dcrawler.entity.start_skill";
    String END_SKILL_EVENT = "main.dcrawler.entity.end_skill";
    String ATTACK_BONUS_EVENT = "main.dcrawler.entity.attack_bonus";
    String ACCURACY_BONUS_EVENT = "main.dcrawler.entity.accuracy_bonus";
    String ON_DAMAGE_EVENT = "main.dcrawler.entity.on_damage";
    String ON_HEAL_EVENT = "main.dcrawler.entity.on_heal";
    String ON_ENCOUNTER_EVENT = "main.dcrawler.entity.on_encounter";
    String ON_DEATH_EVENT = "main.dcrawler.entity.on_death";

    String ENTITY_EXECUTOR_ARG_KEY = "entity_executor";
    String WITH_MOVEMENT_ARG_KEY = "with_movement";
    String MP_ARG_KEY = "mp";
    String CAN_USE_SKILL_ARG_KEY = "can_use_skill";
    String ATTACK_BONUS_ARG_KEY = "attack_bonus";
    String ACCURACY_BONUS_ARG_KEY = "accuracy_bonus";
    String ENTITY_TARGET_ARG_KEY = "entity_target";
    String DAMAGE_ARG_KEY = "damage";
    String HEAL_ARG_KEY = "heal";
    String ENCOUNTER_ARG_KEY = "encounter";

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

    void onEncounter(Encounter encounter);

    void onDeath();
}
