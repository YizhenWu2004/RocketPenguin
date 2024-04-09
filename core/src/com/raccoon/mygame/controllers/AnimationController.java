package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.VentObstacle;
import com.raccoon.mygame.util.FilmStrip;

public class AnimationController {
    /** How fast we change frames (one frame per 4 calls to update) */
    private static final float ANIMATION_SPEED = 0.10f;
    /** The number of animation frames in our filmstrip */

    private final FilmStrip playerWalk;
    private final FilmStrip playerIdle;
    private final FilmStrip playerServe;
    private final FilmStrip playerServeIdle;

    private final FilmStrip goatWalk;
    private final FilmStrip goatIdle;

    private final FilmStrip gooseWalk;
    private final FilmStrip gooseWalkBack;
    private final FilmStrip gooseWalkUp;
    private final FilmStrip gooseIdle;
    private final FilmStrip gooseChase;

    private InputController input;
    AnimationController(InputController input){
        this.input = input;

        playerWalk = new FilmStrip(new Texture("720/rockorun.png"), 1, 5, 5);
        playerIdle = new FilmStrip(new Texture("720/rockoidle.png"), 1, 1, 1);
        playerServe = new FilmStrip(new Texture("720/rockodish.png"), 1, 4, 4);
        playerServeIdle = new FilmStrip(new Texture("720/rockodishidle.png"),1,1,1);

        goatWalk = new FilmStrip(new Texture("720/goatwalk.png"), 1, 4, 4);
        goatIdle = new FilmStrip(new Texture("720/goat.png"), 1,1,1);

        gooseWalk = new FilmStrip(new Texture("720/goosewalk.png"), 2,5,10);
        gooseWalkBack = new FilmStrip(new Texture("720/goosewalkback.png"),1,6,6);
        gooseWalkUp = new FilmStrip(new Texture("720/goosewalkup.png"),1,6,6);
        gooseIdle = new FilmStrip(new Texture("720/gooseidle.png"),1,1,1);
        gooseChase = new FilmStrip(new Texture("720/goosechase.png"),1,6,6);
    }

    public void handleAnimation(Player o, float delta){
        if((o.dishInventory.leftFilled() && (o.getVX() !=0 || o.getVY()!=0)) || (o.dishInventory.rightFilled() && (o.getVX()!=0 || o.getVY()!=0))){
            o.setFilmStrip(playerServe);
            o.updateAnimation(delta);
            return;
        } else if((o.dishInventory.leftFilled() && o.getVX() ==0 && o.getVY()==0) || (o.dishInventory.rightFilled() && o.getVX()==0 && o.getVY()==0)){
            o.setFilmStrip(playerServeIdle);
            o.updateAnimation(delta);
            return;
        }
        if(input.getYMovement()!=0 || input.getXMovement()!=0){
            o.setFilmStrip(playerWalk);
        }
        else{
            o.setFilmStrip(playerIdle);
        }
        o.updateAnimation(delta);
    }
    public void handleAnimation(Guard o, float delta){
        if(o.getAIController().getCurrentState() == GuardAIController.AIState.CHASE){
            o.setFilmStrip(gooseChase);
            o.updateAnimation(delta);
            return;
        }
        if(o.getVX()>1 || o.getVX() < -1){
            //horizontal
            o.setFilmStrip(gooseWalk);
            o.updateAnimation(delta);
            return;
        }
        if(o.getVY() > 0){
            //up
            o.setFilmStrip(gooseWalkBack);
            o.updateAnimation(delta);
            return;
        }
        if(o.getVY() < 0){
            //down
            o.setFilmStrip(gooseWalkUp);
            o.updateAnimation(delta);
            return;
        }

        o.setFilmStrip(gooseIdle);
        o.updateAnimation(delta);
    }

    public void processGuards(Array<Guard> guards, float delta){
        for(Guard g: guards){
            handleAnimation(g, delta);
        }
    }
    public void processCustomers(Array<Customer> customers, float delta){
        for (Customer c: customers) {
            handleAnimation(c, delta);
        }
    }

    public void handleAnimation(Customer o, float delta){
        if(o.getVX() > 0 || o.getVX() < 0 || o.getVY() > 0 || o.getVY() < 0){
            o.setFilmStrip(goatWalk);
        }
        else{
            o.setFilmStrip(goatIdle);
        }

        o.updateAnimation(delta);
    }

    public void handleAnimation(VentObstacle o, float delta){
        //yeahh idk about this one yet
    }
}
