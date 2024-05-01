package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.ButtonHover;
import com.raccoon.mygame.models.ButtonUnhover;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class MenuController extends WorldController{
    private Texture pause_background;
    private SoundController sounds;
    private Texture resume_hover = new Texture("pause/resume_hover.png");
    private Texture option_hover = new Texture("pause/option_hover.png");
    private Texture restart_hover = new Texture("pause/restart_hover.png");
    private Texture quit_hover = new Texture("pause/quit_hover.png");
    private Texture cont_button_hover = new Texture("options_pause/control_hover_button.png");
    private Texture aud_button_hover = new Texture("options_pause/audio_hover_button.png");
    private Texture back_button_hover = new Texture("options_pause/back_hover_button.png");
    private Texture opt_background = new Texture("options_pause/options.png");
    private Texture cont_background = new Texture("options_pause/control_page.png");
    private Texture aud_background = new Texture("options_pause/audio.png");
    private GameCanvas canvas;
    private InputController input;
    //below r buttons for main pause
    private Array<UIButton> buttons = new Array<>();
    public boolean resume = false;
    public boolean restart = false;
    public boolean options = false;
    public boolean quit = false;

    //below r buttons for options
    public boolean control = false;
    public boolean audio = false;
    public boolean opt_back = false;
    private Array<UIButton> opt_buttons = new Array<>();
    //button for control
    private Array<UIButton> cont_buttons = new Array<>();
    public boolean cont_back = false;

    //button for audio page
    private Array<UIButton> aud_buttons = new Array<>();
    public boolean aud_back = false;
    public boolean on_pause = false;
    public boolean on_options = false;
    public boolean on_control = false;
    public boolean on_audio = false;
    public MenuController(GameCanvas canvas, Texture background, InputController input){
        this.pause_background = background;
        this.canvas = canvas;
        this.input = input;

        sounds = new SoundController();
        UIButton resume = new UIButton(new Texture("pause/resume_pause.png"),"resume",485,475,canvas);
        addButton(resume, ()-> {
            sounds.clickPlay();
            this.resume = true;
        }, ()->{
            resume.setSX(0.8f);
            resume.setSY(0.8f);
            resume.setTexture(resume_hover);
        }, resume::resetStyleProperties, buttons);
        resume.setDefaultScale(0.7f,0.7f);

        UIButton options = new UIButton(new Texture("pause/option_pause.png"),"options", 485,375,canvas);
        addButton(options, ()-> {
            sounds.clickPlay();
            this.options = true;
            this.on_options = true;
            this.on_pause = false;
        }, ()->{
            options.setSX(0.8f);
            options.setSY(0.8f);
            options.setTexture(option_hover);
        },options::resetStyleProperties,buttons);
        options.setDefaultScale(0.7f,0.7f);

        UIButton quit = new UIButton(new Texture("pause/quit_pause.png"),"quit",485,275,canvas);
        addButton(quit, ()-> {
            sounds.clickPlay();
            this.quit = true;
        },()->{
            quit.setSX(0.8f);
            quit.setSY(0.8f);
            quit.setTexture(quit_hover);
        },quit::resetStyleProperties,buttons);
        quit.setDefaultScale(0.7f,0.7f);

        UIButton restart = new UIButton(new Texture("pause/restart_pause.png"),"restart",485,175,canvas);
        addButton(restart, ()-> {
            this.restart = true;
            sounds.clickPlay();
        },()->{
            restart.setSX(0.8f);
            restart.setSY(0.8f);
            restart.setTexture(restart_hover);
        },restart::resetStyleProperties,buttons);
        restart.setDefaultScale(0.7f,0.7f);

        UIButton cont = new UIButton(new Texture("options_pause/control_button.png"),"restart",445,445,canvas);
        addButton(cont, ()-> {
            sounds.clickPlay();
            this.control = true;
            this.on_options = false;
            this.on_control = true;

        },()->{
            cont.setSX(1.1f);
            cont.setSY(1.1f);
            cont.setTexture(cont_button_hover);
        },cont::resetStyleProperties,opt_buttons);

        UIButton aud = new UIButton(new Texture("options_pause/audio_button.png"),"restart",490,345,canvas);
        addButton(aud, ()-> {
            sounds.clickPlay();
            this.audio = true;
            this.on_audio = true;
            this.on_options=false;
        },()->{
            aud.setSX(1.1f);
            aud.setSY(1.1f);
            aud.setTexture(aud_button_hover);
        },aud::resetStyleProperties,opt_buttons);

        UIButton back_ = new UIButton(new Texture("options_pause/back_button.png"),"restart",500,245,canvas);
        addButton(back_, ()-> {
            sounds.clickPlay();
            this.opt_back = true;
            this.on_options = false;
            this.on_pause = true;
        },()->{
            back_.setSX(1.1f);
            back_.setSY(1.1f);
            back_.setTexture(back_button_hover);
        },back_::resetStyleProperties,opt_buttons);

        UIButton back2 = new UIButton(new Texture("options_pause/back_button.png"),"restart",520,125,canvas);
        addButton(back2, ()-> {
            sounds.clickPlay();
            this.cont_back = true;
            this.on_options = true;
            this.on_control = false;
        },()->{
            back2.setSX(1.1f);
            back2.setSY(1.1f);
            back2.setTexture(back_button_hover);
        },back2::resetStyleProperties,cont_buttons);

        UIButton back3 = new UIButton(new Texture("options_pause/back_button.png"),"restart",520,200,canvas);
        addButton(back3, ()-> {
            sounds.clickPlay();
            this.aud_back = true;
            this.on_options = true;
            this.on_audio = false;
        },()->{
            back3.setSX(1.1f);
            back3.setSY(1.1f);
            back3.setTexture(back_button_hover);
        },back3::resetStyleProperties,aud_buttons);

    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void resumed(){
        resume = false;
    }

    public void update() {
        if(on_pause){
            checkButtons(buttons);
        } else if (on_options){
            checkButtons(opt_buttons);
        } else if (on_control){
            checkButtons(cont_buttons);
        } else if (on_audio){
            checkButtons(aud_buttons);
        }
    }

    public void draw(){
        if(on_pause){
            canvas.draw(pause_background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 0.7f, 0.7f);
            for(UIButton button : buttons){
                button.draw(canvas);
            }
        }else if (on_options){
            canvas.draw(opt_background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 1f, 1f);
            for(UIButton button : opt_buttons) {
                button.draw(canvas);
            }
        } else if (on_control){
            canvas.draw(cont_background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 1f, 1f);
            for(UIButton button : cont_buttons) {
                button.draw(canvas);
            }
        } else if (on_audio){
            canvas.draw(aud_background, Color.WHITE, 0, 0,
                    0, 0, 0.0f, 1f, 1f);
            for(UIButton button : aud_buttons) {
                button.draw(canvas);
            }
        }

    }

    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover, Array<UIButton> b){
        b.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkButtons(Array<UIButton> buttons) {
        for (UIButton button : buttons) {
            float minX = button.getX();
            float maxX = button.getX() + button.getWidth();
            float minY = button.getY();
            float maxY = button.getY() + button.getHeight();
            if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)) {
                button.setHovered(true);
                button.onHoverEvent();
            } else {
                button.setHovered(false);
                button.onUnhoverEvent();
            }
            //if input is within bounds of button
            if (processBounds(input.getMouseX(), input.getMouseY(), minX, maxX, minY, maxY)
                    && input.click) {
                button.setIsClicked(true);
                button.onClickEvent();
                button.setIsClicked(false);
            }
        }
    }

    public void debug() {

    }
}
