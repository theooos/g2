package client.graphics;

import client.ClientSettings;
import client.audio.AudioManager;
import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import org.lwjgl.input.Mouse;

import java.util.function.Consumer;

/**
 * Created by theo on 22/03/2017.
 */
public class SettingsRenderer {

    private static InterfaceTexture back_button;
    private static InterfaceTexture music_slider;
    private static InterfaceTexture sound_slider;

    private static boolean draggingMusicSlider = false;
    private static boolean draggingSoundSlider = false;

    private static Layer settingsLayer = new Layer();

    private static float sliderLength = 360f;
    private static float leftSliderLimit = 370f;
    private static boolean hasClicked;

    public static void initialise() {
        InterfaceTexture settings_screen = new InterfaceTexture(ISprite.SETTINGS_SCREEN);
        back_button = new InterfaceTexture(ISprite.BACK_BUTTON);
        music_slider = new InterfaceTexture(ISprite.SLIDER);
        sound_slider = new InterfaceTexture(ISprite.SLIDER);

        settings_screen.setRatio(0.5f);
        settings_screen.spawn(0, 400f, 300f, settingsLayer);
        back_button.spawn(1, 400, 120, settingsLayer);
    }

    public static void run(Consumer consumer) {
        checkMouse();

        music_slider.spawn(2, 370 + sliderLength * ClientSettings.MUSIC_VOL, 360f, settingsLayer);
        sound_slider.spawn(3, 370 + sliderLength * ClientSettings.SOUND_VOL, 240f, settingsLayer);

        settingsLayer.render();

        handleClickedBack(consumer);
    }

    private static void checkMouse() {
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 0) {
                if (Mouse.getEventButtonState()) {
                    if (music_slider.isClicked()) draggingMusicSlider = true;
                    if (sound_slider.isClicked()) draggingSoundSlider = true;
                } else {
                    draggingMusicSlider = false;
                    draggingSoundSlider = false;
                }
            }
        }
        if (draggingMusicSlider) {
            float vol = ((float) Mouse.getX() - leftSliderLimit) / sliderLength;
            if (vol < 0) vol = 0;
            if (vol > 1) vol = 1;
            ClientSettings.MUSIC_VOL = vol;
            AudioManager.applyVolume();
        }
        if (draggingSoundSlider) {
            float vol = ((float) Mouse.getX() - leftSliderLimit) / sliderLength;
            if (vol < 0) vol = 0;
            if (vol > 1) vol = 1;
            ClientSettings.SOUND_VOL = vol;
            AudioManager.applyVolume();
        }
    }

    private static void handleClickedBack(Consumer consumer) {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (back_button.isClicked()) {
                consumer.accept(null);
                hasClicked = true;
            }
        }
    }
}
