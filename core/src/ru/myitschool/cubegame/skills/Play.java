package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.utils.Array;

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

    private Array<Action> actions = new Array<Action>();
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

    public void addAction(Action action){
        actions.add(action);
    }

    public void addPlay(Play play){
        plays.add(play);
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

    public void setPlayCheck(PlayCheck playCheck) {
        this.playCheck = playCheck;
    }
}