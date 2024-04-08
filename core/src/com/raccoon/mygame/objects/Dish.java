
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
    public int dishSize;
    private Texture one = new Texture("singleorder.png");
    private Texture two = new Texture("doubleorder.png");
    private Texture three = new Texture("tripleorder.png");


    public Dish(Ingredient[] type, Texture texture, int posInInventory) {
        this.type = type;
        size = type.length;
        this.texture = texture;
        this.isActive = true;
        this.posInInventory = posInInventory;
        this.name = 0;
        dishSize =0;
        for(Ingredient i : type){
            if (i != null){
                dishSize += 1;
            }
        }
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
        if (dishSize == 1){
            canvas.draw(one, Color.WHITE, ox-20, oy-50,x, y, 0, 0.8f, 0.8f);
        } else if (dishSize == 2){
            canvas.draw(two, Color.WHITE, ox-20, oy-50,x, y, 0, 0.8f, 0.8f);
        } else if (dishSize == 3){
            canvas.draw(three, Color.WHITE, ox-20, oy-50,x, y, 0, 0.8f, 0.8f);
        }
        for(int i = 0; i <dishSize; i++){
            type[i].drawTextBubble(canvas, x, y, -30-45*i, -55);
        }
        canvas.draw(texture, Color.WHITE, ox, oy,x, y, 0, sclx, scly);
    }
}
