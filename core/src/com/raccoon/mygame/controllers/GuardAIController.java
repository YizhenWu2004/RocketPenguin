package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;

public class GuardAIController {
    private float leftBoundary;
    private float rightBoundary;
    private float speed;
    private boolean movingRight = true;

    public GuardAIController(float leftBoundary, float rightBoundary, float speed) {
        this.leftBoundary = leftBoundary;
        this.rightBoundary = rightBoundary;
        this.speed = speed;
    }

    public Vector2 updatePosition(Vector2 guardPosition, float deltaTime) {
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
        return guardPosition;
    }
    public float getSpeed(Vector2 guardPosition, float deltaTime) {
        if (movingRight) {
            guardPosition.x += speed * deltaTime;
            if (guardPosition.x >= rightBoundary) {
                movingRight = false;
                return 0;
            }
            return speed;
        } else {
            guardPosition.x -= speed * deltaTime;
            if (guardPosition.x <= leftBoundary) {
                movingRight = true;
                return 0;
            }
            return -speed;
        }
    }

}
