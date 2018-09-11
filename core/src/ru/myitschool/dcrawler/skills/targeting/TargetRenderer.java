package ru.myitschool.dcrawler.skills.targeting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;
import ru.myitschool.dcrawler.tiles.ColorTile;
import ru.myitschool.dcrawler.utils.Utils;

/**
 * Created by Voyager on 28.02.2018.
 */
public class TargetRenderer {

    private Skill skill;

    private Array<TargetDisplayer> displayers = new Array<TargetDisplayer>();

    private static final ColorTile defaultTargetTile = new ColorTile(Color.RED, 0.6f, true);
    private static final ColorTile defaultLineTile = new ColorTile(Color.CYAN, 0.6f, true);
    private static final ColorTile defaultObstructionTile = new ColorTile(Color.FIREBRICK, 1, true);

    public TargetRenderer(Skill skill) {
        this.skill = skill;
    }

    public Array<TilePos> displayTarget(int x, int y){//TODO wall checks
        Array<TilePos> array = new Array<TilePos>();
        Entity doer = skill.getDoer();

        int distanceMin = skill.getDistanceMin();
        int distanceMax = skill.getDistanceMax();
        if (Utils.isTargetInDistance(x, y, doer.getTileX(), doer.getTileY(), distanceMin, distanceMax) && !skill.hasTarget(new Target(x, y))){
            targetCreation(x, y, array);
        }
        return array;
    }

    protected Array<TilePos> targetCreation(int x, int y, Array<TilePos> array){
        boolean activate = true;
        for (TargetDisplayer displayer : displayers){
            if (activate){
                activate = displayer.targetCreation(x, y, array, getSkill());
            }
        }
        return array;
    }

    public void addDisplayer(TargetDisplayer displayer){
        displayers.add(displayer);
    }

    public void clearDisplayers(){
        displayers.clear();
    }

    public static ColorTile getDefaultTargetTile() {
        return defaultTargetTile;
    }

    public static ColorTile getDefaultLineTile() {
        return defaultLineTile;
    }

    public static ColorTile getDefaultObstructionTile() {
        return defaultObstructionTile;
    }

    public Skill getSkill() {
        return skill;
    }
}
