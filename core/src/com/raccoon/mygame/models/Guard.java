package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.raccoon.mygame.controllers.GuardAIController;
import com.raccoon.mygame.view.GameCanvas;

public class Guard {
    protected static final float TEXTURE_SX = 0.5f;
    protected static final float TEXTURE_SY = 0.5f;
    private Vector2 position;

    private float width;
    private float height;

    private Vector2 velocity;

    private Texture patrolTexture;

    private GuardAIController aiController;



    public Guard(float x, float y, float width, float height, Texture texture){
        position = new Vector2(x, y);
        velocity = new Vector2(0.0f, 0.0f);
        this.width = width;
        this.height = height;
        patrolTexture = texture;

        float leftBoundary = 0;
        float rightBoundary = 300;
        float speed = 60;
        this.aiController = new GuardAIController(leftBoundary, rightBoundary, speed);
    }

    //Setters
    public void setX(float x){position.x = x;}
    public void setY(float y){position.y = y;}
    public void setPosition(Vector2 position){this.position = position;}

    public void setVX(float value) {
        velocity.x = value;
    }

    public float getVY() {
        return velocity.y;
    }

    //Getters
    public float getX(){return this.position.x;}
    public float getY(){return this.position.y;}
    public Vector2 getPosition(){return this.position;}

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getVX() {
        return velocity.x;
    }

    public void setVY(float value) {
        velocity.y = value;
    }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public float getTextureWidth() { return patrolTexture.getWidth() * TEXTURE_SX; }
    public float getTextureHeight() { return patrolTexture.getHeight() * TEXTURE_SY; }

    public void update(float delta) {
        position.add(velocity);
        if (aiController != null) {
            position = aiController.updatePosition(new Vector2(position), delta);
        }
    }

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public void draw(GameCanvas canvas) {
        canvas.draw(patrolTexture, Color.WHITE, 10, 10,
                position.x, position.y, 0.0f, TEXTURE_SX, TEXTURE_SY);

        float debugX = position.x;
        float debugY = position.y;
        float debugWidth = getTextureWidth();
        float debugHeight = getTextureHeight();
        Color debugColor = Color.RED;

        canvas.drawDebugRectangle(debugX, debugY, debugWidth, debugHeight, debugColor);
    }

}
