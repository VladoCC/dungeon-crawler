package ru.myitschool.dcrawler.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Voyager on 29.05.2017.
 */
public class InterfaceElements {

    private static final float ANIM_DURATION = 0.2f;

    private static final int TEXTURE_WIDTH = 32;
    private static final int TEXTURE_HEIGHT = 32;

    private static Animation health;
    private static Animation moves;

    public static void init(){
        TextureRegion[][] hpAnim = TextureRegion.split(new Texture("health.png"), TEXTURE_WIDTH, TEXTURE_HEIGHT);
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (int i = 0; i < hpAnim[0].length; i++) {
            regions.add(hpAnim[0][i]);
        }
        health = new Animation(ANIM_DURATION, regions);
        health.setPlayMode(Animation.PlayMode.LOOP);

        TextureRegion[][] moveAnim = TextureRegion.split(new Texture("speed.png"), TEXTURE_WIDTH, TEXTURE_HEIGHT);
        Array<TextureRegion> regs = new Array<TextureRegion>();
        for (int i = 0; i < moveAnim[0].length; i++) {
            regs.add(moveAnim[0][i]);
        }
        moves = new Animation(ANIM_DURATION, regs);
        moves.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static TextureRegion getHealth(){
        return getHealth(0);
    }

    public static TextureRegion getHealth(float stateTime) {
        return (TextureRegion) health.getKeyFrame(stateTime);
    }

    public static TextureRegion getMoves(){
        return getMoves(0);
    }

    public static TextureRegion getMoves(float stateTime) {
        return (TextureRegion) moves.getKeyFrame(stateTime);
    }
}
