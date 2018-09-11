package ru.myitschool.dcrawler.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import ru.myitschool.dcrawler.effects.FloorEffect;
import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 02.04.2018.
 */
public class FinalScreen extends AdvancedScreen {

    private String text;
    private float time = 0;
    private Stage stage;

    public FinalScreen(String text) {
        this.text = text;
    }

    @Override
    public void show() {
        stage = new Stage();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pixelart.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label label = new Label(text, style);
        Container<Label> container = new Container<>(label);
        container.center();
        container.setPosition(0, 0);
        container.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(container);
        FloorEffect.clearEffects();
        for (Entity entity : Entity.getPlayingEntities()){
            entity.clearEffects();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        time += delta;
        if (time >= 3.5f){
            ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
