package client.graphics;

public class Texture {

    public int	texId;
    public int	texWidth;
    public int	texHeight;

    public Texture() {}

    public int getTextureId(){
        return texId;
    }

    public int getTextureHeight() {
        return texHeight;
    }

    public int getTextureWidth(){
        return texWidth;
    }

    public void setTextureId(int id){
        this.texId = id;
    }

    public void setTextureHeight(int height){
        this.texHeight = height;
    }

    public void setTextureWidth(int width){
        this.texWidth = width;
    }
}