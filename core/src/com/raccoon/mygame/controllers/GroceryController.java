package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.NormalCollisionObject;
import com.raccoon.mygame.obstacle.Obstacle;
import com.raccoon.mygame.view.GameCanvas;

public class GroceryController extends WorldController{

    InputController input;
    CollisionController collision;
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

    public GroceryController(GameCanvas canvas){
        super();

        world = new World(new Vector2(0,0),false);
        canvas  = new GameCanvas();
        setCanvas(canvas);

        background = new Texture("groceryfloor.png");
        winPic = new Texture("win.png");
        velCache = new Vector2(0,0);
        win = false;
        input = new InputController();
        bounds = new Rectangle(0,0,canvas.getWidth(),canvas.getHeight());

        vent = new NormalCollisionObject(new Texture("minecraft.png"), 100, 100, 100,100, true);
        vent1 = new NormalCollisionObject(new Texture("minecraft.png"), 1000, 100, 100,100, true);
        vent.setObjectToTeleportTo(vent1);

        objects = new Array<>();
        objects.add(new Ingredient("apple",200,200,new Texture("apple.png"),-1));
        objects.add(new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
        objects.add(new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
        objects.add(new Ingredient("orange",900,400,new Texture("orange.png"),-1));
        objects.add(new Ingredient("banana",1000,800,new Texture("banana.png"),-1));
        objects.add(new Ingredient("apple",2000,300,new Texture("apple.png"),-1));

        objects.add(vent);
        objects.add(vent1);

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

        customers = new Array();
        customers.add(new Customer(0f,1f,1f,0.7f,new Texture("rockoReal.png"),world, canvas));

        gatherAssets();
        setDebug(false);
        setComplete(false);
    }
    @Override
    public void gatherAssets(){
        background = new Texture("groceryfloor.png");
        winPic = new Texture("win.png");
    }

    @Override
    public void reset() {
        for(Obstacle obj : guards) {
            obj.deactivatePhysics(world);
        }
        for(Obstacle obj : customers){
            obj.deactivatePhysics(world);
        }
        objects.clear();
        addQueue.clear();
        world.dispose();

        world = new World(new Vector2(0,0),false);
        setComplete(false);
        populateLevel();
    }

    @Override
    public void update(float dt) {
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
            //idk man
        }
//		if (player.getX() >= 32) {
//			win = true;
//		}

        player.setSpace(input.getSpace());
        player.setInteraction(input.getInteraction());
        collision.processBounds(player);
        collision.processGuards(player,guards);
        collision.processIngredients(player,objects);
        collision.handleCollision(player,trash);
        collision.handleCollision(player, vent);
        collision.handleCollision(player, vent1);
        player.getInventory().setSelected((int) input.getScroll());
        float delta = Gdx.graphics.getDeltaTime();
        for (Guard guard : guards) {
            guard.update(delta, generatePlayerInfo());
        }


        //if the player is in a teleporting state
        if(player.getTeleporting()){
            //check which vent is being teleported to
            if(vent.getBeingTeleportedTo()){
                //translate the camera's position based on the distance between both vents
                canvas.translateCamera(vent.calculateCameraTranslation());
                vent.setBeingTeleportedTo(false);
            }
            if(vent1.getBeingTeleportedTo()){
                canvas.translateCamera(vent1.calculateCameraTranslation());
                vent1.setBeingTeleportedTo(false);
            }

            //player isn't teleporting anymore
            player.setTeleporting(false);
        }

    }

    //guess what this does!
    private void populateLevel(){

    }

    public void draw(){
        if (win){
            canvas.draw(winPic, Color.WHITE, 15, 15,
                    0, 0, 0.0f, 2.8f, 3f);
            return;
        }
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        canvas.draw(background, Color.WHITE, 0, 0,
                background.getWidth(), 0, 0.0f, 1f, 1f);
        //b.draw(canvas, 0.1f, 0.1f);
        for(Guard g : guards){
            g.draw(0.1f,0.1f);
        }
        for(Customer c : customers){
            c.draw(0.25f,0.25f);
        }

        player.draw(0.25f,0.25f);
        trash.draw(canvas);


        for (GameObject i : objects){
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
        for(Customer c : customers){
            c.debug(canvas);
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
