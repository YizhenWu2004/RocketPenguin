package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.ButtonHover;
import com.raccoon.mygame.models.ButtonUnhover;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class MenuController extends WorldController{
    private Texture background;
    private Texture resume_hover = new Texture("pause/resume_hover.png");
    private Texture option_hover = new Texture("pause/option_hover.png");
    private Texture restart_hover = new Texture("pause/restart_hover.png");
    private Texture quit_hover = new Texture("pause/quit_hover.png");
    private GameCanvas canvas;
    private InputController input;
    private Array<UIButton> buttons = new Array<>();
    public boolean resume = false;
    public boolean restart = false;
    public boolean options = false;
    public boolean quit = false;
    public MenuController(GameCanvas canvas, Texture background, InputController input){
        this.background = background;
        this.canvas = canvas;
        this.input = input;

        UIButton resume = new UIButton(new Texture("pause/resume_pause.png"),"resume",485,475,canvas);
        addButton(resume, ()-> {
            this.resume = true;
        }, ()->{
            resume.setSX(0.8f);
            resume.setSY(0.8f);
            resume.setTexture(resume_hover);
        }, resume::resetStyleProperties);
        resume.setDefaultScale(0.7f,0.7f);

        UIButton options = new UIButton(new Texture("pause/option_pause.png"),"options", 485,375,canvas);
        addButton(options, ()-> {
            this.options = true;
        }, ()->{
            options.setSX(0.8f);
            options.setSY(0.8f);
            options.setTexture(option_hover);
        },options::resetStyleProperties);
        options.setDefaultScale(0.7f,0.7f);

        UIButton quit = new UIButton(new Texture("pause/quit_pause.png"),"quit",485,275,canvas);
        addButton(quit, ()-> {
            this.quit = true;
        },()->{
            quit.setSX(0.8f);
            quit.setSY(0.8f);
            quit.setTexture(quit_hover);
        },quit::resetStyleProperties);
        quit.setDefaultScale(0.7f,0.7f);

        UIButton restart = new UIButton(new Texture("pause/restart_pause.png"),"restart",485,175,canvas);
        addButton(restart, ()-> {
            this.restart = true;
        },()->{
            restart.setSX(0.8f);
            restart.setSY(0.8f);
            restart.setTexture(restart_hover);
        },restart::resetStyleProperties);
        restart.setDefaultScale(0.7f,0.7f);
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void resumed(){
        resume = false;
    }

    public void update() {
        checkButtons();
    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 0.7f, 0.7f);
//        canvas.draw(debug, Color.WHITE, 0, 0,
//                500, 400, 0.0f, 1f, 1f);
        for(UIButton button : buttons){
            button.draw(canvas);
        }
    }

    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkButtons() {
        for (UIButton button : buttons) {
            float minX = button.getX();
            float maxX = button.getX() + button.getWidth();
            float minY = button.getY();
            float maxY = button.getY() + button.getHeight();
            if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)) {
                button.setHovered(true);
                button.onHoverEvent();
            } else {
                button.setHovered(false);
                button.onUnhoverEvent();
            }
            //if input is within bounds of button
            if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)
                    && input.click) {
                button.setIsClicked(true);
                button.onClickEvent();
                button.setIsClicked(false);
            }
        }
    }

    public void debug() {

    }
}
