package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import org.lwjgl.input.Mouse;

/**
 * Renders the in-game menus/scoreboard.
 */
class InGameMenuRenderer {

    private GameManager gameManager;

    private InterfaceTexture back_button = new InterfaceTexture(ISprite.BACK_BUTTON);
    private InterfaceTexture resume_game_button = new InterfaceTexture(ISprite.RESUME_GAME_BUTTON);
    private InterfaceTexture settings_game_button = new InterfaceTexture(ISprite.SETTINGS_BUTTON);
    private InterfaceTexture exit_game_button = new InterfaceTexture(ISprite.EXIT_GAME_BUTTON);

    private static Layer menuLayer = new Layer();
    private static Layer settingsLayer = new Layer();
    private static Layer endLayer = new Layer();

    private boolean hasClicked = false;

    InGameMenuRenderer(GameManager gameManager) {
        this.gameManager = gameManager;

        readyMenuLayer();
        readySettingsLayer();
        readyEndLayer();
    }

    void readyMenuLayer() {
        InterfaceTexture menu = new InterfaceTexture(ISprite.IN_GAME_MENU_SCREEN);
        menu.setRatio(0.5f);

        menu.spawn(0, 400, 300, menuLayer);
        resume_game_button.spawn(1, 400, 360, menuLayer);
        settings_game_button.spawn(2, 400, 240, menuLayer);
        exit_game_button.spawn(3, 400, 120, menuLayer);
    }

    void readySettingsLayer(){
        InterfaceTexture settings = new InterfaceTexture(ISprite.SETTINGS_SCREEN);
        settings.setRatio(0.5f);

        settings.spawn(0,400,300,settingsLayer);
        back_button.spawn(1,400,120,settingsLayer);
    }

    void readyEndLayer(){
        InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
        background.setRatio(0.5f);
        background.spawn(0,400,300,endLayer);
    }

    void handleClickedMenu() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (resume_game_button.isClicked()) {
                gameManager.setScreen(GameManager.Screen.GAME);
                hasClicked = true;
            } else if (settings_game_button.isClicked()) {
                gameManager.setScreen(GameManager.Screen.SETTINGS);
                hasClicked = true;
            } else if (exit_game_button.isClicked()){
                gameManager.setScreen(GameManager.Screen.GAMEOVER);
                hasClicked = true;
            }
        }
    }

    void handleClickedSettings() {
        if (hasClicked && !Mouse.isButtonDown(0)) hasClicked = false;

        if (!hasClicked) {
            if (back_button.isClicked()) {
                gameManager.setScreen(GameManager.Screen.MENU);
                hasClicked = true;
            }
        }
    }

    void renderMenu() {
        menuLayer.render(0);
    }

    public void renderSettings() {
        settingsLayer.render(0);
    }
}
