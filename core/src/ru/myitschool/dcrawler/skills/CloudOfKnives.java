package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.CloudOfKnivesEffect;
import ru.myitschool.dcrawler.effects.FloorClearingEffect;
import ru.myitschool.dcrawler.effects.FloorEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 30.11.2017.
 */
public class CloudOfKnives extends Skill {

    private static final Color color = new Color(0xa020f0bf);

    MathAction rollAction = new DiceAction(20);
    MathAction attackAction = new DiceAction(6);

    public CloudOfKnives(Entity doer) {
        super(doer);
        setIcon(new Texture("cloud_knives.png"));
        setName("Cloud ofKnives");
        setDescription("You creates cloud of psi energy, that deal damage to all creatures in zone instantly and everytimecreature starts turn in zone on enter the zone to the end of your next turn");
        setTargetCountMax(1);
        setDistanceMax(4);
        setDistanceMin(3);
        setRange(3);
        setTargetType(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        setTypeDisplayer(SKILL_TARGET_TYPE_FLOOR_SPLASH);
        setType(SKILL_TYPE_ENCOUNTER);
        setWallTargets(true);
        PlayContainer container = addPlayContainer();
        container.getEntityPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                damage = 0;
                if (success  == Play.TARGETING_HIT) {
                     damage = -attackAction.act();
                } else if (success == Play.TARGETING_CRIT_HIT){
                    damage = -attackAction.max();
                }
                Entity entity = target.getEntity();
                entity.addHp(damage);
                if (damage != 0) {
                    mark.addText(damage + "");
                }
            }
        });
        addPlayContainer();
    }

    @Override
    public void use() {
        FloorEffect effect = null;
        effect = new FloorEffect(getTargets(), new CloudOfKnivesEffect(effect, countAttackAction(attackAction)), color, true, true);
        getDoer().addEffect(new FloorClearingEffect(effect));
        super.use();
    }
}
