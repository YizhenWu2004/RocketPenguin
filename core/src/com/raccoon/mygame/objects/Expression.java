package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class Expression {
    protected static final float TEXTURE_SX = 1f;
    protected static final float TEXTURE_SY = 1f;

    public String type;

    protected float x;
    protected float y;
    private Texture texture;

    public Expression(String name, float x, float y) {
        this.type = name;
        this.x = x;
        this.y = y;
        if(name == "exclamation"){
            this.texture = new Texture("720/"+ "exclamation" + ".png");
        }
        else if(name == "zzz"){
            this.texture = new Texture("720/"+ "zzz" + ".png");
        }
//        else if(name == "space"){
//            this.texture = new Texture("720/"+ "space" + ".png");
//        }
        else if(name == "customerQuestion"){
            this.texture = new Texture("720/"+ "customerQuestion" + ".png");
        }
        else if(name == "customerThumbsUp"){
            this.texture = new Texture("720/"+ "customerThumbsUp" + ".png");
        }
        else if(name == "customerThumbsDown"){
            this.texture = new Texture("720/"+ "customerThumbsDown" + ".png");
        }
        //commented out because question should always be QuestionExpression class
//        if(name == "question"){
//            this.texture = new Texture("720/"+ "question" + ".png");
//        }
    }

    public float getXPosition() {
        return x;
    }


    public float getYPosition() {
        return y;
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getTextureWidth() {
        return texture.getWidth() * TEXTURE_SX;
    }

    public float getTextureHeight() {
        return texture.getHeight() * TEXTURE_SY;
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);
    }
    public void drawSpace(GameCanvas canvas, float drawScaleX, float drawScaleY) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                x*drawScaleX, y*drawScaleY, 0.0f, 0.5f, 0.5f);
    }

    public void drawCustomerQuestion(GameCanvas canvas, float given_x, float given_y, float drawScaleX, float drawScaleY) {
        canvas.draw(texture, Color.WHITE, 10, 10,
                given_x*drawScaleX, given_y*drawScaleY, 0.0f, 0.8f, 0.8f);
    }
}
