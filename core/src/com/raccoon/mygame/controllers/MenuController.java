package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.raccoon.mygame.view.GameCanvas;

public class MenuController extends WorldController{
    private Texture background;
    private Texture debug = new Texture("tool.png");
    private GameCanvas canvas;
    private InputController input;
    public MenuController(GameCanvas canvas, Texture background, InputController input){
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
                0, -50, 0.0f, 0.7f, 0.7f);
//        canvas.draw(debug, Color.WHITE, 0, 0,
//                500, 400, 0.0f, 1f, 1f);
    }

    public void debug() {

    }
}