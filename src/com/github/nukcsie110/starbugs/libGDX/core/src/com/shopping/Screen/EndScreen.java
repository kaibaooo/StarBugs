package com.shopping.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.assets.AssetManager;
import com.shopping.Base.InputProcessing;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class EndScreen implements Screen {
    private BitmapFont winDeadFont;// 字體
    private BitmapFont whiteFont;
    private BitmapFont lightGrayFont26;
    private SpriteBatch batch;
    private Texture texture;
    private Pixmap pixmap;
    private TextureRegion region;
    private int lastPlayerNumber;
    private int allPlayerNumber;
    private int kills;

//以下三個為測試值

    public EndScreen(Game aGame){

        lastPlayerNumber=4;
        allPlayerNumber=5;
        kills=1;

        winDeadFont = new BitmapFont(Gdx.files.internal("assets/font/winDeadFont.fnt"), Gdx.files.internal("assets/font/winDeadFont.png"), false);
        whiteFont = new BitmapFont(Gdx.files.internal("assets/font/whiteFont.fnt"), Gdx.files.internal("assets/font/whiteFont.png"), false);
        lightGrayFont26 = new BitmapFont(Gdx.files.internal("assets/font/lightGrayFont26.fnt"), Gdx.files.internal("assets/font/lightGrayFont26.png"), false);
    }
    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deadPrint();
    }
    @Override
    public void resize(int x, int y){

    }
    @Override
    public void pause(){

    }
    @Override
    public void resume(){

    }
    @Override
    public void hide(){

    }
    @Override
    public void dispose(){

    }
    private void deadPrint(){
        batch = new SpriteBatch();
        texture = new Texture(1600, 900, Format.RGBA8888);
        pixmap = new Pixmap(1600, 900, Format.RGBA8888);

        Color backGroundCover = new Color(0,0,0,0.8f);

        pixmap.setColor(backGroundCover);
        pixmap.fillRectangle(0, 0, 1600, 900);//画实心矩形.起点(x,y),(width,height)

        texture.draw(pixmap, 0, 0);
        region = new TextureRegion(texture, 1600, 900);

        batch.begin();
        batch.draw(region, 0, 0);

        whiteFont.draw(batch, "players name", 30, 850);
        winDeadFont.draw(batch, "BETTER LUCK NEXT TIME!", 30, 750);
        winDeadFont.draw(batch, "#"+lastPlayerNumber, 1420, 850);
        whiteFont.draw(batch, "/   "+allPlayerNumber, 1490, 850);

        whiteFont.draw(batch, "kills : "+kills, 30, 650);
        lightGrayFont26.draw(batch, "Press the Key that I Don't Know To Go Back To The Loading Page", 450, 250);

        batch.end();
    }
    private void winPrint(){
        batch = new SpriteBatch();
        texture = new Texture(1600, 900, Format.RGBA8888);
        pixmap = new Pixmap(1600, 900, Format.RGBA8888);

        Color backGroundCover = new Color(0,0,0,0.8f);

        pixmap.setColor(backGroundCover);
        pixmap.fillRectangle(0, 0, 1600, 900);//画实心矩形.起点(x,y),(width,height)

        texture.draw(pixmap, 0, 0);
        region = new TextureRegion(texture, 1600, 900);

        batch.begin();
        batch.draw(region, 0, 0);

        whiteFont.draw(batch, "players name", 30, 850);
        winDeadFont.draw(batch, "WINNER WINNER CHICKEN DINNER!", 30, 750);
        winDeadFont.draw(batch, "#"+lastPlayerNumber, 1420, 850);
        whiteFont.draw(batch, "/   "+allPlayerNumber, 1490, 850);

        whiteFont.draw(batch, "kills : "+kills, 30, 650);
        lightGrayFont26.draw(batch, "Press the Key that I Don't Know To Go Back To The Loading Page", 450, 250);

        batch.end();
    }
}