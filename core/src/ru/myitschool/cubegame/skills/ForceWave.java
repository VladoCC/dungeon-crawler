package ru.myitschool.cubegame.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.ai.AITweaks;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.math.DiceAction;
import ru.myitschool.cubegame.math.MathAction;
import ru.myitschool.cubegame.skills.targeting.TargetDisplayer;
import ru.myitschool.cubegame.skills.targeting.TilePos;
import ru.myitschool.cubegame.tiles.ColorTile;
import ru.myitschool.cubegame.utils.AdvancedArray;

/**
 * Created by Voyager on 06.12.2017.
 */
public class ForceWave extends Skill {

    MathAction rollAction = new DiceAction(1, 20);

    public ForceWave(final Entity doer) {
        super(doer);
        setIcon(new Texture("force_wave.png"));
        setName("Force wave");
        setDescription("You repels your target");
        setTargetCountMax(1);
        setDistanceMax(3);
        setDistanceMin(1);
        setRange(1);
        setTargetType(SKILL_TARGET_TYPE_ENTITY);
        setTypeDisplayer(SKILL_TARGET_TYPE_ENTITY);
        addDisplayer(new TargetDisplayer() {
            @Override
            public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
                AdvancedArray<Vector2> raytrace = AITweaks.getCellRaytrace(doer.getTileX(), doer.getTileY(), x, y, 2);
                raytrace.clip(raytrace.size - 2, raytrace.size - 1);
                Array<Integer> poses = AITweaks.getObstructorIndexes(raytrace, true);
                if (poses.size > 0){
                    raytrace.clip(0, poses.get(0) - 1);
                }
                for (Vector2 vector : raytrace){
                    array.add(new TilePos((int) vector.x, (int) vector.y, new ColorTile(Color.YELLOW, 0.4f, true)));
                }
                return true;
            }
        });
        setObstruct(true);
        setWallTargets(false);
        Play play = new Play() {
            @Override
            public boolean check(Target target) {
                return rollAction.act() + getAccuracyBonus() > target.getEntity().getArmor();
            }
        };
        play.addAction(new Action() {
            @Override
            public void act(Target target, boolean success, FloatingDamageMark mark) {
                if (success) {
                    AdvancedArray<Vector2> array = AITweaks.getCellRaytrace(doer.getTileX(), doer.getTileY(), target.getX(), target.getY(), 2);
                    Vector2 start = array.get(array.size - 3);
                    array.clip(array.size - 2, array.size - 1);
                    Array<Integer> poses = AITweaks.getObstructorIndexes(array, true);
                    Vector2 end = array.getLast();
                    if (poses.size > 0){
                        end = array.get(poses.get(0) - 1);
                    }
                    target.getEntity().throwEntity(new Vector2(end.x - start.x, end.y - start.y));
                    mark.addText("Success");
                }
            }
        });
        addPlay(play);
    }
}
