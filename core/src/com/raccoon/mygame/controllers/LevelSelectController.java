package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class LevelSelectController extends WorldController{
    private Texture background = new Texture("menu/levelselectbackground.png");
    private Texture debug = new Texture("tool.png");
    private GameCanvas canvas;
    private InputController input;
    public LevelSelectController(GameCanvas canvas, InputController input){
        this.canvas = canvas;
        this.input = input;
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {

    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, -50, 0.0f, 0.7f, 0.7f);
//        canvas.draw(debug, Color.WHITE, 0, 0,
//                500, 400, 0.0f, 1f, 1f);
    }

    public void debug() {

    }
}
