package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.Modal;
import com.raccoon.mygame.models.Scene;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class CutsceneController extends WorldController {
    private InputController input;
    private GameCanvas canvas;
    private int currentSceneIndex;
    private Array<Scene> sceneCollection = new Array<>();
    private AssetDirectory assetDirectory;
    private boolean goToLevel = false;

    private Texture comic1;
    private Texture comic2;
    private Texture comic3;
    private Texture comic4;
    private Texture comic5;


    public CutsceneController(GameCanvas canvas, InputController input, AssetDirectory assetDirectory){
        this.input = input;
        this.canvas = canvas;
        this.assetDirectory = assetDirectory;

        comic1 = assetDirectory.getEntry("comic1", Texture.class);
        comic2 = assetDirectory.getEntry("comic2", Texture.class);
        comic3 = assetDirectory.getEntry("comic3", Texture.class);
        comic4 = assetDirectory.getEntry("comic4", Texture.class);
        comic5 = assetDirectory.getEntry("comic5", Texture.class);

        UIButton comicButton1 = new UIButton(comic1, "comic1",0,0,0.67f,0.67f,canvas);
        UIButton comicButton2 = new UIButton(comic2, "comic2", 0,0,0.67f,0.67f,canvas);
        UIButton comicButton3 = new UIButton(comic3, "comic3", 0,0,0.67f,0.67f,canvas);
        UIButton comicButton4 = new UIButton(comic4, "comic4", 0,0,0.67f,0.67f,canvas);
        UIButton comicButton5 = new UIButton(comic5, "comic5", 0,0,0.67f,0.67f,canvas);

        Scene comicScene1 = new Scene(canvas, input);
        comicScene1.addElement(comicButton1, ()-> comicScene1.setGoToNextScene(true));
        sceneCollection.add(comicScene1);

        Scene comicScene2 = new Scene(canvas, input);
        comicScene2.addElement(comicButton2, ()-> comicScene2.setGoToNextScene(true));
        sceneCollection.add(comicScene2);

        Scene comicScene3 = new Scene(canvas, input);
        comicScene3.addElement(comicButton3, ()-> comicScene3.setGoToNextScene(true));
        sceneCollection.add(comicScene3);

        Scene comicScene4 = new Scene(canvas, input);
        comicScene4.addElement(comicButton4, ()-> comicScene4.setGoToNextScene(true));
        sceneCollection.add(comicScene4);

        Scene comicScene5 = new Scene(canvas, input);
        comicScene5.addElement(comicButton5, ()-> this.setGoToLevel(true));
        sceneCollection.add(comicScene5);
    }
    public void draw(){
        sceneCollection.get(currentSceneIndex).draw();
    }
    public void update(){
        getCurrentScene().update();
        if(getCurrentScene().getGoToNextScene()){
            this.setSceneIndex(currentSceneIndex+=1);
            getCurrentScene().setGoToNextScene(false);

        }
    }
    public void setSceneIndex(int index){
        if(index < 0 || index >= sceneCollection.size)
            return;
        this.currentSceneIndex = index;
    }
    private Scene getCurrentScene(){
        return this.sceneCollection.get(currentSceneIndex);
    }

    public boolean getGoToLevel(){
        return this.goToLevel;
    }
    public void setGoToLevel(boolean go){
        this.goToLevel = go;
    }
}
