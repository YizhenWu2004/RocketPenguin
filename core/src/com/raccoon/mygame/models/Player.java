package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.objects.Shadow;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;

public class Player extends BoxObstacle {

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

    //-1 is right
    private int direction = -1;


    //objects should store their own animations
    public FilmStrip playerWalk = new FilmStrip(new Texture("720/rockorun.png"), 1, 5, 5);
    public FilmStrip playerIdle = new FilmStrip(new Texture("720/rockoidle.png"), 1, 1, 1);


    private float height;
    private float width;

    private Shadow shadow;
//    b = new BoxObstacle(1,1);
//		b.setDensity(1.0f);
//		b.activatePhysics(world);
//		b.setDrawScale(canvas.getWidth()/WORLD_WIDTH, canvas.getHeight()/WORLD_HEIGHT); // Pixel width / world width

    public Player(float x, float y, float width, float height,
                  FilmStrip defaultPlayerSprite, Inventory inventory,
                  GameCanvas canvas, World world) {
        super(width, height);
        this.inventory = inventory;
        this.dishInventory = new DishInventory(new Texture("720/inventorynew.png"));
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

        //Because we dont have a loader yet, we need to have a default sprite.
        this.sprite = defaultPlayerSprite;
        NUM_ANIM_FRAMES = defaultPlayerSprite.getSize();

        this.height = height;
        this.width = width;

        shadow = new Shadow(x,y,1.4f,1.4f);
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
                float shadowX = getX()-getWidth() / 2;
                float shadowY = getY() - getHeight() / 2;
                shadow.setPosition(shadowX*40, shadowY*40);
            }
            else{
                float shadowX = getX()-getWidth()*1.2f;
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
        if(this.getVX() > 0)
            this.direction = -1;
        if(this.getVX() < 0)
            this.direction = 1;
        if(this.getVX() == 0)
            this.direction = this.direction;
    }
    private int getDirection(){return direction;}

    //draw with scale
    public void draw(float scaleX, float scaleY) {
        System.out.println(this.drawScale.x +  " " +this.drawScale.y);

        shadow.draw(canvas);
        setDirection();
        //not sure why the x offset needs to be 200 for it to look right
        drawSprite(canvas, scaleX*this.getDirection(), scaleY, (float)this.sprite.getRegionWidth()/2, 20);
//        canvas.draw(this.playerSprite,Color.WHITE,0,-200,this.getX(),this.getY(),0,scaleX,scaleY);
        this.inventory.draw(canvas);
        if(dishInventory.leftFilled()){
            if(direction == -1){
                dishInventory.get(0).draw(canvas, (this.getX()-2) * 40 , (this.getY()+1) * 40, 1,1, 0f, 0);
            }
            else{
                dishInventory.get(0).draw(canvas, (this.getX()-2.3f) * 40 , (this.getY()+1) * 40, 1,1, 0f, 0);
            }
        }
        if(dishInventory.rightFilled()){
            dishInventory.get(1).draw(canvas, (this.getX()+0.5f) * 40, (this.getY()+1) * 40,1,1, 0,0);
        }
    }

    public void clearInv() {
        inventory.clearAll();
    }

}
