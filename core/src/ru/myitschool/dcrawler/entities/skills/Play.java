package ru.myitschool.dcrawler.entities.skills;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.skills.action.Action;
import ru.myitschool.dcrawler.entities.skills.check.PlayCheck;

/**
 * Created by Voyager on 28.06.2017.
 */
public abstract class Play {

    public static final int TARGETING_CRIT_MISS = 0;
    public static final int TARGETING_MISS = 1;
    public static final int TARGETING_HIT = 2;
    public static final int TARGETING_CRIT_HIT = 3;

    private PlayCheck playCheck;

    private Skill skill;

    private Array<ru.myitschool.dcrawler.entities.skills.action.Action> actions = new Array<ru.myitschool.dcrawler.entities.skills.action.Action>();
    private Array<Play> plays = new Array<>();

    public Play(Skill skill) {
        this.skill = skill;
        this.playCheck = new PlayCheck(skill) {
            @Override
            public int check(Target target) {
                return TARGETING_MISS;
            }
        };
    }

    public abstract boolean isValidTarget(Target target);

    public int check(Target target){
        return playCheck.check(target);
    }

    public Play addAction(ru.myitschool.dcrawler.entities.skills.action.Action action){
        actions.add(action);
        return this;
    }

    public Play addPlay(Play play){
        plays.add(play);
        return this;
    }

    public void act(Target target, FloatingDamageMark mark){
        if (isValidTarget(target)) {
            int success = check(target);
            for (Action action : actions) {
                action.act(target, success, mark);
            }
            for (Play play : plays){
                play.act(target, mark);
            }
        }
    }

    public Play setPlayCheck(PlayCheck playCheck) {
        this.playCheck = playCheck;
        return this;
    }
}