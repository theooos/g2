package server.game;

import objects.Sendable;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Random;

/**
 * Created by peran on 27/01/17.
 * A class to handle vector2fs
 */

public class Vector2 implements Sendable{
    private float x;
    private float y;

    /**
     * Gets the x value of the vector
     * @return x
     */
    public float getX(){
        return this.x;
    }

    /**
     * Gets the y value of teh vector
     * @return y
     */
    public float getY(){
        return this.y;
    }

    /**
     * Sets the x value
     * @param x
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     * Sets the y value
     * @param y
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     * Gets the distance to the target vectors
     * @param target the vector to get distance to
     * @return float of the distance to the vector
     */
    public float getDistanceTo(Vector2 target) {
        return (float)Math.sqrt(Math.pow(target.x-this.x, 2)+Math.pow(target.y-this.y, 2));
    }

    /**
     * @return the unit vector of this vector.
     */
    public Vector2 normalise(){
        float mag = (float)Math.sqrt(x*x + y*y);
        if(mag!=0) {
            this.x /= mag;
            this.y /= mag;
        }
        return this;
    }

    /**
     * Gets the vector towards another vector
     * @param target the vector to get towards
     * @return the vector of the difference between the two vectors
     */
    public Vector2 vectorTowards(Vector2 target){
        return new Vector2(target.getX() - this.x, target.getY() - this.y);
    }

    /**
     * Gets the vector towards two floats
     * @param x the xcoord
     * @param y the ycoord
     * @return the vector of the difference between the vector and the point
     */
    public Vector2 vectorTowards(float x, float y) {
        return this.vectorTowards(new Vector2(x, y));
    }

    /**
     * gets the absolute value of the vector
     * @return the absolute value
     */
    public float abs(){
        return (float)Math.sqrt(x*x + y*y);
    }

    /**
     * Multiplies two vectors together
     * @param multVector the vector to multiply with
     * @return the mulitplied vector
     */
    public Vector2 mult(Vector2 multVector){
        return new Vector2(this.x * multVector.getX(), this.y * multVector.getY());
    }

    /**
     * multiply a vector by a float
     * @param val the val to multiply the vector
     * @return the amount each vector is multiplied with
     */
    public Vector2 mult(float val){
        return new Vector2(this.x * val, this.y * val);
    }

    /**
     * divides a vector by another
     * @param divVector the vector to div the current vector by
     * @return the divided vector
     */
    public Vector2 div(Vector2 divVector){
        return new Vector2(this.x/divVector.getX(), this.y/divVector.getY());
    }

    /**
     * Adds two vectors together
     * @param addVector the vector to add
     * @return the sum of the vectors
     */
    public Vector2 add(Vector2 addVector){
        return new Vector2(this.x + addVector.getX(), this.y + addVector.getY());
    }

    /**
     * Subtracts the given vector from this one
     * @param subVector the vector to subtract
     * @return the subtracted vector
     */
    public Vector2 sub(Vector2 subVector){
        return new Vector2(this.x - subVector.getX(), this.y - subVector.getY());
    }

    /**
     * The dot product of two vectors
     * @param v the second vector
     * @return a float value of the scalar product
     */
    public float dot(Vector2 v) {
        return (x*v.y)+(y*v.y);
    }

    /**
     * Sets up a new vector
     * @param x the x coord
     * @param y the y coord
     */
    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Sets up a new vector
     * @param x the x coord
     * @param y the y coord
     */
    public Vector2(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    /**
     * @return Gets a string in a human readable format
     */
    public String toString() {
        return "("+x+", "+y+")";
    }


    public boolean equals(Vector2 other) {
        return (this.x == other.getX() && this.y == other.getY());
    }

    public Point toPoint() {
        return new Point((int)x, (int)y);
    }

    public static Vector2 getDirectionVector(Line2D line){
        Vector2 start = new Vector2((float)line.getX1(), (float)line.getY1());
        Vector2 end = new Vector2((float)line.getX2(), (float)line.getY2());
        return start.vectorTowards(end);
    }

    public static Vector2 getPositionVector(Line2D line){
        return new Vector2((float)line.getX1(), (float)line.getY1());
    }

    /**
     * Sets a vector to a set given size
     * @param size the size of the vector required
     * @return the clamped vector
     */
    public Vector2 clampedTo(float size) {
        return (normalise()).mult(size);
    }

    /**
     * Deviates a vector by a set amount
     * @param targetDirection the starting vector
     * @param inaccuracy the inaccuracy to add to the vector
     * @return the vector with added inaccuracy
     */
    public static Vector2 deviate(Vector2 targetDirection, int inaccuracy){
        Random gen = new Random();
        double ang = Math.atan(targetDirection.getX()/targetDirection.getY());
        if (Double.isInfinite(ang)) {
            ang = 0;
        } else if (targetDirection.getY() < 0) {
            ang += Math.PI;
        }
        ang += Math.toRadians(gen.nextInt(inaccuracy));
        float newX = (float)(Math.sin(ang));
        float newY = (float)(Math.cos(ang));

        return (new Vector2(newX, newY)).normalise();
    }

    /**
     * returns a random vector
     * @param size the magnitude of the vector
     * @return a random vector
     */
    public static Vector2 randomVector(int size){
        Random gen = new Random();
        return new Vector2(gen.nextFloat()*size, gen.nextFloat()*size);
    }
}