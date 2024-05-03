package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.SoundController;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.Shadow;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.obstacle.CapsuleObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

public class Player extends CapsuleObstacle {

    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;
    private final int WORLD_WIDTH = 32;
    private final int WORLD_HEIGHT = 18;

    //the interaction boolean from the InputController to be used to handle inputs
    public boolean interaction;

    //this should be a list of game objects instead
    //holds 3 items at a time
    public Inventory inventory;
    public DishInventory dishInventory;

    private GameCanvas canvas;



    public boolean space;

    //not sure if this should go here uhhh
    private boolean isTeleporting = false;

    private boolean isSwiping = false;

    //-1 is right
    public int direction = -1;

    public int current = -3;

    private float height;
    private float width;

    /**
     * All of these are used for animation state
     * */
    public boolean playerIsCooking = false;
    public boolean playerIsVenting = false;
    public int potCookingIn = -1;

    private Shadow shadow;

    private Shadow shadowStanding;

    public Boolean justDied;

//    public Boolean respawning;

    private boolean ignoreInput = false;

    private SoundController sounds;
    public boolean respawning;

    public boolean isIgnoreInput() {
        return ignoreInput;
    }

    public void setIgnoreInput(boolean ignoreInput) {
        this.ignoreInput = ignoreInput;
    }

    public boolean stopDrawing = false;
//    b = new BoxObstacle(1,1);
//		b.setDensity(1.0f);
//		b.activatePhysics(world);
//		b.setDrawScale(canvas.getWidth()/WORLD_WIDTH, canvas.getHeight()/WORLD_HEIGHT); // Pixel width / world width

    public Player(float x, float y, float width, float height,
                  FilmStrip defaultPlayerSprite, Inventory inventory,
                  GameCanvas canvas, World world, SoundController s) {
        super(width, height);
        sounds = s;
        this.inventory = inventory;
        this.dishInventory = new DishInventory(new Texture("720/inventorynew.png"), sounds);
//        setTexture(new TextureRegion(texture));
        this.canvas = canvas;
        setFixedRotation(true);
        setDensity(1);
        setFriction(0);
        setLinearDamping(0);
        setPosition(x, y);
        activatePhysics(world);
        this.setBodyType(BodyType.DynamicBody);
        setDrawScale(canvas.getWidth() / WORLD_WIDTH, canvas.getHeight() / WORLD_HEIGHT);
        this.getBody().setUserData(this);

        //EVERY (animated) OBJECT NEEDS TO HAVE A DEFAULT SPRITE
        this.sprite = defaultPlayerSprite;
        //We also require a number of animation frames (just cant be null)
        NUM_ANIM_FRAMES = defaultPlayerSprite.getSize();

        this.height = height;
        this.width = width;

        shadow = new Shadow(x,y,1.4f,1.4f);

        shadowStanding = new Shadow(x,y,1f,1f);

        justDied = false;
        respawning = false;
    }
    public void update(float delta) {
        updateShadow();

    }

    private void updateShadow() {
        if(dishInventory.oneFilled()){
            if(direction == -1){
                float shadowX = getX()-getWidth();
                float shadowY = getY() - getHeight() / 2;
                shadow.setPosition(shadowX*40, shadowY*40);
            }
            else{
                float shadowX = getX()-getWidth()*1.2f;
                float shadowY = getY() - getHeight() / 2;
                shadow.setPosition(shadowX*40, shadowY*40);
            }
        }
        else{
            if(direction == -1){
                float shadowX = getX()-getWidth();
                float shadowY = getY() - getHeight() / 2;
                shadow.setPosition(shadowX*40, shadowY*40);
            }
            else{
                float shadowX = getX()-getWidth()*1.8f;
                float shadowY = getY() - getHeight() / 2;
                shadow.setPosition(shadowX*40, shadowY*40);
            }
        }

    }

    //im not adding a pick up method because the inventory seems to handle that just fine?
    public void pickUpItem(Ingredient object) {
        //this is dependent on whether or not this method will exist which I am not sure if it does lol
        this.inventory.add(object);
    }

    //does what you think it would
    public void removeItem() {
        this.inventory.drop();
    }

    public void setInteraction(boolean interaction) {
        this.interaction = interaction;
    }

    public boolean getInteraction() {
        return this.interaction;
    }

    public void setSpace(boolean space) {
        this.space = space;
    }

    public boolean getSpace() {
        return this.space;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setTeleporting(boolean isTeleporting) {
        this.isTeleporting = isTeleporting;
    }

    public boolean getTeleporting() {
        return this.isTeleporting;
    }


    private void setDirection(){
        if(this.getVX() > 1)
            this.direction = -1;
        if(this.getVX() < -1)
            this.direction = 1;
        if(this.getVX() == 0)
            this.direction = this.direction;
    }
    private int getDirection(){return direction;}

    //draw with scale
    public void draw(float scaleX, float scaleY) {

        if(!stopDrawing && !respawning){
            if(dishInventory.oneFilled()) {
                shadow.draw(canvas,0.8f,0.8f);
            }
            else if(current == 1){
                shadow.draw(canvas,0.8f,0.8f);
            }
            else{
                shadow.draw(canvas);
            }
        }

        setDirection();
        //not sure why the x offset needs to be 200 for it to look right
        if(stopDrawing == false){
            if(respawning){
                drawSprite(canvas, -scaleX*this.getDirection(), scaleY, 0.1f, 7f, true);
            }
            else{
                drawSprite(canvas, scaleX*this.getDirection(), scaleY, (float)this.sprite.getRegionWidth()/2, 20);
            }
        }

//        canvas.draw(this.playerSprite,Color.WHITE,0,-200,this.getX(),this.getY(),0,scaleX,scaleY);
        //inventory draw moved to Store and Restaurant
//        this.inventory.draw(canvas);
        if(dishInventory.leftFilled()){
            if(direction == -1){
                dishInventory.get(0).draw(canvas, (this.getX()-2) * 40 , (this.getY()+1) * 40, 1,1, 0f, 0, true);
            }
            else{
                dishInventory.get(0).draw(canvas, (this.getX()-2.3f) * 40 , (this.getY()+1) * 40, 1,1, 0f, 0,true);
            }
        }
        if(dishInventory.rightFilled()){
            dishInventory.get(1).draw(canvas, (this.getX()+0.5f) * 40, (this.getY()+1) * 40,1,1, 0,0, false);
        }
    }

    public void clearInv() {
        inventory.clearAll();
    }

    public void setPotCookingIn(int pot){
        this.potCookingIn = pot;
    }

    public void setSwiping(boolean swipe){
        this.isSwiping = swipe;
    }
    public boolean getSwiping(){
        return this.isSwiping;
    }

}
