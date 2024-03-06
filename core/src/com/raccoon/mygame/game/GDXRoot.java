

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


	//current could represent whether we are in restaurant
	private int current;

	SpriteBatch batch;

	Texture img;

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
	private Body obj;
	private Body p;


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

		camera= new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		world = new World(new Vector2(0, 0), false);
		renderer = new Box2DDebugRenderer();
		obj = createObj();
		BodyDef bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
// Set our body's starting position in the world
		bodyDef.position.set(400, 10);

// Create our body in the world using our body definition
		Body body = world.createBody(bodyDef);

// Create a circle shape and set its radius to 6
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32, 32);

// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

// Remember to dispose of any shapes after you're done with them!
// BodyDef and FixtureDef don't need disposing, but shapes do.
		shape.dispose();
		p = body;




		background = new Texture("background.png");
		winPic = new Texture("win.png");
		canvas  = new GameCanvas();
		current = 0;
		win = false;
		input = new InputController();
		collision = new CollisionController(canvas.getWidth(), canvas.getHeight());
		bounds = new Rectangle(0,0,canvas.getWidth(),canvas.getHeight());
		objects = new Array();
		objects.add(new Ingredient("cat",200,200,new Texture("ingredient.png"),-1));
		objects.add(new Ingredient("cat",1600,300,new Texture("ingredient.png"),-1));
		objects.add(new Ingredient("cat",1500,800,new Texture("ingredient.png"),-1));
		objects.add(new Ingredient("cat",900,400,new Texture("ingredient.png"),-1));
		objects.add(new Ingredient("cat",1000,800,new Texture("ingredient.png"),-1));
		guards = new Array();
		guards.add(new Guard(100,100,10,10,new Texture("guard.png")));
		guards.add(new Guard(100,300,10,10,new Texture("guard.png")));
		guards.add(new Guard(1500,800,10,10,new Texture("guard.png")));
		guards.add(new Guard(750,400,10,10,new Texture("guard.png")));
		guards.add(new Guard(1400,600,10,10,new Texture("guard.png")));
		Inventory inv = new Inventory(new Texture("UI_inventorybar01_030224.png"));
		player = new Player(0,0,30,30, new Texture("rocko.png"),inv, canvas);
		trash = new Trash(100, 800, 10, 10, new Texture("trash.png"));
	}

	public Body createObj(){
		Body body;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;
		def.position.set(0,0);
		body = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32/2, 32/2);
		body.createFixture(shape,1.0f);
		shape.dispose();
		return body;
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
		renderer.dispose();
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
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.render(world,camera.combined);


	}


	public void update(){

		input.readInput();

		float x;
		float y;
		if(input.getXMovement() < 0){
			x = -100;
		} else if (input.getXMovement() > 0){
			x = 100;
		} else {
			x = 0;
		}

		if(input.getYMovement() < 0){
			y = -100;
		} else if (input.getYMovement() > 0){
			y = 100;
		} else {
			y = 0;
		}
		p.setLinearVelocity(x,y);

		if (input.getReset()){
			create();
		}
		if (player.getX() >= 1900) {
			win = true;
		}
		//player.move(8*input.getXMovement(),8*input.getYMovement());
		player.setPosition(p.getPosition());
		player.setSpace(input.getSpace());
		player.setInteraction(input.getInteraction());
		collision.processBounds(player);
		collision.processGuards(player,guards);
		collision.processIngredients(player,objects);
		collision.handleCollision(player,trash);
		player.getInventory().setSelected((int) input.getScroll());
		float delta = Gdx.graphics.getDeltaTime();
		for (Guard guard : guards) {
			guard.update(delta);
		}

		world.step(1/60f, 6,2);

	}


	public void draw(){
		if (win){
			canvas.draw(winPic, Color.WHITE, 15, 15,
					0, 0, 0.0f, 2.8f, 3f);
			return;
		}
		canvas.draw(background, Color.WHITE, 0, 0,
				0, 0, 0.0f, 2f, 2f);
		player.draw();

		for(Guard g : guards){
			g.draw(canvas);
		}
		for (Ingredient i : objects){
			i.draw(canvas);
		}
		trash.draw(canvas);
		//canvas.clear();
		//calls draw method to draw overlay(background) and all the other stuff)
	}
}
