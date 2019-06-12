package com.shopping.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.client.Client;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.util.Logger;

import java.sql.Time;
import java.util.TimerTask;
import java.util.Timer;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoadToGameScreen implements Screen {
    private Stage stage;
    private Game game;
    public AssetManager manager = new AssetManager();
    SpriteBatch batch = new SpriteBatch();
    String user_name;
    Sound sound;

    //Networking
    Client client;
    int currentOnlineUser;
    int maxPlayer;
    byte rank; // determine if the game start
    String playerID;
    ArrayList<User> nameTable;
    public LoadToGameScreen(Game aGame, String name, Client passedClient){
        sound = Gdx.audio.newSound(Gdx.files.internal("assets/sound/loading.mp3"));
        Gdx.graphics.setTitle("STARBUGS Alpha 2.0, Loading...");
        long id = sound.play();
        sound.setVolume(id, 0.3f);
        game = aGame;
        stage = new Stage(new ScreenViewport());

        //Network
        client = passedClient;
        rank = 0x00;
        currentOnlineUser = -1;
        if(!name.equals("")){
            user_name = name;
        }
        else{
            user_name = "NullPointerException";
        }

        //Test

        client.start();

        //End test
        // item
        manager.load("assets/pic/iron_chestplate.png", Texture.class);
        manager.load("assets/pic/gold.png", Texture.class);
        manager.load("assets/pic/diamond.png", Texture.class);
        manager.load("assets/pic/bowBIG.png", Texture.class);
        manager.load("assets/map/Lava_map.png",Texture.class);
        manager.load("assets/map/map.png", Texture.class);
        manager.load("assets/map/smallMap.png", Texture.class);
        manager.load("assets/map/Big_map.png", Texture.class);
        manager.load("assets/pic/icons8-center-of-gravity-64.png", Pixmap.class);
        manager.load("assets/map/navigation.png", Texture.class);
        Gdx.app.log("manager", "section 1 finished");
        // character
        manager.load("assets/pic/CharacterCat.png", Texture.class);
        manager.load("assets/pic/Diamondattack(left).png", Texture.class);
        manager.load("assets/pic/Diamondattack(right).png", Texture.class);
        manager.load("assets/pic/DiamondArmor.png", Texture.class);
        manager.load("assets/pic/DiamondSword.png", Texture.class);
        manager.load("assets/pic/DiamondSwordattack.png", Texture.class);
        manager.load("assets/pic/DiamondBow.png", Texture.class);
        manager.load("assets/pic/Goldenattack(left).png", Texture.class);
        manager.load("assets/pic/Goldenattack(right).png", Texture.class);
        manager.load("assets/pic/GoldenArmor.png", Texture.class);
        manager.load("assets/pic/GoldenSword.png", Texture.class);
        manager.load("assets/pic/GoldenSwordattack.png", Texture.class);
        manager.load("assets/pic/GoldenBow.png", Texture.class);
        manager.load("assets/pic/Ironattack(left).png", Texture.class);
        manager.load("assets/pic/Ironattack(right).png", Texture.class);
        manager.load("assets/pic/IronArmor.png", Texture.class);
        manager.load("assets/pic/IronSword.png", Texture.class);
        manager.load("assets/pic/IronSwordattack.png", Texture.class);
        manager.load("assets/pic/IronBow.png", Texture.class);
        manager.load("assets/pic/attack(left).png", Texture.class);
        manager.load("assets/pic/attack(right).png", Texture.class);
        manager.load("assets/pic/Sword.png", Texture.class);
        manager.load("assets/pic/WholeCat.png", Texture.class);
        manager.load("assets/pic/fistBow.png", Texture.class);
        manager.load("assets/pic/fistSwordattack.png", Texture.class);
        manager.load("assets/pic/big-creeper-face.png", Texture.class);
        Gdx.app.log("manager", "section 2 finished");
        //inventory
        manager.load("assets/inventory/inventory65.png", Texture.class);
        manager.load("assets/inventory/sword.png", Texture.class);
        manager.load("assets/inventory/bow.gif", Texture.class);
        manager.load("assets/inventory/potion.png", Texture.class);
        manager.load("assets/inventory/potion2.png", Texture.class);
        manager.load("assets/inventory/potion3.png", Texture.class);
        manager.load("assets/inventory/choose.png", Texture.class);
        Gdx.app.log("manager", "section 3 finished");
        //bullet
        manager.load("assets/inventory/arrow.png", Texture.class);
        //sound
        manager.load("assets/sound/nyan.mp3", Music.class);
        manager.load("assets/sound/LOL_inGame.mp3", Music.class);
        manager.load("assets/sound/punch.mp3", Music.class);
        manager.load("assets/sound/punchSuccess.mp3", Music.class);
        Gdx.app.log("manager", "section 4 finished");
//        user_name = name;



    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Union ops;
        if(client.isReadable()){
            ops = client.read();
            switch(ops.pkID){
                case 0x01:
                    Logger.log("Recived joinReply");
                    if(ops.state==0){
                        Logger.log("My ID is: "+ops.player.getIDString());
                        playerID = ops.player.getIDString();
                    }else{
                        Logger.log("Failed to join. Abort.");
                        game.setScreen(new TitleScreen(game));
                        sound.stop();
                    }
                    break;
                case 0x02:
                    Logger.log("Recived updateNameTable");
                    currentOnlineUser = ops.nameTable.size();
                    maxPlayer = ops.maxPlayer;
                    nameTable = ops.nameTable;
                    Gdx.graphics.setTitle("STARBUGS Alpha 2.0, Waiting " + currentOnlineUser + " / " + maxPlayer);
                    for(User i:ops.nameTable){
                        Logger.log(i.getDisplayName());
                    }
                    break;
                case 0x10:
                    Logger.log("Game over");
                    Logger.log(ops.rank);
                    rank = ops.rank;
//                    client.close();
                default:
            }
        }


        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
        Sprite cat = new Sprite(new Texture(Gdx.files.internal("assets/pic/loadingScreen.png")));
        batch.begin();
        //旋轉要除以縮放比例
        batch.draw(cat,Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        cat.draw(batch);
        batch.end();
        if(currentOnlineUser == -1&& manager.update()) {
            while(!client.isReady());
            client.join(user_name);
            Gdx.app.log("manager", "update");
            currentOnlineUser = 0;
            Gdx.graphics.setTitle("STARBUGS Alpha 2.0");
//            game.setScreen(new GameScreen(game,user_name,manager, client, playerID, nameTable));
//            sound.stop();
        }
        if(rank == (byte)0xFF){
            game.setScreen(new GameScreen(game,user_name,manager, client, playerID, nameTable));
            sound.stop();
            Gdx.graphics.setTitle("STARBUGS Alpha 2.0");
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
//class failConnection extends TimerTask {
//    public void run() {
//        client.updateDirection((float) oldDeg);
//    }
//}