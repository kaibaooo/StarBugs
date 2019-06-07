package com.shopping.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shopping.Actors.Background;
import com.shopping.Screen.GameScreen;
import com.shopping.Screen.LoadToGameScreen;



public class TitleScreen implements Screen {
    Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/PUBG.mp3"));
    private Stage stage;
    private Game game;
    private OrthographicCamera camera;


    public TitleScreen(Game aGame) {
        music.setLooping(true);
        music.play();
        game = aGame;
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
            textures.add(new Texture(Gdx.files.internal("assets/pic/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        Background background = new Background(textures);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setSpeed((float)0.2);
        stage.addActor(background);

        final Skin fontSkin = new Skin(Gdx.files.internal("assets/skin/craftacular/craftacular-ui.json"));

        final Label label1 = new Label("S T = R B U G S",fontSkin,"title");
        label1.setPosition(Gdx.graphics.getWidth()/3-140,Gdx.graphics.getHeight()/2+100);
        stage.addActor(label1);

        final Skin mySkin = new Skin(Gdx.files.internal("assets/skin/Igdx/lgdxs-ui.json"));
        final Button button1 = new TextButton("Play",mySkin,"oval2");
        button1.setSize(200,50);
        button1.setPosition(Gdx.graphics.getWidth()/3+130,Gdx.graphics.getHeight()/2-70);

        final Button button2 = new TextButton("Score board",mySkin,"oval2");
        button2.setSize(200,50);
        button2.setPosition(Gdx.graphics.getWidth()/3+130,Gdx.graphics.getHeight()/2-140);

        button1.addListener(new InputListener()
        {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                label1.setVisible(false);
                button1.setVisible(false);
                button2.setVisible(false);
                registerScreen(fontSkin,mySkin);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        button2.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                label1.setVisible(false);
                button1.setVisible(false);
                button2.setVisible(false);
                scoreBoard();
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(button1);
        stage.addActor(button2);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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

    public void registerScreen(Skin Skin1,Skin Skin2){

        final TextField textField = new TextField("",Skin1);
        textField.setWidth(Gdx.graphics.getWidth()/3);
        textField.setPosition(Gdx.graphics.getWidth()/3+150,Gdx.graphics.getHeight()/2);

        Texture texture1 = new Texture(Gdx.files.absolute("assets/pic/joystick.png"));
        Image icon = new Image(texture1);
        icon.setSize(100,100);
        icon.setPosition(Gdx.graphics.getWidth()/3-150,Gdx.graphics.getHeight()/2+130);

        Texture texture2 = new Texture(Gdx.files.absolute("assets/pic/Symbol 2 – 1.png"));
        Image str = new Image(texture2);
        str.setSize(250,28);
        str.setPosition(Gdx.graphics.getWidth()/3-120,Gdx.graphics.getHeight()/2+12);

        final Button queue = new TextButton("Ready!",Skin2,"oval2");
        queue.setSize(200,50);
        queue.setPosition(Gdx.graphics.getWidth()/3+500,Gdx.graphics.getHeight()/2-150);

        queue.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                //catch user name
                String user_name = textField.getText();
                System.out.println(user_name);
                game.setScreen(new LoadToGameScreen(game, user_name));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(icon);
        stage.addActor(textField);
        stage.addActor(queue);
        stage.addActor(str);
    }

    public void scoreBoard(){
        Texture texture = new Texture(Gdx.files.absolute("assets/pic/earth.png"));
        Image icon = new Image(texture);
        icon.setPosition(Gdx.graphics.getWidth()/3-195,Gdx.graphics.getHeight()/2+100);
        stage.addActor(icon);
    }
}
