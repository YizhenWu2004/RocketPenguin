package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.models.CookingInventory;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.view.GameCanvas;
import java.awt.Canvas;
import java.util.Arrays;

public class CookingStationObject extends NormalObstacle{
    public boolean interacting;
    private CookingInventory pot;
    private Player p;
    private int state; //0 = idle, 1 = cooking, 2 = finished, 3 = burnt(?)
    private int ticks;
    //public boolean isCookPressed;
    //public boolean isPickUpPressed;
    public GameCanvas canvas;
    public Texture t;
    public int id;
    public int interacting_with;

    public CookingStationObject(float x, float y, float width, float height, float sx, float sy,
            float ox, float oy, Texture texture,
            World world, GameCanvas canvas, Player player, int id) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        interacting = false;
        pot = new CookingInventory(new Texture("inventorybar.png"));
        state = 0;
        this.p= player;
        this.canvas = canvas;
        this.t = texture;
        this.id = id;
        interacting_with = -1;
    }

    public Dish getCookedDish(){
        Ingredient[] dish = Arrays.copyOf(pot.inv, pot.inv.length);
        pot.clearAll();
        return new Dish(dish, new Texture("pot.png"), -1);
    }

    public void update(){
        if (state == 0){
            if(pot.size != 0 && p.interaction && interacting){
                ticks = 0;
                state = 1;
            } else if (pot.size != 0 && !interacting){
                Ingredient[] temp = pot.drop();
                for(Ingredient i : temp){
                    if(i != null) {
                        p.inventory.add(i);
                    }
                }
                pot.clearAll();
            } else if (p.space && pot.size < 3 && p.inventory.isCurrFilled() && interacting){
                pot.add(p.inventory.getSelectedItem());
                p.inventory.drop();
            }
        }else if (state == 1){
            if (ticks == 100){
                state = 2;
            }
            ticks += 1;
        } else if (state == 2){
            if(interacting && p.space){
                if (!p.dishInventory.leftFilled() || !p.dishInventory.rightFilled()) {
                    p.dishInventory.fill(getCookedDish());
                    state = 0;
                }
            }
            ticks += 1;
            //burnt timer not implemented yet
        }

    }

    @Override
    public void draw() {
        super.draw();
        if(interacting){
            pot.draw(canvas);
        }
    }
}
