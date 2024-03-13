

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
import com.raccoon.mygame.controllers.CollisionController;
import com.raccoon.mygame.controllers.InputController;
import com.raccoon.mygame.controllers.RestaurantController;
import com.raccoon.mygame.controllers.StoreController;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.NormalCollisionObject;
import com.raccoon.mygame.util.ScreenListener;
import com.raccoon.mygame.view.GameCanvas;
import com.raccoon.mygame.models.*;
import com.badlogic.gdx.utils.*;

/**
 * Root class for a LibGDX.
 *
 * This class is technically not the ROOT CLASS. Each platform has another class above
 * this (e.g. PC games use DesktopLauncher) which serves as the true root.  However,
 * those classes are unique to each platform, while this class is the same across all
 * plaforms. In addition, this functions as the root class all intents and purposes,
 * and you would draw it as a root class in an architecture specification.
 */
public class GDXRoot extends Game implements ScreenListener {
	private GameCanvas canvas;

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

	/*public OrthographicCamera camera;*/

	private Box2DDebugRenderer renderer;

	private World world;

	private final int WORLD_WIDTH = 32;
	private final int WORLD_HEIGHT = 18;

	private Vector2 velCache;



	/**
	 * Creates a new game from the configuration settings.
	 *
	 * This method configures the asset manager, but does not load any assets
	 * or assign any screen.
	 */
	public GDXRoot() { }

	/**
	 * Called when the Application is first created.
	 *
	 * This is method immediately loads assets for the loading screen, and prepares
	 * the asynchronous loader for all other assets
	 */

	StoreController store;
	RestaurantController restaurant;
	int current;
	public void create() {
		//world = new World(new Vector2(0, 0), false);
		canvas  = new GameCanvas();
		input = new InputController();
		Inventory inv = new Inventory(new Texture("inventorybar.png"));
		restaurant = new RestaurantController(canvas, new Texture("background.png"), input, inv);
		store = new StoreController(canvas,new Texture("groceryfloor.png"), input, inv);
		current = 0; //this means restaurant
//		bounds = new Rectangle(0,0,canvas.getWidth(),canvas.getHeight());
//
//		vent = new NormalCollisionObject(new Texture("minecraft.png"), 100, 100, 100,100, true);
//		vent1 = new NormalCollisionObject(new Texture("minecraft.png"), 1000, 100, 100,100, true);
//		vent.setObjectToTeleportTo(vent1);


//
//		objects.add(vent);
//		objects.add(vent1);
//

//		trash = new Trash(100, 800, 10, 10, new Texture("trash.png"));


	}


	/**
	 * Called when the Application is destroyed.
	 *
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
	 *
	 * This can happen at any point during a non-paused state but will never happen
	 * before a call to create().
	 *
	 * @param width  The new width in pixels
	 * @param height The new height in pixels
	 */
	public void resize(int width, int height) {
		canvas.resize();
		super.resize(width,height);
	}

	/**
	 * The given screen has made a request to exit its player mode.
	 *
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
	public void render(){
		update();
		canvas.begin();
		draw();
		canvas.end();
		drawDebug();
	}


public void update(){
//		if (collision.collide){
//			player.setPosition(new Vector2());
//			player.clearInv();
//			collision.collide = false;
//		}
//		if (collision.inSight){
////			System.out.println("I SEE YOU FUCKER");
//		}
		if (input.click){
			current = current == 0 ? 1: 0;
		}
		if (current == 0){
			restaurant.setActive(true);
			store.setActive(false);
		} else {
			restaurant.setActive(false);
			store.setActive(true);
		}
		input.readInput();

		store.update();
		restaurant.update();
		if (input.getReset()){
			create();
		}

//		collision.processBounds(player);
//		collision.processGuards(player,guards);
//		collision.processIngredients(player,objects);
//		collision.handleCollision(player,trash);
//		collision.handleCollision(player, vent);
//		collision.handleCollision(player, vent1);



		//if the player is in a teleporting state
//		if(player.getTeleporting()){
//			//check which vent is being teleported to
//			if(vent.getBeingTeleportedTo()){
//				//translate the camera's position based on the distance between both vents
//				canvas.translateCamera(vent.calculateCameraTranslation());
//				vent.setBeingTeleportedTo(false);
//			}
//			if(vent1.getBeingTeleportedTo()){
//				canvas.translateCamera(vent1.calculateCameraTranslation());
//				vent1.setBeingTeleportedTo(false);
//			}
//
//			//player isn't teleporting anymore
//			player.setTeleporting(false);
//		}

		//System.out.println(b.getLinearVelocity());

	}

	public void draw(){

		if(current == 0){
			restaurant.draw();
		}else if (current == 1){
			store.draw();
		}
//		trash.draw(canvas);
//
//


		//calls draw method to draw overlay(background) and all the other stuff)
	}

	public void drawDebug(){
		canvas.beginDebug();
		if(current == 1){
			store.debug();
		} else {
			restaurant.debug();
		}
		canvas.endDebug();
	}

}
