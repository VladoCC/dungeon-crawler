package ru.myitschool.dcrawler.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Logger;

import java.util.Random;

public class SeededRandom {

    private static long seed;
    private static final Random random;

    static {
        seed = new Random().nextLong();
        Gdx.app.debug("random seed", "" + seed);
        random = new Random(seed);
    }

    public static Random getInstance() {
        return getInstance(null);
    }

    public static Random getInstance(Long seed) {
        if (seed != null && !seed.equals(SeededRandom.seed)) {
            Gdx.app.debug("random seed", "" + seed);
            random.setSeed(seed);
        }
        return random;
    }

    public static long getSeed() {
        return seed;
    }
}
