package com.raccoon.mygame.util;

import com.badlogic.gdx.math.Vector2;

public class Node implements Comparable<Node> {
    public Vector2 position;
    public Node parent;
    public float cost;

    public Node(Vector2 position, Node parent, float cost) {
        this.position = position;
        this.parent = parent;
        this.cost = cost;
    }

    @Override
    public int compareTo(Node other) {
        return Float.compare(this.cost, other.cost);
    }
}

