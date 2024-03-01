package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.raccoon.mygame.view.GameCanvas;

public class Guard {
    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    private Texture patrolTexture;

    Guard(float x, float y, float width, float height, Texture texture){
        position = new Vector2(0.0f, 0.0f);
        velocity = new Vector2(0.0f, 0.0f);
        this.width = width;
        this.height = height;
        patrolTexture = texture;
    }

    //Setters
    private void setX(float x){position.x = x;}
    private void setY(float y){position.y = y;}
    private void setPosition(Vector2 position){this.position = position;}

    public void setVX(float value) {
        velocity.x = value;
    }

    public float getVY() {
        return velocity.y;
    }

    //Getters
    private float getX(){return this.position.x;}
    private float getY(){return this.position.y;}
    private Vector2 getPosition(){return this.position;}

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getVX() {
        return velocity.x;
    }

    public void setVY(float value) {
        velocity.y = value;
    }

    public void update(float delta) {
        position.add(velocity);
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(patrolTexture, Color.WHITE, 10, 10,
                position.x, position.y, 0.0f, 1.0f, 1.f);
    }

    //ADD PATROL AI CONTROLLER???
}
