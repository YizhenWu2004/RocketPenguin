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
        if (secs < 10) {
            gameCanvas.drawText(Integer.toString(mins),
                    font, 120, 690, 2, 3,layout);
            gameCanvas.drawText( "0" + Integer.toString(secs), font, 170, 690, 3, 3,layout);
        } else {
            gameCanvas.drawText(Integer.toString(mins),
                    font, 120, 690,2,3,layout);
            gameCanvas.drawText(Integer.toString(secs), font,170, 690, 2, 3, layout);
        }
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