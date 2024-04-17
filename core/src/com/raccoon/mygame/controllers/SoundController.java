package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Sound cookingSound;
    private Sound bellSound;
    private Sound doorSound;
    private Sound honkSound;
    private Sound ventSound;
    public SoundController(){
        cookingSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/cooking.ogg"));
        bellSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/bell.ogg"));
        doorSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/door.ogg"));
        honkSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/honk.ogg"));
        ventSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/vent.ogg"));
    }
    public void cookplay(){
        cookingSound.play();
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
}

