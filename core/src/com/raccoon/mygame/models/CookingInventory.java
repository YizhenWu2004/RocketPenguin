package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.view.GameCanvas;
import java.util.Arrays;

public class CookingInventory {
    public Texture texture;
    public Ingredient[] inv;
    private boolean[] filled;
    public int size;

    public CookingInventory(Texture texture){
        this.texture = texture;
        inv = new Ingredient[3];
        filled = new boolean[3];
        size = 0;

    }

    public void clearAll(){
        inv = new Ingredient[3];
        filled = new boolean[3];
        size = 0;
    }

    public Ingredient[] drop(){
        return Arrays.copyOf(inv,inv.length);
    }

    public void add(Ingredient ingredient){
        for (int i = 0; i < inv.length; i++) {
            if (!filled[i]) {
                inv[i] = ingredient;
                filled[i] = true;
                inv[i].setPosInv(100);
                inv[i].setPosInPot(i);
                inv[i].setPosition(-100, -100);
                size += 1;
                break;
            }
        }
    }



    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                1400, 600, 0.0f, 1, 1);
        for(int i = 0; i < inv.length; i++){
            if (filled[i]){
                canvas.draw(inv[i].getTexture(), Color.WHITE, 10, 10, 1410+ inv[i].posInPot()*90, 610, 0.0f, 1, 1);
            }
        }
    }




}
