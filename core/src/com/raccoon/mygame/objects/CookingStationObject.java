package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.controllers.SoundController;
import com.raccoon.mygame.controllers.Worldtimer;
import com.raccoon.mygame.models.CookingInventory;
import com.raccoon.mygame.models.Inventory;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.models.Pot;
import com.raccoon.mygame.util.FilmStrip;
import com.raccoon.mygame.view.GameCanvas;
import java.awt.Canvas;
import java.util.Arrays;

public class CookingStationObject extends NormalObstacle{
    public boolean interacting;
    public CookingInventory pot;
    public int state; //0 = idle, 1 = cooking, 2 = finished, 3 = burnt(?)
    public int station_type; // 0 = wok, 1 = pot, 2 = chop
    public GameCanvas canvas;
    public Texture t;
    public int id;
    public int interacting_with;
    public Worldtimer timer;
    public int maxTime;
    private final FilmStrip wok_pic =new FilmStrip(new Texture("wok.png"),1,1,1);
    private final FilmStrip pot_pic = new FilmStrip(new Texture("pot.png"),1,1,1);
    private final FilmStrip chop_pic = new FilmStrip(new Texture("cutting_board.png"),1,1,1);

    private final FilmStrip wok_pic_cooked =new FilmStrip(new Texture("wok_cooked.png"),1,1,1);
    private final FilmStrip pot_pic_cooked = new FilmStrip(new Texture("pot_cooked.png"),1,1,1);
    public float drawox=0;
    public float drawoy=0;
    public float x;
    public float y;
    public SoundController sounds;
    public Pot wok;
    public Pot pott;
    public Pot chop;

//    private Expression spaceIcon;

    public CookingStationObject(float x, float y, float width, float height, float sx, float sy,
                                float ox, float oy, Texture texture,
                                World world, GameCanvas canvas, int id, int station_type, SoundController s) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        sounds = s;
        interacting = false;
        pot = new CookingInventory(new Texture("720/potbar.png"), sounds);
        state = 0;
        this.canvas = canvas;
        this.t = texture;
        this.id = id;
        interacting_with = -1;
        this.station_type = station_type;
        this.x = x;
        this.y = y;

        wok = new Pot(0,wok_pic, 40, 30, this.x * this.drawScale.x-28,this.y * this.drawScale.y,1f,1f,wok_pic_cooked);
        pott = new Pot(1, pot_pic,50, 5,
                this.x*this.drawScale.x,this.y*this.drawScale.y, 1f, 1f,pot_pic_cooked);
        chop = new Pot(2,chop_pic, 35, 50,
                this.x * this.drawScale.x, this.y * this.drawScale.y, 1f, 1f,chop_pic);
//        spaceIcon = new Expression("space",x,y);

    }

    public Dish getCookedDish(){

        Ingredient[] dish = Arrays.copyOf(pot.inv, pot.inv.length);
        pot.clearAll();
        return new Dish(dish, new Texture("serve.png"), -1,this.station_type);
    }

    @Override
    public void draw() {
        super.drawWithOffset(drawox, drawoy);
        if(station_type == 0) {
//            canvas.draw(wok_pic, Color.WHITE, 40, 30,
//                    this.getX() * this.getDrawScale().x-28, this.getY() * this.getDrawScale().y,
//                    0.0f, 1f, 1f);
            wok.draw(canvas, state == 2);
        }else if(station_type == 1){
//            canvas.draw(pot_pic, Color.WHITE, 50, 5,
//                    this.getX()*this.getDrawScale().x,this.getY()*this.getDrawScale().y, 0.0f, 1f, 1f);
            pott.draw(canvas,state == 2);
        } else {
//            canvas.draw(chop_pic, Color.WHITE, 35, 50,
//                    this.getX() * this.getDrawScale().x, this.getY() * this.getDrawScale().y,
//                    0.0f, 1f, 1f);
            chop.draw(canvas,state == 2);
        }
        if(timer != null){
            timer.drawNoFormat(this.getX()*this.drawScale.x, this.getY()*this.drawScale.y);
        }
    }

    public void drawInventory(){
        if(interacting){
            pot.draw(canvas);
//            if(!pot.isEmpty()){
////                spaceIcon.drawSpace(canvas,this.getDrawScale().x,this.getDrawScale().y);
//            }
        }
    }

    public void setMaxTime(){
        this.maxTime = timer.getTime();
    }
    public int getMaxTime(){
        return this.maxTime;
    }

    /**
     * 0 - wok
     * 1 - pot
     * 2 - board
     * */
    public int getStationType() {
        return station_type;
    }
}
