package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.MarkedEffect;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 28.11.2017.
 */
public class Mark extends Skill {

    MarkedEffect effect = null;
    int penalty = -5;

    public Mark(Entity doer) {
        super(doer);
        setIcon(new Texture("mark.png"));
        setName("Mark");
        setDescription("You mark target and it gets" + penalty + " accuracy penalty for all attacks, except attacks targeted to you, but you can place only one mark at the time");
        setTargetCountMax(1);
        setDistanceMax(8);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENEMY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENEMY);
        addPlayContainer().getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success == Play.TARGETING_HIT || success == Play.TARGETING_CRIT_HIT) {
                    if (effect != null){
                        effect.getEntity().removeEffect(effect);
                    }
                    effect = new MarkedEffect(target.getEntity(), getDoer(), penalty);
                    target.getEntity().addEffect(effect);
                    mark.addText("Success");
                }
            }
        });
        addPlayContainer();
    }
}
