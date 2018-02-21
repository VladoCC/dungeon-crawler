package ru.myitschool.cubegame.encounters;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.utils.AdvancedArray;

/**
 * Created by Voyager on 17.02.2018.
 */
public abstract class Encounter implements Cloneable {

    public static final float FULL_TIME = 3f;
    private static AdvancedArray<Encounter> encounters = new AdvancedArray<Encounter>();
    private static Array<Encounter> nowPlayings = new Array<Encounter>();
    private Entity entity;
    private String text = "";
    private String name = "";
    private float time = 0f;

    public Encounter() {
        encounters.add(this);
    }

    public void trigger(Entity entity){
        addNowPlaying(this);
        this.entity = entity;
    }

    protected abstract void activate(Entity entity);

    public static void update(float delta) {
        for (Encounter encounter : nowPlayings) {
            if (encounter != null) {
                encounter.time += delta;
                if (encounter.time >= FULL_TIME) {
                    encounter.activate(encounter.entity);
                    encounter.time = 0f;
                    nowPlayings.removeValue(encounter, true);
                    encounter.entity = null;
                    encounter = null;
                }
            }
        }
    }
    public static void initEncouters(){
        new DartEncounter();
    }

    public static Encounter getEncounter(int index){
        return encounters.get(index).clone();
    }

    public static Encounter getRandomEncounter(){
        return encounters.getRandom().clone();
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Array<Encounter> getNowPlayings() {
        return nowPlayings;
    }

    public static Encounter getNowPlaying(int index){
        return nowPlayings.get(index);
    }

    public static void addNowPlaying(Encounter nowPlaying) {
        nowPlayings.add(nowPlaying);
    }

    public static boolean hasNowPlayimg(){
        return nowPlayings.size > 0;
    }

    @Override
    protected Encounter clone(){
        try {
            Encounter encounter = (Encounter) super.clone();
            return encounter;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
