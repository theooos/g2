package client.graphics.Sprites;

/**
 * Created by Patrick on 3/9/2017.
 */
public class InterfaceTexture extends Sprite {
    public InterfaceTexture(int type) {
        this.type = type;
        this.setRatio(0.3f);
        initInterfaceSprite();
    }
}
