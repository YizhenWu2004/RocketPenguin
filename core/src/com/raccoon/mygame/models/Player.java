package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.raccoon.mygame.objects.GameObject;

//this is just the directory looking at elmo's commit.
//I don't think she's uploaded it to main yet.
import com.raccoon.mygame.inventory;

public class Player {

    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    //this should be a list of game objects instead
    //holds 3 items at a time
    private Inventory inventory;

    private Texture playerTexture;

    public Player(float x, float y, float width, float height, Texture texture, Inventory inventory){
        this.width = width;
        this.height = height;
        this.inventory = inventory;
        playerTexture = texture;
        position = new Vector2(x, y);
    }

    //Setters
    public void setX(float x){position.x = x;}
    public void setY(float y){position.y = y;}
    public void setPosition(Vector2 position){this.position = position;}

    //Getters
    public float getX(){return this.position.x;}
    public float getY(){return this.position.y;}
    public Vector2 getPosition(){return this.position;}

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
        this.position.x += xOffset;
        this.position.y += yOffset;
    }

    //im not adding a pick up method because the inventory seems to handle that just fine?
    public void pickUpItem(GameObject object){
        //this is dependent on whether or not this method will exist which I am not sure if it does lol
        this.inventory.add(object);
    }

    //does what you think it would
    public void removeItem(){
        this.inventory.drop();
    }
}
