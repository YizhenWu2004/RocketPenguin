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
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("720/PatrickHandSC-Regular.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
    BitmapFont font = generator.generateFont(p);

//    generator.dispose();
    GlyphLayout layout = new GlyphLayout(font, "");

    public Worldtimer(int count, GameCanvas canvas, Texture t){
        countdownSeconds = count;
        gameCanvas = canvas;
        action_round = false;
        timerPaused = false;
        timerStarted = false;
        timerTexture = t;
        font.setColor(Color.BLACK);
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
        gameCanvas.draw(timerTexture, Color.WHITE, 10, 10,
                10, 600, 0.0f, 1, 1);
//        gameCanvas.draw(timerTexture, x, y);
        int mins = countdownSeconds / 60;
        int secs = countdownSeconds % 60;
        if (countdownSeconds > 60) {
            mins = countdownSeconds / 60;
            secs = countdownSeconds % 60;
        }
        if (secs < 10) {
            gameCanvas.drawText(Integer.toString(mins),
                    font, 120, 670, 2, 3,layout);
            gameCanvas.drawText( "0" + Integer.toString(secs), font, 170, 670, 3, 4,layout);
        } else {
            gameCanvas.drawText(Integer.toString(mins),
                    font, 120, 670,2,3,layout);
            gameCanvas.drawText(Integer.toString(secs), font,170, 670, 2, 3, layout);
        }
    }

    public void drawNoFormat(float x, float y){
        int time= Math.max(countdownSeconds, 0);
        gameCanvas.drawText(Integer.toString(time),
                font, x, y, 2, 2, layout);
    }



    public int getTime(){
        return countdownSeconds;
    }
}