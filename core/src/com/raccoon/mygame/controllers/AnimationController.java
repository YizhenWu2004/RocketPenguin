package com.raccoon.mygame.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.models.Guard;
import com.raccoon.mygame.models.Player;
import com.raccoon.mygame.objects.CookingStationObject;
import com.raccoon.mygame.objects.VentObstacle;
import com.raccoon.mygame.util.FilmStrip;

import java.util.Map;
import java.util.Objects;

public class AnimationController {
    /** How fast we change frames (one frame per 4 calls to update) */
    private static final float ANIMATION_SPEED = 0.10f;
    // sus

    //If you want to add a new animation you store it in here.
    private final FilmStrip playerWalk;
    private final FilmStrip playerIdle;
    private final FilmStrip playerServe;
    private final FilmStrip playerServeIdle;
    private final FilmStrip playerCook;
    private final FilmStrip playerChop;
    private final FilmStrip playerSneak;
    //private final FilmStrip playerKickedOut;
    private final FilmStrip playerSwipe;
    private final FilmStrip playerSneakIdle;

    private final FilmStrip goatWalk;
    private final FilmStrip goatIdle;

    private final FilmStrip ferretWalk;
    private final FilmStrip ferretIdle;

    private final FilmStrip catWalk;
    private final FilmStrip catIdle;

    private final FilmStrip bearWalk;
    private final FilmStrip bearIdle;

    private final FilmStrip otterWalk;
    private final FilmStrip otterIdle;

    private final FilmStrip gooseWalk;
    private final FilmStrip gooseWalkBack;
    private final FilmStrip gooseWalkUp;
    private final FilmStrip gooseIdle;
    private final FilmStrip gooseChase;
    //private final FilmStrip gooseCatch;
    private final FilmStrip gooseSleep;
    private final FilmStrip gooseSleepIdle;
    private final FilmStrip gooseWake;
    private final FilmStrip gooseIdleUp;
    private final FilmStrip gooseIdleDown;

    private final FilmStrip ventIn;
    private final FilmStrip ventOut;
    private final FilmStrip ventIdle;

    private final FilmStrip kickedOutReturn;
    private FilmStrip wokIdle;
    private FilmStrip wokSizzle;

    private FilmStrip potIdle;
    private FilmStrip potSizzle;
//    private final FilmStrip potSizzle;
//    private final FilmStrip panSizzle;

    private FilmStrip getCaught;

    private InputController input;
    /**
     * Creates an animation controller
     * Make a new animation controller wherever you wish to have them play
     *
     * @param input The input controller used for determining animation state
     * */
    AnimationController(InputController input, AssetDirectory directory){
        this.input = input;

        playerWalk = directory.getEntry("rockorun.strip", FilmStrip.class);
        playerIdle = directory.getEntry("rockoidle.strip", FilmStrip.class);
        playerServe = directory.getEntry("rockodish.strip", FilmStrip.class);
        playerServeIdle = directory.getEntry("rockodishidle.strip", FilmStrip.class);
        playerCook = directory.getEntry("rockocook.strip", FilmStrip.class);
        playerChop = directory.getEntry("rockochop.strip", FilmStrip.class);
        //playerKickedOut = new FilmStrip(new Texture("720/rockokickedout.png"),4,5,20);
        playerSneak = directory.getEntry("rockosneak.strip", FilmStrip.class);
        playerSwipe = directory.getEntry("rockoswipe.strip", FilmStrip.class);
        playerSneakIdle = directory.getEntry("rockosneakidle.strip", FilmStrip.class);

        goatWalk = directory.getEntry("goatwalk.strip", FilmStrip.class);
        goatIdle = directory.getEntry("goatsit.strip", FilmStrip.class);

        ferretWalk = directory.getEntry("ferretwalk.strip", FilmStrip.class);
        ferretIdle = directory.getEntry("ferretsit.strip", FilmStrip.class);

        catIdle = directory.getEntry("catsit.strip", FilmStrip.class);
        catWalk = directory.getEntry("catwalk.strip", FilmStrip.class);

        bearIdle = directory.getEntry("bearsit.strip", FilmStrip.class);
        bearWalk = directory.getEntry("bearwalk.strip", FilmStrip.class);

        otterIdle = directory.getEntry("ottersit.strip", FilmStrip.class);
        otterWalk = directory.getEntry("otterwalk.strip", FilmStrip.class);

        gooseWalk = directory.getEntry("goosewalk.strip", FilmStrip.class);
        gooseWalkBack = directory.getEntry("goosewalkback.strip", FilmStrip.class);
        gooseWalkUp = directory.getEntry("goosewalkup.strip", FilmStrip.class);
        gooseIdle = directory.getEntry("gooseidle.strip", FilmStrip.class);
        gooseChase = directory.getEntry("goosechase.strip", FilmStrip.class);

        //gooseCatch = new FilmStrip(new Texture("720/rockocaught.png"),1,4,4);

        gooseSleepIdle = directory.getEntry("goosesleepidle.strip", FilmStrip.class);
        gooseSleep = directory.getEntry("goosesleep.strip", FilmStrip.class);
        gooseWake = directory.getEntry("goosewake.strip", FilmStrip.class);
        gooseIdleUp = directory.getEntry("gooseidleup.strip", FilmStrip.class);
        gooseIdleDown = directory.getEntry("gooseidledown.strip", FilmStrip.class);

        ventIdle = directory.getEntry("vent.strip", FilmStrip.class);
        ventIn = directory.getEntry("rockoventin.strip", FilmStrip.class);
        ventOut = directory.getEntry("rockoventout.strip", FilmStrip.class);

        kickedOutReturn = directory.getEntry("rockokickedout.strip", FilmStrip.class);

        wokSizzle = directory.getEntry("wokcooking.strip", FilmStrip.class);
        wokIdle = directory.getEntry("wok.strip", FilmStrip.class);

        potSizzle = directory.getEntry("potcooking.strip", FilmStrip.class);
        potIdle = directory.getEntry("pot.strip", FilmStrip.class);

        getCaught = directory.getEntry("rockocaught.strip", FilmStrip.class);
    }

    //all instances of handleAnimation must be called in the update loop of a given WorldController.
    //view usages to see what I mean if you don't understand.

    /**
     * Handles the animation for the player
     *
     * @param o Player to animate
     * @param delta Deltatime to update the animation frame
     * */
    public void handleAnimation(Player o, float delta, boolean respawning){
        if(respawning){
            o.setFilmStrip(kickedOutReturn);
            o.setIgnoreInput(true);
            o.justDied = false;
            o.updateAnimation(delta);
            return;
        }
        if(o.getSwiping()){
            o.setFilmStrip(playerSwipe);
            o.updateAnimation(delta);
            if(o.getFrame() == o.getFilmStrip().getSize()-1){
                o.setSwiping(false);
            }
            return;
        }

        //This is pretty self-explanatory.
        if(o.playerIsCooking && o.potCookingIn == 2){
            o.setFilmStrip(playerChop);
            o.updateAnimation(delta);
            return;
        }
        if(o.playerIsCooking){
            o.setFilmStrip(playerCook);
            //if you are planning on returning, you must make sure you advance the animation beforehand
            o.updateAnimation(delta);
            //you will want to return if you want to "prioritize" an animation
            //for example this is cooking, if the player is cooking I don't want to check for any other animations
            return;
        }
        //you can understand the rest.
        if((o.dishInventory.leftFilled() && (o.getVX() !=0 || o.getVY()!=0)) || (o.dishInventory.rightFilled() && (o.getVX()!=0 || o.getVY()!=0))){
            o.setFilmStrip(playerServe);
            o.updateAnimation(delta);
            return;
        } else if((o.dishInventory.leftFilled() && o.getVX() ==0 && o.getVY()==0) || (o.dishInventory.rightFilled() && o.getVX()==0 && o.getVY()==0)){
            o.setFilmStrip(playerServeIdle);
            o.updateAnimation(delta);
            return;
        }

        if((input.getYMovement()!=0 || input.getXMovement()!=0) && o.current == 1){
            o.setFilmStrip(playerSneak);
        }
        else if(o.current == 1){
            o.setFilmStrip(playerSneakIdle);
        }
        else if(input.getYMovement()!=0 || input.getXMovement()!=0){
            o.setFilmStrip(playerWalk);
        }
        else{
            o.setFilmStrip(playerIdle);
        }
        o.updateAnimation(delta);
    }

    /**
     * Handles the animation for a guard
     * Functions similarly to every other handleAnimation method
     *
     * @param o The guard to animate
     * @param delta The deltatime to update animation frames with
     * */
    public void handleAnimation(Guard o, float delta, boolean inAction){
        if(inAction){
            o.setFilmStrip(getCaught);
            o.updateAnimation(delta);
            return;
        }
        if(o.getAIController().sleeping()){
            o.setFilmStrip(gooseSleep);
            o.updateAnimation(delta);
            return;
        }
        if(o.getAIController().waking()){
            o.setFilmStrip(gooseWake);
            o.updateAnimation(delta);
            return;
        }
        else if(o.getAIController().getCurrentState() == GuardAIController.AIState.SLEEP){
            o.setFilmStrip(gooseSleepIdle);
            o.updateAnimation(delta);
            return;
        }
        if(o.getAIController().getCurrentState() == GuardAIController.AIState.CHASE){
            o.setFilmStrip(gooseChase);
            o.updateAnimation(delta);
            return;
        }
        if(o.getAIController().getCurrentState() == GuardAIController.AIState.ROTATE){
            if(o.getAIController().getOrien() == GuardAIController.GuardOrientation.UP){
                o.setFilmStrip(gooseIdleUp);
                o.updateAnimation(delta);
                return;
            }
            else if(o.getAIController().getOrien() == GuardAIController.GuardOrientation.DOWN){
                o.setFilmStrip(gooseIdleDown);
                o.updateAnimation(delta);
                return;
            }
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
    /**
     * Sets the idle customer animation depending on the type of customer
     * Not sure of a better way to do this.
     * @param c Customer to set filmstrip for
     * */
    private void setCustomerIdleDependingOnType(Customer c){
        if(Objects.equals(c.getCustomerType(), "bear"))
            c.setFilmStrip(bearIdle);
        if(Objects.equals(c.getCustomerType(), "otter"))
            c.setFilmStrip(otterIdle);
        if(Objects.equals(c.getCustomerType(), "ferret"))
            c.setFilmStrip(ferretIdle);
        if(Objects.equals(c.getCustomerType(), "cat"))
            c.setFilmStrip(catIdle);
        if(Objects.equals(c.getCustomerType(), "goat"))
            c.setFilmStrip(goatIdle);
    }
    /**
     * Sets customer walk animation depending on customer type
     * @param c Customer to set walk filmstrip for
     * */
    private void setCustomerWalkDependingOnType(Customer c){
        if(Objects.equals(c.getCustomerType(), "bear"))
            c.setFilmStrip(bearWalk);
        if(Objects.equals(c.getCustomerType(), "otter"))
            c.setFilmStrip(otterWalk);
        if(Objects.equals(c.getCustomerType(), "ferret"))
            c.setFilmStrip(ferretWalk);
        if(Objects.equals(c.getCustomerType(), "cat"))
            c.setFilmStrip(catWalk);
        if(Objects.equals(c.getCustomerType(), "goat"))
            c.setFilmStrip(goatWalk);
    }
    /**
     * Sets the offset for a given customer type
     * @param type The type of customer. Can be "bear", "otter", "ferret", "cat", "goat"
     * @param c The customer to set offset for
     * @param offsetY The y offset to adjust by
     * @param offsetX The x offset to adjust by
     * */
    private void setOffsetForType(String type, Customer c, float offsetX, float offsetY){
        if(c.getCustomerType().equals(type)){
            c.setOffsetX(offsetX);
            c.setOffsetY(offsetY);
        }
    }

    /**
     * Resets the offsets (X and Y) for a given customer
     * @param c Customer to reset offsets for
     * */
    private void resetOffset(Customer c){
        c.setOffsetX(0);
        c.setOffsetY(0);
    }
    /**
    * Given an array of guards, this handles the animations for every one.
     * @param guards Array of guards to animate
     * @param delta The Deltatime to update filmstrip frames by
    * */
    public void processGuards(Array<Guard> guards, float delta, Guard inAction, boolean action){
        for(Guard g: guards){
            if(g == inAction){
//                System.out.println("GUARD");
                handleAnimation(g, delta, action);
            }
            else{
                handleAnimation(g, delta, false);
            }
        }
    }
    /**
     * Given an array of customers, this handles the animations for each one.
     * @param customers Array of customers to animate
     * @param delta The Deltatime to update filmstrip frames by
     * */
    public void processCustomers(Array<Customer> customers, float delta){
        for (Customer c: customers) {
            handleAnimation(c, delta);
        }
    }
    public void handleAnimation(CookingStationObject o,float delta){
        //wok = 0
        //pot = 1
        //chop = 2
        if(o.interacting && o.getSX() == o.getDefaultSX()){
            o.setSX(o.getSX()+0.1f);
            o.setSY(o.getSY()+0.1f);
        }
        else if(!o.interacting && o.getSX() > o.getDefaultSX()){
            o.resetScales();
        }

        if(o.getStationType() == 0 && o.timer != null){
            if(o.timer.getTime() <= 0){
                o.wok.setFilmStrip(wokIdle);
            }
            else{o.wok.setFilmStrip(wokSizzle);}
        }

        if(o.getStationType() == 1 && o.timer != null){
            if(o.timer.getTime() <= 0){
                o.pott.setFilmStrip(potIdle);
            }
            else{
                o.pott.setFilmStrip(potSizzle);
            }
        }

        o.pott.updateAnimation();
        o.wok.updateAnimation();
    }
    public void processCookingStations(Array<CookingStationObject> stations, float delta){
        for(CookingStationObject o: stations){
            handleAnimation(o, delta);
        }
    }
    /**
     * Handles the animation for a customer
     * @param o Customer to animate
     * @param delta Deltatime to update filmstrip frames by
     * */
    public void handleAnimation(Customer o, float delta){
        if(o.getVX() > 0 || o.getVX() < 0 || o.getVY() > 0 || o.getVY() < 0){
//            o.setFilmStrip(goatWalk);
            setCustomerWalkDependingOnType(o);
            resetOffset(o);
        }
        else{
            //sitting
//            o.setFilmStrip(goatIdle);
            setCustomerIdleDependingOnType(o);
            setOffsetForType("goat", o, 0, -10);
            setOffsetForType("cat", o, 0, 10);
            setOffsetForType("ferret", o, 10, 5);
            setOffsetForType("bear", o, 0, -8);
            setOffsetForType("otter", o, 10, 18);
        }

        o.updateAnimation(delta);
    }
    /**
     * Handles the vent animations.
     * @param o Vent to animate
     * @param p Player for handling vent animation state
     * @param delta Deltatime to update filmstrip frames with
     * */
    public void handleAnimation(VentObstacle o, Player p, float delta,boolean ventingOut){
        if(p.playerIsVenting){
            o.setFilmStrip(ventIn);
            o.setOX(85f);
            if(p.getX() < o.getX()) {
                o.setSX(1);
            }
            if(p.getX() > o.getX()){
                o.setSX(-1);
            }
            p.stopDrawing = true;
            o.updateAnimation(delta);
            return;
        }
        if(ventingOut){
//            System.out.println("Venting out");
            o.setFilmStrip(ventOut);
            o.setOX(170f);
            if(p.getX() < o.getX()) {
                o.setSX(1);
            }
            if(p.getX() > o.getX()){
                o.setSX(-1);
            }
            p.stopDrawing = true;
            o.updateAnimation(delta);
            return;
        }
        p.stopDrawing = false;
        o.setFilmStrip(ventIdle);
        o.setOX(27);
        o.updateAnimation(delta);
    }
}
