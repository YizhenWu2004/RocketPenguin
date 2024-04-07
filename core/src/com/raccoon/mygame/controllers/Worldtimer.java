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
    public boolean action_round;

    public Worldtimer(int count, GameCanvas canvas){
        countdownSeconds = count;
        gameCanvas = canvas;
        action_round = false;
    }
    public void create() {
//        System.out.println("starting timer");
        // Start the countdown timer
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                countdownSeconds--;
                action_round = false;
                if (countdownSeconds <= 0) {
                    Gdx.app.log("Countdown Timer", "Countdown finished!");
                    this.cancel(); // Stop the timer
                }
            }
        }, 1, 1);
    }
  
   public void draw(int x,int y) {
        int mins = 0;
        int secs = 0;
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


    }
}