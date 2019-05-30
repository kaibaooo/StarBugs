package com.shopping.Screen;

import java.time.Instant;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.assets.AssetManager;

public class GameScreen implements Screen {

    private Stage stage;
    private Game game;
    private String player;
    private Image map;
    private OrthographicCamera camera;
    private Sprite sprite;
    private SpriteBatch batch;

    private final int startX = Gdx.graphics.getWidth()/2;// -Gdx.graphics.getWidth()/2;
    private final int startY = Gdx.graphics.getHeight()/2;

    private final int endX = 9000;// -Gdx.graphics.getWidth()/2;
    private final int endY = 9000;

    private float minAltitude = 1.7f;
    private float maxAltitude = 10.5f;

    private float percent;
    private float counter;
    private long startTime;
    private final float animation_duration = 15000;

    private float currentX = startX;
    private float currentY = startY;
    private float percentZ = Math.abs(percent - 0.5f)*2;
    private float currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ  ;
    public GameScreen(Game aGame,String Player, AssetManager manager) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        player = Player;
        map = new Image(new Texture("assets/map/map.png"));
        System.out.println(map.getX()+" "+map.getY());
        stage.addActor(map);
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        camera.translate(startX,startY);
        counter = 0;
        startTime = Instant.now().toEpochMilli();

        Pixmap pixmap = new Pixmap(Gdx.files.internal("assets/pic/icons8-center-of-gravity-64.png"));
        int xHotspot = pixmap.getWidth() / 2;
        int yHotspot = pixmap.getHeight() / 2;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void render(float delta) {
        float width=Gdx.graphics.getWidth()/2;
        float height=Gdx.graphics.getHeight()/2;
        double deltaX = Gdx.input.getX()-width;
        double deltaY = height - Gdx.input.getY();
        // dot with (1,0)
        double dot = deltaX;
        double normalize = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        double ang = Math.acos(dot/normalize);
        double deg = 0;
        //Gdx.app.log("mousePos", String.valueOf(deltaX) + " " + String.valueOf(deltaY));
        if(deltaY<0){
            deg = 360 - Math.toDegrees(ang);
            Gdx.app.log("angle between mouse", String.valueOf(360 - Math.toDegrees(ang)));
        }
        else{
            deg = Math.toDegrees(ang);
            Gdx.app.log("angle between mouse", String.valueOf(Math.toDegrees(ang)));
        }


        // FPS
        Gdx.app.log("render FPS",String.valueOf(Gdx.graphics.getFramesPerSecond()));

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                currentX-=20;
            else
                currentX-=20;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT))
                currentX+=20;
            else
                currentX+=20;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
                currentY-=20;
            else
                currentY-=20;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            if(Gdx.input.isKeyPressed(Input.Keys.UP))
                currentY+=20;
            else
                currentY+=20;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        moveCamera();
        stage.act();
        stage.draw();

        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/skin/craftacular/font-export.fnt"),Gdx.files.internal("assets/skin/craftacular/font-export.png"),false);
        sprite = new Sprite(new Texture(Gdx.files.internal("assets/map/navigation.png")));
        batch = new SpriteBatch();
        batch.begin();
        font.draw(batch,player,width,height+100);
        sprite.setPosition(width, height);
        //float deltaAngle = Gdx.input.getX();
        sprite.rotate((float)deg);
        sprite.draw(batch);
        batch.end();
    }

    private void moveCamera(){
        camera.position.x = currentX;
        camera.position.y = currentY;
        camera.zoom = currentZ;
        camera.update();
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