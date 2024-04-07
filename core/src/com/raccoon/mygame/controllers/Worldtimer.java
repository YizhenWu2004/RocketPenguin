package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.view.GameCanvas;

public class Worldtimer extends ApplicationAdapter {
    private int countdownSeconds;
    private GameCanvas gameCanvas;

    public Worldtimer(int count, GameCanvas canvas){
        countdownSeconds = count;
        gameCanvas = canvas;
    }
    public void create() {
//        System.out.println("starting timer");
        // Start the countdown timer
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                countdownSeconds--;
                if (countdownSeconds <= 0) {
                    Gdx.app.log("Countdown Timer", "Countdown finished!");
                    this.cancel(); // Stop the timer
                }
            }
        }, 1, 1);
    }
    public void draw(){
        int mins = 0;
        int secs = 0;
        if(countdownSeconds > 60){
            mins = countdownSeconds /60;
            secs = countdownSeconds % 60;
        }
        if(secs < 10){
            gameCanvas.drawText(Integer.toString(mins) + ":0" + Integer.toString(secs), new BitmapFont(), 20, 700 );
        } else {
            gameCanvas.drawText(Integer.toString(mins) + ":" + Integer.toString(secs), new BitmapFont(), 20, 700 );
        }
    }
}