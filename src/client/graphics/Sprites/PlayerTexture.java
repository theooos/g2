package client.graphics.Sprites;

/**
 * Created by bianca on 26/02/2017.
 */
public class PlayerTexture extends Sprite {

    public PlayerTexture(int type){
        this.type = type;
        this.setRatio(0.4f);
        initGameSprite();
    }
}
