package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.raccoon.mygame.objects.GameObject;
import com.raccoon.mygame.objects.Ingredient;
import com.raccoon.mygame.obstacle.BoxObstacle;
import com.raccoon.mygame.view.GameCanvas;
import com.badlogic.gdx.graphics.Texture;

public class Inventory {

    //list of all the gameobjects into inventory
    private Ingredient[] items;
    //pointer representing the current slot in the inventory thats selected
    private int selected;
    private Texture itexture;
    //boolean array to keep track of which indices in items are filled
    private boolean[] filled;

    public Inventory(Texture t) {
        items = new Ingredient[5];
        filled = new boolean[5];
        selected = 0;
        itexture = t;
        for (int i = 0; i < filled.length; i++) {
            filled[i] = false;
        }
    }
    //used when the player selects another item in their inventory

    public void setSelected(int i) {
//        if(i != 0){
//            System.out.println("changed selecteg");
//        }
        if (selected == 0 && i == -1) {
            selected = 4;
        } else if (selected == 4 && i == 1) {
            selected = 0;
        } else if (0 <= selected + i && selected + i < 5) {
            selected = selected + i;
        }
    }

    //gets the index of the current slot selected
    public int getSelected() {
        return selected;
    }

    //gets the item in the current selected slot
    public Ingredient getSelectedItem() {
        return items[selected];
    }

    //draws the objects in items
    public void draw(GameCanvas canvas) {
        canvas.draw(itexture, Color.WHITE, 10, 10,
                500, 0, 0.0f, 0.5f, 0.5f);
        Texture h = new Texture("inventoryselected.png");
        canvas.draw(h, Color.WHITE, 0, 5, selected * (94 - selected) + 480, 0, 0.0f, 0.5f, 0.5f);
        for (int i = 0; i < items.length; i++) {
            if (filled[i]) {
                canvas.draw(items[i].getTexture(), Color.WHITE, 10, 10, i * 90 + 506, 5, 0.0f, 0.2f, 0.2f);
            }
        }
    }

    //adds another item to the inventory. The item is added to the first available slot
    public void add(Ingredient object) {
        for (int i = 0; i < items.length; i++) {
            if (!filled[i]) {
                items[i] = object;
                filled[i] = true;
                items[i].setPosInv(i);
                items[i].setPosition(-100, -100);
                break;
            }
        }
    }

    //drops the object in the current selected slot if there is an object
    public void drop() {
        if (filled[selected] = false) {
            System.out.println("cannot drop item at this slot");
        }
        filled[selected] = false;
    }

    //clears all objects in the inventory. Used when the player is caught
    public void clearAll() {
        for (int i = 0; i < filled.length; i++) {
            filled[i] = false;
        }
    }

}
