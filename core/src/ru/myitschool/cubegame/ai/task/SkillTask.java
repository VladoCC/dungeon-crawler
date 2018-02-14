package ru.myitschool.cubegame.ai.task;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.skills.Skill;
import ru.myitschool.cubegame.skills.Target;

/**
 * Created by Voyager on 12.02.2018.
 */
public class SkillTask extends Task {

    private Skill skill;
    private Array<Target> targets;

    public SkillTask(Entity entity, Skill skill) {
        super(entity);
        this.skill = skill;
    }

    public SkillTask(Entity entity, Skill skill, Target target) {
        this(entity, skill, new Array<Target>());
        addTarget(target);
    }

    public SkillTask(Entity entity, Skill skill, Array<Target> targets) {
        super(entity);
        this.skill = skill;
        this.targets = targets;
    }

    public void addTarget(Target target){
        targets.add(target);
    }

    @Override
    void startTask() {
        entity.setUsedSkill(skill );
        if (targets != null){
            entity.addTargets(targets);
        }
    }

    @Override
    public void endSkill() {
        complete();
    }
}
