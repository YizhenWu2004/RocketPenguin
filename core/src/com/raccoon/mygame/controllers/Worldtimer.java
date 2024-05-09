package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;

public class Worldtimer extends ApplicationAdapter {
    private int countdownSeconds;
    private GameCanvas gameCanvas;
    public boolean action_round;
    private Timer.Task timerTask;
    public boolean timerPaused;
    private boolean timerStarted;
    private Texture timerTexture;
    private int maxTime;

    private Texture timer_frame;
    private Texture timer_background ;
    private Texture cooking_timer ;
    private Texture timer_green ;
    private Texture green2 ;
    private Texture timer_red ;

    private Texture one;
    private Texture two;
    private Texture three;
    private Texture four;
    private Texture five;
    private Texture six;
    private Texture seven;
    private Texture eight;
    private Texture nine;
    private Texture zero;
    private Texture twozeros;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("720/PatrickHandSC-Regular.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
    BitmapFont font = generator.generateFont(p);

//    generator.dispose();
    GlyphLayout layout = new GlyphLayout(font, "");

    public Worldtimer(int count, GameCanvas canvas, Texture t, AssetDirectory directory){

       timer_frame = directory.getEntry("timerframe",Texture.class);
        timer_background = directory.getEntry("timerbackground",Texture.class);
        cooking_timer = directory.getEntry("cooktimer",Texture.class);
        timer_green = directory.getEntry("timergreen",Texture.class);
        green2 = directory.getEntry("timergreen2",Texture.class);
        timer_red = directory.getEntry("timerred",Texture.class);
        one = directory.getEntry("tone",Texture.class);
        two = directory.getEntry("ttwo",Texture.class);
        three = directory.getEntry("tthree",Texture.class);
        four = directory.getEntry("tfour",Texture.class);
        five = directory.getEntry("tfive",Texture.class);
        six = directory.getEntry("tsix",Texture.class);
        seven = directory.getEntry("tseven",Texture.class);
        eight = directory.getEntry("teight",Texture.class);
        nine = directory.getEntry("tnine",Texture.class);
        zero = directory.getEntry("tzero",Texture.class);
        twozeros = directory.getEntry("t2zero",Texture.class);

        countdownSeconds = count;
        gameCanvas = canvas;
        action_round = false;
        timerPaused = false;
        timerStarted = false;
        timerTexture = t;
        font.setColor(Color.BLACK);
        this.maxTime = count;
    }

    public void create() {
        // Start the countdown timer only if it's not already started
        if (!timerStarted) {
            timerTask = new Timer.Task() {
                @Override
                public void run() {
                    if (!timerPaused) {
                        countdownSeconds--;
                        action_round = false;
                        if (countdownSeconds <= 0) {
                            Gdx.app.log("Countdown Timer", "Countdown finished!");
                            this.cancel(); // Stop the timer
                        }
                    }
                }
            };
            Timer.schedule(timerTask, 1, 1);
            timerStarted = true;
        }
    }

    public void pauseTimer() {
        // Pause the timer
        timerPaused = true;
    }

    public void resumeTimer() {
        // Resume the timer
        timerPaused = false;
    }


    public void draw(int x,int y) {
        float ratio = (float)countdownSeconds/maxTime;
        gameCanvas.draw(timer_background, Color.WHITE, 10, 10,
                103, 635, 0.0f, 0.92f, 1);
        if(ratio >= 0.15){
            gameCanvas.draw(timer_green, Color.WHITE, 10, 10,
                    93, 635, 0.0f, ratio, 1);
        } else {
            gameCanvas.draw(timer_red, Color.WHITE, 10, 10,
                    93, 635, 0.0f, ratio, 1);
        }

        gameCanvas.draw(timer_frame, Color.WHITE, 10, 10,
                10, 615, 0.0f, 1, 1);




//        gameCanvas.draw(timerTexture, x, y);
        int mins = countdownSeconds / 60;
        int secs = countdownSeconds % 60;
        if (countdownSeconds > 60) {
            mins = countdownSeconds / 60;
            secs = countdownSeconds % 60;
        }
        int min1 = mins/10;
        int min2 = mins % 10;
        int sec1 = secs/10;
        int secs2 = secs % 10;
        Texture [] tex = helper(min1,min2,sec1,secs2);
        if (secs < 10) {
//            gameCanvas.draw(tex[0], 120, 690);
            gameCanvas.draw(tex[1], 110, 640);
            gameCanvas.draw(tex[2], 150, 640);
            gameCanvas.draw(tex[3], 180, 640);
//            gameCanvas.drawText(Integer.toString(mins),
//                    font, 120, 690, 2, 3,layout);
//            gameCanvas.drawText( "0" + Integer.toString(secs), font, 170, 690, 3, 3,layout);
        } else {
//            gameCanvas.draw(tex[0], 120, 690);
            gameCanvas.draw(tex[1], 110, 640);
            gameCanvas.draw(tex[2], 150, 640);
            gameCanvas.draw(tex[3], 180, 640);
//            gameCanvas.drawText(Integer.toString(mins),
//                    font, 120, 690,2,3,layout);
//            gameCanvas.drawText(Integer.toString(secs), font,170, 690, 2, 3, layout);
        }
    }

    private Texture[] helper(int min1, int min2, int min3, int min4){
        Texture[] out = new Texture[4];
        for(int i = 0; i < 10; i++){
            if(i == 0){
                if(min1 == i){
                    out[0] = zero;
                }
                if(min2 == i){
                    out[1] = zero;
                }
                if(min3 == i){
                    out[2] = zero;
                }
                if(min4 == i){
                    out[3] = zero;
                }
            }
            if(i == 1){
                if(min1 == i){
                    out[0] = one;
                }
                if(min2 == i){
                    out[1] = one;
                }
                if(min3 == i){
                    out[2] = one;
                }
                if(min4 == i){
                    out[3] = one;
                }
            }
            if(i == 2){
                if(min1 == i){
                    out[0] = two;
                }
                if(min2 == i){
                    out[1] = two;
                }
                if(min3 == i){
                    out[2] = two;
                }
                if(min4 == i){
                    out[3] = two;
                }
            }
            if(i == 3){
                if(min1 == i){
                    out[0] = three;
                }
                if(min2 == i){
                    out[1] = three;
                }
                if(min3 == i){
                    out[2] = three;
                }
                if(min4 == i){
                    out[3] = three;
                }
            }
            if(i == 4){
                if(min1 == i){
                    out[0] = four;
                }
                if(min2 == i){
                    out[1] = four;
                }
                if(min3 == i){
                    out[2] = four;
                }
                if(min4 == i){
                    out[3] = four;
                }
            }
            if(i == 5){
                if(min1 == i){
                    out[0] = five;
                }
                if(min2 == i){
                    out[1] = five;
                }
                if(min3 == i){
                    out[2] = five;
                }
                if(min4 == i){
                    out[3] = five;
                }
            }
            if(i == 6){
                if(min1 == i){
                    out[0] = six;
                }
                if(min2 == i){
                    out[1] = six;
                }
                if(min3 == i){
                    out[2] = six;
                }
                if(min4 == i){
                    out[3] = six;
                }
            }
            if(i == 7){
                if(min1 == i){
                    out[0] = seven;
                }
                if(min2 == i){
                    out[1] = seven;
                }
                if(min3 == i){
                    out[2] = seven;
                }
                if(min4 == i){
                    out[3] = seven;
                }
            }
            if(i == 8){
                if(min1 == i){
                    out[0] = eight;
                }
                if(min2 == i){
                    out[1] = eight;
                }
                if(min3 == i){
                    out[2] = eight;
                }
                if(min4 == i){
                    out[3] = eight;
                }
            }
            if(i == 9){
                if(min1 == i){
                    out[0] = nine;
                }
                if(min2 == i){
                    out[1] = nine;
                }
                if(min3 == i){
                    out[2] = nine;
                }
                if(min4 == i){
                    out[3] = nine;
                }
            }
        }
        return out;
    }
    public void drawNoFormat(float x, float y, int type) {
        if (type == 1) {
            int time = Math.max(countdownSeconds, 0);
            float ratio = 1 - (float) time / maxTime;
            gameCanvas.draw(cooking_timer, Color.WHITE, 10, 10,
                    x - 35, y - 25, 0.0f, 1, 1);
            gameCanvas.draw(green2, Color.WHITE, 0, 0,
                    x - 40, y - 25, 0.0f, ratio, 1);
//        gameCanvas.drawText(Integer.toString(time),
//                font, x, y, 2, 2, layout);
        } else {
            int time = Math.max(countdownSeconds, 0);
            float ratio = 1 - (float) time / maxTime;
            gameCanvas.draw(cooking_timer, Color.WHITE, 10, 10,
                    x - 45, y - 50, 0.0f, 1, 1);
            gameCanvas.draw(green2, Color.WHITE, 0, 0,
                    x - 50, y - 50, 0.0f, ratio, 1);
        }
    }

    public int getTime(){
        return countdownSeconds;
    }
}