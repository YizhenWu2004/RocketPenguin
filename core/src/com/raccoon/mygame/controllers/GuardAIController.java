package com.raccoon.mygame.controllers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.raccoon.mygame.enums.enums.PatrolDirection;
import com.raccoon.mygame.util.Node;
import com.raccoon.mygame.view.GameCanvas;

import java.util.PriorityQueue;

public class GuardAIController {
    public enum AIState {
        WANDER,
        CHASE
    }

    private float upperBoundary;
    private float lowerBoundary;
    private float speed;

    private float chaseSpeed;
    private boolean movingRight = true;

    private boolean movingup = true;
    private boolean movingPositive = true;

    private PatrolDirection patrolDirection;

    boolean[][] collisionLayer;

    int counter;
    int counter2;
    private AIState currentState = AIState.WANDER;

    private Array<Vector2> currentPath;
    private int currentPathIndex;

    Vector2 guardDimension;

    private int chaseCounter;

    /**
     * constant that determines after how many updates, findPath is called in
     * updateChaseMode
     */
    int CHASE_COUNTER_CONSTANT = 25;

    public GuardAIController(float x, float y, float worldWidth,
                             float worldHeight, float patrolRange,
                             float speed, PatrolDirection patrolDirection,
                             boolean[][] collisionLayer, Vector2 guardDimension) {
        this.speed = speed;
        this.patrolDirection = patrolDirection;
        this.collisionLayer = collisionLayer;
        if (patrolDirection == PatrolDirection.LEFT_RIGHT) {
            this.lowerBoundary = Math.max(x - patrolRange, 0);
            this.upperBoundary = Math.min(x + patrolRange, worldWidth);
        } else {
            this.lowerBoundary = Math.max(y - patrolRange, 0);
            this.upperBoundary = Math.min(y + patrolRange, worldHeight);
        }

        this.currentPath = new Array<>();
        this.currentPathIndex = 0;

        this.guardDimension = guardDimension;
        this.counter = 0;
        this.chaseSpeed = speed*3;
        this.chaseCounter = 0;
        this.counter2 = 0;
    }

    public void setAIStateChase() {
        currentState = AIState.CHASE;
    }

    public void setAIStateWander() {
        currentState = AIState.WANDER;
    }

    public void reverseDirection() {
        if(patrolDirection == PatrolDirection.LEFT_RIGHT){
            if(counter == 1){
            movingRight = false;
            counter = 0;
            }  else {movingRight = true;
            counter++;}
        }
        movingPositive = !movingPositive;
        if(patrolDirection == PatrolDirection.UP_DOWN){
            if(counter2 == 1){
                movingup = false;
                counter2 = 0;
            } else {
                movingup = true;
                 counter2++;
            }
        }
    }

    public Vector2 getSpeed(Vector2 guardPosition, float deltaTime, Array<Float> info) {
       // System.out.println(currentState);
        Vector2 speedVector = new Vector2(0f, 0f);

        if (currentState == AIState.WANDER) {
            switch (patrolDirection) {
                case LEFT_RIGHT:
                    if (movingPositive) {
                        if (guardPosition.x + speed * deltaTime >= upperBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.x = speed;
                        }
                    } else {
                        if (guardPosition.x - speed * deltaTime <= lowerBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.x = -speed;
                        }
                    }
                    break;
                case UP_DOWN:
                    if (movingPositive) {
                        if (guardPosition.y + speed * deltaTime >= upperBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.y = speed;
                        }
                    } else {
                        if (guardPosition.y - speed * deltaTime <= lowerBoundary) {
                            reverseDirection();
                        } else {
                            speedVector.y = -speed;
                        }
                    }
                    break;
            }
        } else if (currentState == AIState.CHASE) {
            chaseCounter++;
            Vector2 playerPosition = new Vector2(info.get(2), info.get(3));
            Vector2 chaseSpeed = updateChaseMode(guardPosition, info ,chaseCounter);
            if(chaseCounter >= CHASE_COUNTER_CONSTANT){
                //System.out.println("CHASE COUNTER");
                chaseCounter = 0;
            }
            if (chaseSpeed != null) {
                return chaseSpeed;
            }
        }

        return speedVector;
    }

    public Array<Vector2> findPathMain(Vector2 start, Array<Float> info){
        Vector2 lower = new Vector2(info.get(0),info.get(1));
        Vector2 mid = new Vector2(info.get(2),info.get(3));
        Vector2 upper = new Vector2(info.get(4),info.get(5));

        Array<Vector2> pathLower = findPath(start, lower);
        if(pathLower.size != 0){
            return pathLower;
        }

        Array<Vector2> pathMid = findPath(start, mid);
        if(pathMid.size != 0){
            return pathMid;
        }

        return findPath(start,upper);
    }

    /**
     * runs dijskra's and finds the shortest path from start to goal
     * @param start guard's position
     * @param goal the goal tile
     * @return array of coordinates that the guard should take to reach to goal
     */
    public Array<Vector2> findPath(Vector2 start, Vector2 goal) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(new Node(start, null, 0));

        Vector2 goalFloor = new Vector2((float) Math.floor(goal.x), (float) Math.floor(goal.y));
        boolean[][] visited = new boolean[collisionLayer.length][collisionLayer[0].length];
        Node[][] cameFrom = new Node[collisionLayer.length][collisionLayer[0].length];

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();

            Vector2 currentFloor = new Vector2((float) Math.floor(current.position.x), (float) Math.floor(current.position.y));
            if (currentFloor.equals(goalFloor)) {
                return reconstructPath(cameFrom, current);
            }

            for (Vector2 next : getNeighbors(current.position, guardDimension.x, guardDimension.y)) {
                if (!visited[(int)next.x][(int)next.y]) {
                    visited[(int)next.x][(int)next.y] = true;
                    float newCost = current.cost + 1;
                    frontier.add(new Node(next, current, newCost));
                    cameFrom[(int)next.x][(int)next.y] = current;
                }
            }
        }

        return new Array<>();
    }

    private Array<Vector2> reconstructPath(Node[][] cameFrom, Node current) {
        Array<Vector2> path = new Array<>();
        while (current != null) {
            path.add(current.position);
            current = current.parent;
        }
        path.reverse();
        return path;
    }


    private Array<Vector2> getNeighbors(Vector2 position, float width, float height) {
//        System.out.println("Width" + width);
//        System.out.println("Height" + height);
        Array<Vector2> neighbors = new Array<>();
        int[][] directions = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        int guardWidthCells = 2;
        int guardHeightCells = 2;

        for (int[] direction : directions) {
            int nx = (int)position.x + direction[0];
            int ny = (int)position.y + direction[1];

            if (canFitInPosition(nx, ny, guardWidthCells, guardHeightCells)) {
                neighbors.add(new Vector2(nx, ny));
            }
        }
        return neighbors;
    }

    /**
     *
     *
     * @param x
     * @param y
     * @param widthCells
     * @param heightCells
     * @return if guard can fit into a position
     */
    private boolean canFitInPosition(int x, int y, int widthCells, int heightCells) {
        for (int offsetX = 0; offsetX < widthCells; offsetX++) {
            for (int offsetY = 0; offsetY < heightCells; offsetY++) {
                int checkX = x + offsetX - widthCells / 2;
                int checkY = y + offsetY - heightCells / 2;
                if (checkX < 0 || checkX >= collisionLayer.length || checkY < 0 || checkY >= collisionLayer[0].length || collisionLayer[checkX][checkY]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * First finds a path, if necessary, calling findPath, storing it in
     * currentPath, then initializing currentPathIndex to 0
     *
     * Then,
     *
     * @param guardPosition position of guard right now
     * @param info information (see store controller)
     * @return the appropriate velocity according to shortest path
     */

    //THE CAUSE OF THE OSCCILATION ISSUE IS pathfinding when the target
    // (in this case, the player) is moving and the pathfinder
    // (the guard) gets stuck oscillating between two points or
    // directions because it's constantly recalculating the path
    // based on the player's new position.
    //THIS can be fixed by changed the constant in playerPosition.dst(guardPosition) > 10
    //drawback: the larger the constant, the less osccilation but guard has inaccurate
    //estimate of player location

    //Other problem right now, guard sometimes get stuck on NormalObstacles
    //may be able to be fixed by altering get neighbors and canFitInPosition functions
    public Vector2 updateChaseMode(Vector2 guardPosition, Array<Float> info, int chaseCounter) {
        Vector2 playerPosition = new Vector2(info.get(0),info.get(1));
        if(chaseCounter >=CHASE_COUNTER_CONSTANT){
            currentPath = findPathMain(guardPosition, info);
            currentPathIndex = 0;
        }
        else if (currentPath.isEmpty() || currentPathIndex >= currentPath.size || playerPosition.dst(guardPosition) > 10) {
            currentPath = findPathMain(guardPosition, info);
            currentPathIndex = 0;
        }

        if (!currentPath.isEmpty() && currentPathIndex < currentPath.size) {
            Vector2 nextStep = currentPath.get(currentPathIndex);
            if (guardPosition.dst(nextStep) < 1f) {
                currentPathIndex++;
                if (currentPathIndex < currentPath.size) {
                    nextStep = currentPath.get(currentPathIndex);
                }
            }

            Vector2 direction = new Vector2(nextStep.x - guardPosition.x, nextStep.y - guardPosition.y).nor();
            //System.out.println("direction" + direction);
            return direction.scl(chaseSpeed);
        }

        return null;
    }
    public boolean getDirection(){
        //returns true if moving right
        return movingRight;
    }
    public boolean getDirection2(){
        return movingup;
    }
    public PatrolDirection getPatrolDirection(){
        return this.patrolDirection;
    }

    public AIState getCurrentState() {
        return currentState;
    }
}
