package client.graphics.Sprites;

/**
 * Created by bianca on 19/03/2017.
 */
public class BackgroundTexture extends Sprite{
    public BackgroundTexture(int type) {
        this.type = type;
        this.setRatio(0.5f);
        initInterfaceSprite();
    }
}
