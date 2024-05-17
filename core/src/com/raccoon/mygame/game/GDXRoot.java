

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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.raccoon.mygame.assets.AssetDirectory;
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
    private AssetDirectory directory;
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
    private Viewport viewport;

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
    Array<Customer> notepadOrders;

    int current; // 0 = restaurant, 1 = store, 2 = result
    public boolean isPaused;
    public int[] star_req;
    public int customerLeaveTimer = 0;
    public int unsatisfiedCustomerTimer = 0;

    public int timeoutTimer = 0;

    public void create() {
        //world = new World(new Vector2(0, 0), false);
        canvas = new GameCanvas();

        sounds = new SoundController();
        directory = new AssetDirectory( "assets.json" );
        directory.loadAssets();
        directory.finishLoading();
        //180
        //w = new Worldtimer(180, canvas, new Texture("720/BaseTimer.png"));
        w = new Worldtimer(180, canvas, directory.getEntry( "basetimer", Texture.class ), directory);
        w.create();
        input = new InputController(sounds);

        //I think I fucked something up

        splash = new SplashScreenController(canvas);


        inv = new Inventory(directory.getEntry("inventory", Texture.class), directory.getEntry("inventoryselect", Texture.class), sounds);
        restaurant = new RestaurantController(canvas, directory.getEntry("floorrestaurant", Texture.class), input, inv,w, star_req, sounds, directory, false,false);
        notepadOrders = new Array<>();
        store = new StoreController(canvas, directory.getEntry("floorstore", Texture.class), input, inv, w, notepadOrders, sounds, directory);
        loader = new LevelLoader(canvas, sounds, directory);

        saveController = new SaveController(loader);


        //store.setLevel(loader.getLevels().get(levelToGoTo), inv);
        result = new ResultController(canvas, directory.getEntry("r_result", Texture.class),input, directory);
        levelselect = new LevelSelectController(canvas, input, loader, saveController, sounds, directory);
        mainmenu = new MainMenuController(canvas, input, saveController, levelselect, sounds, directory, loader);
        pause = new MenuController(canvas, directory.getEntry("p_paused", Texture.class),input, sounds, directory);
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
        System.out.println("restarted");
        sounds.potStop();
        sounds.panStopp();
        sounds.potplaying = false;
        sounds.panplaying = false;
        store.setA(1);
        w = new Worldtimer(180, canvas, directory.getEntry("basetimer", Texture.class),directory);
        w.create();
        result.ticks = 0;

        inv = new Inventory(directory.getEntry("inventory", Texture.class), directory.getEntry("inventoryselect", Texture.class), sounds);
        restaurant = new RestaurantController(canvas, directory.getEntry("floorrestaurant", Texture.class), input, inv,w,star_req, sounds, directory, loader.getLevels().get(levelToGoTo).isEndless(), loader.getLevels().get(levelToGoTo).isTutorial());
        notepadOrders = new Array<>();
        //store = new StoreController(canvas, new Texture("720/grocerybg.png"), input, inv);
        //restaurant.setTimer(w);
        store.t=w;
        restaurant.setCustomers(loader.getLevels().get(levelToGoTo).getCustomerData());
        store.setLevel(loader.getLevels().get(levelToGoTo), inv);

        //pause = new MenuController(canvas, new Texture("pause/paused_final.png"),input);
        //result = new ResultController(canvas, new Texture("result/result_final.png"),input);
        levelselect = new LevelSelectController(canvas, input, loader, saveController,sounds,directory);
        //mainmenu = new MainMenuController(canvas,input);
        mainmenu.on_main = true;
        current = 0;
        isPaused = false;
        customerLeaveTimer = 0;
        timeoutTimer = 0;
        unsatisfiedCustomerTimer = 0;
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

        if (directory != null) {
            directory.unloadAssets();
            directory.dispose();
            directory = null;
        }

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
//        canvas.setCamera((OrthographicCamera) viewport.getCamera());
        canvas.begin();
        draw();
        canvas.end();
        drawDebug();
    }


    public void update() {
        System.out.println("CURRENT"+current);
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
            sounds.potStop();
            sounds.panStopp();
            sounds.potplaying = false;
            sounds.panplaying = false;
            sounds.storeStop();
            sounds.cafeStop();
            if(mainmenu.checkForGoToLevelSelect()){
                current = -1;
                levelselect.setCameraToLastCameraY();
                levelselect.resetLevelSelectors();
                levelselect.setSaveController(saveController);
                levelselect.generateLevelSelectors(loader.getLevels().size);
                mainmenu.setForGoToLevelSelect(false);
            }
            if(mainmenu.checkForExit()){
                Gdx.app.exit();
            }
        }
        else if(current == -1) {
            levelselect.update();
            sounds.potStop();
            sounds.panStopp();
            sounds.potplaying = false;
            sounds.panplaying = false;
            w.pauseTimer();
            sounds.storeStop();
            sounds.cafeeactualstop();
            restaurant.setActive(false);
            store.setActive(false);
            if(levelselect.checkForGoToMainMenu()){
                this.current = -2;
                levelselect.setLastCameraY();
                levelselect.setForGoToMainMenu(false);
                canvas.getCamera().position.y = 360;
                canvas.getCamera().update();
            }
            if(levelselect.checkForGoToLevel()){
                levelselect.setLastCameraY();
                this.levelToGoTo = levelselect.getLevelToGoTo();

                restart();
                //store.setLevel(loader.getLevels().get(levelToGoTo),this.inv);
            }
            levelselect.setGoToLevel(false);
        }
        //System.out.println("PSST" +canvas.getWidth());
        //store is supposed to be 1, if this is different we change current
        //todo make customerLeaveTimer better
        if((timeoutTimer>100 && (!loader.getLevels().get(levelToGoTo).isEndless())) || ((customerLeaveTimer >100) && (current == 1 || current == 0))
        || (loader.getLevels().get(levelToGoTo).isEndless() && unsatisfiedCustomerTimer > 100)){
            current = 2;
            timeoutTimer = 0;
//            restaurant.setActive(false);
//            store.setActive(false);
//            sounds.storeStop();
        }

        if(w.getTime() <= 0 && (current == 0 || current == 1)){
            timeoutTimer++;
        }

        if((restaurant.unsatisfiedCustomers>=3 && current == 0)){
            unsatisfiedCustomerTimer ++;
        }
        else if(current != 0){
            unsatisfiedCustomerTimer =0;
        }

        if((restaurant.allCustomersLeave() && current == 0)){
            customerLeaveTimer ++;
        }
        else if(current != 0){
            customerLeaveTimer=0;
        }

        if (current == 2){
            sounds.cafeeactualstop();
            sounds.storeStop();
            sounds.panStopp();
            sounds.potplaying = false;
            sounds.panplaying = false;
            sounds.potStop();
            result.setStatus(restaurant.happy, restaurant.neutral, restaurant.angry, restaurant.happy+restaurant.neutral+restaurant.angry, restaurant.score, star_req);
            saveController.editKeyValuePair(levelToGoTo, result.score);
            levelselect.setSaveController(saveController);
            levelselect.resetLevelSelectors();
            result.update();
            if (result.retry){
                System.out.println("clicked retry");
                sounds.cafeeactualstop();
                sounds.storeStop();
                restart();
                result.retry = false;
            } else if (result.next){
                current = -1;
//                levelselect = new LevelSelectController(canvas,input,loader,saveController,sounds,directory);
                levelselect.generateLevelSelectors(loader.getLevels().size);
                levelselect.setNext(this.levelToGoTo);
                result.next = false;

            }else if (result.select){
                current = -1;
                //this just resets the scores according to whats new
                //System.out.println("pressed,current is" + current);
//                levelselect = new LevelSelectController(canvas,input,loader,saveController,sounds,directory);
                levelselect.generateLevelSelectors(loader.getLevels().size);
                levelselect.setCameraToLastCameraY();
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
          sounds.potStop();
          sounds.panStopp();
          restaurant.pauseTimer();
          pause.on_pause = true;
      }

      else if(isPaused){
          pause.update();
          if(pause.resume){
              isPaused = false;
              pause.resumed();
              if(sounds.potplaying){
                  sounds.potPlay();
              }
              if(sounds.panplaying){
                  sounds.panPlay();
              }
              pause.on_pause = false;
              if(current == 1){
                  sounds.storePlay();
              }
              sounds.cafePlay();
          }
          if(pause.quit){
              pause.quit = false;
              levelselect.resetLevelSelectors();
              levelselect.setSaveController(saveController);
              levelselect.generateLevelSelectors(loader.getLevels().size);
              current = -1;
          }
          if(pause.restart){
              sounds.storeStop();
              sounds.cafeeactualstop();
              notepadOrders = new Array<>();
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
              store.setActive(false);
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
              store.setActive(false);
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
          notepadOrders = restaurant.getTakenCustomers();
          store.setNotepadOrders(notepadOrders);

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
            sounds.menuPlay();
            return;
        }
        else if(current == -1){
            levelselect.draw();
            return;
        }
        else if (current == 0) {
            sounds.menuStop();
            restaurant.draw();
            //todo banner
            if(unsatisfiedCustomerTimer > 0 && loader.getLevels().get(levelToGoTo).isEndless()){
                canvas.drawTextCentered("Too many broken hearts",new BitmapFont(),0);
            }
            else if(customerLeaveTimer > 0 ){
                canvas.drawTextCentered("No more customers",new BitmapFont(),0);
            }
            else if(timeoutTimer > 0 && !loader.getLevels().get(levelToGoTo).isEndless()){
                canvas.drawTextCentered("The day is over",new BitmapFont(),0);
            }
        } else if (current == 1) {
            store.draw();
            if(timeoutTimer > 0){
                canvas.drawTextCentered("The day is over",new BitmapFont(),0);
            }
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

    public void drawBanner(){

    }

    public void drawDebug() {
//        canvas.beginDebug();
//        if (current == 1) {
//            store.debug();
//        } else if(current == 0){
//            restaurant.debug();
//        }
//        canvas.endDebug();
    }
}
