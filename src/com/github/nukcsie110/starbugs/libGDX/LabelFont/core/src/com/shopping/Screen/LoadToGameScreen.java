package com.shopping.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoadToGameScreen implements Screen {
    private Stage stage;
    private Game game;
    public AssetManager manager = new AssetManager();
    SpriteBatch batch = new SpriteBatch();
    String user_name;
    public LoadToGameScreen(Game aGame, String name){
        game = aGame;
        stage = new Stage(new ScreenViewport());
        manager.load("assets/pic/iron_chestplate.png", Texture.class);
        manager.load("assets/map/map.png", Texture.class);
        manager.load("assets/pic/icons8-center-of-gravity-64.png", Pixmap.class);
        manager.load("assets/map/navigation.png", Texture.class);
        user_name = name;
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        Sprite cat = new Sprite(new Texture(Gdx.files.internal("assets/pic/cat3.jpg")));
        batch.begin();
        //旋轉要除以縮放比例
        batch.draw(cat,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        cat.draw(batch);
        batch.end();
        if(manager.update()) {
            game.setScreen(new GameScreen(game,user_name,manager));
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
        stage.dispose();
    }

}
