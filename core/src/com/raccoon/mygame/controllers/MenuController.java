package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.ButtonHover;
import com.raccoon.mygame.models.ButtonUnhover;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class MenuController extends WorldController{
    private Texture pause_background;
    private SoundController sounds;
    private Texture resume_hover ;
    private Texture option_hover ;
    private Texture restart_hover;
    private Texture quit_hover;
    private Texture cont_button_hover ;
    private Texture aud_button_hover ;
    private Texture back_button_hover;
    private Texture opt_background;
    private Texture cont_background ;
    private Texture aud_background;
    private Texture add;
    private Texture add_hover;
    private Texture minus;
    private Texture minus_hover;
    private Texture bar;
    private Texture barfill;
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

    private Texture resume_pause;
    private Texture option_pause;
    private Texture quit_pause;
    private Texture restart_pause;
    private Texture control_opt;
    private Texture audio_opt;
    private Texture back;

    private int musicbar;
    private int sfxbar;

    public MenuController(GameCanvas canvas, Texture background, InputController input, SoundController s, AssetDirectory directory){

        resume_hover = directory.getEntry("p_resume_hover",Texture.class);
        option_hover = directory.getEntry("p_option_hover",Texture.class);
        restart_hover = directory.getEntry("p_restart_hover",Texture.class);
        quit_hover =directory.getEntry("p_quit_hover",Texture.class);
        cont_button_hover = directory.getEntry("op_control_hover_b",Texture.class);
        aud_button_hover = directory.getEntry("op_audio_hover_b",Texture.class);
        back_button_hover = directory.getEntry("op_back_hover_b",Texture.class);
        opt_background = directory.getEntry("op_options",Texture.class);
        cont_background = directory.getEntry("op_control_page",Texture.class);
        aud_background = directory.getEntry("op_audio",Texture.class);
        resume_pause = directory.getEntry("p_resume_pause",Texture.class);
        option_pause = directory.getEntry("p_option_pause",Texture.class);
        quit_pause = directory.getEntry("p_quit_pause",Texture.class);
        restart_pause = directory.getEntry("p_restart_pause",Texture.class);
        control_opt =  directory.getEntry("op_control_b",Texture.class);
        audio_opt =  directory.getEntry("op_audio_b",Texture.class);
        back = directory.getEntry("op_back_b",Texture.class);
        add = directory.getEntry("add2",Texture.class);
        add_hover = directory.getEntry("addhover2",Texture.class);
        minus = directory.getEntry("minus2",Texture.class);
        minus_hover = directory.getEntry("minushover2",Texture.class);
        barfill = directory.getEntry("fill2",Texture.class);
        this.pause_background = background;
        this.canvas = canvas;
        this.input = input;
        //System.out.println(s.getmusic());
        //System.out.println(s.getsfx());
//        musicbar = (int)(s.getmusic() * 5);
//        sfxbar = (int)(s.getsfx() * 5);

        sounds = s;
        UIButton resume = new UIButton(resume_pause,"resume",485,475,canvas);
        addButton(resume, ()-> {
            sounds.clickPlay();
            this.resume = true;
        }, ()->{
            resume.setSX(0.8f);
            resume.setSY(0.8f);
            resume.setTexture(resume_hover);
        }, resume::resetStyleProperties, buttons);
        resume.setDefaultScale(0.7f,0.7f);

        UIButton options = new UIButton(option_pause,"options", 485,375,canvas);
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

        UIButton quit = new UIButton(quit_pause,"quit",485,275,canvas);
        addButton(quit, ()-> {
            sounds.clickPlay();
            this.quit = true;
        },()->{
            quit.setSX(0.8f);
            quit.setSY(0.8f);
            quit.setTexture(quit_hover);
        },quit::resetStyleProperties,buttons);
        quit.setDefaultScale(0.7f,0.7f);

        UIButton restart = new UIButton(restart_pause,"restart",485,175,canvas);
        addButton(restart, ()-> {
            this.restart = true;
            sounds.clickPlay();
        },()->{
            restart.setSX(0.8f);
            restart.setSY(0.8f);
            restart.setTexture(restart_hover);
        },restart::resetStyleProperties,buttons);
        restart.setDefaultScale(0.7f,0.7f);

        UIButton cont = new UIButton(control_opt,"restart",445,445,canvas);
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

        UIButton aud = new UIButton(audio_opt,"restart",490,345,canvas);
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

        UIButton incmusic = new UIButton(add,"restart",730,400,canvas);
        addButton(incmusic, ()-> {
            sounds.clickPlay();
            sounds.incmusic();
            musicbar = Integer.min(8, musicbar + 1);
            this.audio = true;
            this.on_audio = true;
            this.on_options=false;
        },()->{
            incmusic.setTexture(add_hover);
        },incmusic::resetStyleProperties,aud_buttons);

        UIButton decmusic = new UIButton(minus,"restart",400,400,canvas);
        addButton(decmusic, ()-> {
            sounds.clickPlay();
            sounds.decmusic();
            musicbar = Integer.max(0, musicbar - 1);
            this.audio = true;
            this.on_audio = true;
            this.on_options=false;
        },()->{
            decmusic.setTexture(minus_hover);
        },decmusic::resetStyleProperties,aud_buttons);

        UIButton incsfx = new UIButton(add,"restart",730,260,canvas);
        addButton(incsfx, ()-> {
            sounds.clickPlay();
            sounds.incsfx();
            sfxbar = Integer.min(8, sfxbar + 1);
            this.audio = true;
            this.on_audio = true;
            this.on_options=false;
        },()->{
//            incsfx.setSX(1.1f);
//            incsfx.setSY(1.1f);
            incsfx.setTexture(add_hover);
        },incsfx::resetStyleProperties,aud_buttons);

        UIButton decsfx = new UIButton(minus,"restart",410,260,canvas);
        addButton(decsfx, ()-> {
            sounds.clickPlay();
            sounds.decsfx();
            sfxbar = Integer.max(0, sfxbar - 1);
            this.audio = true;
            this.on_audio = true;
            this.on_options=false;
        },()->{
            decsfx.setTexture(minus_hover);
        },decsfx::resetStyleProperties,aud_buttons);

        UIButton back_ = new UIButton(back,"restart",500,245,canvas);
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

        UIButton back2 = new UIButton(back,"restart",530,135,canvas);
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

        UIButton back3 = new UIButton(back,"restart",535,150,canvas);
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
        musicbar = (int)(sounds.getmusic() * 5);
        sfxbar = (int)(sounds.getsfx() * 5);
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
//            System.out.println("draw options");
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
//            System.out.println("on audio");
            canvas.draw(aud_background, Color.WHITE, 0, 0,
                    -10, -10, 0.0f, 0.7f, 0.7f);
            for(UIButton button : aud_buttons) {
                button.draw(canvas);
            }
            for(int i = 0; i < musicbar; i++){
                canvas.draw(barfill, Color.WHITE, 0, 0, 475 + i * 30, 400, 0.0f, 0.9f,0.9f);
            }
            for(int i = 0; i < sfxbar; i++){
                canvas.draw(barfill, Color.WHITE, 0, 0, 482 + i * 30, 260, 0.0f, 0.9f,0.9f);
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
