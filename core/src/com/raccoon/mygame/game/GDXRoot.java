

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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
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
		canvas  = new GameCanvas();
		current = 0;
		input = new InputController();
		collision = new CollisionController(canvas.getWidth(), canvas.getHeight());
		bounds = new Rectangle(0,0,canvas.getWidth(),canvas.getHeight());
		objects = new Array();
		objects.add(new Ingredient("cat",200,200,new Texture("ingredient.png"),-1));
		guards = new Array();
		guards.add(new Guard(100,100,10,10,new Texture("guard.png")));
		Inventory inv = new Inventory();
		player = new Player(0,0,30,30, new Texture("rocko.png"),inv, canvas);

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
		draw();
	}

	public void update(){
		input.readInput();
		player.move(5*input.getXMovement(),5*input.getYMovement());
		player.setSpace(input.getSpace());
		player.setInteraction(input.getInteraction());
		collision.processBounds(player);
		collision.processGuards(player,guards);
		collision.processIngredients(player,objects);
		//System.out.println(player.getX() + " " +player.getY());
		//update position, inventory, etc, according to current state plus InputController
	}

	public void draw(){
		canvas.begin();
		canvas.draw(new Texture("background.png"), Color.WHITE, 0, 0,
				0, 0, 0.0f, 2f, 2f);
		player.draw();
		for(Guard g : guards){
			g.draw(canvas);
		}
		for (Ingredient i : objects){
			i.draw(canvas);
		}
		//canvas.clear();
		canvas.end();

		//calls draw method to draw overlay(background) and all the other stuff)
	}
}
