package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GuardAIController {
    private enum AIState {
        WANDER,
        CHASE
    }
    private float leftBoundary;
    private float rightBoundary;
    private float speed;
    private boolean movingRight = true;

    private AIState currentState = AIState.WANDER;

    public GuardAIController(float leftBoundary, float rightBoundary, float speed) {
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.speed = speed;
    }

    public void setAIStateChase(){
        currentState =  AIState.CHASE;
    }

    public Vector2 updatePosition(Vector2 guardPosition, float deltaTime) {
        if(currentState == AIState.WANDER){
            if (movingRight) {
                guardPosition.x += speed * deltaTime;
                if (guardPosition.x >= rightBoundary) {
                    guardPosition.x = rightBoundary;
                    movingRight = false;
                }
            } else {
                guardPosition.x -= speed * deltaTime;
                if (guardPosition.x <= leftBoundary) {
                    guardPosition.x = leftBoundary;
                    movingRight = true;
                }
            }
        }
        else if (currentState == AIState.CHASE){

        }

        return guardPosition;
    }
    public Vector2 getSpeed(Vector2 guardPosition, float deltaTime, Array<Float> info) {
        Vector2 speedVector = new Vector2(0f, 0f);

        if (currentState == AIState.WANDER) {
            float xSpeed = 0f;
            if (movingRight) {
                guardPosition.x += speed * deltaTime;
                if (guardPosition.x >= rightBoundary) {
                    guardPosition.x = rightBoundary;
                    movingRight = false;
                    xSpeed = 0f;
                } else {
                    xSpeed = speed;
                }
            } else {
                guardPosition.x -= speed * deltaTime;
                if (guardPosition.x <= leftBoundary) {
                    guardPosition.x = leftBoundary;
                    movingRight = true;
                    xSpeed = 0f;
                } else {
                    xSpeed = -speed;
                }
            }
            speedVector.set(xSpeed, 0f);
        } else if (currentState == AIState.CHASE) {
            float playerX = info.get(0);
            float playerY = info.get(1);

            Vector2 direction = new Vector2(playerX - guardPosition.x, playerY - guardPosition.y).nor();
            Vector2 chaseSpeed = direction.scl(speed);

            guardPosition.add(chaseSpeed.x * deltaTime, chaseSpeed.y * deltaTime);
            speedVector.set(chaseSpeed.x, chaseSpeed.y);
        }

        return speedVector;
    }

}
