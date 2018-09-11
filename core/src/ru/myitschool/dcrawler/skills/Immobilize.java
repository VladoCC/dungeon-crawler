package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.DisarmedEffect;
import ru.myitschool.dcrawler.effects.ImmobilizedEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.check.EntityPlayCheck;

/**
 * Created by Voyager on 02.12.2017.
 */
public class Immobilize extends Skill {

    int normalTurns = 1;
    int criticalTurns = 2;

    public Immobilize(Entity doer) {
        super(doer);
        setIcon(new Texture("immobilization.png"));
        setName("Immobilize");
        setDescription("You immobilizes your target for " + normalTurns + "(" + criticalTurns + ")");
        setTargetCountMax(1);
        setDistanceMax(8);
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
        container.getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_CRIT_HIT || success == Play.TARGETING_HIT) {
                    int turns = 1;
                    if (success == Play.TARGETING_CRIT_HIT){
                        turns = 2;
                    }
                    target.getEntity().addEffect(new ImmobilizedEffect(target.getEntity(), turns));
                    target.getEntity().addEffect(new DisarmedEffect(target.getEntity(), turns));
                    mark.addText("Success");
                }
            }
        });
        addPlayContainer();
    }
}
