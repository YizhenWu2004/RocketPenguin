package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.raccoon.mygame.models.*;
import com.raccoon.mygame.view.GameCanvas;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.FileNotFoundException;

public class SplashScreenController extends WorldController{
    private VideoPlayer videoPlayer;
    private FileHandle video;
    private GameCanvas canvas;
    public boolean videoDonePlaying = false;

    public SplashScreenController(GameCanvas canvas){
        this.canvas = canvas;

        videoPlayer = VideoPlayerCreator.createVideoPlayer();

        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                //System.out.println("Video done playing!");
                videoDonePlaying = true;
            }
        });

        video = Gdx.files.internal("videos/splashscreenthree.webm");
        if (video.exists()) {
            try {
                videoPlayer.play(video);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Video file not found!");
        }
    }
    public void dispose() {
        if (videoPlayer != null) {
            videoPlayer.dispose();
            videoPlayer = null;
        }
        super.dispose();
    }

    public void update() {
        videoPlayer.update();
    }

    public void draw(){
        try {
            Texture frame = videoPlayer.getTexture();
            canvas.draw(frame, 0, 0);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void debug() {

    }
}
