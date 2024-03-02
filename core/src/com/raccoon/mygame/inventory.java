package com.raccoon.mygame;

import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;

public class inventory {

    //list of all the gameobjects into inventory
    private GameObject[] items;
    //pointer representing the current slot in the inventory thats selected
    private int selected;
    //boolean array to keep track of which indices in items are filled
    private boolean[] filled;

    public inventory(){
        items = new GameObject[5];
        filled = new boolean[5];
        selected = 0;
        for(int i = 0; i < filled.length; i++){
            filled[i] = false;
        }
    }
    //used when the player selects another item in their inventory

    public void setSelected(int i){
        selected = i;
    }

    //gets the index of the current slot selected
    public int getSelected(){
        return selected;
    }

    //gets the item in the current selected slot
    public GameObject getSelectedItem(){
        return items[selected];
    }

    //draws the objects in items
    public void draw(GameCanvas canvas){
        for(int i = 0; i < items.length; i++){
            if(filled[i] = false){
                canvas.draw(items[i].getTexture(), 100 * (i+1), 100);
            }
        }
    }

    //adds another item to the inventory. The item is added to the first available slot
    public void add(){
        for(int i = 0; i < items.length; i++){
            if(filled[i] = false){
                filled[i] = true;
                break;
            }
        }
    }

    //drops the object in the current selected slot if there is an object
    public void drop(){
        if(filled[selected] = false){
            System.out.println("cannot drop item at this slot");
        }
        filled[selected] = false;
    }

    //clears all objects in the inventory. Used when the player is caught
    public void clearAll(){
        for(int i = 0; i < filled.length; i++){
            filled[i] = false;
        }
    }
    
}
