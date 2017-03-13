package client.graphics.Sprites;

/**
 * Created by bianca on 26/02/2017.
 */
public class WallTexture extends Sprite {

    public WallTexture(int type){
        this.type = type;
        this.setRatio(1f);
        initGameSprite();
    }
}
