package client.graphics;

import client.graphics.Sprites.Sprite;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

/**
 * Created by bianca on 25/02/2017.
 */
public class Layer {

    public HashMap<Integer, Sprite> sprites = new HashMap<>();

    public Layer(){}

    public void add(int id, Sprite e)
    {
        sprites.put(id, e);
    }

    public void remove(int id)
    {
        sprites.remove(id);
    }

    public void render(){
        render(0);
    }

    public void render(int phase)
    {
        for (Sprite s : sprites.values())
        {
            if(s.getPhase() == phase)
                s.draw();
        }
    }

    public void destroy(){
        sprites.clear();
    }

    public void update(int id, Vector2f newPos, int newPhase){
        sprites.get(id).setPosition(newPos);
        sprites.get(id).setPhase(newPhase);
    }

    public HashMap<Integer, Sprite> getSprites() {
        return sprites;
    }
}
