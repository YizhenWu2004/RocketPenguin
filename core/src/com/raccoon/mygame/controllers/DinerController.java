package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.Obstacle;

public class DinerController extends WorldController{
    private Player player;
    private Inventory inv;

    private Ingredient apple;
    public Texture background = new Texture("background.png");

    public DinerController(){
        super(DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_GRAVITY);
        setDebug(false);
        setComplete(false);
        setFailure(false);
    }

    @Override
    public void reset() {
//        Vector2 gravity = new Vector2(world.getGravity());
//
//        for (Obstacle obj : objects) {
//            obj.deactivatePhysics(world);
//        }
//        objects.clear();
//        addQueue.clear();
//        world.dispose();

        world = new World(new Vector2(0,0), false);
        setComplete(false);
        setFailure(false);
        populateWorld();
    }

    public void populateWorld(){
        System.out.println("populating world!");
//        Inventory inv = new Inventory(new Texture("inventorybar.png"));
//        apple = new Ingredient("apple", 1000, 1000, new Texture("apple.png"), 1);
//        addNonPhysicsObject(apple);
//        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),inv, canvas, world);
//        background = new Texture("groceryfloor.png");
//        addObject(player);
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
