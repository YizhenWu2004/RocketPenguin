package com.raccoon.mygame.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.view.GameCanvas;

public class QuestionExpression extends Expression{
    private final Texture backgroundTexture;
    private final Texture hollowTexture;

    private Texture fillTexture;

    float currentProgress;
    float totalProgress;

    public Texture color1;
    public Texture color2;
    public Texture color3;
    public Texture color4;
    public Texture color5;
    public Texture color6;

    public QuestionExpression(String name, float x, float y, float currentProgress, float totalProgress, AssetDirectory directory) {
        super(name, x, y, directory);

        color1=directory.getEntry("color1",Texture.class);
        color2=directory.getEntry("color2",Texture.class);
        color3=directory.getEntry("color3",Texture.class);
        color4=directory.getEntry("color4",Texture.class);
        color5=directory.getEntry("color5",Texture.class);
        color6=directory.getEntry("color6",Texture.class);

        this.backgroundTexture = directory.getEntry("circlebackground",Texture.class);
        this.hollowTexture = directory.getEntry("hollow",Texture.class);

        this.fillTexture = color1;

        this.currentProgress = currentProgress;
        this.totalProgress = totalProgress;
    }

    public void updateCurProgress(float newVal){
        currentProgress = newVal;

        if(currentProgress/totalProgress <= 0.1f){
            fillTexture = color1;
        }
        else if(currentProgress/totalProgress <= 0.3f){
            fillTexture = color2;
        }
        else if(currentProgress/totalProgress <= 0.5f){
            fillTexture = color3;
        }
        else if(currentProgress/totalProgress <= 0.7f){
            fillTexture = color4;
        }
        else if(currentProgress/totalProgress <= 0.9f){
            fillTexture = color5;
        }
        else{
            fillTexture = color6;
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
