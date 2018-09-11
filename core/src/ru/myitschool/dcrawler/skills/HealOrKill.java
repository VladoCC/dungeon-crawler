package ru.myitschool.dcrawler.skills;

import com.badlogic.gdx.graphics.Texture;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.math.DiceAction;
import ru.myitschool.dcrawler.math.MathAction;
import ru.myitschool.dcrawler.math.NegativeAction;
import ru.myitschool.dcrawler.skills.check.PlayCheck;

/**
 * Created by Voyager on 26.11.2017.
 */
public class HealOrKill extends Skill {

    MathAction healAction = new DiceAction(6);

    public HealOrKill(Entity doer) {
        super(doer);
        setIcon(new Texture("heal.png"));
        setName("Heal or Kill");
        setDescription("You can heal your ally or damage enemy for " + healAction.getDescription());
        setTargetCountMax(1);
        setDistanceMax(8);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENTITY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENTITY);
        PlayContainer container = addPlayContainer();
        container.getPlayerPlay().setPlayCheck(new PlayCheck(this) {
            @Override
            public int check(Target target) {
                return Play.TARGETING_HIT;
            }
        });
        container.getPlayerPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) {
            }
        }.setAttack(new NegativeAction(healAction)));
        container.getEnemyPlay().addAction(new Action(this) {
            @Override
            public void effect(Target target, int success, int damage, FloatingDamageMark mark) { }
        }.setAttack(healAction));
        addPlayContainer();
    }
}
