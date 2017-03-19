package client.graphics;

import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.InterfaceTexture;
import objects.GameData;

/**
 * Renders the in-game menus/scoreboard.
 */
class InGameMenuRenderer {

    private GameData gameData;
    private int playerID;

    InGameMenuRenderer(GameData gameData, int playerID) {
        this.gameData = gameData;
        this.playerID = playerID;
    }

    void readyMenuLayer(){
        InterfaceTexture background = new InterfaceTexture(ISprite.BACKGROUND);
    }

    void handleClickedMenu(){

    }

    void handleClickedScoreboard(){

    }

    void handleClickedEndScreen(){

    }

    void renderMenu(){
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("The in-game menu will be here.", 0, 50);
    }

    void renderEndScreen(){
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("The end game screen will be here.", 0, 50);
    }
}
