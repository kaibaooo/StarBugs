package com.shopping.Screen;

//import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

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

import java.util.TimerTask;
import java.util.Timer;
import java.util.Date;
//import com.shopping.Base.InputProcessing;

public class GameScreen implements Screen, InputProcessor{

    private Stage stage;
    private Stage mapItem;
    private Game game;
    private String player;
    private Image Basemap;
    private Image map;
    private Image mapOutline;
    private Image smallMap;
    private OrthographicCamera camera;
    private Sprite sprite;
    private SpriteBatch batch;
    private Sprite itemSprite;
    private Texture texture;
    private TextureRegion region;
    private Pixmap maps;
    private Pixmap pixmap;
    private Music click;
    private int R = 8000;
    private final float TIME_SINCE_COLLISION = 30;
    float timeSinceCollision = 0;

    // Player settings
    private final int userSpeedX = 15;
    private final int userSpeedY = 15;
    private final int startX = Gdx.graphics.getWidth()/2;// -Gdx.graphics.getWidth()/2;
    private final int startY = Gdx.graphics.getHeight()/2;
    private float minAltitude = 1.7f;
    private float maxAltitude = 10.5f;
    private float percent;
    private float counter;
    private float currentX = startX;
    private float currentY = startY;
    private float percentZ = Math.abs(percent - 0.5f)*2;
    private float currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ  ;
    private float isAttackingState = 0;
    private int armorType = 0; // 0 none 1 iron 2 gold 3 diamond

    // general attributes
    private float halfWindowWidth = Gdx.graphics.getWidth()/2;
    private float halfWindowHeight = Gdx.graphics.getHeight()/2;
    private ArrayList<Item> lst = new ArrayList<Item>();
    private AssetManager manager = new AssetManager();

    //inventory
    private int[] inventory = {1,1,1,3};
    private int inventoryChoose = 0;
    Sprite inventory65;
    Sprite sword;
    Sprite bow;
    Sprite potion;
    Sprite potion2;
    Sprite potion3;
    Sprite choose;
    Sprite character;
    // blood
    private int blood;
    private TextureRegion bloodRegion;
    //sound
    Music music;

    BitmapFont font;
    public GameScreen(Game aGame,String Player, AssetManager mng) {

        game = aGame;
        stage = new Stage(new ScreenViewport());
        mapItem = new Stage(new ScreenViewport());
        player = Player;
        batch = new SpriteBatch();
        manager = mng;
        maps = new Pixmap(Gdx.files.internal("assets/map/map.png"));
        Basemap = new Image(manager.get("assets/map/Lava_map.png",Texture.class));
        map = new Image(new Texture(roundPixmap(maps,R)));
        map.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Basemap.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        System.out.println(map.getX()+" "+map.getY());
        stage.addActor(Basemap);
        stage.addActor(map);
        //change map
        smallMap = new Image(new Texture(roundPixmap(maps,R)));
        mapOutline = new Image(manager.get("assets/map/smallMap.png",Texture.class));
        smallMap.setPosition(Gdx.graphics.getWidth()-mapOutline.getWidth()/2-13,Gdx.graphics.getHeight()/3-290);
        smallMap.setSize(253,253);
        mapOutline.setPosition(Gdx.graphics.getWidth()-mapOutline.getWidth()/2-23,Gdx.graphics.getHeight()/3-300);
        mapOutline.setSize(274,275);
        mapItem.addActor(mapOutline);
        mapItem.addActor(smallMap);
        System.out.println(map.getX()+" "+map.getY());
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        camera.translate(startX,startY);
        counter = 0;
//        inputProcessor = new InputProcessing();
        //startTime = Instant.now().toEpochMilli();
        click = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/ButtonSoundEffects.mp3"));

        itemSprite = new Sprite(manager.get("assets/pic/iron_chestplate.png", Texture.class));
        Pixmap pixmap = manager.get("assets/pic/icons8-center-of-gravity-64.png", Pixmap.class);
        int xHotspot = pixmap.getWidth() / 2;
        int yHotspot = pixmap.getHeight() / 2;
        Cursor cursor = Gdx.graphics.newCursor(pixmap, xHotspot, yHotspot);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        // Inventory pre load
        inventory65 = new Sprite(manager.get("assets/inventory/inventory65.png", Texture.class));
        sword = new Sprite(manager.get("assets/inventory/sword.png", Texture.class));
        bow = new Sprite(manager.get("assets/inventory/bow.gif", Texture.class));
        potion = new Sprite(manager.get("assets/inventory/potion.png", Texture.class));
        potion2 = new Sprite(manager.get("assets/inventory/potion2.png", Texture.class));
        potion3 = new Sprite(manager.get("assets/inventory/potion3.png", Texture.class));
        choose = new Sprite(manager.get("assets/inventory/choose.png", Texture.class));

        // blood
        blood = 50;
        texture = new Texture(510, 40, Pixmap.Format.RGBA8888);
        pixmap = new Pixmap(510, 40, Pixmap.Format.RGBA8888);
        // character
        character = new Sprite(manager.get("assets/pic/CharacterCat.png", Texture.class));
        //sound
        music = manager.get("assets/sound/LOL_inGame.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
        
        font = new BitmapFont(Gdx.files.internal("assets/skin/craftacular/font-export.fnt"),Gdx.files.internal("assets/skin/craftacular/font-export.png"),false);

    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
//        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        moveCamera();
        mapItem.act();
        stage.act();
        stage.draw();

        drawBlood();
        inventory();
        drawItemsTest();
        keyInProcess();
        keyInProcessDebug();
        drawMainPlayer();
        mapItem.draw();
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
        mapItem.dispose();
    }

    /*public void update(float delta) {
        timeSinceCollision += delta;
        if (timeSinceCollision >= TIME_SINCE_COLLISION) {
            for(int i=0;i<timeSinceCollision;i++){
                R = R-2000;
                map = new Image(new Texture(roundPixmap(maps,R)));
                smallMap = new Image(new Texture(roundPixmap(maps,R)));
                mapItem.addActor(mapOutline);
                mapItem.addActor(smallMap);
                System.out.println("Mappppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppps");
            }
            timeSinceCollision -= TIME_SINCE_COLLISION;
        }
    }*/

    public static Pixmap roundPixmap(Pixmap pixmap,double r)
    {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        Pixmap round = new Pixmap(width,height,Pixmap.Format.RGBA8888);
        if(width != height)
        {
            Gdx.app.log("error", "Cannot create round image if width != height");
            round.dispose();
            return pixmap;
        }
        double radius = width/2;
        for(int y=0;y<height;y++)
        {
            for(int x=0;x<width;x++)
            {
                //check if pixel is outside circle. Set pixel to transparant;
                double dist_x = x-radius;
                double dist_y = y-radius;
                double dist = Math.sqrt((dist_x*dist_x) + (dist_y*dist_y));
                if (dist <= r) {
                    round.drawPixel(x, y, pixmap.getPixel(x, y));
                }
                else {
                    round.drawPixel(x, y, 0);
                }
            }
        }
        Gdx.app.log("info", "pixmal rounded!");
        return round;
    }

    private void keyInProcessDebug(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)){
            blood -= 30;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)){
            armorType = 0;
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/CharacterCat.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/Sword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)){
            armorType = 1;
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/IronArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/IronSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)){
            armorType = 2;
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/GoldenArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/GoldenSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);

            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)){
            armorType = 3;
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/DiamondArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/DiamondSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            if(inventory[3] < 3) {
                inventory[3]++;
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
            Gdx.app.log("LogStart", "=========================================");
            Gdx.app.log("UserPosition", currentX + ", " + currentY);
            Gdx.app.log("Blood", String.valueOf(blood));
            Gdx.app.log("ArmorType", String.valueOf(armorType));
            Gdx.app.log("inventoryChoose", String.valueOf(inventoryChoose));
            Gdx.app.log("LogEnd", "=========================================");
        }
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

    private void drawItemsTest(){
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
            deg = 360 - Math.toDegrees(ang) + 90;
        }
        else{
            deg = Math.toDegrees(ang) + 90;
        }

        //sprite.setPosition(-10,100);
        character.rotate((float)deg);
        batch.begin();
        //旋轉要除以縮放比例
        choosePlayerTexture();
        Gdx.app.log("SIZE", String.valueOf(character.getWidth()) + ", " +String.valueOf(character.getHeight()));
        batch.draw(character,halfWindowWidth, halfWindowHeight,character.getOriginX()/minAltitude, character.getOriginY()/minAltitude, character.getWidth()/minAltitude, character.getHeight()/minAltitude,1,1,(float)deg);
        font.draw(batch,player,halfWindowWidth,halfWindowHeight+100);
        batch.end();
    }
    private void inventory(){

        inventory65.setSize(88, 260);
        batch.begin();
        inventory65.setPosition(Gdx.graphics.getWidth()-120, Gdx.graphics.getHeight()/3);
        inventory65.draw(batch);
        batch.end();
        if(inventory[1]==1){
            sword.setSize(43, 44);
            sword.setPosition(Gdx.graphics.getWidth()-95, 436);
            batch.begin();
            sword.draw(batch);
            batch.end();
        }
        if(inventory[2]==1){
            bow.setSize(44, 45);
            bow.setPosition(Gdx.graphics.getWidth()-96, 382);
            batch.begin();
            bow.draw(batch);
            batch.end();
        }
        if(inventory[3]==1){
            potion.setSize(54, 54);
            batch.begin();
            potion.setPosition(Gdx.graphics.getWidth()-100, Gdx.graphics.getHeight()/3+23);
            potion.draw(batch);
            batch.end();
        }
        else if(inventory[3]==2){
            potion2.setSize(54, 54);
            batch.begin();
            potion2.setPosition(Gdx.graphics.getWidth()-101, Gdx.graphics.getHeight()/3+23);
            potion2.draw(batch);
            batch.end();
        }
        else if(inventory[3]==3){
            potion3.setSize(48, 52);
            batch.begin();
            potion3.setPosition(Gdx.graphics.getWidth()-100, Gdx.graphics.getHeight()/3+23);
            potion3.draw(batch);
            batch.end();
        }
        if(inventoryChoose==0){
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth()-98, 490);
            batch.begin();
            choose.draw(batch);
            batch.end();
        }
        else if(inventoryChoose==1){
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth()-98, 436);
            batch.begin();
            choose.draw(batch);
            batch.end();
        }
        else if(inventoryChoose==2){
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth()-98, 381);
            batch.begin();
            choose.draw(batch);
            batch.end();
        }

    }
    private void drawBlood(){
        texture = new Texture(510, 40, Pixmap.Format.RGBA8888);
        pixmap = new Pixmap(510, 40, Pixmap.Format.RGBA8888);
        Color bloodColor = new Color(61,0,0,0.8f);
        Color bloodBlockFill = new Color(183,183,183,0.5f);
        Color borderColor = new Color(255,255,255,0.8f);
        pixmap.setColor(bloodBlockFill);
        pixmap.fillRectangle(5, 5, 500, 30);
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(0, 0, 510, 40);//画空心矩形.起点(x,y),(width,height)
        pixmap.drawRectangle(1, 1, 508, 38);//画空心矩形.起点(x,y),(width,height)
        pixmap.drawRectangle(2, 2, 506, 36);//画空心矩形.起点(x,y),(width,height)
        pixmap.drawRectangle(3, 3, 504, 34);//画空心矩形.起点(x,y),(width,height)
        pixmap.drawRectangle(4, 4, 502, 32);//画空心矩形.起点(x,y),(width,height)
        pixmap.setColor(bloodColor);
        pixmap.fillRectangle(5, 5, blood*5, 30);//画实心矩形.起点(x,y),(width,height)
        texture.draw(pixmap, 0, 0);//在texture中套一个pixmap图层
        bloodRegion = new TextureRegion(texture, 510, 40);

        batch.begin();
        batch.draw(bloodRegion, 545, 50);
        batch.end();
        texture.dispose();
        pixmap.dispose();
    }
    private void choosePlayerTexture(){
        if(armorType == 0 && isAttackingState == 0){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/CharacterCat.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/Sword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 1 && isAttackingState == 0){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/IronArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/IronSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 2 && isAttackingState == 0){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/GoldenArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/GoldenSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);

            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 3 && isAttackingState == 0){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/DiamondArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/DiamondSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }

        if(armorType == 0 && isAttackingState == 1){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/attack(left).png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/fistSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 1 && isAttackingState == 1){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/Ironattack(left).png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/IronSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 2 && isAttackingState == 1){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/Goldenattack(left).png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/GoldenSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);

            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
        else if(armorType == 3 && isAttackingState == 1){
            if(inventoryChoose == 0){
                Texture current = manager.get("assets/pic/Diamondattack(left).png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 1){
                Texture current = manager.get("assets/pic/DiamondSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
            else if(inventoryChoose == 2){
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth()/2, current.getHeight()/2);
                character.setTexture(current);
            }
        }
    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.NUM_1 && inventory[1] > 0){
            inventoryChoose = 0;
            click.play();
        }
        if(keycode == Input.Keys.NUM_2 && inventory[1] > 0){
            inventoryChoose = 1;
            click.play();
        }
        if(keycode == Input.Keys.NUM_3 && inventory[2] > 0){
            inventoryChoose = 2;
            click.play();
        }
        if(keycode == Input.Keys.NUM_4 && inventory[3] > 0){
            click.play();
            inventory[3]--;
            blood = (100-blood)/2+blood;
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
        if(button == Input.Buttons.LEFT) {

            isAttackingState = 1;
            Timer timer = new Timer();
            timer.schedule(new attackDelay(), 100);
            Music effect = manager.get("assets/sound/punch.mp3", Music.class);
            effect.play();
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            isAttackingState = 0;
            return true;
        }
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
    class Item{
        public float posX;
        public float posY;
        Random rnd = new Random();
        public Item(){
            posX = rnd.nextInt(10000)+500;
            posY = rnd.nextInt(10000)+500;
        }
    }
    class attackDelay extends TimerTask {
        public void run() {
            isAttackingState = 0;

        }
    }

}