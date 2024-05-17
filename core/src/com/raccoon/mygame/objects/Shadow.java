package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.view.GameCanvas;

public class Shadow {
    public static float texture_sx = 1f;
    public static float texture_sy = 1f;

    public String type;

    private float x;
    private float y;
    private Texture texture;

    public Shadow(float x, float y, float xScale, float yScale, AssetDirectory directory) {
        this.x = x;
        this.y = y;
        this.texture = directory.getEntry("shadow",Texture.class);
        texture_sx = xScale;
        texture_sy = yScale;
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

    public void draw(GameCanvas canvas,float scaleX, float scaleY) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                x, y, 0.0f, texture_sx*scaleX, texture_sx*scaleY);
    }

    public void draw(GameCanvas canvas, float scaleX, float scaleY, float ox, float oy) {
        float originX = texture.getWidth() / 2.0f;
        float originY = texture.getHeight() / 2.0f;
        canvas.draw(texture, Color.WHITE, 10, 10,
                x+ox, y+oy, 0.0f, texture_sx*scaleX, texture_sx*scaleY);
//        canvas.draw(texture,Color.WHITE,10,10,x+ox,y+oy,texture_sx*scaleX,texture_sx*scaleY);
    }
}
