package ru.myitschool.dcrawler.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AITweaks;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 09.05.2018.
 */
public class Utils {

    public static int getDistance(int x1, int y1, int x2, int y2){
        return (int) Math.round(Math.sqrt(getDistanceRaw(x1, y1, x2, y2)));
    }

    /** function to count distance without sqrt*/
    public static long getDistanceRaw(int x1, int y1, int x2, int y2){
        return (long) (Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    public static boolean isTargetInDistance(int x1, int y1, int x2, int y2, int distanceMin, int distanceMax){
        long distance = getDistanceRaw(x1, y1, x2, y2);
        return distanceMin*distanceMin <= distance && distance <= distanceMax*distanceMax;
    }

    public static void pushEntity(Entity doer, Entity target, int distance){
        AdvancedArray<Vector2> array = AITweaks.getCellRaytrace(doer.getTileX(), doer.getTileY(), target.getTileX(), target.getTileY(), distance);
        Vector2 start = array.get(array.size - distance - 1);
        array.clip(array.size - distance, array.size - 1);
        Array<Integer> poses = AITweaks.getObstructorIndexes(array, true);
        Vector2 end = array.getLast();
        if (poses.size > 0){
            if (poses.get(0) != 0){
                end = array.get(poses.get(0) - 1);
            } else {
                end = start;
            }
        }
        target.throwEntity(new Vector2(end.x - start.x, end.y - start.y));
    }

    public static void pullEntity(Entity doer, Entity target, int distance){
        AdvancedArray<Vector2> array = AITweaks.getCellRaytrace(doer.getTileX(), doer.getTileY(), target.getTileX(), target.getTileY(), 0);
        Vector2 start = array.get(array.size - 1);
        array.clip(array.size - distance - 1, array.size - 2);
        Array<Integer> poses = AITweaks.getObstructorIndexes(array, true);
        Vector2 end = array.getFirst();
        if (poses.size > 0){
            if (poses.get(0) != 0) {
                end = array.get(poses.get(poses.size - 1) - 1);
            } else {
                end = start;
            }
        }
        target.throwEntity(new Vector2(end.x - start.x, end.y - start.y));
    }
}
