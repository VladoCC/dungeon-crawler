package ru.myitschool.dcrawler.ui.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import ru.myitschool.dcrawler.MyGdxGame;

/**
 * Created by Voyager on 03.04.2018.
 */
public abstract class AdvancedScreen implements Screen {

    private InputMultiplexer input;

    public AdvancedScreen() {
        input = new InputMultiplexer();
        MyGdxGame.setScreenInput(input);
    }

    public InputMultiplexer getInput() {
        return input;
    }
}
