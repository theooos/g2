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

import static client.graphics.Sprites.ISprite.*;

/**
 * Created by bianca on 25/02/2017.
 */
public class TextureLoader {

    private static Texture[] textures = new Texture[32];

    public static void initialise() {
        textures[BACKGROUND] = loadTexture("background.png");
        textures[TITLE] = loadTexture("title.png");
        textures[LOADING_SCREEN] = loadTexture("loading_screen.png");
        textures[CONTROLS_SCREEN] = loadTexture("controls_screen.png");
        textures[ABOUT_SCREEN] = loadTexture("about_screen.png");
        textures[LOBBY_SCREEN] = loadTexture("lobby_screen.png");

        textures[IN_GAME_MENU_SCREEN] = loadTexture("in_game_menu_screen.png");
        textures[SETTINGS_SCREEN] = loadTexture("settings_screen.png");
        textures[END_GAME_SCREEN] = loadTexture("end_game_screen.png");

        textures[SOLO_GAME_BUTTON] = loadTexture("solo_game_button.png");
        textures[VERSUS_GAME_BUTTON] = loadTexture("versus_game_button.png");
        textures[CONTROLS_BUTTON] = loadTexture("controls_button.png");
        textures[ABOUT_BUTTON] = loadTexture("about_button.png");
        textures[BACK_BUTTON] = loadTexture("back_button.png");
        textures[BACK_BUTTON_HALF] = loadTexture("back_button_half.png");
        textures[RESUME_GAME_BUTTON] = loadTexture("resume_game_button.png");
        textures[SETTINGS_BUTTON] = loadTexture("settings_button.png");
        textures[EXIT_GAME_BUTTON] = loadTexture("exit_game_button.png");

        textures[SLIDER] = loadTexture("slider.png");
    }

    private static Texture loadTexture(String path) {
        Hashtable imageCache = new Hashtable();
        BufferedImage buffImage = null;
        try {
            if (imageCache.get(path) != null)
                buffImage = (BufferedImage) imageCache.get(path);
            else {
                buffImage = ImageIO.read(TextureLoader.class.getResource("../../" + path));
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

    public static Texture getTexture(int id) {
        return textures[id];
    }
}
