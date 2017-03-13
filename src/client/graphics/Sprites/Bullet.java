package client.graphics.Sprites;

/**
 * Created by bianca on 26/02/2017.
 */
public class Bullet extends Sprite {

    public Bullet(int type){
        this.type = type;
        this.setRatio(0.2f);
        initGameSprite();
    }
}
