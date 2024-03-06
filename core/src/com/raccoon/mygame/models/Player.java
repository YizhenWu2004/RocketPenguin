package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class Player {

    protected static final float TEXTURE_SX = 0.1f;
    protected static final float TEXTURE_SY = 0.1f;
    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    //the interaction boolean from the InputController to be used to handle inputs
    private boolean interaction;

    //this should be a list of game objects instead
    //holds 3 items at a time
    private Inventory inventory;

    private Texture playerTexture;
    private GameCanvas canvas;

    public BoxObstacle p;

    private boolean space;
    public Player(float x, float y, float width, float height, Texture texture, Inventory inventory,
            GameCanvas canvas, BoxObstacle b){
        this.width = width;
        this.height = height;
        this.inventory = inventory;
        playerTexture = texture;
        position = new Vector2(x, y);
        this.canvas = canvas;
        this.p = b;
        p.setBodyType(BodyType.DynamicBody);
        p.setFixedRotation(true);
    }

    //Setters
    public void setX(float x){p.setX(x);}
    public void setY(float y){p.setY(y);}
    public void setPosition(Vector2 position){
        p.setPosition(position);
    }

    //Getters
    public float getX(){return p.getX();}
    public float getY(){return p.getY();}
    public Vector2 getPosition(){return p.getPosition();}

    /**
     * Draws the player onto the given canvas
     * @param canvas The canvas to draw to
     * */
//    private void draw(GameCanvas canvas){
//        canvas.draw(playerTexture, this.position.x, this.position.y);
//    }

    /**
     * Moves the player's position given a specified offset
     * @param xOffset The x distance to move the player
     * @param yOffset The y distance to move the player
     * */
    public void move(float xOffset, float yOffset){
        //p.setPosition(p.getX() + xOffset, p.getY() + yOffset);
        p.setVX(xOffset);
        p.setVY(yOffset);
        //Body b= p.getBody();
        //b.setLinearVelocity(new Vector2(xOffset, yOffset));

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

    public float getTextureWidth() { return p.getWidth(); }
    public float getTextureHeight() { return p.getHeight(); }

    public void draw(){
        p.draw(canvas, 0.1f, 0.1f, 0, -250);
//        canvas.draw(playerTexture, Color.WHITE, 10, 10,
//                position.x, position.y, 0.0f, TEXTURE_SX, TEXTURE_SY);
        this.inventory.draw(canvas);

    }
    public void clearInv(){
        inventory.clearAll();
    }

}
