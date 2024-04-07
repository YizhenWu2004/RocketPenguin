package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Expression {
    private static final float TEXTURE_SX = 1f;
    private static final float TEXTURE_SY = 1f;

    public String type;

    private float x;
    private float y;
    private Texture texture;

    public Expression(String name, float x, float y) {
        this.type = name;
        this.x = x;
        this.y = y;
        if(name == "exclamation"){
            this.texture = new Texture("720/"+ "exclamation" + ".png");
        }
    }

    public float getXPosition() {
        return x;
    }


    public float getYPosition() {
        return y;
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getTextureWidth() {
        return texture.getWidth() * TEXTURE_SX;
    }

    public float getTextureHeight() {
        return texture.getHeight() * TEXTURE_SY;
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);
    }
}
