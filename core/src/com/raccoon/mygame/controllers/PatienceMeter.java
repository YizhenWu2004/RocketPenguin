package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.view.GameCanvas;

public class PatienceMeter extends ApplicationAdapter{

    private int countdownSeconds;
    private int max_countdown;
    private GameCanvas gameCanvas;
    private Customer customer;
    private Texture high;
    private Texture mid;
    private Texture low;
    private boolean timerPaused;
    private Timer.Task timerTask;
    BitmapFont f = new BitmapFont();
    GlyphLayout layout = new GlyphLayout(f, "");

    public PatienceMeter(int count, GameCanvas canvas, Customer cus){
        countdownSeconds = count;
        max_countdown = count;
        gameCanvas = canvas;
        customer = cus;
        high= new Texture("goodpatience.png");
        mid= new Texture("mediumpatience.png");
        low= new Texture("badpatience.png");
        timerPaused = false;
    }

    public void create() {
        //System.out.println("creating patience meter");
        timerTask = new Timer.Task() {
            @Override
            public void run() {
                if (!timerPaused) {
                    countdownSeconds--;
                    if (countdownSeconds <= 0) {
                        Gdx.app.log("Countdown Timer", "Countdown finished!");
                        this.cancel(); // Stop the timer
                    }
                }
            }
        };
        Timer.schedule(timerTask, 1, 1);
    }

    public int getTime(){
        return countdownSeconds;
    }

    public int getMaxTime(){
        return max_countdown;
    }

    public void draw(float scalex, float scaley){
        //System.out.println("drawing patience meter");
        if (countdownSeconds >= max_countdown/2){
            gameCanvas.draw(high, Color.WHITE, 0, 0,
                    customer.getX() * scalex + 30, customer.getY() * scaley + 10, 0.0f, 0.25f, 0.25f);
        } else if (countdownSeconds >= max_countdown*0.2){
            gameCanvas.draw(mid, Color.WHITE, 0, 0,
                    customer.getX() *  scalex + 30, customer.getY() * scaley + 10, 0.0f, 0.25f, 0.25f);
        } else if(countdownSeconds >= 0) {
            gameCanvas.draw(low, Color.WHITE, 0, 0,
                    customer.getX() * scalex + 30, customer.getY() * scalex + 10, 0.0f, 0.25f, 0.25f);
        }

        gameCanvas.drawText(Integer.toString(countdownSeconds), f, customer.getX() * scalex+55, customer.getY() * scaley + 10, 2, 2, layout);
    }

    public float multiplier(){
        if (countdownSeconds >= max_countdown/2) {
            return 1;
        } else if (countdownSeconds >= max_countdown*0.2){
            return 0.7f;
        } else {
            return 0.3f;
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
}
