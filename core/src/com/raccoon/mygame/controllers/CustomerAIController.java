package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.models.Customer;
import com.raccoon.mygame.objects.TableObstacle;

public class CustomerAIController implements AIController {
    private static enum FSMState {
        SPAWN,
        SEAT,
        WAIT,
        LEAVE
    }

    private FSMState state;

    private int tick = 0;
    private int move;
    private Array<TableObstacle> tables;
    private Vector2 goal;
    Vector2 temp = new Vector2();
    private Customer customer;

    public CustomerAIController(Array<TableObstacle> tables, Customer customer) {
        this.tables = tables;
        this.customer = customer;
        state = FSMState.SPAWN;
        move = CONTROL_NO_ACTION;
        goal = null;
    }

    @Override
    public int getAction() {
        tick -= 1;
        //(tick);
        if (customer.getX() < 0) {
            customer.deactivate();
        }
        changeState();
        setGoal();
        //System.out.println(customer.getPosition()+" " + goal + " " + state);
        switch (state) {
            case SPAWN:
            case WAIT:
                customer.setLinearVelocity(new Vector2());
                break;
            case SEAT:
                if (goal.x > customer.getX()) {
                    customer.setLinearVelocity(new Vector2(4, 0));
                } else {
                    float y = goal.y < customer.getY() ? -4 : 4;
                    customer.setLinearVelocity(new Vector2(0, y));
                }
                break;
            case LEAVE:
                //if(tick <= 0) {
                if (Math.abs(goal.y - customer.getY()) > 0.05) {
                    float y = goal.y < customer.getY() ? -4 : 4;
                    customer.setLinearVelocity(new Vector2(0, y));
                } else {
                    customer.setLinearVelocity(new Vector2(-4, 0));
                    if(customer.getX() <= -20) {
                        customer.setActive(false);
                    }
                }
                break;
            // }
        }
        return 0;
    }

    public void changeState() {
        switch (state) {
            case SPAWN:
                if (goal != null) {
                    state = FSMState.SEAT;
                }
                break;
            case SEAT:
//
                if (goal.dst(customer.getPosition()) <= 0.5) {
                    customer.setPosition(goal);
                    state = FSMState.WAIT;
                    customer.setBodyType(BodyType.StaticBody);
                }
                break;
            case WAIT:
                if (customer.onRight) {
                    customer.flipScale = 1;
                }
                if (customer.isSatisfied()) {
                    state = FSMState.LEAVE;
                    customer.setBodyType(BodyType.DynamicBody);
                }

                break;
            case LEAVE:
                customer.flipScale = 1;
                break;
        }
    }

    public void setGoal() {
        switch (state) {
            case SPAWN:
                for (TableObstacle t : tables) {
                    if (!t.isOccupied(1)) {
                        goal = t.occupy(1);
                        customer.onRight = true;
                        break;
                    } else if (!t.isOccupied(0)) {
                        goal = t.occupy(0);
                        break;
                    }
                }
                break;
            case LEAVE:
                goal = new Vector2(-0.5f, 8.5f);
        }
    }

    public void timeOut(){
        state = FSMState.LEAVE;
        customer.setBodyType(BodyType.DynamicBody);
    }
}
