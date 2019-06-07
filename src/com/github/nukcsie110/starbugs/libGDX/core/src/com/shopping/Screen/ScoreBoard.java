package com.shopping.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shopping.Actors.Background;

public class ScoreBoard implements Screen {
    private Stage stage;
    private Game game;

    public ScoreBoard(Game aGame){
        game = aGame;
        stage = new Stage(new ScreenViewport());

        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("assets/pic/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        Background background = new Background(textures);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setSpeed((float)0.5);
        stage.addActor(background);

        //icoc image
        Texture texture = new Texture(Gdx.files.absolute("assets/pic/earth.png"));
        Image icon = new Image(texture);
        icon.setPosition(Gdx.graphics.getWidth()/3-195,Gdx.graphics.getHeight()/2+100);
        stage.addActor(icon);

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
