package ru.myitschool.dcrawler.ai.task;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

/**
 * Created by Voyager on 12.02.2018.
 */
public class SkillTask extends Task {

    private Skill skill;
    private Array<Target> targets;

    public SkillTask(Entity entity, Skill skill) {
        this.skill = skill;
    }

    public SkillTask(Skill skill, Target target) {
        this(skill, new Array<Target>());
        addTarget(target);
    }

    public SkillTask(Skill skill, Array<Target> targets) {
        this.skill = skill;
        this.targets = targets;
    }

    public void addTarget(Target target){
        targets.add(target);
    }

    @Override
    protected void startTask() {
        entity.setUsedSkill(skill);
        if (targets != null){
            entity.addTargets(targets);
        }
    }

    @Override
    public void endSkill() {
        complete();
    }
}
