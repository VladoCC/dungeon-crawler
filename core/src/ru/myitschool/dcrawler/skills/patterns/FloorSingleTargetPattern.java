package ru.myitschool.dcrawler.skills.patterns;

import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public class FloorSingleTargetPattern extends TargetPattern {
    public FloorSingleTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        return target;
    }
}
