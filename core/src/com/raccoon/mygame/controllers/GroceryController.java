package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.obstacle.Obstacle;
import com.raccoon.mygame.view.GameCanvas;

public class GroceryController extends WorldController{
    private Player player;
    private Inventory inv;

    private Ingredient apple;
    public Texture background = new Texture("groceryfloor.png");

    private InputController input;

    private Vector2 velCache;
    private GameCanvas canvas;


    public GroceryController(GameCanvas canvas){

        super(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_GRAVITY);
        setDebug(false);
        setComplete(false);
        setFailure(false);

        //desperate times call for desperate measures, THROW EVERYTHIN IN CONSTRUCTOR
        input = new InputController();
        velCache = new Vector2(0,0);

        Inventory inv = new Inventory(new Texture("inventorybar.png"));
//        addNonPhysicsObject(inv);

        apple = new Ingredient("apple", 1000, 1000, new Texture("apple.png"), 1);
        addNonPhysicsObject(apple);

        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),inv, canvas);
        background = new Texture("groceryfloor.png");
        addObject(player);
        player.getBody().setUserData(player);

        BoxObstacle obstacle = new BoxObstacle(100, 100, 100, 100);
        obstacle.setTexture(new TextureRegion(new Texture("apple.png")));
        addObject(obstacle);

        this.canvas = canvas;
    }

    @Override
    public void reset() {
        Vector2 gravity = new Vector2(world.getGravity() );

        for(Obstacle obj : objects) {
            obj.deactivatePhysics(world);
        }
        objects.clear();
        addQueue.clear();
        world.dispose();

        world = new World(gravity,false);
        setComplete(false);
        setFailure(false);

        populateWorld();
    }

    public void populateWorld(){
        input = new InputController();

        System.out.println("populating world!");
        Inventory inv = new Inventory(new Texture("inventorybar.png"));
//        addNonPhysicsObject(inv);

        apple = new Ingredient("apple", 1000, 1000, new Texture("apple.png"), 1);
        addNonPhysicsObject(apple);

        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),inv, canvas);
        background = new Texture("groceryfloor.png");
        addObject(player);

        BoxObstacle obstacle = new BoxObstacle(100, 100, 100, 100);
        obstacle.setTexture(new TextureRegion(new Texture("apple.png")));
        addObject(obstacle);
    }

    /**
     * Returns whether to process the update loop
     *
     * At the start of the update loop, we check if it is time
     * to switch to a new game mode.  If not, the update proceeds
     * normally.
     *
     * @param dt	Number of seconds since last animation frame
     *
     * @return whether to process the update loop
     */
    public boolean preUpdate(float dt) {
        if (!super.preUpdate(dt)) {
            return false;
        }

        return true;
    }

    @Override
    public void gatherAssets() {
        super.gatherAssets();
    }

    @Override
    public void update(float dt) {
        input.readInput();

        float x = 5f*input.getXMovement();
        float y =5f*input.getYMovement();
        velCache = velCache.set(x,y);
        System.out.println(velCache);
        player.setLinearVelocity(velCache);

        System.out.println(player.getPosition() + " Player pos");
    }

    public void draw(float dt) {
        canvas.clear();
        canvas.begin();

        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);

        for(Obstacle obj : objects) {
            obj.draw(canvas);
            System.out.println("I drew a " + obj.getClass().getSimpleName());
        }

        for(GameObject obj : nonPhysicsObjects){
            obj.draw(canvas);
            System.out.println("I drew a " + obj.getClass().getSimpleName());
        }
        canvas.end();

        if (isDebug()) {
            canvas.beginDebug();
            for(Obstacle obj : objects) {
                obj.drawDebug(canvas);
            }
            canvas.endDebug();
        }

    }
}
