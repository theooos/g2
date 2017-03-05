package client.graphics;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glVertex2f;

/**
 * Created by peran on 3/5/17.
 *  used for draw methods
 */
class Draw {
    private int width;
    private int height;

    Draw(int width, int height) {
        this.width = width;
        this.height = height;
    }

    void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        GL11.glBegin(GL_TRIANGLE_FAN);

        for (int ii = 0; ii < num_segments; ii++) {
            glVertex2f(x + cx, y + cy);//output vertex

            //calculate the tangential vector; remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            float tx = -y;
            float ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
        GL11.glEnd();
    }

    void verticalDraw(float xStart, float yStart, float rectWidth, float rectHeight) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(checkX(xStart), checkY(height - yStart));
        GL11.glVertex2f(checkX(xStart + rectWidth), checkY(height - yStart));
        GL11.glVertex2f(checkX(xStart + rectWidth), checkY(height - (yStart+rectHeight)));
        GL11.glVertex2f(checkX(xStart), checkY(height - (yStart+rectHeight)));
        GL11.glEnd();
    }

    /**
     * Keep x coordinate on the screen
     *
     * @param x Initial x coordinate
     * @return Checked x coordinate
     */
    private float checkX(float x) {
        if (x < 0) x = 0;
        if (x > width) x = width;
        return x;
    }

    /**
     * Keep y coordinate on the screen
     *
     * @param y Initial y coordinate
     * @return Checked y coordinate
     */
    private float checkY(float y) {
        if (y < 0) y = 0;
        if (y > height) y = height;
        return y;
    }
}
