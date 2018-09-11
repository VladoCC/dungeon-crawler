package ru.myitschool.dcrawler.event;

import ru.myitschool.dcrawler.encounters.Encounter;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.MathAction;

import java.util.HashMap;

public class EntityEventListener extends EventListener {

    public EntityEventListener() throws Exception {
        super();
    }

    @Override
    public HashMap<String, Object> fire(String eventCode, HashMap<String, Object> args) {
        HashMap<String, Object> result = new HashMap<>();
        Entity entity = (Entity) args.get(EntityEvent.ENTITY_EXECUTOR_ARG_KEY);
        switch (eventCode){
            case EntityEvent.START_TURN_EVENT:
                entity.startTurn();
                break;
            case EntityEvent.END_TURN_EVENT:
                entity.endTurn();
                break;
            case EntityEvent.START_MOVE_EVENT:
                entity.startMove();
                break;
            case EntityEvent.END_MOVE_EVENT:
                entity.endMove();
                break;
            case EntityEvent.COUNT_MP_EVENT:
                boolean withMovement = (boolean) args.get(EntityEvent.WITH_MOVEMENT_ARG_KEY);
                int mp = entity.countMp(withMovement);
                result.put(EntityEvent.MP_ARG_KEY, mp);
                break;
            case EntityEvent.CAN_USE_SKILL_EVENT:
                boolean canUseSkill = entity.canUseSkill();
                result.put(EntityEvent.CAN_USE_SKILL_ARG_KEY, canUseSkill);
                break;
            case EntityEvent.START_SKILL_EVENT:
                entity.startSkill();
                break;
            case EntityEvent.END_SKILL_EVENT:
                entity.endSkill();
                break;
            case EntityEvent.ATTACK_BONUS_EVENT:
                MathAction action = (MathAction) args.get(EntityEvent.ATTACK_BONUS_ARG_KEY);
                action = entity.attackBonus(action);
                result.put(EntityEvent.ATTACK_BONUS_ARG_KEY, action);
                break;
            case EntityEvent.ACCURACY_BONUS_EVENT:
                int accuracy = (int) args.get(EntityEvent.ACCURACY_BONUS_ARG_KEY);
                Entity target = (Entity) args.get(EntityEvent.ENTITY_TARGET_ARG_KEY);
                accuracy = entity.accuracyBonus(accuracy, target);
                result.put(EntityEvent.ACCURACY_BONUS_ARG_KEY, accuracy);
                break;
            case EntityEvent.ON_DAMAGE_EVENT:
                int damage = (int) args.get(EntityEvent.DAMAGE_ARG_KEY);
                damage = entity.onDamage(damage);
                result.put(EntityEvent.DAMAGE_ARG_KEY, damage);
                break;
            case EntityEvent.ON_HEAL_EVENT:
                int heal = (int) args.get(Entity.HEAL_ARG_KEY);
                heal = entity.onHeal(heal);
                result.put(EntityEvent.HEAL_ARG_KEY, heal);
                break;
            case EntityEvent.ON_ENCOUNTER_EVENT:
                Encounter encounter = (Encounter) args.get(EntityEvent.ENCOUNTER_ARG_KEY);
                entity.onEncounter(encounter);
                break;
            case EntityEvent.ON_DEATH_EVENT:
                entity.onDeath();
                break;
        }
        return result;
    }

    @Override
    public String[] eventCodes() {
        String[] codes = {EntityEvent.START_TURN_EVENT, EntityEvent.END_TURN_EVENT, EntityEvent.START_MOVE_EVENT,
                EntityEvent.END_MOVE_EVENT, EntityEvent.COUNT_MP_EVENT, EntityEvent.CAN_USE_SKILL_EVENT,
                EntityEvent.START_SKILL_EVENT, EntityEvent.END_SKILL_EVENT, EntityEvent.ATTACK_BONUS_EVENT,
                EntityEvent.ACCURACY_BONUS_EVENT, EntityEvent.ON_DAMAGE_EVENT, EntityEvent.ON_HEAL_EVENT,
                EntityEvent.ON_ENCOUNTER_EVENT, EntityEvent.ON_DEATH_EVENT};
        return codes;
    }
}
