package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.cubegame.effects.ImmobilizedEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.skills.targeting.EntityPlayCheck;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Immobilize extends Skill {

    public Immobilize(Entity doer) {
        super(doer);
        setIcon(new Texture("immobilization.png"));
        setName("Immobilize");
        setDescription("You immobilizes your target");
        setTargetCountMax(1);
        setDistanceMax(5);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        setType(SKILL_TYPE_COOLDOWN_DICE);
        setCooldownMax(2);
        setObstruct(false);
        setWallTargets(false);
        PlayContainer container = addPlayContainer();
        container.getEnemyPlay().setPlayCheck(new EntityPlayCheck(this));
        container.getEnemyPlay().addAction(new Action() {
            @Override
            public void act(Target target, int success, FloatingDamageMark mark) {
                if (success == Play.TARGETING_CRIT_HIT || success == Play.TARGETING_HIT) {
                    int turns = 1;
                    if (success == Play.TARGETING_CRIT_HIT){
                        turns = 2;
                    }
                    target.getEntity().addEffect(new ImmobilizedEffect(target.getEntity(), turns));
                    mark.addText("Success");
                }
            }
        });
        addPlayContainer();
    }
}
