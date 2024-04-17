package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class ResultController extends WorldController {

    private Texture background;
    private GameCanvas canvas;
    private InputController input;

    public ResultController(GameCanvas canvas, Texture background, InputController input){
        this.background = background;
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
                0, 0, 0.0f, 0.7f, 0.7f);
    }

    public void debug() {

    }
}
