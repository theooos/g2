package server.game;

/**
 * Created by peran on 27/01/17.
 */

public class Vector2 {
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
     * gets the normlisation of the vector
     * @return the value of the normal
     */
    public Vector2 normalise(){
        float mag = (float)Math.sqrt(x*x + y*y);
        if(mag!=0) {
            this.x /= mag;
            this.y /= mag;
        }
        return this;
    }

    public Vector2 vectorTowards(Vector2 target){
        return new Vector2(target.getX() - this.x, target.getY() - this.y);
    }

    public Vector2 vectorTowards(float x, float y) {
        return this.vectorTowards(new Vector2(x, y));
    }

    public float abs(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public Vector2 mult(Vector2 multVector){
        return new Vector2(this.x * multVector.getX(), this.y * multVector.getY());
    }

    public Vector2 mult(float val){
        return new Vector2(this.x * val, this.y * val);
    }

    public Vector2 div(Vector2 divVector){
        return new Vector2(this.x/divVector.getX(), this.y/divVector.getY());
    }

    public Vector2 add(Vector2 addVector){
        return new Vector2(this.x + addVector.getX(), this.y + addVector.getY());
    }

    public Vector2 sub(Vector2 subVector){
        return new Vector2(this.x - subVector.getX(), this.y - subVector.getY());
    }

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "("+x+", "+y+")";
    }

    public Vector2(){

    }
}