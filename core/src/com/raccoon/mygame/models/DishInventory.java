package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.objects.Dish;
import com.raccoon.mygame.objects.Ingredient;

public class DishInventory {
    private Dish[] inv;
    private Texture texture;
    private boolean[] filled;
    public DishInventory(Texture texture){
        inv = new Dish[2];
        filled = new boolean[2];
        this.texture = texture;
    }

    public boolean leftFilled(){
        return filled[0];
    }

    public boolean rightFilled(){
        return filled[1];
    }
    public void clear(int index){
        filled[index] = false;
    }

    public void fill(Dish dish){
        if (!leftFilled()){
            inv[0] = dish;
            filled[0] = true;
        } else if (!rightFilled()){
            inv[1] = dish;
            filled[1] = true;
        }
    }

    public Dish get(int index){
        if (!filled[index]){
            return null;
        }
        return inv[index];
    }

//    public boolean serve(Dish dish){
//        if (leftFilled()){
//            if (inv[0].check(dish)){
//                filled[0] = false;
//                return true;
//            }
//        } else if (rightFilled()){
//            if(inv[1].check(dish)){
//                filled[1] = false;
//                return true;
//            }
//        }
//        return false;
//    }
}
