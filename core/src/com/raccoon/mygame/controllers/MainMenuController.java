package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class MainMenuController extends WorldController{
    private Texture background = new Texture("menu/menu.png");

    private GameCanvas canvas;
    private InputController input;

    private Array<UIButton> buttons = new Array<>();

    private boolean goToLevelSelect = false;
    private boolean goToOptionsSelect = false;
    private boolean exit = false;

    public MainMenuController(GameCanvas canvas, InputController input){
        this.canvas = canvas;
        this.input = input;

        UIButton play = new UIButton(new Texture("menu/play.png"),"play",100,500,canvas);
        addButton(play, ()-> {
            this.goToLevelSelect = true;
            System.out.println(play.getID());
        });

        UIButton options = new UIButton(new Texture("menu/options.png"),"options", 100,300,canvas);
        addButton(options, ()-> {
            this.goToOptionsSelect = true;
            System.out.println(options.getID());
        });

        UIButton exit = new UIButton(new Texture("menu/exit.png"),"exit",100,100,canvas);
        addButton(exit, ()-> {
            this.exit = true;
            System.out.println(exit.getID());
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
        canvas.draw(new Texture("restaurantfloor.png"), Color.WHITE, 0, 0,
                0, 0, 0.0f, 1, 1);
        canvas.draw(background, Color.WHITE, 0, 0,
                125, 0, 0.0f, 0.5f, 0.5f);
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
    public boolean checkForGoToLevelSelect(){return this.goToLevelSelect;}
    public boolean checkForGoToOptions(){return this.goToOptionsSelect;}
    public boolean checkForExit(){return this.exit;}
}
