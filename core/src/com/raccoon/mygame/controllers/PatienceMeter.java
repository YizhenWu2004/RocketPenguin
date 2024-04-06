package com.raccoon.mygame.controllers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.view.GameCanvas;

public class PatienceMeter extends ApplicationAdapter{

    private int countdownSeconds;
    private GameCanvas gameCanvas;
    private Customer customer;
    public PatienceMeter(int count, GameCanvas canvas, Customer cus){
        countdownSeconds = count;
        gameCanvas = canvas;
        customer = cus;
    }
    public void create() {
        System.out.println("creating patience meter");
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
        System.out.println("drawing patience meter");
        gameCanvas.drawText(Integer.toString(countdownSeconds), new BitmapFont(), customer.getX() * gameCanvas.getWidth() / 32, customer.getY() * gameCanvas.getHeight()/ 18 + 10);
    }
}

