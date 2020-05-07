package ru.myitschool.dcrawler.entities.skills.targeting;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.skills.Skill;

/**
 * Created by Voyager on 16.03.2018.
 */
public interface TargetDisplayer {

    boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill);
}
