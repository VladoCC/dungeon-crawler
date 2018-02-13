package ru.myitschool.cubegame.ai;

import com.badlogic.gdx.math.Vector2;
import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.Node;
import ru.myitschool.cubegame.ai.pathfinding.NodePath;
import ru.myitschool.cubegame.ai.pathfinding.Pathfinder;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.skills.ProtectiveStance;
import ru.myitschool.cubegame.skills.Scratches;
import ru.myitschool.cubegame.skills.Target;

/**
 * Created by Voyager on 16.08.2017.
 */
public class GoblinAI extends AI {

    public static final int LONG_DISTANCE_STATE = 0;
    public static final int NORMALL_DISTANCE_STATE = 1;
    public static final int SHORT_DISTANCE_STATE = 2;

    int state;

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
        Entity character = AITweaks.getNearestEntity(controlledEntity.getTileX(), controlledEntity.getTileY(), AITweaks.TYPE_CHARACTER);
        int rawDistance = AITweaks.countDistanceRaw(controlledEntity.getTileX(), controlledEntity.getTileY(), character.getTileX(), character.getTileY(), true);
        int speed = controlledEntity.getSpeed();
        boolean longDist = false;
        if (rawDistance > 0 && rawDistance <= controlledEntity.getSpeed()){
            NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(controlledEntity.getTileX(), controlledEntity.getTileY()), GraphStorage.getNodeBottom(character.getTileX(), character.getTileY()), true, controlledEntity.getSpeed() + 1, 5, false, true);
            if (path != null){
                path.cutLast();
                controlledEntity.setPath(path);
                scratchSkill.addTarget(new Target(character.getTileX(), character.getTileY()));
                DungeonMap.moveChar();
                state = NORMALL_DISTANCE_STATE;
            } else {
                longDist = true;
            }
        }
        System.out.println("Enemy short distance " + rawDistance);
        if (rawDistance > speed || longDist){
            //NodePath roomPath = Pathfinder.searchRoomPath(controlledEntity.getTileX(), controlledEntity.getTileY(), character.getTileX(), character.getTileY());
            //Vector2 target = AITweaks.getDoorCell(roomPath.get(0).getX(), roomPath.get(0).getY(), roomPath.get(1).getX(), roomPath.get(1).getY(), roomPath.get(2).getX(), roomPath.get(2).getY());
            //NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(controlledEntity.getTileX(), controlledEntity.getTileY()), GraphStorage.getNodeBottom((int)target.x, (int) target.y), true, 30);
            NodePath path = Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(controlledEntity.getTileX(), controlledEntity.getTileY()), GraphStorage.getNodeBottom(character.getTileX(), character.getTileY()), true, 300, 5, false, true);
            System.out.println(path == null);
            if (path.getCost() > controlledEntity.getSpeed() * 2) {
                path.cut(controlledEntity.getSpeed() * 2);
            } else {
                path.cutLast();
            }
            controlledEntity.setPath(path);
            DungeonMap.moveChar();
            System.out.println("WTF ENEMY? " + longDist + " " + rawDistance);
            state = LONG_DISTANCE_STATE;
        } else if (rawDistance == 0) {
            state = SHORT_DISTANCE_STATE;
            stanceSkill.addTarget(new Target(character.getTileX(), character.getTileY()));
            controlledEntity.setUsedSkill(stanceSkill);

        }
    }

    @Override
    public void startTurn() {
        super.startTurn();
        aiAnalyze();
        switch (state){
            case LONG_DISTANCE_STATE:
                break;
            case NORMALL_DISTANCE_STATE:
                break;
            case SHORT_DISTANCE_STATE:
                stanceSkill.drawTargets();
                break;
        }
    }

    @Override
    public void endMove() {
        super.endMove();
        System.out.println("WTF ENEMY? " + state);
        if (state == LONG_DISTANCE_STATE) {
            Entity.nextTurn();
        } else if (state == NORMALL_DISTANCE_STATE) {
            controlledEntity.setUsedSkill(scratchSkill);
            scratchSkill.drawTargets();
            System.out.println("WTF ENEMY? " + controlledEntity.isSkillUse());
        }
    }

    @Override
    public void endSkill() {
        super.endSkill();
        Entity.nextTurn();
    }
}