package ru.myitschool.dcrawler.entities.ai;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.ai.pathfinding.NodePath;
import ru.myitschool.dcrawler.entities.ai.task.MoveTask;
import ru.myitschool.dcrawler.entities.ai.task.SkillTask;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.entities.skills.ProtectiveStance;
import ru.myitschool.dcrawler.entities.skills.Scratches;
import ru.myitschool.dcrawler.entities.skills.Target;

import java.util.Map;

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
        Map<NodePath, Entity> paths = AIUtils.getAllEntityPaths(controlledEntity.getTileX(), controlledEntity.getTileY(),false, Entity::isCharacter);
        if (paths.size() > 0) {
            Map.Entry<NodePath, Entity> entityPath = (Map.Entry<NodePath, Entity>) paths.entrySet().toArray()[0];
            Entity entity = entityPath.getValue();
            NodePath path = entityPath.getKey();
            path.cutLast();
            int distance = path.getCost();
            int speed = controlledEntity.getSpeed();
            if (distance > 0 && distance <= controlledEntity.getSpeed()) {
                addTask(new MoveTask(path));
                Array<Target> targets = new Array<Target>();
                targets.add(new Target(entity.getTileX(), entity.getTileY()));
                addTask(new SkillTask(scratchSkill, targets));
            } else if (distance > speed) {
                addTask(new MoveTask(path, true, true));
            } else if (distance == 0) {
                addTask(new SkillTask(stanceSkill, new Target(entity.getTileX(), entity.getTileY())));
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