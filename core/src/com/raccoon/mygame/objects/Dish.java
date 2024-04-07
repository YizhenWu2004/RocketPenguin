
package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Dish implements GameObject {
    public int name;
    public Ingredient[] type;
    public int size; //size of dish, from 1-3
    private Texture texture;
    private boolean isActive;

    //position in inventory, -1 means not in inventory (and thus discarded)
    private int posInInventory;

    public Dish(Ingredient[] type, Texture texture, int posInInventory) {
        this.type = type;
        size = type.length;
        this.texture = texture;
        this.isActive = true;
        this.posInInventory = posInInventory;
        this.name = 0;
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

    @Override
    public void draw(GameCanvas canvas) {

        //doesn't draw anything yet
        return;
    }

    public boolean check(Dish dish){
        if(this.type.length != dish.type.length){
            return false;
        }
        for(int i = 0; i < dish.type.length; i++){
            if (this.type[i].name != dish.type[i].name){
                return false;
            }
        }
        return true;
    }

    public void draw(GameCanvas canvas, float x, float y, float sclx, float scly, float ox, float oy){
        canvas.draw(texture, Color.WHITE, ox, oy,x, y, 0, sclx, scly);
    }
}
