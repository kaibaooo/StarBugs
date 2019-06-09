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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.assets.AssetManager;
import com.shopping.Base.InputProcessing;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.shopping.Actors.Background;
import java.util.Timer;

public class EndScreen implements Screen, InputProcessor {
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
    private int showScreenType;
    private Stage stage;
    private OrthographicCamera camera;
//以下三個為測試值

    public EndScreen(Game aGame, int type){
        showScreenType = type;
        lastPlayerNumber=4;
        allPlayerNumber=5;
        kills=1;

        stage = new Stage(new ScreenViewport());
        camera = (OrthographicCamera) stage.getViewport().getCamera();

        Pixmap pixmap = new Pixmap(Gdx.files.internal("assets/pic/icons8-center-of-gravity-64.png"));
        int xHotspot = pixmap.getWidth()/2;
        int yHotspot = pixmap.getHeight()/2;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=6;i++){
            if(i==2||i==3) continue;
            textures.add(new Texture(Gdx.files.internal("assets/pic/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        Background background = new Background(textures);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setSpeed((float)0.2);
        stage.addActor(background);

        final Skin fontSkin = new Skin(Gdx.files.internal("assets/skin/craftacular/craftacular-ui.json"));




        winDeadFont = new BitmapFont(Gdx.files.internal("assets/font/winDeadFont.fnt"), Gdx.files.internal("assets/font/winDeadFont.png"), false);
        whiteFont = new BitmapFont(Gdx.files.internal("assets/font/whiteFont.fnt"), Gdx.files.internal("assets/font/whiteFont.png"), false);
        lightGrayFont26 = new BitmapFont(Gdx.files.internal("assets/font/lightGrayFont26.fnt"), Gdx.files.internal("assets/font/lightGrayFont26.png"), false);
    }
    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        if(showScreenType == 0){
            deadPrint();
        }
        else{
            winPrint();
        }


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
        lightGrayFont26.draw(batch, "Press Esc to end the game!", 450, 250);

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
        lightGrayFont26.draw(batch, "Press Esc to end the game! ", 450, 250);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.ESCAPE){
            Gdx.app.log("KEY", "ESC");
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}