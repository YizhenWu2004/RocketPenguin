package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.raccoon.mygame.view.GameCanvas;

public class Trash {
    protected static final float TEXTURE_SX = 0.5f;
    protected static final float TEXTURE_SY = 0.5f;
    private Vector2 position;

    private float width;
    private float height;

    private Texture trashTexture;

    public Trash(float x, float y, float width, float height, Texture texture){
        position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        trashTexture = texture;
    }

    //Setters
    public void setX(float x){position.x = x;}
    public void setY(float y){position.y = y;}
    public void setPosition(Vector2 position){this.position = position;}

    //Getters
    public float getX(){return this.position.x;}
    public float getY(){return this.position.y;}
    public Vector2 getPosition(){return this.position;}

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public float getTextureWidth() { return trashTexture.getWidth() * TEXTURE_SX; }
    public float getTextureHeight() { return trashTexture.getHeight() * TEXTURE_SY; }

    public void draw(GameCanvas canvas) {
        canvas.draw(trashTexture, Color.WHITE, 10, 10,
                position.x, position.y, 0.0f, TEXTURE_SX, TEXTURE_SY);
    }
}
