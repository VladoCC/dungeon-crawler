package ru.myitschool.cubegame.skills.targeting;

import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.skills.Play;
import ru.myitschool.cubegame.skills.PlayCheck;
import ru.myitschool.cubegame.skills.Skill;
import ru.myitschool.cubegame.skills.Target;

/**
 * Created by Voyager on 19.04.2018.
 */
public class EntityPlayCheck extends PlayCheck {

    public EntityPlayCheck(Skill skill) {
        super(skill);
    }

    @Override
    public int check(Target target) {
        int roll = new DiceAction(20).act();
        if (roll == 20){
            return Play.TARGETING_CRIT_HIT;
        } else if (roll == 1){
            return Play.TARGETING_CRIT_MISS;
        }
        return roll + getSkill().getAccuracyBonus() > target.getEntity().getArmor()? Play.TARGETING_HIT : Play.TARGETING_MISS;
    }
}
