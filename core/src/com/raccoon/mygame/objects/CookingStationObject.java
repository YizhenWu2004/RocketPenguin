package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.assets.AssetDirectory;
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
    private Texture potbar;
    private Texture serve;
    private FilmStrip wok_pic;
    private FilmStrip pot_pic;
    private FilmStrip chop_pic;

    private FilmStrip wok_pic_cooked;
    private FilmStrip pot_pic_cooked;
    public float drawox=0;
    public float drawoy=0;
    public float x;
    public float y;
    public SoundController sounds;
    public Pot wok;
    public Pot pott;
    public Pot chop;
    private AssetDirectory directory;

    private void createTextures(AssetDirectory directory) {
        potbar = directory.getEntry("potbar", Texture.class);
        serve = directory.getEntry("serve", Texture.class);
        wok_pic = directory.getEntry("wok.strip", FilmStrip.class);
        pot_pic = directory.getEntry("pot.strip", FilmStrip.class);
        chop_pic = directory.getEntry("cutting_board.strip", FilmStrip.class);
        wok_pic_cooked = directory.getEntry("wok_cooked.strip", FilmStrip.class);
        pot_pic_cooked = directory.getEntry("pot_cooked.strip", FilmStrip.class);
        this.directory = directory;
    }

//    private Expression spaceIcon;

    public CookingStationObject(float x, float y, float width, float height, float sx, float sy,
                                float ox, float oy, Texture texture,
                                World world, GameCanvas canvas, int id, int station_type, SoundController s, AssetDirectory directory) {
        super(x, y, width, height, sx, sy, ox, oy, texture, world, canvas);
        createTextures(directory);
        sounds = s;
        interacting = false;
        pot = new CookingInventory(potbar, sounds);
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
        return new Dish(dish, serve, -1,this.station_type, directory);
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
        if(timer != null) {
            if (station_type != 2) {
                timer.drawNoFormat(this.getX() * this.drawScale.x, this.getY() * this.drawScale.y, station_type);
            }
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
