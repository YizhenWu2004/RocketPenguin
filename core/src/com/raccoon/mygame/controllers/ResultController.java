package com.raccoon.mygame.controllers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.assets.AssetDirectory;
import com.raccoon.mygame.models.ButtonAction;
import com.raccoon.mygame.models.ButtonHover;
import com.raccoon.mygame.models.ButtonUnhover;
import com.raccoon.mygame.models.UIButton;
import com.raccoon.mygame.view.GameCanvas;

public class ResultController extends WorldController {

    private Texture background;
    private GameCanvas canvas;
    private InputController input;
    private Array<UIButton> buttons = new Array<>();
    public boolean retry = false;
    public boolean next = false;
    public boolean select = false;

    public int happy;
    public int neutral;
    public int angry;
    public int total;
    public int score;

    public int[] star_req;
    private Texture retry_hover ;
    private Texture next_hover ;
    private Texture select_hover ;

    public Texture c_0 ;
    public Texture c_1 ;
    public Texture c_2 ;
    public Texture c_3 ;
    public Texture c_4 ;
    public Texture c_5 ;
    public Texture c_6 ;
    public Texture c_7 ;
    public Texture c_8 ;
    public Texture c_9 ;

    public Texture s_0 ;
    public Texture s_1 ;
    public Texture s_2 ;
    public Texture s_3 ;
    public Texture s_4 ;
    public Texture s_5 ;
    public Texture s_6 ;
    public Texture s_7 ;
    public Texture s_8;
    public Texture s_9 ;

    public Texture t_0 ;
    public Texture t_1 ;
    public Texture t_2 ;
    public Texture t_3 ;
    public Texture t_4 ;
    public Texture t_5;
    public Texture t_6 ;
    public Texture t_7 ;
    public Texture t_8 ;
    public Texture t_9 ;
    public Texture zero_star;
    public Texture one_star;
    public Texture two_stars;
    public Texture three_stars;
    public Texture retry_b;
    public Texture next_b;
    public Texture levelselect;


    public ResultController(GameCanvas canvas, Texture background, InputController input, AssetDirectory directory){
        this.background = background;
        this.canvas = canvas;
        this.input = input;

        retry_hover = directory.getEntry("r_retry_hover",Texture.class);
        next_hover = directory.getEntry("r_next_hover",Texture.class);
        select_hover = directory.getEntry("r_select_hover",Texture.class);
        retry_b = directory.getEntry("r_retry",Texture.class);
        next_b = directory.getEntry("r_next",Texture.class);
        levelselect = directory.getEntry("r_select",Texture.class);

        c_0 = directory.getEntry("rc_0",Texture.class);
        c_1 = directory.getEntry("rc_1",Texture.class);
        c_2 = directory.getEntry("rc_2",Texture.class);
         c_3 = directory.getEntry("rc_3",Texture.class);
         c_4 = directory.getEntry("rc_4",Texture.class);
        c_5 = directory.getEntry("rc_5",Texture.class);
         c_6 =directory.getEntry("rc_6",Texture.class);
        c_7 =directory.getEntry("rc_7",Texture.class);
         c_8 = directory.getEntry("rc_8",Texture.class);
        c_9 =directory.getEntry("rc_9",Texture.class);

         s_0 = directory.getEntry("rs_0",Texture.class);
         s_1 = directory.getEntry("rs_1",Texture.class);
         s_2 = directory.getEntry("rs_2",Texture.class);
         s_3 = directory.getEntry("rs_3",Texture.class);
         s_4 = directory.getEntry("rs_4",Texture.class);
         s_5 = directory.getEntry("rs_5",Texture.class);
        s_6 = directory.getEntry("rs_6",Texture.class);
        s_7 = directory.getEntry("rs_7",Texture.class);
         s_8 = directory.getEntry("rs_8",Texture.class);
        s_9 = directory.getEntry("rs_9",Texture.class);

        t_0 =directory.getEntry("rt_0",Texture.class);
         t_1 =directory.getEntry("rt_1",Texture.class);
         t_2 = directory.getEntry("rt_2",Texture.class);
        t_3 = directory.getEntry("rt_3",Texture.class);
         t_4 = directory.getEntry("rt_4",Texture.class);
         t_5 = directory.getEntry("rt_5",Texture.class);
         t_6 = directory.getEntry("rt_6",Texture.class);
         t_7 = directory.getEntry("rt_7",Texture.class);
        t_8 = directory.getEntry("rt_8",Texture.class);
         t_9 = directory.getEntry("rt_9",Texture.class);
         zero_star= directory.getEntry("r_zerostar",Texture.class);
        one_star= directory.getEntry("r_onestar",Texture.class);
         two_stars= directory.getEntry("r_twostars",Texture.class);
       three_stars= directory.getEntry("r_threestars",Texture.class);

        UIButton retry = new UIButton(retry_b,"retry",375,102,canvas);
        addButton(retry, ()-> {
            this.retry = true;
        }, ()->{
            retry.setSX(1.1f);
            retry.setSY(1.1f);
            retry.setTexture(retry_hover);
        }, retry::resetStyleProperties);

        UIButton next = new UIButton(next_b,"next", 545,120,canvas);
        addButton(next, ()-> {
            this.next = true;
        }, ()->{
            next.setSX(1.1f);
            next.setSY(1.1f);
            next.setTexture(next_hover);
        },next::resetStyleProperties);

        UIButton select = new UIButton(levelselect,"select",425,65,canvas);
        addButton(select, ()-> {
            this.select = true;
        },()->{
            select.setSX(1.1f);
            select.setSY(1.1f);
            select.setTexture(select_hover);
        },select::resetStyleProperties);

    }


    public void setStatus(int happy, int neutral, int angry, int total, int score, int[] star_req){
        this.happy=happy;
        this.neutral = neutral;
        this.angry=angry;
        this.total=total;
        this.score = score;
        this.star_req = star_req;
    }

    public void dispose() {
        canvas.dispose();
        canvas = null;
        super.dispose();
    }

    public void update() {
        checkButtons();
    }

    public void draw(){
        canvas.draw(background, Color.WHITE, 0, 0,
                0, 0, 0.0f, 1f, 1f);
        for(UIButton button : buttons){
            button.draw(canvas);
        }
        drawTotal(canvas);
        drawComplete(canvas);
        drawScore();

    }

    public Texture get(int index, int num){ //index = 0 = complete, 1=score, 2=total
        if (index == 0){
            if(num == 0){
                return c_0;
            } else if (num == 1){
                return c_1;
            }
            else if (num == 2){
                return c_2;
            }
            else if (num == 3){
                return c_3;
            }
            else if (num == 4){
                return c_4;
            }
            else if (num == 5){
                return c_5;
            }
            else if (num == 6){
                return c_6;
            }
            else if (num == 7){
                return c_7;
            }
            else if (num == 8){
                return c_8;
            }
            else if (num == 9){
                return c_9;
            }
        } else if (index == 1){
            if(num == 0){
                return s_0;
            } else if (num == 1){
                return s_1;
            }
            else if (num == 2){
                return s_2;
            }
            else if (num == 3){
                return s_3;
            }
            else if (num == 4){
                return s_4;
            }
            else if (num == 5){
                return s_5;
            }
            else if (num == 6){
                return s_6;
            }
            else if (num == 7){
                return s_7;
            }
            else if (num == 8){
                return s_8;
            }
            else if (num == 9){
                return s_9;
            }
        } else if (index == 2){
            if(num == 0){
                return t_0;
            } else if (num == 1){
                return t_1;
            }
            else if (num == 2){
                return t_2;
            }
            else if (num == 3){
                return t_3;
            }
            else if (num == 4){
                return t_4;
            }
            else if (num == 5){
                return t_5;
            }
            else if (num == 6){
                return t_6;
            }
            else if (num == 7){
                return t_7;
            }
            else if (num == 8){
                return t_8;
            }
            else if (num == 9){
                return t_9;
            }
        }
        return c_0;
    }

    public void drawAt(int one, int ten, int hundred, int x, int y, int total){
        if(total < 10){
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x-20, y+5, 0.0f, 1f, 1f);
        } else if (total < 100){
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x-10, y+5, 0.0f, 1f, 1f);
            canvas.draw(get(1,ten), Color.WHITE, 0, 0,
                    x-30, y+3, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(1,one), Color.WHITE, 0, 0,
                    x, y+5, 0.0f, 1f, 1f);
            canvas.draw(get(1,ten), Color.WHITE, 0, 0,
                    x-20, y+3, 0.0f, 1f, 1f);
            canvas.draw(get(1,hundred), Color.WHITE, 0, 0,
                    x-40, y, 0.0f, 1f, 1f);
        }
    }

    public void drawScore(){
        //star_req[0] = 50;
        int one_one = star_req[0]%10;
        int one_ten = (star_req[0]/10)%10;
        int one_hundred = star_req[0]/100;
        int two_one = star_req[1]%10;
        int two_ten = (star_req[1]/10)%10;
        int two_hundred = star_req[1]/100;
        int three_one = star_req[2]%10;
        int three_ten = (star_req[2]/10)%10;
        int three_hundred = star_req[2]/100;


        drawAt(one_one, one_ten, one_hundred,370,310,star_req[0]);
        drawAt(two_one, two_ten, two_hundred,475,325,star_req[1]);
        drawAt(three_one, three_ten, three_hundred,580,340,star_req[2]);







        if(score >= star_req[2]){
            canvas.draw(three_stars, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        } else if (score >= star_req[1]){
            canvas.draw(two_stars, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);

        } else if (score >= star_req[0]){
            canvas.draw(one_star, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        }else{
            canvas.draw(zero_star, Color.WHITE, 0, 0,
                    325, 225, 0.0f, 1f, 1f);
        }
    }

    public void drawComplete(GameCanvas canvas){
//        int happy = 1;
//        int neutral = 34;
//        int angry = 56;
//        int total = 78;
//        int score = 123;


        int happy_one = happy%10;
        int happy_ten = (happy/10)%10;
        int neu_one = neutral%10;
        int neu_ten = (neutral/10)%10;
        int angry_one = angry%10;
        int angry_ten = (angry/10)%10;
        int total_one = total%10;
        int total_ten = (total/10)%10;

        if(total < 10){
            canvas.draw(get(0,total_one), Color.WHITE, 0, 0,
                    502, 545, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,total_one), Color.WHITE, 0, 0,
                    517, 545, 0.0f, 1f, 1f);
            canvas.draw(get(0,total_ten), Color.WHITE, 0, 0,
                    502, 543, 0.0f, 1f, 1f);
        }

        if(happy < 10){
            canvas.draw(get(0,happy_one), Color.WHITE, 0, 0,
                    350, 483, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,happy_one), Color.WHITE, 0, 0,
                    365, 483, 0.0f, 1f, 1f);
            canvas.draw(get(0,happy_ten), Color.WHITE, 0, 0,
                    350, 480, 0.0f, 1f, 1f);
        }

        if(neutral < 10){
            canvas.draw(get(0,neu_one), Color.WHITE, 0, 0,
                    475, 497, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,neu_one), Color.WHITE, 0, 0,
                    490, 497, 0.0f, 1f, 1f);
            canvas.draw(get(0,neu_ten), Color.WHITE, 0, 0,
                    475, 495, 0.0f, 1f, 1f);
        }

        if(angry < 10){
            canvas.draw(get(0,angry_one), Color.WHITE, 0, 0,
                    580, 512, 0.0f, 1f, 1f);
        } else{
            canvas.draw(get(0,angry_one), Color.WHITE, 0, 0,
                    595, 512, 0.0f, 1f, 1f);
            canvas.draw(get(0,angry_ten), Color.WHITE, 0, 0,
                    580, 510, 0.0f, 1f, 1f);
        }

        float pos1 = 560;
        float pos2 = 575;
        float pos3 = 590;
        int one = score %10;
        int ten = (score/10)%10;
        int hundred =(score/100);
        if(score < 10){
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
        } else if (score < 100){
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
            canvas.draw(get(0,ten), Color.WHITE, 0, 0,
                    pos2, 555, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(0,one), Color.WHITE, 0, 0,
                    pos3, 557, 0.0f, 1f, 1f);
            canvas.draw(get(0,ten), Color.WHITE, 0, 0,
                    pos2, 555, 0.0f, 1f, 1f);
            canvas.draw(get(0,hundred), Color.WHITE, 0, 0,
                    pos1, 552, 0.0f, 1f, 1f);
        }

    }
    public void drawTotal(GameCanvas canvas){
        //score = 123;
        float pos1 = 560;
        float pos2 = 580;
        float pos3 = 600;
        int one = score %10;
        int ten = (score/10)%10;
        int hundred =(score/100);
        if(score < 10){
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
        } else if (score < 100){
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
            canvas.draw(get(2,ten), Color.WHITE, 0, 0,
                    pos2, 445, 0.0f, 1f, 1f);
        } else {
            canvas.draw(get(2,one), Color.WHITE, 0, 0,
                    pos3, 447, 0.0f, 1f, 1f);
            canvas.draw(get(2,ten), Color.WHITE, 0, 0,
                    pos2, 445, 0.0f, 1f, 1f);
            canvas.draw(get(2,hundred), Color.WHITE, 0, 0,
                    pos1, 442, 0.0f, 1f, 1f);
        }
    }

    private void addButton(UIButton button, ButtonAction action, ButtonHover hover, ButtonUnhover unhover){
        buttons.add(button);
        button.setOnClickAction(action);
        button.setOnHoverAction(hover);
        button.setOnUnhoverAction(unhover);
    }

    private boolean processBounds(float x, float y, float minX, float maxX, float minY, float maxY){
        return (x >= minX && x <= maxX && y >= minY && y <= maxY);
    }
    private void checkButtons() {
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
    public boolean getStarReqNull(){
        if(this.star_req == null){
            return true;
        }
        return false;
    }
    public void setStarReq(int[] reqs){
        this.star_req = reqs;
    }
}
