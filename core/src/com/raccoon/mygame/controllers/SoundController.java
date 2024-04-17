package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private Sound cookingSound;

    public SoundController(){
        cookingSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/cooking.ogg"));
    }
    public void cookplay(){
        cookingSound.play();
    }
}
