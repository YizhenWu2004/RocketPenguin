package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class Player extends BoxObstacle{

    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;

    //the interaction boolean from the InputController to be used to handle inputs
    private boolean interaction;

    //this should be a list of game objects instead
    //holds 3 items at a time
    private Inventory inventory;

    private Texture playerTexture;
    private GameCanvas canvas;


    private boolean space;

    //not sure if this should go here uhhh
    private boolean isTeleporting = false;


//    b = new BoxObstacle(1,1);
//		b.setDensity(1.0f);
//		b.activatePhysics(world);
//		b.setDrawScale(canvas.getWidth()/WORLD_WIDTH, canvas.getHeight()/WORLD_HEIGHT); // Pixel width / world width

    public Player(float x, float y, float width, float height, Texture texture, Inventory inventory, GameCanvas canvas/*, World world*/){
        super(width, height);
        this.inventory = inventory;
//        this.canvas = canvas;
        setTexture(new TextureRegion(texture));
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        setPosition(x,y);
//        activatePhysics(world);
        this.setBodyType(BodyType.DynamicBody);
        setDrawScale(canvas.getWidth()/WORLD_WIDTH, canvas.getHeight()/WORLD_HEIGHT);
//        this.getBody().setUserData(this);
    }

    public void postPhysicsInitialization() {
        if (this.getBody() != null) {
            this.getBody().setUserData(this);
        }
    }
    //im not adding a pick up method because the inventory seems to handle that just fine?
    public void pickUpItem(Ingredient object){
        //this is dependent on whether or not this method will exist which I am not sure if it does lol
        this.inventory.add(object);
    }


    //does what you think it would
    public void removeItem(){
        this.inventory.drop();
    }

    public void setInteraction(boolean interaction){
        this.interaction = interaction;
    }
    public boolean getInteraction(){
        return this.interaction;
    }

    public void setSpace(boolean space){
        this.space = space;
    }
    public boolean getSpace(){
        return this.space;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setTeleporting(boolean isTeleporting){
        this.isTeleporting = isTeleporting;
    }
    public boolean getTeleporting(){
        return this.isTeleporting;
    }


    //draw with scale
//    public void draw(float scaleX, float scaleY){
//        draw(canvas, scaleX, scaleY, 0, -200);
//        this.inventory.draw(canvas);
//    }
    public void clearInv(){
        inventory.clearAll();
    }

}
