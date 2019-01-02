package ru.myitschool.dcrawler.skills.patterns;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public abstract class TargetPattern {

    private Skill skill;

    public TargetPattern(Skill skill) {
        this.skill = skill;
    }

    public abstract Target createTarget(Target target);

    public Skill getSkill() {
        return skill;
    }
}
