package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.view.GameCanvas;

public class Modal {
    private String id;
    private float x;
    private float y;
    private Texture background;
    private Array<UIButton> elements = new Array<>();

    private boolean isSticky = false;

    private boolean isActive = false;

    public Modal(String id, float x, float y, Texture texture) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.background = texture;
    }
    public Modal(String id, float x, float y, Texture texture, boolean isSticky) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.background = texture;
        this.isSticky = isSticky;
    }

    public void addElement(UIButton element){
        elements.add(element);

        //just setting the positions relative to the modal
        element.setX(element.getX() + this.x);
        element.setY(element.getY() + this.y);
    }

    public void draw(GameCanvas canvas){
        OrthographicCamera camera = canvas.getCamera();
        // Center the modal in the camera's viewport
        float drawX = camera.position.x-camera.viewportWidth/2;
        float drawY = camera.position.y - 360;

        canvas.draw(background, drawX, drawY);
        for(UIButton element : elements){
            if(!element.getActive()){
                continue;
            }
            // Draw each element relative to the modal's position
            element.draw(canvas, drawX + element.getX(), drawY + element.getY());
        }
    }
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean getActive(){return this.isActive;}

    public Array<UIButton> getElements(){return this.elements;}

    public String getId(){
        return this.id;
    }
}
