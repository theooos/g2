package client.graphics.Sprites;

import client.graphics.GameRenderer;
import client.graphics.Layer;
import client.graphics.Texture;
import client.ui.StartScreen;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

public abstract class Sprite implements ISprite {

    public int original_width;
    public int original_height;

    private float ratio = 1f;
    public float width;
    public float height;

    public void setRatio(float newRatio)
    {
        this.ratio = newRatio;
        this.width = this.original_width * ratio;
        this.height = this.original_height * ratio;
    }

    public void setDimension(float width, float height){
        this.width = width;
        this.height = height;
        this.ratio = this.original_height / this.height;
    }

    public Vector2f position = new Vector2f();

    protected Texture texture;
    protected Layer layer = null;
    public int type;
    private int id;
    private int phase;

    private float textureUp = 1;
    private float textureDown = 0;
    private float textureLeft = 0;
    private float textureRight = 1;

    protected void init()
    {
        this.texture = GameRenderer.textureHandler.getTexture(type);
        this.original_width = this.texture.getTextureWidth();
        this.original_height = this.texture.getTextureHeight();
        this.width = this.original_width * ratio;
        this.height = this.original_height * ratio;
    }

    protected void initInterface()
    {
        this.texture = StartScreen.textureHandler.getTexture(type);
        this.original_width = this.texture.getTextureWidth();
        this.original_height = this.texture.getTextureHeight();
        this.width = this.original_width * ratio;
        this.height = this.original_height * ratio;
    }

    public void spawn(int id, Vector2f position,int phase, Layer layer) {
        this.id = id;
        this.phase = phase;
        this.position.x = position.x;
        this.position.y = position.y;

        this.layer = layer;
        layer.add(id, this);
    }

    public void unSpawn() {
        layer.remove(id);
    }

    public void draw()
    {
        GL11.glLoadIdentity();
        GL11.glTranslatef(position.x,position.y,0);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureId() );

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(textureRight,textureUp); //Upper right
            GL11.glVertex2f(width, -height);

            GL11.glTexCoord2f(textureLeft,textureUp); //Upper left
            GL11.glVertex2f(-width, -height);

            GL11.glTexCoord2f(textureLeft,textureDown); //Lower left
            GL11.glVertex2f(-width,height);

            GL11.glTexCoord2f(textureRight,textureDown); // Lower right
            GL11.glVertex2f(width,height);
        }
        GL11.glEnd();
    }

    public Vector2f getPosition(){
        return position;
    }

    public void setPosition(Vector2f newPosition) {
        this.position = newPosition;
    }

    public int getPhase(){
        return this.phase;
    }

    public void setPhase(int newPhase){
        this.phase = newPhase;
    }

    public int getType(){
        return this.type;
    }

    public void setType(int newType){
        this.type = newType;
    }

    public int getId(){
        return this.id;
    }
}