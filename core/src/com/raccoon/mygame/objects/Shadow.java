package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Shadow {
    private static float texture_sx = 1f;
    private static float texture_sy = 1f;

    public String type;

    private float x;
    private float y;
    private Texture texture;

    public Shadow(float x, float y, float xScale, float yScale) {
        this.x = x;
        this.y = y;
        this.texture = new Texture("720/"+ "shadow" + ".png");
        texture_sx = texture_sx*xScale;
        texture_sy = texture_sy*yScale;
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
        return texture.getWidth() * texture_sx;
    }

    public float getTextureHeight() {
        return texture.getHeight() * texture_sy;
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                x, y, 0.0f, texture_sx, texture_sy);
    }
}
