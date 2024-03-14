package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.VentObstacle;
import com.raccoon.mygame.objects.NormalObstacle;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class StoreController extends WorldController implements ContactListener {
    private World world;
    private GameCanvas canvas;
    private Texture background;
    private InputController input;
    private Player player;
    private Array<Ingredient> ingredients;
    private Array<Guard> guards;

    private VentObstacle vent1;
    private boolean ventCollision;

    private int globalIndex = 1;
    private Vector2 localStartingPos;

    private Array<NormalObstacle> obstacles;
    public boolean playerGuardCollide;
    private CollisionController collision;
    boolean active;
    private void addShelfHorizontal(float x, float y) {
        obstacles.add(new NormalObstacle(x, y, 5.25f, 1f, 0.25f, 0.25f, 0f, -100f,
                new Texture("groceryshelfhorizontal.png"),world, canvas));
    }
    private void addShelfVertical(float x, float y) {
        obstacles.add(new NormalObstacle(x, y, 1f, 6f, 0.3f, 0.3f, 0f, 0f,
                new Texture("groceryshelfvertical.png"),world, canvas));
    }
    private void addFruitCrate(float x, float y) {
        obstacles.add(new NormalObstacle(x, y, 2f, 1f, 0.4f, 0.4f, 0f, 0f,
                new Texture("fruitcrate.png"),world, canvas));
    }
    public StoreController(GameCanvas canvas, Texture texture, InputController input, Inventory sharedInv){
        world = new World(new Vector2(0,0), false);
        this.canvas = canvas;
        this.background = texture;
        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),sharedInv, canvas, world);
        this.input = input;
        ingredients = new Array<>();
        ingredients.add(new Ingredient("apple",200,200,new Texture("apple.png"),-1));
        ingredients.add(new Ingredient("banana",1600,300,new Texture("banana.png"),-1));
        ingredients.add(new Ingredient("greenpepper",1500,800,new Texture("greenpepper.png"),-1));
        ingredients.add(new Ingredient("orange",900,400,new Texture("orange.png"),-1));
        ingredients.add(new Ingredient("banana",1000,800,new Texture("banana.png"),-1));
        ingredients.add(new Ingredient("apple",2000,300,new Texture("apple.png"),-1));
        guards = new Array();

        vent1 = new VentObstacle(1.5f,1f, 1.5f,1.5f, 1, 1, 0, 0f, new Texture("vent.png"),world, canvas);
        localStartingPos = new Vector2(vent1.getX()+1.5f, vent1.getY());

        guards.add(new Guard(2.5f,5,1.67f,0.83f,new Texture("gooseReal.png"),world,canvas));
        guards.add(new Guard(25,13.3f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
        guards.add(new Guard(12.5f,6.67f,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
        guards.add(new Guard(23.3f,10,1.67f,0.83f,new Texture("gooseReal.png"),world, canvas));
        obstacles = new Array();
        addShelfHorizontal(2.5f, 16.5f);
        addShelfHorizontal(7.75f, 16.5f);
        addShelfHorizontal(13f, 16.5f);
        addShelfHorizontal(18.25f, 16.5f);
        addShelfHorizontal(23.5f, 16.5f);
        addShelfHorizontal(28.75f, 16.5f);

        addShelfHorizontal(7.5f, 12.5f);
        addShelfHorizontal(12.75f, 12.5f);
        addShelfHorizontal(18.5f, 12.5f);
        addShelfHorizontal(23.75f, 12.5f);

        addFruitCrate(28.5f, 5f);
        addFruitCrate(24f, 5f);
        addFruitCrate(28.5f, 9f);
        addFruitCrate(24f, 9f);
        addShelfHorizontal(23.5f, 0.5f);
        addShelfHorizontal(28.75f, 0.5f);

        addShelfHorizontal(3f, 8.5f);
        addShelfHorizontal(13f, 8.5f);
        addShelfHorizontal(18.25f, 8.5f);

        addShelfVertical(20.5f, 7f);
        addShelfVertical(10.5f, 2f);

        addShelfHorizontal(18.25f, 4.5f);

        addShelfVertical(32f, 14.75f);
        addShelfVertical(32f, 8.5f);
        addShelfVertical(32f, 2.25f);
        addShelfVertical(0f, 14.75f);
        addShelfVertical(0f, 8.5f);
        //addShelfVertical(0f, 2.25f);
        active = false;
        world.setContactListener(this);
        collision = new CollisionController(canvas.getWidth(), canvas.getHeight());
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
        if (playerGuardCollide){
            player.setPosition(0,0);
            player.clearInv();
            playerGuardCollide = false;
        }
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

        collision.processBounds(player);
        collision.processGuards(player,guards);
        collision.processIngredients(player,ingredients);
        world.step(1/60f, 6,2);

        Guard[] guards2 = new Guard[guards.size];
        for(int i = 0; i < guards.size; i++){
            guards2[i] = guards.get(i);
        }
        for(int i = 0; i < guards2.length - 1; i++){
            collision.processGuards(guards.get(i), guards2, i);
        }
    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        vent1.draw();
        for (NormalObstacle o : obstacles){
            o.draw();
        }
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
        vent1.drawDebug(canvas);
        for (NormalObstacle o : obstacles) {
            o.debug(canvas);
        }
    }

    public Array<Float> generatePlayerInfo() {
        Array<Float> playerInfo = new Array<Float>();
        playerInfo.add(player.getX());
        playerInfo.add(player.getY());
        return playerInfo;
    }


    @Override
    public void beginContact(Contact contact) {
        Body body1 = contact.getFixtureA().getBody();
        Body body2 = contact.getFixtureB().getBody();
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof Guard)|| (body2.getUserData() instanceof Player && body1.getUserData() instanceof Guard)){
            playerGuardCollide = true;
        }
        if ((body1.getUserData() instanceof Player && body2.getUserData() instanceof VentObstacle)|| (body2.getUserData() instanceof Player && body1.getUserData() instanceof VentObstacle)){
            System.out.println("colliding with vent");
            setVentCollision(true);
            //execute

        }
    }

    @Override
    public void endContact(Contact contact) {
        playerGuardCollide = false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int getIndex(){
        return this.globalIndex;
    }
    public void setIndex(int ind){
        globalIndex = ind;
    }

    public boolean getActive(){
        return this.active;
    }

    public boolean getVentCollision(){
        return this.ventCollision;
    }
    public void setVentCollision(boolean isColliding){
        this.ventCollision = isColliding;
    }

    /**
     * Called upon when this level is switched to.
     *
     * */
    public void onSet(){
        player.setPosition(localStartingPos);
    }
}
