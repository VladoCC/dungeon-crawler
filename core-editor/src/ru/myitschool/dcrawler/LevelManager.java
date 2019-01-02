package ru.myitschool.dcrawler;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;

/**
 * Created by КАРАТ on 21.03.2017.
 */

/**
 *Это основной класс, который заведует всеми уровнями
 * система как в Unity со сценами: каждый Screen - "сцена"
 * небольшой TODO : сделать суперкласс для классов "сцены"
 *
 * Метод StartLevel запускает уровень, который ему скормили
 */
public class LevelManager extends Game implements ApplicationListener
{

    public static BitmapFont mainFont;

    @Override
    public void create()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("10771.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        //parameter.characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэяю1234567890-_/.,:!@#$%^&*()+[]=?<>{}";
        mainFont = generator.generateFont(parameter);
        startLev(new ru.myitschool.dcrawler.StartScreen());
    }

    public void startLev(Screen sc)
    {
        setScreen(sc);
    }

    static void setAlphaAll(ArrayList<Sprite> sl, float am)
    {
        for (Sprite s:sl)
        {
            s.setAlpha(am);
        }
    }
}
