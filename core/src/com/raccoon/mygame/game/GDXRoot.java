

/*
 * GDXRoot.java
 *
 * This is the primary class file for running the game.  It is the "static main" of
 * LibGDX.  In the first lab, we extended ApplicationAdapter.  In previous lab
 * we extended Game.  This is because of a weird graphical artifact that we do not
 * understand.  Transparencies (in 3D only) is failing when we use ApplicationAdapter.
 * There must be some undocumented OpenGL code in setScreen.
 *
 * Author: Walker M. White
 * Based on original PhysicsDemo Lab by Don Holden, 2007
 * Updated asset version, 2/6/2021
 */
package com.raccoon.mygame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.*;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.NormalCollisionObject;
import com.raccoon.mygame.util.ScreenListener;
import com.raccoon.mygame.view.GameCanvas;
import com.raccoon.mygame.models.*;
import com.badlogic.gdx.utils.*;

import java.util.Arrays;

/**
 * Root class for a LibGDX.
 * <p>
 * This class is technically not the ROOT CLASS. Each platform has another class above
 * this (e.g. PC games use DesktopLauncher) which serves as the true root.  However,
 * those classes are unique to each platform, while this class is the same across all
 * plaforms. In addition, this functions as the root class all intents and purposes,
 * and you would draw it as a root class in an architecture specification.
 */
public class GDXRoot extends Game implements ScreenListener {
    private GameCanvas canvas;
    private Worldtimer w;
    InputController input;
    CollisionController collision;
    Rectangle bounds;
    private Array<GameObject> objects;
    private Array<Guard> guards;
    private Array<Customer> customers;
    private Player player;
    private Trash trash;
    private NormalCollisionObject vent;
    private NormalCollisionObject vent1;
    private boolean win;
    public Texture background;
    public Texture winPic;
    public SoundController sounds;

    public SaveController saveController;

    private Box2DDebugRenderer renderer;

    private World world;

    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;

    private Vector2 velCache;

    private int levelToGoTo = 0;


    /**
     * Creates a new game from the configuration settings.
     * <p>
     * This method configures the asset manager, but does not load any assets
     * or assign any screen.
     */
    public GDXRoot() {
    }

    /**
     * Called when the Application is first created.
     * <p>
     * This is method immediately loads assets for the loading screen, and prepares
     * the asynchronous loader for all other assets
     */

    StoreController store;
    RestaurantController restaurant;
    LevelSelectController levelselect;
    MainMenuController mainmenu;
    LevelLoader loader;
    SplashScreenController splash;

    MenuController pause;

    ResultController result;
    Inventory inv;

    int current; // 0 = restaurant, 1 = store, 2 = result
    public boolean isPaused;
    public int[] star_req;

    public void create() {
        //world = new World(new Vector2(0, 0), false);
        canvas = new GameCanvas();
        sounds = new SoundController();
        //180
        w = new Worldtimer(180, canvas, new Texture("720/BaseTimer.png"));
        w.create();
        input = new InputController();

        //I think I fucked something up

        splash = new SplashScreenController(canvas);

        inv = new Inventory(new Texture("720/inventorynew.png"));
        restaurant = new RestaurantController(canvas, new Texture("720/floorrestaurant.png"), input, inv,w, star_req);
        store = new StoreController(canvas, new Texture("720/grocerybg.png"), input, inv, w);
        loader = new LevelLoader(canvas);
        saveController = new SaveController(loader);
        //store.setLevel(loader.getLevels().get(levelToGoTo), inv);

        pause = new MenuController(canvas, new Texture("pause/paused_final.png"),input);
        result = new ResultController(canvas, new Texture("result/result_final.png"),input);
        levelselect = new LevelSelectController(canvas, input, loader, saveController);
        mainmenu = new MainMenuController(canvas,input, saveController, levelselect);
        mainmenu.on_main = true;

        /*
         *Remember a few things about current.
         * 0 = restaurant
         * 1 = store
         * -1 = level select
         * -2 = main menu
         * -3 = splash screen
         * */
        current = -3;
        isPaused = false;
        star_req = new int[]{50,75,100};
    }

    public void restart(){
        //canvas = new GameCanvas();
        //180
//        sounds.storeStop();
//        sounds.cafeStop();
        store.setA(1);
        w = new Worldtimer(180, canvas, new Texture("720/BaseTimer.png"));
        w.create();

        inv = new Inventory(new Texture("720/inventorynew.png"));
        restaurant = new RestaurantController(canvas, new Texture("720/floorrestaurant.png"), input, inv,w,star_req);
        //store = new StoreController(canvas, new Texture("720/grocerybg.png"), input, inv);
        //restaurant.setTimer(w);
        store.t=w;
        restaurant.setCustomers(loader.getLevels().get(levelToGoTo).getCustomerData());
        store.setLevel(loader.getLevels().get(levelToGoTo), inv);

        //pause = new MenuController(canvas, new Texture("pause/paused_final.png"),input);
        //result = new ResultController(canvas, new Texture("result/result_final.png"),input);
        //levelselect = new LevelSelectController(canvas, input, loader, saveController);
        //mainmenu = new MainMenuController(canvas,input);
        mainmenu.on_main = true;
        current = 0;
        isPaused = false;
        //star_req = new int[]{50,75,100};
    }


    /**
     * Called when the Application is destroyed.
     * <p>
     * This is preceded by a call to pause().
     */
    public void dispose() {
        // Call dispose on our children
        setScreen(null);
        //world.dispose();

        canvas.dispose();
        canvas = null;

        super.dispose();
    }

    /**
     * Called when the Application is resized.
     * <p>
     * Thiss can happen at any point during a non-paused state but will never happen
     * before a call to create().
     *
     * @param width  The new width in pixels
     * @param height The new height in pixels
     */
    public void resize(int width, int height) {
        canvas.resize();
        canvas.setSize(1280, 720);
        super.resize(width, height);
    }

    /**
     * The given screen has made a request to exit its player mode.
     * <p>
     * The value exitCode can be used to implement menu options.
     *
     * @param screen   The screen requesting to exit
     * @param exitCode The state of the screen upon exit
     */
    public void exitScreen(Screen screen, int exitCode) {
        // We quit the main application
        Gdx.app.exit();
    }

    @Override
    public void render() {
        update();
        canvas.begin();
        draw();
        canvas.end();
        drawDebug();
    }


    public void update() {
        store.current = this.current;
        restaurant.current = this.current;
        input.readInput();
        if(current == -3){
            splash.update();
            w.pauseTimer();
            restaurant.setActive(false);
            restaurant.setPaused(true);
            store.setActive(false);
            sounds.storeStop();
            sounds.cafeStop();
            if(splash.videoDonePlaying){
                current = -2;
            }
        }
        if(current == -2){
            mainmenu.update();
            w.pauseTimer();
            restaurant.setActive(false);
            restaurant.setPaused(true);
            store.setActive(false);
            sounds.storeStop();
            sounds.cafeStop();
            if(mainmenu.checkForGoToLevelSelect()){
                current = -1;
                mainmenu.setForGoToLevelSelect(false);
            }
            if(mainmenu.checkForExit()){
                Gdx.app.exit();
            }
        }
        else if(current == -1) {
            levelselect.update();
            w.pauseTimer();
            sounds.storeStop();
            sounds.cafeeactualstop();
            restaurant.setActive(false);
            store.setActive(false);
            if(levelselect.checkForGoToMainMenu()){
                this.current = -2;
                levelselect.setForGoToMainMenu(false);
                canvas.getCamera().position.y = 360;
                canvas.getCamera().update();
            }
            if(levelselect.checkForGoToLevel()){
                this.levelToGoTo = levelselect.getLevelToGoTo();
                restart();
                //store.setLevel(loader.getLevels().get(levelToGoTo),this.inv);
            }
            levelselect.setGoToLevel(false);
        }
        //System.out.println("PSST" +canvas.getWidth());
        //store is supposed to be 1, if this is different we change current
        else if(w.getTime() <= 0 ){
            current = 2;
//            restaurant.setActive(false);
//            store.setActive(false);
//            sounds.storeStop();
        }
        if (current == 2){
            sounds.cafeeactualstop();
            sounds.storeStop();
            result.setStatus(restaurant.happy, restaurant.neutral, restaurant.angry, restaurant.happy+restaurant.neutral+restaurant.angry, restaurant.score, star_req);
            saveController.editKeyValuePair(levelToGoTo, result.score);
            result.update();
            if (result.retry){
                sounds.cafeeactualstop();
                sounds.storeStop();
                restart();
                result.retry = false;
            } else if (result.next){

            }else if (result.select){
                current = -1;
                //this just resets the scores according to whats new
                //System.out.println("pressed,current is" + current);
                levelselect = new LevelSelectController(canvas,input,loader,saveController);
                result.select = false;
            }
            return;
        }
        //System.out.println(isPaused);
      else if(input.getPause() && (current == 0||current == 1)){
          isPaused = true;
          w.pauseTimer();
          sounds.storeStop();
          sounds.cafeStop();
          restaurant.pauseTimer();
          pause.on_pause = true;
      }

      else if(isPaused){
          pause.update();
          if(pause.resume){
              isPaused = false;
              pause.resumed();
              pause.on_pause = false;
              if(current == 1){
                  sounds.storePlay();
              }
              sounds.cafePlay();
          }
          if(pause.quit){
              pause.quit = false;
              current = -1;
          }
          if(pause.restart){
              sounds.storeStop();
              sounds.cafeeactualstop();
              pause.on_pause = false;
              restart();
              pause.restart = false;
          }
          if(pause.options){

          }
      }
      else if (!isPaused) {
          if (w.timerPaused) {
              w.resumeTimer();
              restaurant.startTimer();
          }
          else if (current == 1 && store.playerJustDied && !store.gettingCaught()) {
              store.player.setPosition(0,0);
              store.guardWanderReset();
              current = 0;
              sounds.storeStop();
              store.playerJustDied = false;
              store.guardInAction = null;
              restaurant.uponPlayerDeathReset();
              restaurant.setPlayerJustDied(true);
              store.setVentCollision(false);
              restaurant.setVentCollision(false);
              sounds.cafePlay();
              store.guardWanderReset();
          }
          else if (store.getVentCollision() && current == 1) {
              current = 0;
              sounds.storeStop();
              store.guardWanderReset();
              store.setVentCollision(false);
              restaurant.setVentCollision(false);
              restaurant.ventOutFlag = true;
              restaurant.onSet();
          }
          else if (restaurant.getVentCollision()&& current == 0) {
              current = 1;
              sounds.storePlay();
              sounds.cafeStop();
              store.setVentCollision(false);
              restaurant.setVentCollision(false);
              store.ventOutFlag = true;
              store.onSet();
          }
          else if (current == 0) {
              canvas.getCamera().position.y = 360;
              canvas.getCamera().update();

              restaurant.setActive(true);
              store.setActive(false);
              sounds.cafePlay();
          } else {
              canvas.getCamera().position.y = 360;
              canvas.getCamera().update();

              restaurant.setActive(false);
              store.setActive(true);
          }
          store.update();
          restaurant.update();

          if (input.getReset()) {
              create();
          }
      }
    }

    public void draw() {
        if(current == -3){
            splash.draw();
            return;
        }
        if(current == -2){
            mainmenu.draw();
            return;
        }
        else if(current == -1){
            levelselect.draw();
            return;
        }
        else if (current == 0) {
            restaurant.draw();
        } else if (current == 1) {
            store.draw();
        } else if (current == 2){
//            result.setStarReq(this.star_req);
            result.draw();
        }
        if(isPaused){
            pause.draw();
        }

//		trash.draw(canvas);
//
//
        //calls draw method to draw overlay(background) and all the other stuff)
    }

    public void drawDebug() {
        canvas.beginDebug();
        if (current == 1) {
            store.debug();
        } else if(current == 0){
            restaurant.debug();
        }
        canvas.endDebug();
    }
}
