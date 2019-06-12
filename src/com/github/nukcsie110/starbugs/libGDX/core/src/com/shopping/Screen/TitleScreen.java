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
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shopping.Actors.Background;
import com.shopping.Screen.GameScreen;
import com.shopping.Screen.LoadToGameScreen;

//import com.badlogic.gdx.controllers.*;
//import com.badlogic.gdx.controllers.ControllerListener;
//import com.badlogic.gdx.controllers.Controllers;
//import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class TitleScreen implements Screen{
    Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/PUBGremix.mp3"));
    private Stage stage;
    private Game game;
    private OrthographicCamera camera;

    // controller
    boolean hasControllers;
    Sprite sprite;

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
            if(i==2||i==3) continue;
            textures.add(new Texture(Gdx.files.internal("assets/pic/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        Background background = new Background(textures);
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setSpeed((float)0.2);
        stage.addActor(background);

        final Skin fontSkin = new Skin(Gdx.files.internal("assets/skin/craftacular/craftacular-ui.json"));

        final Label label1 = new Label("S T = R B U G S",fontSkin,"title");
        label1.setPosition(Gdx.graphics.getWidth()/2-label1.getWidth()/2,Gdx.graphics.getHeight()/2+100);
        stage.addActor(label1);

        final Skin mySkin = new Skin(Gdx.files.internal("assets/skin/Igdx/lgdxs-ui.json"));
        final Button button1 = new TextButton("Play",mySkin,"oval2");
        button1.setSize(200,50);
        button1.setPosition(Gdx.graphics.getWidth()/2-button1.getWidth()/2,Gdx.graphics.getHeight()/2-100);

        final Button button2 = new TextButton("Score board",mySkin,"oval2");
        button2.setSize(200,50);
        button2.setPosition(Gdx.graphics.getWidth()/2-button2.getWidth()/2,Gdx.graphics.getHeight()/2-180);

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


        // controller
        hasControllers = false;
//        Controllers.addListener(this);
//
//        if(Controllers.getControllers().size == 0)
//        {
//            hasControllers = false;
//        }
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

        Image regBG = new Image(new Texture(Gdx.files.absolute("assets/map/registerBG.png")));
        regBG.setSize(1000,600);
        regBG.setPosition(Gdx.graphics.getWidth()/2-475,Gdx.graphics.getHeight()/2-300);

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
                music.stop();
                game.setScreen(new LoadToGameScreen(game, user_name));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(regBG);
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


    // connected and disconnect dont actually appear to work for XBox 360 controllers.

//    @Override
//    public void connected(Controller controller) {
//        hasControllers = true;
//    }
//
//    @Override
//    public void disconnected(Controller controller) {
//        hasControllers = false;
//    }
//
//    @Override
//    public boolean buttonDown(Controller controller, int buttonCode) {
//        if(buttonCode == XBox360Pad.BUTTON_Y)
//            Gdx.app.log("Controller", "Y");
//        if(buttonCode == XBox360Pad.BUTTON_A)
//            Gdx.app.log("Controller", "A");
//        if(buttonCode == XBox360Pad.BUTTON_X)
//            Gdx.app.log("Controller", "X");
//        if(buttonCode == XBox360Pad.BUTTON_B)
//            Gdx.app.log("Controller", "B");
//
//        if(buttonCode == XBox360Pad.BUTTON_LB)
//            Gdx.app.log("Controller", "LB");
//        if(buttonCode == XBox360Pad.BUTTON_RB)
//            Gdx.app.log("Controller", "RB");
//        return false;
//    }
//
//    @Override
//    public boolean buttonUp(Controller controller, int buttonCode) {
//        return false;
//    }
//
//    @Override
//    public boolean axisMoved(Controller controller, int axisCode, float value) {
//        // This is your analog stick
//        // Value will be from -1 to 1 depending how far left/right, up/down the stick is
//        // For the Y translation, I use a negative because I like inverted analog stick
//        // Like all normal people do! ;)
//
//        // Left Stick
//        if(axisCode == XBox360Pad.AXIS_LEFT_X)
//            Gdx.app.log("Controller", "AXIS LX");
//        if(axisCode == XBox360Pad.AXIS_LEFT_Y)
//            Gdx.app.log("Controller", "AXIX LY");
//
//        // Right stick
//        if(axisCode == XBox360Pad.AXIS_RIGHT_X)
//            Gdx.app.log("Controller", "AXIS RX");
//        return false;
//    }
//
//    @Override
//    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
//        // This is the dpad
//        if(value == XBox360Pad.BUTTON_DPAD_LEFT)
//            Gdx.app.log("Controller", "LEFT");
//        if(value == XBox360Pad.BUTTON_DPAD_RIGHT)
//            Gdx.app.log("Controller", "RIGHT");
//        if(value == XBox360Pad.BUTTON_DPAD_UP)
//            Gdx.app.log("Controller", "UP");
//        if(value == XBox360Pad.BUTTON_DPAD_DOWN)
//            Gdx.app.log("Controller", "DOWN");
//        return false;
//    }
//
//    @Override
//    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
//        return false;
//    }
//
//    @Override
//    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
//        return false;
//    }
//
//    @Override
//    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
//        return false;
//    }
//

}
//class XBox360Pad
//{
//    /*
//     * It seems there are different versions of gamepads with different ID Strings.
//     * Therefore its IMO a better bet to check for:
//     * if (controller.getName().toLowerCase().contains("xbox") &&
//                   controller.getName().contains("360"))
//     *
//     * Controller (Gamepad for Xbox 360)
//       Controller (XBOX 360 For Windows)
//       Controller (Xbox 360 Wireless Receiver for Windows)
//       Controller (Xbox wireless receiver for windows)
//       XBOX 360 For Windows (Controller)
//       Xbox 360 Wireless Receiver
//       Xbox Receiver for Windows (Wireless Controller)
//       Xbox wireless receiver for windows (Controller)
//     */
//    //public static final String ID = "XBOX 360 For Windows (Controller)";
//    public static final int BUTTON_X = 2;
//    public static final int BUTTON_Y = 3;
//    public static final int BUTTON_A = 0;
//    public static final int BUTTON_B = 1;
//    public static final int BUTTON_BACK = 6;
//    public static final int BUTTON_START = 7;
//    public static final PovDirection BUTTON_DPAD_UP = PovDirection.north;
//    public static final PovDirection BUTTON_DPAD_DOWN = PovDirection.south;
//    public static final PovDirection BUTTON_DPAD_RIGHT = PovDirection.east;
//    public static final PovDirection BUTTON_DPAD_LEFT = PovDirection.west;
//    public static final int BUTTON_LB = 4;
//    public static final int BUTTON_L3 = 8;
//    public static final int BUTTON_RB = 5;
//    public static final int BUTTON_R3 = 9;
//    public static final int AXIS_LEFT_X = 1; //-1 is left | +1 is right
//    public static final int AXIS_LEFT_Y = 0; //-1 is up | +1 is down
//    public static final int AXIS_LEFT_TRIGGER = 4; //value 0 to 1f
//    public static final int AXIS_RIGHT_X = 3; //-1 is left | +1 is right
//    public static final int AXIS_RIGHT_Y = 2; //-1 is up | +1 is down
//    public static final int AXIS_RIGHT_TRIGGER = 4; //value 0 to -1f
//}