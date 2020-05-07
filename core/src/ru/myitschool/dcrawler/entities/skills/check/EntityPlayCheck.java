package ru.myitschool.dcrawler.entities.skills.check;

import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.entities.skills.Play;
import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

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
        return roll + getSkill().getAccuracyBonus(target.getEntity()) > target.getEntity().getArmor()? Play.TARGETING_HIT : Play.TARGETING_MISS;
    }
}
