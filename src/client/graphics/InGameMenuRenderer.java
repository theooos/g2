package client.graphics;

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

    void renderMenu(){

    }

    void renderScoreboard(){

    }

    void renderEndScreen(){
        TextRenderer textRenderer = new TextRenderer();
        textRenderer.drawText("Game over. To be changed.", 0, 50);
    }
}
