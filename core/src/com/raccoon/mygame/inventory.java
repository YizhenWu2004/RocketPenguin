package com.raccoon.mygame;

import com.raccoon.mygame.obstacle.BoxObstacle;

public class inventory {
    private BoxObstacle[] items;
    private int index;
    private boolean[] filled;

    public inventory(){
        items = new BoxObstacle[5];
        index = 0;
        filled = new boolean[5];
        for(int i = 0; i < filled.length; i++){
            filled[i] = false;
        }
    }

    public void draw(GameCanvas canvas){
        for(int i = 0; i < items.length; i++){
            if(filled[i] = false){
                items[i].drawDebug(canvas);
            }
        }
    }

    public void add(){
        if(index >= 4){
            System.out.println("cannot add another item");
        }
        filled[index] = true;
        index++;
    }

    public void drop(){
        if(index <= 0){
            System.out.println("cannot drop another item");
        }
        filled[index-1] = false;
        index--;
    }

    public void clearAll(){
        for(int i = 0; i < filled.length; i++){
            filled[i] = false;
        }
        index = 0;
    }
}
