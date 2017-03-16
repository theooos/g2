package client.graphics;

import client.graphics.Sprites.ISprite;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

/**
 * Created by bianca on 25/02/2017.
 */
public class TextureLoader {

    private static Texture[] textures = new Texture[1024];

    public void initialise(){

        textures[ISprite.PLAYER_P1] = loadTexture("Player1.png", 0,0,54,55);
        textures[ISprite.PLAYER_P2] = loadTexture("Player2.png", 0,0,55,55);

        textures[ISprite.ORB_P1] = loadTexture("orb_P1.png",0,0,100,100);
        textures[ISprite.ORB_P2] = loadTexture("orb_P2.png", 0,0,100,101);

        textures[ISprite.SHOOT_P1] = loadTexture("Bullet2_P1.png", 0,0,18,28);
        textures[ISprite.SHOOT_P2] = loadTexture("Bullet2_P2.png", 0,0,18,28);

        textures[ISprite.BULLET_P1] = loadTexture("Bullet1_P1.png", 0,0,18,32);
        textures[ISprite.BULLET_P2] = loadTexture("Bullet1_P2.png", 0,0,18,31);

        textures[ISprite.WALL_P1] = loadTexture("Wall_P1.png", 0,0,10,10);
        textures[ISprite.WALL_P2] = loadTexture("Wall_P2.png", 0,0,10,10);

        //textures[ISprite.START] = loadTexture("play.png",0,0,350,125);
        textures[ISprite.ABOUT] = loadTexture("about.png",0,0,500,100);
        textures[ISprite.HELP] = loadTexture("help1.png",0,0,256,256);
        textures[ISprite.ABOUTTEXT] = loadTexture("testAbout.png",0,0,300,171);
        textures[ISprite.GOBACK] = loadTexture("back.png",0,0,300,300);
        textures[ISprite.LOBBY] = loadTexture("lobbyfinal.png",0,0,500,100);


        textures[ISprite.OPTIONS] = loadTexture("options.png",0,0,638, 348);
        textures[ISprite.CONTINUE] = loadTexture("continue.png",0,0,29,7);
        textures[ISprite.MUTE] = loadTexture("mute.png",0,0,18,7);
        textures[ISprite.ENDGAME] = loadTexture("endgame.png",0,0,29,7);

        textures[ISprite.COLLISION] = loadTexture("collision.png", 0,0, 50,50);
    }


    private Hashtable imageCache = new Hashtable();

    private  Texture loadTexture(String path,int xOffSet, int yOffSet, int textWidth, int textHeight) {

        BufferedImage buffImage = null;
        try
        {
            if (imageCache.get(path) != null)
                buffImage = (BufferedImage)imageCache.get(path);
            else
            {
                buffImage = ImageIO.read(this.getClass().getResource("../../" + path));

                byte[] data = ((DataBufferByte) buffImage.getRaster().getDataBuffer()).getData();
                switch (buffImage.getType())
                {
                    case BufferedImage.TYPE_4BYTE_ABGR:  convertFromARGBToBGRA(data);break;
                    case BufferedImage.TYPE_3BYTE_BGR:  convertFromBGRToRGB(data);   break;
                    default: System.out.println("Unknown type:"+buffImage.getType()); break;
                }
                imageCache.put(path, buffImage);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;

        ByteBuffer scratch = ByteBuffer.allocateDirect(textWidth*textHeight*bytesPerPixel).order(ByteOrder.nativeOrder());


        DataBufferByte dataBufferByte = ((DataBufferByte) buffImage.getRaster().getDataBuffer());

        for (int i = 0 ; i < textHeight ; i++)
            scratch.put(dataBufferByte.getData(),(xOffSet+(yOffSet+i)*buffImage.getWidth())*bytesPerPixel, textWidth * bytesPerPixel);

        scratch.rewind();


        // Create A IntBuffer For Image Address In Memory
        IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL11.glGenTextures(buf); // Create Texture In OpenGL

        // Create Nearest Filtered Texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0,GL11.GL_RGBA,textWidth,textHeight, 0,GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, scratch);

        Texture newTexture = new Texture();
        newTexture.texId = buf.get(0);     // Return Image Addresses In Memory
        newTexture.texHeight = textHeight;
        newTexture.texWidth = textWidth;

        return newTexture;
    }

    private static void convertFromARGBToBGRA(final byte[] data) {
        for (int i = 0; i <  data.length; i += 4)
        {
            data[i] ^= data[i + 3];
            data[i+3] ^= data[i];
            data[i] ^= data[i + 3];

            data[i + 1] ^= data[i + 2];
            data[i + 2] ^= data[i + 1];
            data[i + 1] ^= data[i + 2];
        }
    }

    private static void convertFromBGRToRGB(final byte[] data)
    {
        for (int i = 0; i < data.length; i += 3)
        {
            data[i] ^= data[i + 2];
            data[i + 2 ] ^= data[i ];
            data[i] ^= data[i + 2];
        }
    }

    public Texture getTexture(int id) {
        return textures[id];
    }
}
