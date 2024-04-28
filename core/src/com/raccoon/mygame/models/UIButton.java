package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.view.GameCanvas;

import javax.swing.plaf.ButtonUI;

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
    private Texture default_texture;

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
    private float defaultWidth;
    private float defaultHeight;

    private float OX = defaultOX, OY = defaultOY;
    private float SX = defaultSX, SY = defaultSY;
    private Color COLOR = defaultCOLOR;

    private OrthographicCamera camera;
    private boolean isSticky = false;

    private Array<UIButton> children = new Array<UIButton>();

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
        this.defaultWidth = width;
        this.defaultHeight = height;
        this.width = defaultWidth;
        this.height = defaultHeight;
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
        this.default_texture = texture;
        this.defaultWidth = texture.getWidth();
        this.defaultHeight = texture.getHeight();
        this.width = defaultWidth;
        this.height = defaultHeight;
    }
    /**
     * Constructor to create a button with a provided texture.
     * All parameters are in pixels unless specified otherwise.
     * Width and height of a button are based on the texture provided.
     *
     * @param texture The texture to apply to the button
     * @param x The x position of the button
     * @param y THe y position of the button
     * @param defaultSX The default x draw scale of the button
     * @param defaultSY The default y draw scale of the button
     * @param canvas Gonna be honest don't know why I need this. Probably pointless.
     * */
    public UIButton(Texture texture, String id, float x, float y, float defaultSX, float defaultSY,GameCanvas canvas) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.defaultWidth = texture.getWidth();
        this.defaultHeight = texture.getHeight();
        this.defaultSX = defaultSX;
        this.defaultSY = defaultSY;
        this.width = defaultWidth * defaultSX;
        this.height = defaultHeight * defaultSY;
        this.SX = defaultSX;
        this.SY = defaultSY;
        this.default_texture = texture;
    }

    /**
     * Creates a UIButton relative to camera
     * DONT LET THIS TYPE OF BUTTON HAVE CHILDREN I DONT ADJUST FOR IT
     * NO BABIES
     * */
    public UIButton(Texture texture, String id, float x, float y, float defaultSX, float defaultSY,GameCanvas canvas, OrthographicCamera camera, boolean isSticky) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.defaultWidth = texture.getWidth();
        this.defaultHeight = texture.getHeight();
        this.defaultSX = defaultSX;
        this.defaultSY = defaultSY;
        this.width = defaultWidth * defaultSX;
        this.height = defaultHeight * defaultSY;
        this.SX = defaultSX;
        this.SY = defaultSY;
        this.default_texture = texture;
        this.camera = camera;
        this.isSticky = isSticky;
    }


    /**
     * Draws this button.
     * @param canvas Canvas to draw the button onto.
     * */
    public void draw(GameCanvas canvas) {
        if(this.texture == null)
            return;

        if (isSticky && camera != null) {
            float drawX = this.x;
            float drawY = this.y;
            drawX += camera.position.x - camera.viewportWidth / 2;
            drawY += camera.position.y - camera.viewportHeight / 2;
            canvas.draw(new TextureRegion(this.texture), this.COLOR, this.OX, this.OY, drawX, drawY, 0, this.SX, this.SY);
            return;
        }

        canvas.draw(new TextureRegion(this.texture),COLOR, this.OX, this.OY, this.x, this.y, 0, this.SX, this.SY);
//        TextureRegion region, Color tint, float ox, float oy,
//        float x, float y, float angle, float sx, float sy
        for(UIButton child: children){
            float[] childOffset = calculateChildPosition(child);
            float childX = x + childOffset[0] + child.getX();
            float childY = y + childOffset[1] + child.getY();
            float childSX = SX * child.getSX();
            float childSY = SY * child.getSY();
            child.draw(canvas, childX, childY, childSX, childSY);
        }
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
    public void setSX(float SX) {
        this.SX = SX;
        this.width = defaultWidth * SX;
    }
    public void setSY(float SY) {
        this.SY = SY;
        this.height = defaultHeight * SY;
    }
    public void setTexture(Texture texture){this.texture = texture;}
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
        this.width = defaultWidth * defaultSX;
        this.height = defaultHeight * defaultSY;
        this.texture = default_texture;
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

    public void setDefaultScale(float sx, float sy){
        this.defaultSX = sx;
        this.defaultSY = sy;
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

    public void draw(GameCanvas canvas, float x, float y) {
        canvas.draw(new TextureRegion(this.texture),COLOR, this.OX, this.OY, x, y, 0, this.SX, this.SY);
        for(UIButton child: children){
            float childX = x + child.getX();
            float childY = y + child.getY();
            float childSX = SX * child.SX;
            float childSY = SY * child.SY;
            child.draw(canvas, childX, childY);
        }
    }
    public void draw(GameCanvas canvas, float x, float y, float sx, float sy){
        if(this.texture == null)
            return;
        canvas.draw(new TextureRegion(this.texture),COLOR, this.OX, this.OY, x, y, 0, sx, sy);
    }
    public void addChild(UIButton child){
        children.add(child);
//        child.setX(this.x + child.x);
//        child.setY(this.y + child.y);
    }
    private float[] calculateChildPosition(UIButton child){
        float offsetX = (this.width - defaultWidth) * child.getX() / defaultWidth;
        float offsetY = (this.height - defaultHeight) * child.getY() / defaultHeight;
        return new float[]{offsetX, offsetY};
    }
    private float getSX(){
        return this.SX;
    }
    private float getSY(){
        return this.SY;
    }

    public boolean getSticky(){
        return this.isSticky;
    }

}