package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    //this should be a list of game objects instead
    //holds 3 items at a time
    private Integer[] inventory = new Integer[3];

    private Texture playerTexture;

    Player(float x, float y, float width, float height, Texture texture){
        this.width = width;
        this.height = height;
        playerTexture = texture;
        position = new Vector2(x, y);
    }

    //Setters
    private void setX(float x){position.x = x;}
    private void setY(float y){position.y = y;}
    private void setPosition(Vector2 position){this.position = position;}

    //Getters
    private float getX(){return this.position.x;}
    private float getY(){return this.position.y;}
    private Vector2 getPosition(){return this.position;}

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
    private void move(float xOffset, float yOffset){
        this.position.x += xOffset;
        this.position.y += yOffset;
    }

    //need to finish implementing this
    private void addItem(int item, int position){
        //if the inventory is full,
        //TO BE IMPLEMENTED
        return;
    }

    private void removeItem(int item, int position){
        //TO BE IMPLEMENTED
        return;
    }
}
