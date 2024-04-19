package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.view.GameCanvas;

import java.awt.*;

public class LevelSelectController extends WorldController{
    private Texture background = new Texture("menu/levelselectbackground.png");
    private Texture debug = new Texture("tool.png");
    private GameCanvas canvas;
    private InputController input;

    private Array<UIButton> buttons = new Array<>();
    

    private boolean goToLevel = false;

    public LevelSelectController(GameCanvas canvas, InputController input){
        this.canvas = canvas;
        this.input = input;

        UIButton levelOneButton = new UIButton(new Texture("menu/levelbook.png"),"level1",10,10,canvas);
        addButton(levelOneButton,
                ()-> {
            this.goToLevel = true;
            System.out.println(levelOneButton.getID());
                    },
                ()->{
            System.out.println("hovering");
            levelOneButton.setSX(1.05f);
            levelOneButton.setSY(1.05f);
        }, levelOneButton::resetStyleProperties
        );

        UIButton levelTwoButton = new UIButton(new Texture("menu/levelbook.png"),"level2",420,10,canvas);
        addButton(levelTwoButton, ()-> {
            this.goToLevel = true;
            System.out.println(levelTwoButton.getID());
        },()->{
                    levelTwoButton.setSX(1.05f);
                    levelTwoButton.setSY(1.05f);
        }, levelTwoButton::resetStyleProperties
        );

        UIButton levelThreeButton = new UIButton(new Texture("menu/levelbook.png"),"level3",840,10,canvas);
        addButton(levelThreeButton, ()-> {
            this.goToLevel = true;
            System.out.println(levelThreeButton.getID());
        },()->{
            levelThreeButton.setSX(1.05f);
            levelThreeButton.setSY(1.05f);
        }, levelThreeButton::resetStyleProperties);
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
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

    public void debug() {

    }

    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }
    private void addButton(UIButton button){
        buttons.add(button);
    }
    private void addButton(UIButton button, ButtonAction action){
        buttons.add(button);
        button.setOnClickAction(action);
    }
    private void addButton(UIButton button, ButtonAction action, ButtonHover hover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkButtons(){
        for(UIButton button : buttons){
            float minX = button.getX();
            float maxX = button.getX() + button.getWidth();
            float minY = button.getY();
            float maxY = button.getY() + button.getHeight();
            if(processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)){
                button.setHovered(true);
                button.onHoverEvent();
            }
            else{
                button.setHovered(false);
                button.onUnhoverEvent();
            }
            //if input is within bounds of button
            if(processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY) && input.click){
                button.setIsClicked(true);
                button.onClickEvent();
                button.setIsClicked(false);
            }
        }
    }
    public boolean checkForGoToLevel(){return this.goToLevel;}
}
