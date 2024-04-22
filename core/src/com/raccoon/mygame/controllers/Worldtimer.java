package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;

public class Worldtimer extends ApplicationAdapter {
    private int countdownSeconds;
    private GameCanvas gameCanvas;
    public boolean action_round;
    private Timer.Task timerTask;
    public boolean timerPaused;
    private boolean timerStarted;
    BitmapFont f = new BitmapFont();
    GlyphLayout layout = new GlyphLayout(f, "");

    public Worldtimer(int count, GameCanvas canvas){
        countdownSeconds = count;
        gameCanvas = canvas;
        action_round = false;
        timerPaused = false;
        timerStarted = false;
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
        int mins = countdownSeconds / 60;
        int secs = countdownSeconds % 60;
        if (countdownSeconds > 60) {
            mins = countdownSeconds / 60;
            secs = countdownSeconds % 60;
        }
        if (secs < 10) {
            gameCanvas.drawText(Integer.toString(mins) + ":0" + Integer.toString(secs),
                    f, 20, 700, 2, 2,layout);
        } else {
            gameCanvas.drawText(Integer.toString(mins) + ":" + Integer.toString(secs),
                    f, 20, 700,2,2,layout);
        }
    }

    public void drawNoFormat(float x, float y){
        int time= Math.max(countdownSeconds, 0);
        gameCanvas.drawText(Integer.toString(time),
                f, x, y, 2, 2,layout);
    }



    public int getTime(){
        return countdownSeconds;
    }
}