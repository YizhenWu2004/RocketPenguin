package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.controllers.SoundController;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.view.GameCanvas;
import java.util.Arrays;

public class CookingInventory {
    public Texture texture;
    public Ingredient[] inv;
    private boolean[] filled;
    public int size;
    private SoundController sounds;

    public CookingInventory(Texture texture, SoundController s){
        this.texture = texture;
        inv = new Ingredient[3];
        filled = new boolean[3];
        size = 0;
        sounds = s;
    }

    public void clearAll(){
//        for(Ingredient i:inv){
//            i.discard();
//        }
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
                sounds.switchPlay();
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
        float midpoint = (float)canvas.getWidth()/2 - (float)this.texture.getWidth()/2;
        canvas.draw(texture, Color.WHITE, 10, 10,
                midpoint, 80, 0.0f, 1, 1);
        for(int i = 0; i < inv.length; i++){
            if (filled[i]){
                canvas.draw(inv[i].getTexture(), Color.WHITE, 10, 10,i * (texture.getWidth()/3.45f) + midpoint+ 33, 100, 0.0f, 1, 1);
            }
        }
    }

    public boolean isEmpty(){
        return (filled[0] == false);
    }

}
