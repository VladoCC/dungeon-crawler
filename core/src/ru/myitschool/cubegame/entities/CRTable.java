package ru.myitschool.cubegame.entities;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.utils.AdvancedArray;

import java.util.*;

/**
 * Created by Voyager on 25.01.2018.
 */
public class CRTable {

    public static final int ANY_TYPE = 0;
    public static final int STANDARD_TYPE = 1;
    public static final int SOLO_TYPE = 2;

    private TreeMap<Float, AdvancedArray<Enemy>> standardTable = new TreeMap<Float, AdvancedArray<Enemy>>();
    private TreeMap<Float, AdvancedArray<Enemy>> soloTable = new TreeMap<Float, AdvancedArray<Enemy>>();

    public void add(Enemy enemy){
        TreeMap<Float, AdvancedArray<Enemy>> challengeRatingTable;
        if (enemy.isSolo()){
            challengeRatingTable = soloTable;
        } else {
            challengeRatingTable = standardTable;
        }
        boolean has = challengeRatingTable.containsKey(enemy.getChallengeRating());
        if (!has){
            challengeRatingTable.put(enemy.getChallengeRating(), new AdvancedArray<Enemy>());
        }
        challengeRatingTable.get(enemy.getChallengeRating()).add(enemy);
    }

    public Enemy getEnemy(float cr, int type){
        int max = 0;
        TreeMap<Float, AdvancedArray<Enemy>> challengeRatingTable = standardTable;
        if (type == ANY_TYPE){
            if (!standardTable.containsKey(cr)){
                challengeRatingTable = soloTable;
                max = soloTable.get(cr).size;
            } else if (!soloTable.containsKey(cr)){
                challengeRatingTable = standardTable;
                max = standardTable.get(cr).size;
            } else {
                challengeRatingTable = standardTable;
                max = standardTable.get(cr).size + soloTable.get(cr).size;
            }
        } else if (type == STANDARD_TYPE){
            challengeRatingTable = standardTable;
            max = standardTable.get(cr).size;
        } else if (type == SOLO_TYPE){
            challengeRatingTable = soloTable;
            max = soloTable.get(cr).size;
        }
        int random = new Random().nextInt(max);
        if (type == ANY_TYPE){
            if (soloTable.containsKey(cr) && standardTable.containsKey(cr) && random - standardTable.get(cr).size >= 0 ){
                challengeRatingTable = soloTable;
                random -= standardTable.get(cr).size;
            }
        }
        return challengeRatingTable.get(cr).get(random).clone();
    }

    public Enemy getEnemyByFormula(int type){
        Float[] array = null;
        Set<Float> crs = standardTable.keySet();
        if (type == ANY_TYPE){
            crs = standardTable.keySet();
        } else if (type == STANDARD_TYPE){
            crs = standardTable.keySet();
        } else if (type == SOLO_TYPE){
            crs = soloTable.keySet();
        }
        array = new Float[crs.size()];
        int pos = 0;
        for (Float fl : crs){
            array[pos] = fl;
            pos++;
        }
        float max = 1 / array[0];
        float min = 1 / array[array.length - 1];
        float random = 1 / (min + (float) (Math.random() * (max - min)));
        int index = 0;
        float key = 0;
        float biggerKey = 0;
        if (random < array[index]){
            key = array[index];
        } else {
            for (int i = 0; i < array.length; i++) {
                if (random < array[i]){
                    index = i;
                    biggerKey = array[i];
                    key = array[i - 1];
                    break;
                }
            }
            if (index == 0){
                key = array[array.length - 1];
            } else {
                if (biggerKey - random < random - key){
                    key = biggerKey;
                }
            }
        }
        return getEnemy(key,type);
    }
}
