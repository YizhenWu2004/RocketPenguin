package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

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
        addButton(levelOneButton, ()-> {
            this.goToLevel = true;
            System.out.println(levelOneButton.getID());
        });

        UIButton levelTwoButton = new UIButton(new Texture("menu/levelbook.png"),"level2",420,10,canvas);
        addButton(levelTwoButton, ()-> {
            this.goToLevel = true;
            System.out.println(levelTwoButton.getID());
        });

        UIButton levelThreeButton = new UIButton(new Texture("menu/levelbook.png"),"level3",840,10,canvas);
        addButton(levelThreeButton, ()-> {
            this.goToLevel = true;
            System.out.println(levelThreeButton.getID());
        });
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {
        checkForButtonClicks();
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

    private void addButton(UIButton button, ButtonAction action){
        buttons.add(button);
        button.setOnClickAction(action);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkForButtonClicks(){
        for(UIButton button : buttons){
            float minX = button.getX();
            float maxX = button.getX() + button.getWidth();
            float minY = button.getY();
            float maxY = button.getY() + button.getHeight();
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
