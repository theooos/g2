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

    private static Texture[] textures = new Texture[32];

    public void initialise() {

        textures[ISprite.PLAYER_P1] = loadTexture("Player1.png");
        textures[ISprite.PLAYER_P2] = loadTexture("Player2.png");

        textures[ISprite.ORB_P1] = loadTexture("orb_P1.png");
        textures[ISprite.ORB_P2] = loadTexture("orb_P2.png");

        textures[ISprite.SHOOT_P1] = loadTexture("Bullet2_P1.png");
        textures[ISprite.SHOOT_P2] = loadTexture("Bullet2_P2.png");

        textures[ISprite.BULLET_P1] = loadTexture("Bullet1_P1.png");
        textures[ISprite.BULLET_P2] = loadTexture("Bullet1_P2.png");

        textures[ISprite.WALL_P1] = loadTexture("Wall_P1.png");
        textures[ISprite.WALL_P2] = loadTexture("Wall_P2.png");

        //textures[ISprite.START] = loadTexture("play.png",350,125);
        textures[ISprite.ABOUT] = loadTexture("about.png");
        textures[ISprite.CONTROLS] = loadTexture("controls.png");
        textures[ISprite.ABOUTTEXT] = loadTexture("testAbout.png");
        textures[ISprite.GOBACK] = loadTexture("back.png");
        textures[ISprite.LOBBY] = loadTexture("lobbyfinal.png");


        textures[ISprite.OPTIONS] = loadTexture("options.png");
        textures[ISprite.CONTINUE] = loadTexture("continue.png");
        textures[ISprite.MUTE] = loadTexture("mute.png");
        textures[ISprite.ENDGAME] = loadTexture("endgame.png");

        textures[ISprite.COLLISION] = loadTexture("collision.png");
        textures[ISprite.BACKGROUND] = loadTexture("background.png");
        textures[ISprite.SCOREBOARD] = loadTexture("scoreboard.png");
    }


    private Hashtable imageCache = new Hashtable();

    private Texture loadTexture(String path) {

        BufferedImage buffImage = null;
        try {
            if (imageCache.get(path) != null)
                buffImage = (BufferedImage) imageCache.get(path);
            else {
                buffImage = ImageIO.read(this.getClass().getResource("../../" + path));
                byte[] data = ((DataBufferByte) buffImage.getRaster().getDataBuffer()).getData();

                switch (buffImage.getType()) {
                    case BufferedImage.TYPE_4BYTE_ABGR:
                        convertFromARGBToBGRA(data);
                        break;
                    case BufferedImage.TYPE_3BYTE_BGR:
                        convertFromBGRToRGB(data);
                        break;
                    default:
                        System.err.println("Unknown type of image:" + buffImage.getType());
                        break;
                }
                imageCache.put(path, buffImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int bytesPerPixel = buffImage.getColorModel().getPixelSize() / 8;

        int width = buffImage.getWidth();
        int height = buffImage.getHeight();

        ByteBuffer scratch = ByteBuffer.allocateDirect(width * height * bytesPerPixel).order(ByteOrder.nativeOrder());

        DataBufferByte dataBufferByte = ((DataBufferByte) buffImage.getRaster().getDataBuffer());

        for (int i = 0; i < height; i++)
            scratch.put(dataBufferByte.getData(), (i * width) * bytesPerPixel, width * bytesPerPixel);

        scratch.rewind();


        // Create A IntBuffer For Image Address In Memory
        IntBuffer buf = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL11.glGenTextures(buf); // Create Texture In OpenGL

        // Create Nearest Filtered Texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, buf.get(0));
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, scratch);

        Texture newTexture = new Texture();
        newTexture.texId = buf.get(0);     // Return Image Addresses In Memory
        newTexture.texHeight = height;
        newTexture.texWidth = width;

        return newTexture;
    }

    private static void convertFromARGBToBGRA(final byte[] data) {
        for (int i = 0; i < data.length; i += 4) {
            data[i] ^= data[i + 3];
            data[i + 3] ^= data[i];
            data[i] ^= data[i + 3];

            data[i + 1] ^= data[i + 2];
            data[i + 2] ^= data[i + 1];
            data[i + 1] ^= data[i + 2];
        }
    }

    private static void convertFromBGRToRGB(final byte[] data) {
        for (int i = 0; i < data.length; i += 3) {
            data[i] ^= data[i + 2];
            data[i + 2] ^= data[i];
            data[i] ^= data[i + 2];
        }
    }

    public Texture getTexture(int id) {
        return textures[id];
    }
}
