package com.shopping.Screen;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

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


    private final int userSpeedX = 15;
    private final int userSpeedY = 15;

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
    Sprite itemSprite;

    private float halfWindowWidth = Gdx.graphics.getWidth()/2;
    private float halfWindowHeight = Gdx.graphics.getHeight()/2;
    private ArrayList<Item> lst = new ArrayList<Item>();
    private AssetManager manager = new AssetManager();

    public GameScreen(Game aGame,String Player, AssetManager mng) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        player = Player;
        batch = new SpriteBatch();
        manager = mng;
        map = new Image(manager.get("assets/map/map.png", Texture.class));
        System.out.println(map.getX()+" "+map.getY());
        stage.addActor(map);
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        camera.translate(startX,startY);
        counter = 0;
        startTime = Instant.now().toEpochMilli();

        itemSprite = new Sprite(manager.get("assets/pic/iron_chestplate.png", Texture.class));
        Pixmap pixmap = manager.get("assets/pic/icons8-center-of-gravity-64.png", Pixmap.class);
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        keyInProcess();
        moveCamera();
        stage.act();
        stage.draw();


        drawItems();
        drawMainPlayer();
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

    private void keyInProcess(){
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            currentX-=userSpeedX;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            currentX+=userSpeedX;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            currentY-=userSpeedY;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            currentY+=userSpeedY;
        }
        // 切換高倍鏡
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            if(minAltitude==1.7f)
                minAltitude = 2.5f;
            else
                minAltitude = 1.7f;
        }
    }
    private void drawItems(){
        batch.begin();
        //旋轉要除以縮放比例
        // 物品放置
        for(Item ele : lst) {
            float deltaItemX = (ele.posX - currentX) / minAltitude;
            float deltaItemY = (ele.posY - currentY) / minAltitude;
            if (ele.posX < currentX + halfWindowWidth * minAltitude && ele.posX > currentX - halfWindowWidth * minAltitude) {
                if (ele.posY < currentY + halfWindowHeight * minAltitude && ele.posY > currentY - halfWindowHeight * minAltitude) {
                    batch.draw(itemSprite, 800 + deltaItemX, 450 + deltaItemY, itemSprite.getOriginX() / minAltitude, itemSprite.getOriginY() / minAltitude, itemSprite.getHeight() / minAltitude, itemSprite.getWidth() / minAltitude, 1, 1, 0);
                }
            }
        }
        batch.end();
    }
    private void drawMainPlayer(){
        double deltaX = Gdx.input.getX()-800;
        double deltaY = 450 - Gdx.input.getY();
        float percentZ = Math.abs(percent - 0.5f)*2;
        currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ  ;
        // vector dot with (1,0)
        double dot = deltaX;
        double normalize = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        double ang = Math.acos(dot/normalize);
        double deg = 0;
        if(deltaY<0){
            deg = 360 - Math.toDegrees(ang);
        }
        else{
            deg = Math.toDegrees(ang);
        }

        sprite = new Sprite(manager.get("assets/map/navigation.png", Texture.class));
        //sprite.setPosition(-10,100);
        sprite.rotate((float)deg);
        batch.begin();
        BitmapFont font = new BitmapFont(Gdx.files.internal("assets/skin/craftacular/font-export.fnt"),Gdx.files.internal("assets/skin/craftacular/font-export.png"),false);
        font.draw(batch,player,halfWindowWidth,halfWindowHeight+100);
        //旋轉要除以縮放比例
        batch.draw(sprite,halfWindowWidth, halfWindowHeight,sprite.getOriginX()/minAltitude, sprite.getOriginY()/minAltitude, sprite.getHeight()/minAltitude, sprite.getWidth()/minAltitude,1,1,(float)deg);
        //Gdx.app.log("pos", String.valueOf(sprite.getWidth()/minAltitude) + " " + String.valueOf(sprite.getHeight()));
        sprite.draw(batch);
        batch.end();
    }

    class Item{
        public float posX;
        public float posY;
        Random rnd = new Random();
        public Item(){
            posX = rnd.nextInt(10000)+500;
            posY = rnd.nextInt(10000)+500;
        }
    }
}