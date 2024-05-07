
package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.view.GameCanvas;

public class Ingredient implements GameObject, Comparable<Ingredient> {

    //Initially 0.2, changed to 1
    private AssetDirectory directory;
    private float defaultSX = 1f;
    private float defaultSY = 1f;
    private float TEXTURE_SX = 1f;
    private float TEXTURE_SY = 1f;
    private Texture jalapeno;

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

    public Ingredient(String name, float x, float y, Texture texture, int posInInventory, AssetDirectory directory) {
        jalapeno = directory.getEntry("o_jalapeno", Texture.class);
        this.directory = directory;
        this.type = name;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.isActive = true;
        this.posInInventory = posInInventory;
        this.posInPot = -1;
        this.name = 0;
    }

    public Ingredient(String name, Texture texture, int posInInventory){
        this.type = name;
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

    public void setPosInv(int index) {
        posInInventory = index;
    }

    public int posInPot() {
        return posInPot;
    }
    public void setPosInPot(int index) {
        posInPot = index;
    }


    public float getTextureWidth() {
        return texture.getWidth() * TEXTURE_SX;
    }

    public float getTextureHeight() {
        return texture.getHeight() * TEXTURE_SY;
    }

    public void setSX(float sx){
        this.TEXTURE_SX = sx;
    }
    public void setSY(float sy){
        this.TEXTURE_SY = sy;
    }
    public void resetScales(){
        this.TEXTURE_SX = defaultSX;
        this.TEXTURE_SY = defaultSY;
    }

    @Override
    public void draw(GameCanvas canvas) {
        if(posInInventory == 100){
            return;
        }
        if (posInInventory < 0) {
            canvas.draw(texture, Color.WHITE, 10, 10,
                    x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);
        }
    }

    public void drawTextBubble(GameCanvas canvas, float x, float y, float ox, float oy) {
        Texture texture = this.texture;
        if(this.type == "greenpepper") {
          texture = jalapeno;
        }
        canvas.draw(texture, Color.WHITE, ox, oy,
                x, y, 0.0f, 0.7f, 0.7f);
    }

    @Override
    public int compareTo(Ingredient other) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.type, other.type);
    }

    public Ingredient clone(){
        return new Ingredient(this.type,x,y,directory.getEntry(this.type, Texture.class), posInInventory, directory);
    }
}



