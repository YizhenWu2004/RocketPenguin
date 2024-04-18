package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class UIButton {
    //all in pixels
    private float x, y;
    private String id;
    private float width, height;
    private Texture texture;
    private boolean isClicked = false;
    private ButtonAction onClickAction;

    /**
     * Constructor to create a TEXTURELESS button
     *
     * @param id     the id of the button
     * @param x      the x coordinate in pixels
     * @param y      the y coordinate in pixels
     * @param width  the width of the button in pixels
     * @param height the height of the button in pixels
     */
    public UIButton(String id, float x, float y, float width, float height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public UIButton(Texture texture, String id, float x, float y, GameCanvas canvas) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(this.texture, this.x, this.y);
    }
    public void setOnClickAction(ButtonAction action) {
        this.onClickAction = action;
    }
    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
    public boolean getClicked(){
        return isClicked;
    }
    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public float getWidth(){return this.width;}
    public float getHeight(){return this.height;}
    public String getID(){return this.id;}
    public void onClickEvent(){
        if(isClicked && onClickAction != null){
            onClickAction.execute();
        }
    }
}