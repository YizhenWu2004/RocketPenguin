
package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Dish implements GameObject {
    public int name;
    public Ingredient[] type;
    //public int size; //size of dish, from 1-3
    private Texture texture;
    private boolean isActive;

    //position in inventory, -1 means not in inventory (and thus discarded)
    private int posInInventory;
    public int dishSize;
    private Texture one = new Texture("singleorder.png");
    private Texture two = new Texture("doubleorder.png");
    private Texture three = new Texture("tripleorder.png");
    private Texture wok =new Texture("wok.png");
    private Texture pot = new Texture("pot.png");
    private Texture cutting_board = new Texture("cutting_board.png");
    public int station_type;


    public Dish(Ingredient[] type, Texture texture, int posInInventory, int station_type) {
        this.type = type;
        //size = type.length;
        this.texture = texture;
        this.isActive = true;
        this.posInInventory = posInInventory;
        this.name = 0;
        dishSize =0;
        this.station_type = station_type;
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
            canvas.draw(two, Color.WHITE, ox-20, oy-50,x, y, 0, 1f, 0.8f);
        } else if (dishSize == 2){
            canvas.draw(three, Color.WHITE, ox-20, oy-50,x, y, 0, 0.9f, 0.8f);
        } else if (dishSize == 3){
            canvas.draw(three, Color.WHITE, ox-20, oy-50,x, y, 0, 1.1f, 0.8f);
        }
        if(station_type == 0) {
            canvas.draw(wok, Color.WHITE, -30,-50,x, y,
                    0.0f, 1f, 1f);
        }else if(station_type == 1){
            canvas.draw(pot, Color.WHITE, -30,-50,x, y,
                    0.0f, 1f, 1f);
        } else {
            canvas.draw(cutting_board, Color.WHITE, -30,-50,x, y,
                    0.0f, 1f, 1f);
        }
        for(int i = 0; i <dishSize; i++){
            type[i].drawTextBubble(canvas, x, y, -40-50*(i+1), -55);
        }
        canvas.draw(texture, Color.WHITE, ox, oy,x, y, 0, sclx, scly);
    }

    public int getScore(){
        int score = 0;
        if (dishSize == 1){
            score = 10;
        } else if (dishSize == 2){
            score = 20;
        } else {
            score = 30;
        }
        if (station_type == 0){
            score *= 1.5;
        } else if (station_type == 1){
            score *= 2;
        }
        return score;

    }
}
