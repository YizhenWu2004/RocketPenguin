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
        float syScale = Math.min(progressRatio,1);
        canvas.draw(fillTexture, Color.WHITE, 10, 10, x+13, y-1, 0.0f, 0.5f, 0.5f * syScale);

        canvas.draw(hollowTexture, Color.WHITE, 10, 10, x, y, 0.0f, TEXTURE_SX, TEXTURE_SY);
    }
}
