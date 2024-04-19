package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raccoon.mygame.view.GameCanvas;

public class UIButton {
    //all in pixels
    private float x, y;
    private String id;
    private float width, height;
    private Texture texture;

    private boolean isClicked = false;
    private boolean isHovered = false;
    private ButtonAction onClickAction;
    private ButtonHover onHoverAction;
    private ButtonUnhover onUnhoverAction;

    private float defaultOX = 0;
    private float defaultOY = 0;
    private float defaultSX = 1;
    private float defaultSY = 1;
    private Color defaultCOLOR = Color.WHITE;

    private float OX = defaultOX, OY = defaultOY;
    private float SX = defaultSX, SY = defaultSY;
    private Color COLOR = defaultCOLOR;

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
        canvas.draw(new TextureRegion(this.texture),Color.WHITE, this.OX, this.OY, this.x, this.y, 0, this.SX, this.SY);
//        TextureRegion region, Color tint, float ox, float oy,
//        float x, float y, float angle, float sx, float sy
    }
    public void setOnClickAction(ButtonAction action) {
        this.onClickAction = action;
    }
    public void setOnHoverAction(ButtonHover action) {
        this.onHoverAction = action;
    }
    public void setOnUnhoverAction(ButtonUnhover action) {
        this.onUnhoverAction = action;
    }
    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
    public boolean getClicked(){
        return isClicked;
    }
    public void setHovered(boolean isHovered){this.isHovered = isHovered;}
    public boolean getHovered(){return isHovered;}

    public float getX(){return this.x;}
    public float getY(){return this.y;}
    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public float getWidth(){return this.width;}
    public float getHeight(){return this.height;}
    public String getID(){return this.id;}
    public void setSX(float SX) {this.SX = SX;}
    public void setSY(float SY) {this.SY = SY;}
    public void setOX(float OX) {this.OX = OX;}
    public void setOY(float OY) {this.OY = OY;}
    public void setCOLOR(Color color){this.COLOR = color;}

    public void resetStyleProperties(){
        this.COLOR = defaultCOLOR;
        this.OX = defaultOX;
        this.OY = defaultOY;
        this.SX = defaultSX;
        this.SY = defaultSY;
    }
    public void onClickEvent(){
        if(isClicked && onClickAction != null){
            onClickAction.execute();
        }
    }
    public void onHoverEvent(){
        if(isHovered && onHoverAction != null){
            onHoverAction.execute();
        }
    }
    public void onUnhoverEvent(){
        if(!isHovered && onUnhoverAction != null){
            onUnhoverAction.execute();
        }
    }

}