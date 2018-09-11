package ru.myitschool.dcrawler.ai;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.pathfinding.EntityPath;
import ru.myitschool.dcrawler.ai.pathfinding.NodePath;
import ru.myitschool.dcrawler.ai.task.MoveTask;
import ru.myitschool.dcrawler.ai.task.SkillTask;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.ProtectiveStance;
import ru.myitschool.dcrawler.skills.Scratches;
import ru.myitschool.dcrawler.skills.Target;

/**
 * Created by Voyager on 16.08.2017.
 */
public class GoblinAI extends AI {

    public static final int LONG_DISTANCE_STATE = 0;
    public static final int NORMALL_DISTANCE_STATE = 1;
    public static final int SHORT_DISTANCE_STATE = 2;

    Entity controlledEntity;

    Scratches scratchSkill;
    ProtectiveStance stanceSkill;

    public GoblinAI(Entity controlledEntity) {
        super(controlledEntity);
        this.controlledEntity = controlledEntity;
        scratchSkill = new Scratches(controlledEntity);
        stanceSkill = new ProtectiveStance(controlledEntity);
        controlledEntity.addSkill(scratchSkill);
        controlledEntity.addSkill(stanceSkill);
    }

    @Override
    public void aiAnalyze() {
        /*Object[] nearest = AITweaks.getNearestEntityAndPath(controlledEntity.getTileX(), controlledEntity.getTileY(), AITweaks.TYPE_CHARACTER);
        Entity entity = (Entity) nearest[0];
        NodePath path = (NodePath) nearest[1];*/
        Array<EntityPath> paths = AITweaks.getAllEntityPaths(controlledEntity.getTileX(), controlledEntity.getTileY(), AITweaks.TYPE_CHARACTER, false);
        if (paths.size > 0) {
            EntityPath entityPath = paths.first();
            Entity entity = entityPath.getEntity();
            NodePath path = entityPath.getPath();
            path.cutLast();
            int distance = path.getCost();
            int speed = controlledEntity.getSpeed();
            if (distance > 0 && distance <= controlledEntity.getSpeed()) {
                addTask(new MoveTask(controlledEntity, path));
                Array<Target> targets = new Array<Target>();
                targets.add(new Target(entity.getTileX(), entity.getTileY()));
                addTask(new SkillTask(controlledEntity, scratchSkill, targets));
            } else if (distance > speed) {
                addTask(new MoveTask(controlledEntity, path, true, true));
            } else if (distance == 0) {
                addTask(new SkillTask(controlledEntity, stanceSkill, new Target(entity.getTileX(), entity.getTileY())));
            }
        }
    }

    @Override
    public void startTurn() {
        super.startTurn();
    }

    @Override
    public void endMove() {
        super.endMove();
    }

    @Override
    public void endSkill() {
        super.endSkill();
    }
}