package com.shopping.Screen;
//Online version
//import java.time.Instant;
import java.util.*;

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.assets.AssetManager;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.client.Client;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.util.Logger;
import com.shopping.Base.InputProcessing;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.badlogic.gdx.controllers.*;
import com.shopping.Base.GameJudger;

import java.util.ArrayList;
//import com.shopping.Base.InputProcessing;

public class GameScreen implements Screen, InputProcessor, ControllerListener {
    // Pre declare object
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
    private Texture timerTexture;
    private Pixmap timerPixmap;
    private GameJudger judge;
    private Timer timer;
    private AssetManager manager = new AssetManager();
    // controller
    boolean hasControllers;
    private int R = 8000;
    private final float TIME_SINCE_COLLISION = 30;
    float timeSinceCollision = 0;

    // Player settings
    private User mainPlayer;
    private User enemy;
    private final int userSpeedX = 15;
    private final int userSpeedY = 15;
    private final int startX = 1000;// -Gdx.graphics.getWidth()/2;
    private final int startY = 1000;
    private float minAltitude = 1.7f;
    private float maxAltitude = 10.5f;
    private float percent;
    private float counter;
    private float currentX = startX;
    private float currentY = startY;
    private float percentZ = Math.abs(percent - 0.5f) * 2;
    private float currentZ = maxAltitude - (maxAltitude - minAltitude) * percentZ;
    private float isAttackingState = 0;
    private int armorType = 0; // 0 none 1 iron 2 gold 3 diamond
    private int attackHand;
    private double deg;
    private double oldDeg;

    // general attributes
    private float halfWindowWidth = Gdx.graphics.getWidth() / 2;
    private float halfWindowHeight = Gdx.graphics.getHeight() / 2;
    private ArrayList<Item> lst = new ArrayList<Item>();
    private HashMap<Short, User> onlineUsers = new HashMap<Short, User>();

    private float timeSeconds = 0f;
    private float period = 1f;
    private int passTime = 0;

    // inventory
    private int[] inventory = { 1, 1, 1, 3 };
    private int inventoryChoose = 0;
    Sprite inventory65;
    Sprite sword;
    Sprite bow;
    Sprite potion;
    Sprite potion2;
    Sprite potion3;
    Sprite choose;
    Sprite character;
    Sprite bulletPicture;
    Sprite creeper;
    // blood
    private int blood;
    private Pixmap bloodPix;
    private TextureRegion bloodRegion;
    private Color bloodColor;
    private Color bloodBlockFill;
    private Color borderColor;
    // bullet
    Bullet bullet, currentBullet;
    ArrayList<Bullet> bulletManager = new ArrayList<Bullet>();
    // sound
    Music music;
    // timer
    Color timerBlockCover;
    Color timerBlockMargin;
    BitmapFont font;
    private BitmapFont lightGrayFont26;

    //IDK
    int tmpX = 0;
    int tmpY = 0;

    //Networking
    Client client;
    String playerID;
    ArrayList<User> nameTable;

    public GameScreen(Game aGame,
                      String Player,
                      AssetManager mng,
                      Client passedClient,
                      String pID,
                      ArrayList<User> passedNameTable) {
        // get input arguement
        game = aGame;
        manager = mng;

        // new default attributes
        stage = new Stage(new ScreenViewport());
        mapItem = new Stage(new ScreenViewport());
        judge = new GameJudger();
        batch = new SpriteBatch();
        mainPlayer = new User();
        timer = new Timer();
        timer.schedule(new updateDirSchedule(), 500, 100);
        // Networking
        client = passedClient;
        player = Player;
        playerID = pID;
        nameTable = passedNameTable;

        for(User ele:nameTable){

            onlineUsers.put(ele.getID(),new User(ele.getID(), ele.getName(), new Coordinate(0,0,0)));
        }
        for(int i = 0;i<5;i++){
            User tmp  = new User(i,"a"+i,new Coordinate((float)(Math.random()*1000+1000),(float)(Math.random()*1000+1000),0));
            onlineUsers.put((short)i, tmp);
        }

        // camera
        camera = (OrthographicCamera) stage.getViewport().getCamera();
        camera.translate(startX, startY);


        // Load files
        counter = 0;
        click = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/ButtonSoundEffects.mp3"));
        itemSprite = new Sprite(manager.get("assets/pic/iron_chestplate.png", Texture.class));
        map = new Image(manager.get("assets/map/map.png", Texture.class));
        stage.addActor(map);
        map.setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        Pixmap pixmap = manager.get("assets/pic/icons8-center-of-gravity-64.png", Pixmap.class);
        int xHotspot = pixmap.getWidth() / 2;
        int yHotspot = pixmap.getHeight() / 2;

        // Cursor setup
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

        // Blood
        blood = 50;
        bloodColor = new Color(61, 0, 0, 0.8f);
        bloodBlockFill = new Color(183, 183, 183, 0.5f);
        borderColor = new Color(255, 255, 255, 0.8f);

        // Character
        character = new Sprite(manager.get("assets/pic/CharacterCat.png", Texture.class));
        creeper = new Sprite(manager.get("assets/pic/big-creeper-face.png", Texture.class));
        attackHand = 0;

        //Bullet
        bulletPicture = new Sprite(manager.get("assets/inventory/arrow.png", Texture.class));
        bulletPicture.setSize(50,50);
        bulletPicture.setOrigin(25,25);

        // sound
        music = manager.get("assets/sound/LOL_inGame.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        // timer
        timerTexture = new Texture(150, 50, Format.RGBA8888);
        timerPixmap = new Pixmap(150, 50, Format.RGBA8888);
        lightGrayFont26 = new BitmapFont(Gdx.files.internal("assets/font/lightGrayFont26.fnt"),
                Gdx.files.internal("assets/font/lightGrayFont26.png"), false);
        region = new TextureRegion();
        timerBlockCover = new Color(0, 0, 0, 0.7f);
        timerBlockMargin = new Color(255, 255, 255, 0.85f);
        font = new BitmapFont(Gdx.files.internal("assets/skin/craftacular/font-export.fnt"),
                Gdx.files.internal("assets/skin/craftacular/font-export.png"), false);

        //Controller
        hasControllers = false;
        Controllers.addListener(this);

        if(Controllers.getControllers().size == 0)
        {
            hasControllers = false;
        }

        // random test item
        for(int i = 0;i<100;i++){
            Coordinate testPos = new Coordinate((float)(Math.random() * 10000 + 600), (float)(Math.random() * 10000 + 1000), 1);
            Item tmp = new Item((byte)i, testPos);
            lst.add(tmp);
        }

    }

    public void networkUpdate(){
        Union ops;
        if(client.isReadable()){
            ops = client.read();
            switch(ops.pkID){
                case 0x01:
                    Logger.log("Recived joinReply");
                    if(ops.state==0){
                        Logger.log("My ID is: "+ops.player.getIDString());
                    }else{
                        Logger.log("Failed to join. Abort.");
                    }
                    break;
                case 0x02:
                    Logger.log("Recived updateNameTable");
                    for(User i:ops.nameTable){
                        Logger.log(i.getDisplayName());
                    }
                    break;
                case 0x04:
                    enemy = ops.player;
                    onlineUsers.get(enemy.getID()).getPos().setPosX(enemy.getPos().getPosX());
                    onlineUsers.get(enemy.getID()).getPos().setPosY(enemy.getPos().getPosY());
                    onlineUsers.get(enemy.getID()).getPos().setDir(enemy.getPos().getDir());
                    break;
                case 0x05:
                    mainPlayer = ops.player;
                    break;
                case 0x10:
                    Logger.log("Game over");
                    client.close();
                default:
            }
        }
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen", "show");
        // Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        //======================================
        networkUpdate();
        //======================================

        currentX = mainPlayer.getPos().getPosX();
        currentY = mainPlayer.getPos().getPosY();
        deg = mainPlayer.getPos().getDir();

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
        drawEnemy();
        showTimer();
        drawMainPlayer();

        mapItem.draw();
        if (blood <= 0) {
            stage.dispose();
            game.setScreen(new EndScreen(game, 1));
            music.stop();
        }



        if(bulletManager.size()!=0)    drawBullet();

        timeSeconds += Gdx.graphics.getRawDeltaTime();
        if (timeSeconds > period) {
            timeSeconds -= period;
            passTime++;
        }


        //controller
        if(tmpX == 15){
            currentX+=tmpX;
        }
        else if(tmpX == -15){
            currentX+=tmpX;
        }
        if(tmpY == 15){
            currentY+=tmpY;
        }
        else{
            currentY+=tmpY;
        }
    }

    private void moveCamera() {
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



    private void keyInProcessDebug() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            blood -= 30;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
            armorType = 0;
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/CharacterCat.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/Sword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
            armorType = 1;
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/IronArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/IronSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) {
            armorType = 2;
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/GoldenArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/GoldenSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);

            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) {
            armorType = 3;
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/DiamondArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/DiamondSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (inventory[3] < 3) {
                inventory[3]++;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            Gdx.app.log("LogStart", "=========================================");
            Gdx.app.log("UserPosition", currentX + ", " + currentY);
            Gdx.app.log("Blood", String.valueOf(blood));
            Gdx.app.log("ArmorType", String.valueOf(armorType));
            Gdx.app.log("inventoryChoose", String.valueOf(inventoryChoose));
            Gdx.app.log("LogEnd", "=========================================");
        }
    }

    private void keyInProcess() {

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if(judge.judgeUserMoveIllegal(currentX-userSpeedX, currentY)){
                client.keyDown((byte)'A');
                currentX -= userSpeedX;
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if(judge.judgeUserMoveIllegal(currentX+userSpeedX, currentY)){
                client.keyDown((byte)'D');
                currentX += userSpeedX;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if(judge.judgeUserMoveIllegal(currentX, currentY-userSpeedY)){
                client.keyDown((byte)'S');
                currentY -= userSpeedY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if(judge.judgeUserMoveIllegal(currentX, currentY+userSpeedY)){
                client.keyDown((byte)'W');
                currentY += userSpeedY;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            client.keyDown((byte)'E');
        }


        // 切換高倍鏡
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            client.keyDown((byte)'S');
            if (minAltitude == 1.7f)
                minAltitude = 2.5f;
            else
                minAltitude = 1.7f;
        }
    }

    private void drawItemsTest() {
        batch.begin();
        // 旋轉要除以縮放比例
//        // 物品放置
//        for (Item ele : lst) {
//            float deltaItemX = (ele.coordinate.getPosX() - currentX) / minAltitude;
//            float deltaItemY = (ele.coordinate.getPosY() - currentY) / minAltitude;
//            if (ele.coordinate.getPosX()-100 < currentX + halfWindowWidth * minAltitude
//                    && ele.coordinate.getPosX()+100 > currentX - halfWindowWidth * minAltitude) {
//                if (ele.coordinate.getPosY()-100 < currentY + halfWindowHeight * minAltitude
//                        && ele.coordinate.getPosY()+100 > currentY - halfWindowHeight * minAltitude) {
//                    batch.draw(itemSprite, 800 + deltaItemX, 450 + deltaItemY, itemSprite.getOriginX() / minAltitude,
//                            itemSprite.getOriginY() / minAltitude, itemSprite.getHeight() / minAltitude,
//                            itemSprite.getWidth() / minAltitude, 1, 1, 0);
//                }
//            }
//        }

        for (short key : onlineUsers.keySet()) {
            float deltaItemX = (onlineUsers.get(key).getPos().getPosX() - currentX) / minAltitude;
            float deltaItemY = (onlineUsers.get(key).getPos().getPosY() - currentY) / minAltitude;
            if (onlineUsers.get(key).getPos().getPosX()-100 < currentX + halfWindowWidth * minAltitude
                    && onlineUsers.get(key).getPos().getPosX()+100 > currentX - halfWindowWidth * minAltitude) {
                if (onlineUsers.get(key).getPos().getPosY() - 100 < currentY + halfWindowHeight * minAltitude
                        && onlineUsers.get(key).getPos().getPosY() + 100 > currentY - halfWindowHeight * minAltitude) {
                    batch.draw(creeper, 800 + deltaItemX, 450 + deltaItemY, creeper.getOriginX() / minAltitude,
                            creeper.getOriginY() / minAltitude, creeper.getHeight() / minAltitude,
                            creeper.getWidth() / minAltitude, 0.3f, 0.3f, 0);
                }
            }
        }

        batch.end();
    }

    private void drawMainPlayer() {
//        oldDeg = deg;
        double deltaX = Gdx.input.getX() - 800;
        double deltaY = 450 - Gdx.input.getY();
        float percentZ = Math.abs(percent - 0.5f) * 2;
        currentZ = maxAltitude - (maxAltitude - minAltitude) * percentZ;
        // vector dot with (1,0)
        double dot = deltaX;
        double normalize = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double ang = Math.acos(dot / normalize);
        //double deg = 0;
        if (deltaY < 0) {
            deg = 360 - Math.toDegrees(ang) + 90;
        } else {
            deg = Math.toDegrees(ang) + 90;
        }

        //predict
        oldDeg = deg;

        character.rotate((float) deg);
        batch.begin();
        choosePlayerTexture();
        batch.draw(character, halfWindowWidth, halfWindowHeight, character.getOriginX() / minAltitude,
                character.getOriginY() / minAltitude, character.getWidth() / minAltitude,
                character.getHeight() / minAltitude, 1, 1, (float) deg);
        font.draw(batch, player, halfWindowWidth, halfWindowHeight + 100);
        batch.end();
    }

    private void drawEnemy() {
        batch.begin();
//        choosePlayerTexture();


        batch.end();
    }

    private void inventory() {

        inventory65.setSize(88, 260);
        batch.begin();
        inventory65.setPosition(Gdx.graphics.getWidth() - 120, Gdx.graphics.getHeight() / 3);
        inventory65.draw(batch);
        batch.end();
        if (inventory[1] == 1) {
            sword.setSize(43, 44);
            sword.setPosition(Gdx.graphics.getWidth() - 95, 436);
            batch.begin();
            sword.draw(batch);
            batch.end();
        }
        if (inventory[2] == 1) {
            bow.setSize(44, 45);
            bow.setPosition(Gdx.graphics.getWidth() - 96, 382);
            batch.begin();
            bow.draw(batch);
            batch.end();
        }
        if (inventory[3] == 1) {
            potion.setSize(54, 54);
            batch.begin();
            potion.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() / 3 + 23);
            potion.draw(batch);
            batch.end();
        } else if (inventory[3] == 2) {
            potion2.setSize(54, 54);
            batch.begin();
            potion2.setPosition(Gdx.graphics.getWidth() - 101, Gdx.graphics.getHeight() / 3 + 23);
            potion2.draw(batch);
            batch.end();
        } else if (inventory[3] == 3) {
            potion3.setSize(48, 52);
            batch.begin();
            potion3.setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() / 3 + 23);
            potion3.draw(batch);
            batch.end();
        }
        if (inventoryChoose == 0) {
            minAltitude = 1.7f;
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth() - 98, 490);
            batch.begin();
            choose.draw(batch);
            batch.end();
        } else if (inventoryChoose == 1) {
            minAltitude = 1.7f;
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth() - 98, 436);
            batch.begin();
            choose.draw(batch);
            batch.end();
        } else if (inventoryChoose == 2) {
            choose.setSize(46, 47);
            choose.setPosition(Gdx.graphics.getWidth() - 98, 381);
            batch.begin();
            choose.draw(batch);
            batch.end();
        }

    }

    private void drawBlood() {
        texture = new Texture(510, 40, Pixmap.Format.RGBA8888);
        bloodPix = new Pixmap(510, 40, Pixmap.Format.RGBA8888);
        bloodPix.setColor(bloodBlockFill);
        bloodPix.fillRectangle(5, 5, 500, 30);
        bloodPix.setColor(borderColor);
        bloodPix.drawRectangle(0, 0, 510, 40);// 画空心矩形.起点(x,y),(width,height)
        bloodPix.drawRectangle(1, 1, 508, 38);// 画空心矩形.起点(x,y),(width,height)
        bloodPix.drawRectangle(2, 2, 506, 36);// 画空心矩形.起点(x,y),(width,height)
        bloodPix.drawRectangle(3, 3, 504, 34);// 画空心矩形.起点(x,y),(width,height)
        bloodPix.drawRectangle(4, 4, 502, 32);// 画空心矩形.起点(x,y),(width,height)
        bloodPix.setColor(bloodColor);
        bloodPix.fillRectangle(5, 5, blood * 5, 30);// 画实心矩形.起点(x,y),(width,height)
        texture.draw(bloodPix, 0, 0);// 在texture中套一个pixmap图层
        bloodRegion = new TextureRegion(texture, 510, 40);

        batch.begin();
        batch.draw(bloodRegion, 545, 50);
        batch.end();
        texture.dispose();
        bloodPix.dispose();
    }

    private void choosePlayerTexture() {
        if (armorType == 0 && isAttackingState == 0) {
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/CharacterCat.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/Sword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 1 && isAttackingState == 0) {
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/IronArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/IronSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 2 && isAttackingState == 0) {
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/GoldenArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/GoldenSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);

            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 3 && isAttackingState == 0) {
            if (inventoryChoose == 0) {
                Texture current = manager.get("assets/pic/DiamondArmor.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/DiamondSword.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }

        if (armorType == 0 && isAttackingState == 1) {
            if (inventoryChoose == 0) {
                Texture current;
                if(attackHand == 1){
                    current = manager.get("assets/pic/attack(left).png", Texture.class);
                }
                else{
                    current = manager.get("assets/pic/attack(right).png", Texture.class);
                }
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/fistSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/fistBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 1 && isAttackingState == 1) {
            if (inventoryChoose == 0) {
                Texture current;
                if(attackHand == 1){
                    current = manager.get("assets/pic/Ironattack(left).png", Texture.class);
                }
                else{
                    current = manager.get("assets/pic/Ironattack(right).png", Texture.class);
                }
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/IronSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/IronBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 2 && isAttackingState == 1) {
            if (inventoryChoose == 0) {
                Texture current;
                if(attackHand == 1){
                    current = manager.get("assets/pic/Goldenattack(left).png", Texture.class);
                }
                else{
                    current = manager.get("assets/pic/Goldenattack(right).png", Texture.class);
                }
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/GoldenSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);

            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/GoldenBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        } else if (armorType == 3 && isAttackingState == 1) {
            if (inventoryChoose == 0) {
                Texture current;
                if(attackHand == 1){
                    current = manager.get("assets/pic/Diamondattack(left).png", Texture.class);
                }
                else{
                    current = manager.get("assets/pic/Diamondattack(right).png", Texture.class);
                }
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 1) {
                Texture current = manager.get("assets/pic/DiamondSwordattack.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            } else if (inventoryChoose == 2) {
                Texture current = manager.get("assets/pic/DiamondBow.png", Texture.class);
                character.setSize(current.getWidth(), current.getHeight());
                character.setCenter(current.getWidth() / 2, current.getHeight() / 2);
                character.setTexture(current);
            }
        }
    }

    private void drawBullet() {
        for (int i = 0; i < bulletManager.size(); i++) {
            currentBullet = bulletManager.get(i);
            // bulletPicture.rotate((float)currentBullet.deg);
            currentBullet.update();
            if (currentBullet.TempBulletPositionX > 1600 || currentBullet.TempBulletPositionY > 900
                    || currentBullet.TempBulletPositionX < 0 || currentBullet.TempBulletPositionY < 0) {
                bulletManager.remove(i);
            } else {
                bulletPicture.rotate((float) currentBullet.deg);
                batch.begin();
                bulletPicture.setPosition(currentBullet.TempBulletPositionX, currentBullet.TempBulletPositionY);
                bulletPicture.draw(batch);
                batch.end();
                bulletPicture.rotate(-(float) currentBullet.deg);
            }
        }
    }

    private void showTimer() {
        timerPixmap.setColor(timerBlockCover);
        timerPixmap.fillRectangle(4, 4, 141, 41);// 画实心矩形.起点(x,y),(width,height)
        timerPixmap.setColor(timerBlockMargin);
        timerPixmap.drawRectangle(3, 3, 143, 43);
        timerPixmap.drawRectangle(2, 2, 145, 45);
        timerPixmap.drawRectangle(1, 1, 147, 47);
        timerPixmap.drawRectangle(0, 0, 149, 49);
        timerTexture.draw(timerPixmap, 0, 0);
        region.setRegion(timerTexture);
        region.setRegionWidth(150);
        region.setRegionHeight(50);

        batch.begin();
        batch.draw(region, 1440, 840);

        lightGrayFont26.draw(batch, passTime / 60 + " m " + passTime % 60 + " s", 1470, 878);

        batch.end();
    }


    // ==================================
    // |         Keyboard event         |
    // ==================================
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.NUM_1 && inventory[1] > 0) {
            client.keyDown((byte)'1');
            inventoryChoose = 0;
            click.play();
        }
        if (keycode == Input.Keys.NUM_2 && inventory[1] > 0) {
            client.keyDown((byte)'2');
            inventoryChoose = 1;
            click.play();
        }
        if (keycode == Input.Keys.NUM_3 && inventory[2] > 0) {
            client.keyDown((byte)'3');
            inventoryChoose = 2;
            click.play();
        }
        if (keycode == Input.Keys.NUM_4 && inventory[3] > 0) {
            client.keyDown((byte)'4');
            click.play();
            inventory[3]--;
            blood = (100 - blood) / 2 + blood;
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
        if (button == Input.Buttons.LEFT ) {
            client.keyDown((byte)'L');
            if(inventory[2] == 1 && inventoryChoose == 2) {
                double deltaX = Gdx.input.getX()-800;
                double deltaY = 450 - Gdx.input.getY();
                float percentZ = Math.abs(percent - 0.5f)*2;
                currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ;
                // vector dot with (1,0)
                double dot = deltaX;
                double normalize = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
                double ang = Math.acos(dot/normalize);
                double deg = 0;
                if(deltaY<0){
                    deg = -ang;
                }
                else{
                    deg = ang;
                }
                bullet = new Bullet(800,450,deg);
                bulletManager.add(bullet);
                Timer timer = new Timer();
                timer.schedule(new attackDelay(), 150);
                return true;
            }
            isAttackingState = 1;
            Music effect = manager.get("assets/sound/punch.mp3", Music.class);
            effect.play();
            Timer timer = new Timer();
            timer.schedule(new attackDelay(), 150);
            return true;
        }
        if(button == Input.Buttons.RIGHT && inventory[2] == 1 && inventoryChoose == 2){
            client.keyDown((byte)'R');
            if(minAltitude == 1.7f)
                minAltitude = 2.7f;
            else
                minAltitude = 1.7f;
        }

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

    // ==================================
    // |      XBOX controller event     |
    // ==================================
    @Override
    public void connected(Controller controller) {
        hasControllers = true;
    }

    @Override
    public void disconnected(Controller controller) {
        hasControllers = false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        if(buttonCode == XBox360Pad.BUTTON_Y)
            Gdx.app.log("Controller", "Y");
        if(buttonCode == XBox360Pad.BUTTON_A){
            if(inventory[2] == 1 && inventoryChoose == 2) {
                double deltaX = Gdx.input.getX()-800;
                double deltaY = 450 - Gdx.input.getY();
                float percentZ = Math.abs(percent - 0.5f)*2;
                currentZ = maxAltitude - (maxAltitude-minAltitude)*percentZ;
                // vector dot with (1,0)
                double dot = deltaX;
                double normalize = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
                double ang = Math.acos(dot/normalize);
                double deg = 0;
                if(deltaY<0){
                    deg = -ang;
                }
                else{
                    deg = ang;
                }
                bullet = new Bullet(800,450,deg);
                bulletManager.add(bullet);
                Timer timer = new Timer();
                timer.schedule(new attackDelay(), 150);
                return true;
            }
            isAttackingState = 1;
            Music effect = manager.get("assets/sound/punch.mp3", Music.class);
            effect.play();
            Timer timer = new Timer();
            timer.schedule(new attackDelay(), 150);
        }
        if(buttonCode == XBox360Pad.BUTTON_X)
            Gdx.app.log("Controller", "X");
        if(buttonCode == XBox360Pad.BUTTON_B)
            Gdx.app.log("Controller", "B");

        if(buttonCode == XBox360Pad.BUTTON_LB)
            Gdx.app.log("Controller", "LB");
        if(buttonCode == XBox360Pad.BUTTON_RB)
            Gdx.app.log("Controller", "RB");
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // This is your analog stick
        // Value will be from -1 to 1 depending how far left/right, up/down the stick is
        // For the Y translation, I use a negative because I like inverted analog stick
        // Like all normal people do! ;)

        // Left Stick

//         Right stick
        if(axisCode == XBox360Pad.AXIS_RIGHT_X) {
            Gdx.app.log("Controller X", String.valueOf(value));
            deg = value*180;
        }
        if(axisCode == XBox360Pad.AXIS_RIGHT_Y){
            Gdx.app.log("Controller Y", String.valueOf(value));
            deg = value*180;
        }

        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        // This is the dpad

        if(value == XBox360Pad.BUTTON_DPAD_LEFT) {
            Gdx.app.log("Controller", "LEFT");
            tmpX = -15;
            return true;
        }
        if(value == XBox360Pad.BUTTON_DPAD_RIGHT) {
            Gdx.app.log("Controller", "RIGHT");
            tmpX = 15;
            return true;
        }
        if(value == XBox360Pad.BUTTON_DPAD_UP) {
            Gdx.app.log("Controller", "UP");
            tmpY = 15;
            return true;
        }
        if(value == XBox360Pad.BUTTON_DPAD_DOWN) {
            Gdx.app.log("Controller", "DOWN");
            tmpY = -15;
            return true;
        }
        tmpX = 0;
        tmpY = 0;
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }

    class attackDelay extends TimerTask {
        public void run() {
            isAttackingState = 0;
            if(attackHand == 1){
                attackHand = 2;
            }
            else{
                attackHand = 1;
            }
        }
    }

    class updateDirSchedule extends TimerTask{
        public void run() {
            Logger.log("Dir update" + oldDeg);
            client.updateDirection((float) oldDeg);
        }
    }
    public class Bullet{
        private int TempBulletPositionX,TempBulletPositionY;
        private float TempBulletVelocityX,TempBulletVelocityY;
        private double deg;
        private float currentXPath ;
        private float currentYPath;

        public Bullet(int bulletPositionX,int bulletPositionY,double degree){
            TempBulletPositionX = bulletPositionX;
            TempBulletPositionY = bulletPositionY;
            TempBulletVelocityX = 18*(float)(Math.cos((float)degree));
            TempBulletVelocityY = 18*(float)(Math.sin((float)degree));
            currentXPath = currentX;
            currentYPath = currentY;
            deg = Math.toDegrees(degree);
        }
        public void update(){
            TempBulletPositionX += (int)TempBulletVelocityX;
            TempBulletPositionY += (int)TempBulletVelocityY;
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                TempBulletPositionX += userSpeedX/minAltitude;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                TempBulletPositionX -= userSpeedX/minAltitude;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                TempBulletPositionY += userSpeedY/minAltitude;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                TempBulletPositionY -= userSpeedY/minAltitude;
            }
        }
    }

}
