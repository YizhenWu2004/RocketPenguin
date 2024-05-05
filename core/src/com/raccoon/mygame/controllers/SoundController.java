package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;
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
    private Sound catSound;
    private Sound bearSound;
    private Sound ferretSound;
    private Sound otterSound;
    private Sound goatSound;
    private Music cafeSong;

    private Sound pot;
    private Sound pan;
    private float musicVol;
    private Music menu;
    private float sfxvol;
    public SoundController(){
        pan = Gdx.audio.newSound(Gdx.files.internal("sounds/pan.ogg"));
        pot =  Gdx.audio.newSound(Gdx.files.internal("sounds/pot.ogg"));
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
        chopSound = Gdx.audio.newSound(Gdx.files.internal("sounds/newchop.ogg"));
        catSound = Gdx.audio.newSound(Gdx.files.internal("sounds/cat.ogg"));
        ferretSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ferret.ogg"));
        bearSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bear.ogg"));
        otterSound = Gdx.audio.newSound(Gdx.files.internal("sounds/otter.ogg"));
        menu = Gdx.audio.newMusic(Gdx.files.internal("sounds/racoon_menu.mp3"));
        goatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/goat.ogg"));
        musicVol = 0.8f;
        sfxvol = 1.4f;
        cafeSong = Gdx.audio.newMusic(Gdx.files.internal("sounds/cafetrackv2.ogg"));
    }
    public void cookplay(){
        cookingSound.play(sfxvol);
    }
    public void goatPlay(){
        goatSound.play(sfxvol);
    }
    public void ferretPlay(){
        ferretSound.play(sfxvol);
    }
    public void catPlay(){
        catSound.play(sfxvol);
    }
    public void otterPlay(){
        otterSound.play(sfxvol);
    }
    public void bearPlay(){
        bearSound.play(sfxvol);
    }
    public void chopPlay(){
        chopSound.play(sfxvol);
    }
    public void menuPlay(){
        menu.setVolume(musicVol);
        menu.play();
        menu.setLooping(true);
    }
    public void menuStop(){
        menu.stop();
    }
    public void panStopp(){
        pan.stop();
    }
    public void potStop(){
        pot.stop();
    }
    public void orderPlay(){
        orderSound.play(sfxvol);
    }
    public void bellPlay(){
        bellSound.play(sfxvol);
    }
    public void panPlay(){
        pan.play(sfxvol);
    }
    public void potPlay(){
        pot.play(sfxvol);
    }
    public void doorPlay(){
        doorSound.play(sfxvol);
    }
    public void honkPlay(){
        long id = honkSound.play(sfxvol);
        honkSound.setPitch(id, pitching2());
    }
    public void ventPlay(){
        ventSound.play(sfxvol);
    }

//    public void setsfx(float sfx){
//        sfxvol = sfx;
//    }
//    public void setmusic(float music){
//        musicVol = music;
//    }

    public void incmusic(){
        if(musicVol < 1.0f){
            System.out.println("increasing music");
            musicVol = musicVol + 0.1f;
        } else {
            musicVol = 2.0f;
        }
    }
    public void decmusic(){
        if(musicVol > 0.0f){
            if(musicVol - 0.1f > 0) {
                musicVol = musicVol - 0.1f;
            } else {
                musicVol = 0.0f;
            }
        } else {
            musicVol = 0.0f;
        }
    }

    public float getmusic(){
        return musicVol;
    }
    public float getsfx(){
        return sfxvol;
    }
    public void incsfx(){
        if(sfxvol < 2.0f){
            System.out.println("increasing sfx");
            sfxvol = sfxvol + 0.2f;
        } else {
            sfxvol = 2.0f;
        }
    }

    public void decsfx(){
        if(sfxvol > 0.0f){
            System.out.println("decreasing sfx");
            sfxvol = sfxvol - 0.2f;
        } else {
            sfxvol = 0.0f;
        }
    }
    public void cafeeactualstop(){
        cafeSong.stop();
    }
    public void storePlay(){
        storeSound.setVolume(musicVol);
        storeSound.play();
        storeSound.setLooping(true);
    }
    public void cafePlay(){
        cafeSong.setVolume(musicVol);
        cafeSong.play();
        cafeSong.setLooping(true);
    }
    public void cafeStop(){
        cafeSong.pause();
    }
    public void storeStop(){
        storeSound.stop();
    }
    public void switchPlay(){
        switchSound.play(sfxvol);
    }

    public void clickPlay(){
//        clickSound.setVolume(3,3);
       long id = clickSound.play(sfxvol);
       clickSound.setPitch(id, pitching());
    }

    public void swipePlay(){
        swipeSound.play(sfxvol);
    }

    private float pitching(){
        Random rand = new Random();
        int randint = rand.nextInt(5);
        if(randint == 0){
            return 1.0f;
        } else if (randint == 1){
            return 0.85f;
        } else if (randint == 2){
            return 0.7f;
        } else if (randint == 3){
            return 0.5f;
        }
        return 0.5f;
    }

    private float pitching2(){
        Random rand = new Random();
        int randint = rand.nextInt(5);
        if(randint == 0){
            return 1.0f;
        } else if (randint == 1){
            return 0.95f;
        } else if (randint == 2){
            return 0.9f;
        } else if (randint == 3){
            return 0.85f;
        }
        return 0.8f;
    }
}


