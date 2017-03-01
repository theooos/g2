package server.ai.vision;

import server.game.Vector2;

import java.awt.*;

/**
 * Created by rhys on 2/25/17.
 */
public class VertexAngle {

    private Vector2 vertex;
    private double angle;

    public VertexAngle(Vector2 vertex, double angle) {
        this.vertex = vertex;
        this.angle = angle;
    }

    public Vector2 getVertex() {
        return vertex;
    }

    public double getAngle() {
        return angle;
    }
}
