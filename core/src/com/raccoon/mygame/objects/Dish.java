
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
    private Texture one = new Texture("order/one.png");
    private Texture two = new Texture("order/two.png");
    private Texture three = new Texture("order/three.png");
    private Texture wok =new Texture("order/wok.png");
    private Texture pot = new Texture("order/pot.png");
    private Texture cutting_board = new Texture("order/cutting_board.png");
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

    public void draw(GameCanvas canvas, float x, float y, float sclx, float scly, float ox, float oy, boolean isLeft){
        if(isLeft) {
            if (dishSize == 1) {
                canvas.draw(one, Color.WHITE, ox + 80, oy - 50, x, y, 0, 0.7f, 0.7f);
                drawCooking(canvas, x, y, 90, -55);
                drawOrder(canvas, x, y, 25, -55);
            } else if (dishSize == 2) {
                canvas.draw(two, Color.WHITE, ox + 140, oy - 50, x, y, 0, 0.7f, 0.7f);
                drawCooking(canvas, x, y, 160, -53);
                drawOrder(canvas, x, y, 30, -55);
            } else if (dishSize == 3) {
                canvas.draw(three, Color.WHITE, ox + 200, oy - 50, x, y, 0, 0.7f, 0.7f);
                drawCooking(canvas, x, y, 220, -53);
                drawOrder(canvas, x, y, 40, -55);
            }
            canvas.draw(texture, Color.WHITE, ox, oy, x, y, 0, sclx, scly);
        } else {
            if (dishSize == 1) {
                canvas.draw(one, Color.WHITE, ox + 180, oy - 50, x, y, 0, -0.7f, 0.7f);
                drawCooking(canvas, x, y, -40, -55);
                drawOrder(canvas, x, y, -120, -55);
            } else if (dishSize == 2) {
                canvas.draw(two, Color.WHITE, ox + 240, oy - 50, x, y, 0, -0.7f, 0.7f);
                drawCooking(canvas, x, y, -50, -53);
                drawOrder(canvas, x, y, -180, -55);
            } else if (dishSize == 3) {
                canvas.draw(three, Color.WHITE, ox + 300, oy - 50, x, y, 0, -0.7f, 0.7f);
                drawCooking(canvas, x, y, -60, -53);
                drawOrder(canvas, x, y, -230, -55);
            }
            canvas.draw(texture, Color.WHITE, ox, oy, x, y, 0, sclx, scly);
        }
    }

    public void drawOrder(GameCanvas canvas, float x, float y, int ox, int oy){
        for (int i = dishSize-1; i >= 0; i--) {
            if (type[i] != null) {
                type[i].drawTextBubble(canvas, x,y, ox + (45 * i), oy);
            }
        }
    }

    public void drawCooking(GameCanvas canvas, float x, float y, int ox, int oy){
        if (station_type == 0) {
            canvas.draw(wok, Color.WHITE, ox, oy,
                    x,y,
                    0.0f, 0.7f, 0.7f);
        } else if (station_type == 1) {
            canvas.draw(pot, Color.WHITE, ox-30, oy,
                    x,y, 0.0f, 0.7f, 0.7f);
        } else {
            canvas.draw(cutting_board, Color.WHITE, ox-30, oy,
                    x,y, 0.0f, 0.7f, 0.7f);
        }
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
