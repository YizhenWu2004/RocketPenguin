package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.view.GameCanvas;

public class QuestionExpression extends Expression{
    private final Texture backgroundTexture;
    private final Texture hollowTexture;

    private Texture fillTexture;

    float currentProgress;
    float totalProgress;

    public QuestionExpression(String name, float x, float y, float currentProgress, float totalProgress) {
        super(name, x, y);
        this.backgroundTexture = new Texture("720/"+ "circleBackground" + ".png");
        this.hollowTexture = new Texture("720/"+ "hollow" + ".png");

        this.fillTexture = new Texture("fillColor/"+ "color1" + ".png");

        this.currentProgress = currentProgress;
        this.totalProgress = totalProgress;
    }

    public void updateCurProgress(float newVal){
        currentProgress = newVal;

        if(currentProgress/totalProgress <= 0.1f){
            fillTexture = new Texture("fillColor/"+ "color1" + ".png");
        }
        else if(currentProgress/totalProgress <= 0.3f){
            fillTexture = new Texture("fillColor/"+ "color2" + ".png");
        }
        else if(currentProgress/totalProgress <= 0.5f){
            fillTexture = new Texture("fillColor/"+ "color3" + ".png");
        }
        else if(currentProgress/totalProgress <= 0.7f){
            fillTexture = new Texture("fillColor/"+ "color4" + ".png");
        }
        else if(currentProgress/totalProgress <= 0.9f){
            fillTexture = new Texture("fillColor/"+ "color5" + ".png");
        }
        else{
            fillTexture = new Texture("fillColor/"+ "color6" + ".png");
        }
    }

    public void draw(GameCanvas canvas) {
        canvas.draw(backgroundTexture, Color.WHITE, 10, 10, x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);

        float progressRatio = currentProgress / totalProgress;
        float syScale;

        if(progressRatio <= 1.0f / 24.0f) {
            syScale = 1.0f / 24.0f;
        }
        else if(progressRatio <= 2.0f / 24.0f) {
            syScale = 2.0f / 24.0f;
        }
        else if(progressRatio <= 3.0f / 24.0f) {
            syScale = 3.0f / 24.0f;
        }
        else if(progressRatio <= 4.0f / 24.0f) {
            syScale = 4.0f / 24.0f;
        }
        else if(progressRatio <= 5.0f / 24.0f) {
            syScale = 5.0f / 24.0f;
        }
        else if(progressRatio <= 6.0f / 24.0f) {
            syScale = 6.0f / 24.0f;
        }
        else if(progressRatio <= 7.0f / 24.0f) {
            syScale = 7.0f / 24.0f;
        }
        else if(progressRatio <= 8.0f / 24.0f) {
            syScale = 8.0f / 24.0f;
        }
        else if(progressRatio <= 9.0f / 24.0f) {
            syScale = 9.0f / 24.0f;
        }
        else if(progressRatio <= 10.0f / 24.0f) {
            syScale = 10.0f / 24.0f;
        }
        else if(progressRatio <= 11.0f / 24.0f) {
            syScale = 11.0f / 24.0f;
        }
        else if(progressRatio <= 12.0f / 24.0f) {
            syScale = 12.0f / 24.0f;
        }
        else if(progressRatio <= 13.0f / 24.0f) {
            syScale = 13.0f / 24.0f;
        }
        else if(progressRatio <= 14.0f / 24.0f) {
            syScale = 14.0f / 24.0f;
        }
        else if(progressRatio <= 15.0f / 24.0f) {
            syScale = 15.0f / 24.0f;
        }
        else if(progressRatio <= 16.0f / 24.0f) {
            syScale = 16.0f / 24.0f;
        }
        else if(progressRatio <= 17.0f / 24.0f) {
            syScale = 17.0f / 24.0f;
        }
        else if(progressRatio <= 18.0f / 24.0f) {
            syScale = 18.0f / 24.0f;
        }
        else if(progressRatio <= 19.0f / 24.0f) {
            syScale = 19.0f / 24.0f;
        }
        else if(progressRatio <= 20.0f / 24.0f) {
            syScale = 20.0f / 24.0f;
        }
        else if(progressRatio <= 21.0f / 24.0f) {
            syScale = 21.0f / 24.0f;
        }
        else if(progressRatio <= 22.0f / 24.0f) {
            syScale = 22.0f / 24.0f;
        }
        else if(progressRatio <= 23.0f / 24.0f) {
            syScale = 23.0f / 24.0f;
        }
        else {
            syScale = 1.0f;
        }

        canvas.draw(fillTexture, Color.WHITE, 10, 10, x+13, y-1, 0.0f, 0.5f, 0.5f * syScale);

        canvas.draw(hollowTexture, Color.WHITE, 10, 10, x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);
    }

}
