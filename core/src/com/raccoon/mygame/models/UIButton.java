package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.raccoon.mygame.view.GameCanvas;

/**
 * In reality, this should just be called UIElement.
 *
 * This class allows for the creation of buttons, or just drawing random UI things.
 * Your buttons don't necessarily need to have functionality.
 * */
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

    /**
     * Constructor to create a button with a provided texture.
     * All parameters are in pixels unless specified otherwise.
     * Width and height of a button are based on the texture provided.
     *
     * @param texture The texture to apply to the button
     * @param x The x position of the button
     * @param y THe y position of the button
     * @param canvas Gonna be honest don't know why I need this. Probably pointless.
     * */
    public UIButton(Texture texture, String id, float x, float y, GameCanvas canvas) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    /**
     * Draws this button.
     * @param canvas Canvas to draw the button onto.
     * */
    public void draw(GameCanvas canvas) {
        canvas.draw(new TextureRegion(this.texture),Color.WHITE, this.OX, this.OY, this.x, this.y, 0, this.SX, this.SY);
//        TextureRegion region, Color tint, float ox, float oy,
//        float x, float y, float angle, float sx, float sy
    }
    /**
     * Sets the action that should be performed upon button click.
     * Use lambda expressions or :: notation to specify a function/method to use.
     * @param action Method to perform on click. Must be void.
     * */
    public void setOnClickAction(ButtonAction action) {
        this.onClickAction = action;
    }
    /**
     * Sets the action that should be performed on button hover.
     * Use lambda expressions or :: notation to specify a function/method to use.
     * @param action Method to perform on hover. Must be void.
     * */
    public void setOnHoverAction(ButtonHover action) {
        this.onHoverAction = action;
    }
    /**
     * Sets the action that should be performed when the button is NOT being hovered on.
     * Use lambda expressions or :: notation to specify a function/method to use.
     * @param action Method to perform on un-hover. Must be void.
     * */
    public void setOnUnhoverAction(ButtonUnhover action) {
        this.onUnhoverAction = action;
    }

    /**
     * Sets whether or not this button is clicked.
     * @param isClicked The state of button click to set to.
     * */
    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
    /**
     * Gets whether or not this button is clicked.
     * Probably pointless. No usages. Maybe one day.
     * */
    public boolean getClicked(){
        return isClicked;
    }
    /**
     * Sets whether or not a button is being hovered over.
     * @param isHovered hover state to set to
     * */
    public void setHovered(boolean isHovered){this.isHovered = isHovered;}
    /**
     * Gets the hover state.
     * */
    public boolean getHovered(){return isHovered;}

    //this shit better be self-explanatory
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

    /**
     * Resets all stylistic properties of this button.
     * This does not include position.
     * */
    public void resetStyleProperties(){
        this.COLOR = defaultCOLOR;
        this.OX = defaultOX;
        this.OY = defaultOY;
        this.SX = defaultSX;
        this.SY = defaultSY;
    }
    /**
     * Executes the provided lambda method on click.
     *
     * Set the method you want to use using setOnClickAction
     * */
    public void onClickEvent(){
        if(isClicked && onClickAction != null){
            onClickAction.execute();
        }
    }

    /**
     * Executes the provided lambda method on click.
     *
     * Set the method you want to use using setOnHoverAction
     * */
    public void onHoverEvent(){
        if(isHovered && onHoverAction != null){
            onHoverAction.execute();
        }
    }

    /**
     * Executes the provided lambda method on click.
     *
     * Set the method you want to use using setOnUnhoverAction
     * */
    public void onUnhoverEvent(){
        if(!isHovered && onUnhoverAction != null){
            onUnhoverAction.execute();
        }
    }

}