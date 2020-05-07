package ru.myitschool.dcrawler.entities.skills.patterns;

import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

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
