package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.Worldtimer;
import com.raccoon.mygame.models.CookingInventory;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.view.GameCanvas;
import java.awt.Canvas;
import java.util.Arrays;

public class CookingStationObject extends NormalObstacle{
    public boolean interacting;
    public CookingInventory pot;
    public int state; //0 = idle, 1 = cooking, 2 = finished, 3 = burnt(?)
    public int station_type; // 0 = wok, 1 = pot
    public GameCanvas canvas;
    public Texture t;
    public int id;
    public int interacting_with;
    public Worldtimer timer;
    public int maxTime;

    public CookingStationObject(float x, float y, float width, float height, float sx, float sy,
            float ox, float oy, Texture texture,
            World world, GameCanvas canvas, int id, int station_type) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        interacting = false;
        pot = new CookingInventory(new Texture("720/potbar.png"));
        state = 0;
        this.canvas = canvas;
        this.t = texture;
        this.id = id;
        interacting_with = -1;
        this.station_type = station_type;

    }

    public Dish getCookedDish(){
        Ingredient[] dish = Arrays.copyOf(pot.inv, pot.inv.length);
        pot.clearAll();
        return new Dish(dish, new Texture("serve.png"), -1,this.station_type);
    }

    @Override
    public void draw() {
        super.draw();
        if(interacting){
            pot.draw(canvas);
        }
        if(timer != null){
            //System.out.println("HERE" + timer.getTime());
            timer.drawNoFormat(this.getX()*this.drawScale.x, this.getY()*this.drawScale.y);
        }
    }

    public void setMaxTime(){
        this.maxTime = timer.getTime();
    }
    public int getMaxTime(){
        return this.maxTime;
    }

    
}
