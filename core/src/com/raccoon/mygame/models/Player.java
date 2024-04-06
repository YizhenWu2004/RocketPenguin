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


    /** How fast we change frames (one frame per 4 calls to update) */
    private static final float ANIMATION_SPEED = 0.10f;
    /** The number of animation frames in our filmstrip */
    private int   NUM_ANIM_FRAMES = 1;
    //current animation frame
    private float animeframe = 0;


//    b = new BoxObstacle(1,1);
//		b.setDensity(1.0f);
//		b.activatePhysics(world);
//		b.setDrawScale(canvas.getWidth()/WORLD_WIDTH, canvas.getHeight()/WORLD_HEIGHT); // Pixel width / world width

    public Player(float x, float y, float width, float height, FilmStrip defaultPlayerSprite, Inventory inventory,
                  GameCanvas canvas, World world) {
        super(width, height);
        this.inventory = inventory;
        this.dishInventory = new DishInventory(new Texture("inventorybar.png"));
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

    public FilmStrip getFilmStrip() {
        return sprite;
    }

    public void setFilmStrip(FilmStrip value) {
        NUM_ANIM_FRAMES = value.getSize();
        sprite = value;
        //set to 0th frame
        if(animeframe > NUM_ANIM_FRAMES)
            animeframe = 0;
        sprite.setFrame((int)animeframe);
    }
    public void setFrame(int frameNumber){
        sprite.setFrame(frameNumber);
    }
    public void update(float delta){
        // Increase animation frame
        animeframe += ANIMATION_SPEED;
        if (animeframe >= NUM_ANIM_FRAMES) {
            animeframe -= NUM_ANIM_FRAMES;
        }
    }

    //draw with scale
    public void draw(float scaleX, float scaleY) {
        //System.out.println(scaleX+";"+scaleY);

        //not sure why the x offset needs to be 200 for it to look right
        drawSprite(canvas, scaleX, scaleY, (float)this.sprite.getRegionWidth()/2, 20);
//        canvas.draw(this.playerSprite,Color.WHITE,0,-200,this.getX(),this.getY(),0,scaleX,scaleY);
        this.inventory.draw(canvas);
        if(dishInventory.leftFilled()){
            dishInventory.get(0).draw(canvas, this.getX() , this.getY(), 0.25f,0.25f, 30f, 0);
        }
        if(dishInventory.rightFilled()){
            dishInventory.get(1).draw(canvas, this.getX(), this.getY(),0.25f,0.25f, -30f,0);
        }
    }

    public void clearInv() {
        inventory.clearAll();
    }

}
