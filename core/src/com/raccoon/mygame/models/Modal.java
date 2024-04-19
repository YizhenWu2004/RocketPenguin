package com.raccoon.mygame.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.view.GameCanvas;

public class Modal {
    private String id;
    private float x;
    private float y;
    private Texture background;
    private Array<UIButton> elements = new Array<>();

    private boolean isActive = false;

    public Modal(String id, float x, float y, Texture texture) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.background = texture;
    }

    public void addElement(UIButton element){
        elements.add(element);

        //just setting the positions relative to the modal
        element.setX(element.getX() + this.x);
        element.setY(element.getY() + this.y);
    }

    public void draw(GameCanvas canvas){
        canvas.draw(background,0,0);
        for(UIButton element : elements){
            element.draw(canvas);
        }
    }
    public void setActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean getActive(){return this.isActive;}
}
