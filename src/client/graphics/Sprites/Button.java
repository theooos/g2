package client.graphics.Sprites;
import org.lwjgl.input.Mouse;

import java.awt.*;

/**
 * reused the class from Cip
 */
public class Button extends Sprite {

    boolean mouseOverEnabled = false, visible = true;

    public Button(int type){
        this.type = type;
        this.setRatio(1f);
        this.init();
    }

    public boolean isMouseOver(){
        Rectangle r = new Rectangle();
        r.x = (int)this.getPosition().getX();
        r.y = (int)this.getPosition().getY();
        r.width = (int)this.width;
        r.height = (int) this.height;
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return r.contains(new Point(Mouse.getX(), Mouse.getY()));
    }

    public void enableMouseOver(boolean b){
        mouseOverEnabled = b;
    }
    public void setVisible(boolean b){
        visible = b;
    }
}
