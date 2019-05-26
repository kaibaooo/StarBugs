package com.shopping.Base;

import com.badlogic.gdx.Game;
import com.shopping.Screen.TitleScreen;

public class Base extends Game{
    @Override
    public void create () {
        this.setScreen(new TitleScreen(this));
    }

    @Override
    public void render () {
        super.render();
    }
}