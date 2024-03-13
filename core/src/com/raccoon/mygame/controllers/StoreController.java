package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.view.GameCanvas;

public class StoreController extends WorldController{
    private World world;
    private GameCanvas canvas;
    private Texture background;
    private InputController input;
    private Player player;
    private Array<Ingredient> ingredients;
    private Array<Guard> guards;
    boolean active;
    public StoreController(GameCanvas canvas, Texture texture, InputController input){
        world = new World(new Vector2(0,0), false);
        this.canvas = canvas;
        this.background = texture;
        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),new Inventory(new Texture("inventorybar.png")), canvas, world);
        this.input = input;
        ingredients = new Array<>();
        ingredients.add(new Ingredient("apple",200,200,new Texture("apple.png"),-1));
        ingredients.add(new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
        ingredients.add(new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
        ingredients.add(new Ingredient("orange",900,400,new Texture("orange.png"),-1));
        ingredients.add(new Ingredient("banana",1000,800,new Texture("banana.png"),-1));
        ingredients.add(new Ingredient("apple",2000,300,new Texture("apple.png"),-1));
        guards = new Array();
        guards.add(new Guard(2.5f,1.67f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
	guards.add(new Guard(2.5f,5,1.67f,0.83f,new Texture("gooseReal.png"),world,canvas));
	guards.add(new Guard(25,13.3f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
	guards.add(new Guard(12.5f,6.67f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
	guards.add(new Guard(23.3f,10,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
        active = false;
    }

    public void setActive(boolean b){
        active = b;
    }

    @Override
    public void dispose() {
        world.dispose();
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    @Override
    public void render(float delta) {

    }
    public void update(){
        if (active){
            float x = 5f*input.getXMovement();
            float y =5f*input.getYMovement();
            player.setLinearVelocity(new Vector2(x,y));
            player.setSpace(input.getSpace());
            player.setInteraction(input.getInteraction());
            player.getInventory().setSelected((int) input.getScroll());
        }
        float delta = Gdx.graphics.getDeltaTime();
        for (Guard guard : guards) {
            guard.update(delta, generatePlayerInfo());
        }
        world.step(1/60f, 6,2);

        //System.out.println("store");
    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        player.draw(0.25f,0.25f);
        for (Ingredient i : ingredients){
            i.draw(canvas);
        }
        for(Guard g : guards){
            g.draw(0.1f,0.1f);
        }
    }

    public void debug(){
        player.drawDebug(canvas);
        for(Guard g : guards){
            g.debug(canvas);
        }
    }

    public Array<Float> generatePlayerInfo() {
        Array<Float> playerInfo = new Array<Float>();
        playerInfo.add(player.getX());
        playerInfo.add(player.getY());
        return playerInfo;
    }


}
