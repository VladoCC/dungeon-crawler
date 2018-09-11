package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.effects.GodsProtectionEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;

/**
 * Created by Voyager on 26.11.2017.
 */
public class ArcaneShielding extends Skill {

    MathAction rollAction = new DiceAction(20);

    public ArcaneShielding(Entity doer) {
        super(doer);
        setIcon(new Texture("gods_protection.png"));
        setName("Arcane shielding");
        setDescription("This shield will protect its target from any damage for one turn");
        setTargetCountMax(1);
        setDistanceMax(4);
        setDistanceMin(0);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_CHARACTER);
        setTypeDisplayer(SKILL_TARGET_TYPE_CHARACTER);
        setType(SKILL_TYPE_ENCOUNTER);
        setMark(false);
        setWallTargets(false);
        addPlayContainer().getPlayerPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
                if (success >= Play.TARGETING_HIT){
                    target.getEntity().addEffect(new GodsProtectionEffect(getDoer()));
                }
            }
        });
    }
}
