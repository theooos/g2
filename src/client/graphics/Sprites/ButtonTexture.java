package client.graphics.Sprites;
/**
 * Created by Patrick on 3/3/2017.
 * texture for the initial menu
 */
public class ButtonTexture extends Sprite {

    public ButtonTexture(int type)
    {
        this.type = type;
        this.setRatio(1f);
        initGameSprite();
    }
}

