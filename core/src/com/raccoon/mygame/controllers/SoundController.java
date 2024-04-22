package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Sound cookingSound;
    private Sound bellSound;
    private Sound doorSound;
    private Sound chopSound;
    private Sound honkSound;
    private Sound ventSound;
    private Sound clickSound;
    private Sound swipeSound;
    private Sound switchSound;
    private Music storeSound;
    private Sound orderSound;
    public SoundController(){
        cookingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/cooking.ogg"));
        bellSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bell.ogg"));
        doorSound = Gdx.audio.newSound(Gdx.files.internal("sounds/door.ogg"));
        honkSound = Gdx.audio.newSound(Gdx.files.internal("sounds/honk.ogg"));
        ventSound = Gdx.audio.newSound(Gdx.files.internal("sounds/vent.ogg"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menuclick.ogg"));
        swipeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/swipe.ogg"));
        switchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/inventory.ogg"));
        storeSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/storeAmbience.ogg"));
        orderSound = Gdx.audio.newSound(Gdx.files.internal("sounds/order.ogg"));
        chopSound = Gdx.audio.newSound(Gdx.files.internal("sounds/chop.ogg"));
    }
    public void cookplay(){
        cookingSound.play();
    }
    public void chopPlay(){
        chopSound.play();
    }
    public void orderPlay(){
        orderSound.play();
    }
    public void bellPlay(){
        bellSound.play();
    }
    public void doorPlay(){
        doorSound.play();
    }
    public void honkPlay(){
        honkSound.play();
    }
    public void ventPlay(){
        ventSound.play();
    }

    public void storePlay(){
        storeSound.play();
        storeSound.setLooping(true);
    }
    public void storeStop(){
        storeSound.stop();
    }
    public void switchPlay(){
        switchSound.play();
    }

    public void clickPlay(){
//        clickSound.setVolume(3,3);
        clickSound.play();
    }

    public void swipePlay(){
        swipeSound.play();
    }
}

