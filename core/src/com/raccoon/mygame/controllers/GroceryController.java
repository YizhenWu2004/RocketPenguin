package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;

public class GroceryController extends WorldController{
    private Player player;
    private Inventory inv;
    @Override
    public void reset() {

    }

    public void populateWorld(){
        Inventory inv = new Inventory(new Texture("inventorybar.png"));
        player = new Player(0,0,1,0.7f, new Texture("rockoReal.png"),inv, canvas, world);
        addObject(player);
    }

    @Override
    public void update(float dt) {

    }
}
