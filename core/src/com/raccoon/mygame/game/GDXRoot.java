

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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.CollisionController;
import com.raccoon.mygame.controllers.InputController;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
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
	private Array<Ingredient> objects;
	private Array<Guard> guards;

	private Player player;
	private Trash trash;
	private boolean win;
	public Texture background;
	public Texture winPic;

	public OrthographicCamera camera;

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
	 * the asynchronous loader for all other assets.
	 */
	public void create() {


		world = new World(new Vector2(0, 0), false);
		canvas  = new GameCanvas();

		background = new Texture("groceryfloor.png");
		winPic = new Texture("win.png");
		velCache = new Vector2(0,0);
		win = false;
		input = new InputController();
		bounds = new Rectangle(0,0,canvas.getWidth(),canvas.getHeight());
		objects = new Array();
		objects.add(new Ingredient("apple",200,200,new Texture("apple.png"),-1));
		objects.add(new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
		objects.add(new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
		objects.add(new Ingredient("orange",900,400,new Texture("orange.png"),-1));
		objects.add(new Ingredient("banana",1000,800,new Texture("banana.png"),-1));

		guards = new Array();
		guards.add(new Guard(2.5f,1.67f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
		guards.add(new Guard(2.5f,5,1.67f,0.83f,new Texture("gooseReal.png"),world,canvas));
		guards.add(new Guard(25,13.3f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
		guards.add(new Guard(12.5f,6.67f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
		guards.add(new Guard(23.3f,10,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
		Inventory inv = new Inventory(new Texture("inventorybar.png"));
		player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),inv, canvas, world);
		//player.setBodyType(BodyType.KinematicBody);
		trash = new Trash(100, 800, 10, 10, new Texture("trash.png"));
		collision = new CollisionController(canvas.getWidth(), canvas.getHeight(),player, guards);
		world.setContactListener(collision);
	}


	/**
	 * Called when the Application is destroyed.
	 *
	 * This is preceded by a call to pause().
	 */
	public void dispose() {
		// Call dispose on our children
		setScreen(null);

		canvas.dispose();
		canvas = null;
		world.dispose();

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

		if (collision.collide){
			player.setPosition(new Vector2());
			player.clearInv();
			collision.collide = false;
		}
		if (collision.inSight){
//			System.out.println("I SEE YOU FUCKER");
		}
		input.readInput();

		float x = 5f*input.getXMovement();
		float y =5f*input.getYMovement();
		velCache = velCache.set(x,y);
		player.setLinearVelocity(velCache);


		if (input.getReset()){
			create();
		}
		if (player.getX() >= 32) {
			win = true;
		}

		player.setSpace(input.getSpace());
		player.setInteraction(input.getInteraction());
		collision.processBounds(player);
		collision.processGuards(player,guards);
		collision.processIngredients(player,objects);
		collision.handleCollision(player,trash);
		player.getInventory().setSelected((int) input.getScroll());
		float delta = Gdx.graphics.getDeltaTime();
		for (Guard guard : guards) {
			guard.update(delta, generatePlayerInfo());
		}

		//System.out.println(b.getLinearVelocity());
		world.step(1/60f, 6,2);

	}


	public void draw(){
		if (win){
			canvas.draw(winPic, Color.WHITE, 15, 15,
					0, 0, 0.0f, 2.8f, 3f);
			return;
		}
		canvas.draw(background, Color.WHITE, 0, 0,
				0, 0, 0.0f, 1f, 1f);
		//b.draw(canvas, 0.1f, 0.1f);
		player.draw(0.25f,0.25f);
		trash.draw(canvas);

		for(Guard g : guards){
			g.draw(0.1f,0.1f);
		}
		for (Ingredient i : objects){
			i.draw(canvas);
		}


		//calls draw method to draw overlay(background) and all the other stuff)
	}

	public void drawDebug(){
		canvas.beginDebug();
		player.drawDebug(canvas);
		for(Guard g : guards){
			g.debug(canvas);
		}
		canvas.endDebug();
	}

	public Array<Float> generatePlayerInfo() {
		Array<Float> playerInfo = new Array<Float>();
		playerInfo.add(player.getX());
		playerInfo.add(player.getY());
		return playerInfo;
	}
}
