
package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Ingredient implements GameObject{
    public int name;
    public String type;
    private float x;
    private float y;
    private Texture texture;
    private boolean isActive;

    //position in inventory, -1 means not in inventory (and thus discarded)
    private int posInInventory;

    //position in pot, -1 means not in pot
    private int posInPot;

    public Ingredient(String name, float x, float y, Texture texture, int posInInventory) {
        this.type = name;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.isActive = true;
        this.posInInventory = posInInventory;
        this.posInPot = -1;
        this.name = 0;
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

    @Override
    public void discard() {
        isActive = false;
        posInInventory = -1;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public int posInInventory() {
        return posInInventory;
    }
    public void setPosInv(int index){
        posInInventory = index;
    }

    public int posInPot(){
        return posInPot;
    }

    public void inPot(int index){
        posInPot = index;
    }

    public void draw(GameCanvas canvas){
        if (posInInventory < 0) {
            canvas.draw(texture, Color.WHITE, 10, 10,
                    x, y, 0.0f, 0.2f, 0.2f);
        }
    }
}



