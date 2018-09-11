package ru.myitschool.dcrawler.skills.check;

import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

/**
 * Created by Voyager on 19.04.2018.
 */
public abstract class PlayCheck {

    Skill skill;

    public PlayCheck(Skill skill) {
        this.skill = skill;
    }

    public abstract int check(Target target);

    public Skill getSkill() {
        return skill;
    }
}
