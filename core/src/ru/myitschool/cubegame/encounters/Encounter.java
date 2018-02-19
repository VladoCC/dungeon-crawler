package ru.myitschool.cubegame.encounters;

import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.utils.AdvancedArray;

/**
 * Created by Voyager on 17.02.2018.
 */
public abstract class Encounter {

    public static final float FULL_TIME = 3f;
    private static AdvancedArray<Encounter> encounters = new AdvancedArray<Encounter>();
    private static Encounter nowPlaying;
    private Entity entity;
    private String text = "";
    private String name = "";
    private float time = 0f;

    public Encounter() {
        encounters.add(this);
    }

    public void trigger(Entity entity){
        setNowPlaying(this);
        this.entity = entity;
    }

    protected abstract void activate(Entity entity);

    public static void update(float delta) {
        if (nowPlaying != null) {
            nowPlaying.time += delta;
            if (nowPlaying.time >= FULL_TIME) {
                nowPlaying.activate(nowPlaying.entity);
                nowPlaying.entity = null;
                nowPlaying = null;
            }
        }
    }
    public static void initEncouters(){
        new DartEncounter();
    }

    public static Encounter getEncounter(int index){
        return encounters.get(index);
    }

    public static Encounter getRandomEncounter(){
        return encounters.getRandom();
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

    public static Encounter getNowPlaying() {
        return nowPlaying;
    }

    public static void setNowPlaying(Encounter nowPlaying) {
        if (Encounter.nowPlaying != null){
            Encounter.nowPlaying.activate(Encounter.nowPlaying.entity);
        }
        Encounter.nowPlaying = nowPlaying;
    }
}
