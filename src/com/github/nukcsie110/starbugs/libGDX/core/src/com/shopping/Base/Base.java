package com.shopping.Base;

import com.badlogic.gdx.Game;
import com.shopping.Screen.TitleScreen;
import com.shopping.Screen.LoadToGameScreen;

import java.util.TooManyListenersException;

public class Base extends Game{
    @Override
    public void create () {
        this.setScreen(new LoadToGameScreen(this,"Valorlo"));
    }

    @Override
    public void render () {
        super.render();
    }
}
