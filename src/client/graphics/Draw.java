package client.graphics;

import static client.ClientSettings.SCREEN_HEIGHT;
import static client.ClientSettings.SCREEN_WIDTH;
import static client.graphics.GameManager.out;
import static org.lwjgl.opengl.GL11.*;

import client.ClientSettings;
import client.audio.AudioManager;
import objects.GameData;
import objects.InitPlayer;
import objects.LobbyData;
import server.game.Map;
import server.game.Scoreboard;
import server.game.Vector2;

/**
 * Created by peran on 3/5/17.
 *  used for draw methods
 */
class Draw {
    private int width;
    private int height;
    private int oldHealth;
    private double displayHealth;
    private double oldHeat;
    private double displayHeat;
    private GameData gameData;
    private TextRenderer smallText;
    private TextRenderer largeText;
    private int playerID;

    /**
     * Sets up a new draw object
     * @param gd the game data
     * @param playerID this player's id
     * @param textRenderers the text renders
     */
    Draw(GameData gd, int playerID, TextRenderer[] textRenderers) {
        this.playerID = playerID;
        this.width = ClientSettings.SCREEN_WIDTH;
        this.height = ClientSettings.SCREEN_HEIGHT;
        oldHealth = 0;
        displayHealth = 100;
        oldHeat = 0;
        displayHeat = 0;
        gameData = gd;
        smallText = textRenderers[0];
        largeText = textRenderers[1];
    }

    /**
     * Draws a triangle
     * @param centre vector2 of centre pos
     * @param rotation how much it's rotated in degrees
     * @param drawWidth how wide the triangle is
     * @param drawHeight how tall the triangle is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    void drawTriangle(Vector2 centre, float rotation, float drawWidth, float drawHeight, float red, float green, float blue) {
        glColor4f(red, green, blue, 1);
        glPushMatrix();
        float cx = centre.getX();
        float cy = height-centre.getY();
        glTranslatef(cx, cy, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-cx, -cy, 0);
        glBegin(GL_TRIANGLES);
        glVertex2f(cx-drawWidth, height-(centre.getY()-drawHeight));
        glVertex2f(cx, height-(centre.getY()+drawHeight));
        glVertex2f(cx+drawWidth, height-(centre.getY()-drawHeight));
        //glVertex2f(cx-drawWidth/2, drawHeight-(centre.getY()+drawHeight/2));
        glEnd();
        glPopMatrix();
    }

    /**
     * Draw spikes (for shotgun reticule).  Draws two triangles
     * @param centre the centre of the shap
     * @param rotation the amount it's rotated
     * @param drawWidth the width of the shape
     * @param drawHeight the height of the shape
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    void drawSpikes(Vector2 centre, float rotation, float drawWidth, float drawHeight, float red, float green, float blue) {
        glColor4f(red, green, blue, 1);
        glPushMatrix();
        float cx = centre.getX();
        float cy = height-centre.getY();
        glTranslatef(cx, cy, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-cx, -cy, 0);
        glBegin(GL_TRIANGLES);
        glVertex2f(cx-drawWidth, height-(centre.getY()-drawHeight));
        glVertex2f(cx-drawWidth, height-(centre.getY()+drawHeight));
        glVertex2f(cx+drawWidth, height-(centre.getY()-drawHeight));
        glEnd();

        glBegin(GL_TRIANGLES);
        glVertex2f(cx-drawWidth, height-(centre.getY()-drawHeight));
        glVertex2f(cx+drawWidth, height-(centre.getY()+drawHeight));
        glVertex2f(cx+drawWidth, height-(centre.getY()-drawHeight));
        glEnd();

        glPopMatrix();
    }

    /**
     * draws a cube
     * @param centre the centre of the cube
     * @param rotation how much it's rotated
     * @param radius the radius of the cube
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param glow whether it glows or not
     */
    void drawQuad(Vector2 centre, float rotation, float radius, float red, float green, float blue, boolean glow) {
        glColor4f(red, green, blue, 1);
        glPushMatrix();
        float cx = centre.getX();
        float cy = height-centre.getY();
        glTranslatef(cx, cy, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-cx, -cy, 0);
        glBegin(GL_QUADS);
        glVertex2f(cx-radius, height-(centre.getY()-radius));
        glVertex2f(cx+radius, height-(centre.getY()-radius));
        glVertex2f(cx+radius, height-(centre.getY()+radius));
        glVertex2f(cx-radius, height-(centre.getY()+radius));
        glEnd();

        if (glow) {
            glBegin(GL_QUAD_STRIP);

            float intensity = 0.8f;
            glColor4f(red, green, blue, 0);
            glVertex2f(cx - radius * 2, cy - radius * 2);  //outer bottom left
            glColor4f(red, green, blue, intensity);
            glVertex2f(cx - radius, cy - radius); //inner bottom left
            glColor4f(red, green, blue, 0);
            glVertex2f(cx + radius * 2, cy - radius * 2); //outer bottom right
            glColor4f(red, green, blue, intensity);
            glVertex2f(cx + radius, cy - radius); //inner bottom right
            glColor4f(red, green, blue, 0);
            glVertex2f(cx + radius * 2, cy + radius * 2); //outer top right
            glColor4f(red, green, blue, intensity);
            glVertex2f(cx + radius, cy + radius); //inner top right
            glColor4f(red, green, blue, 0);
            glVertex2f(cx - radius * 2, cy + radius * 2); //outer top left
            glColor4f(red, green, blue, intensity);
            glVertex2f(cx - radius, cy + radius); //inner top left
            glColor4f(red, green, blue, 0);
            glVertex2f(cx - radius * 2, cy - radius * 2);  //outer bottom left
            glColor4f(red, green, blue, intensity);
            glVertex2f(cx - radius, cy - radius); //inner bottom left

            glEnd();
        }
        else {
            System.out.println("Quad rotate: "+rotation);
        }

        glPopMatrix();
    }

    /**
     * Flashes the damage on the screen
     * @param intensity how bright the glow is
     * @param hurt whether red or green
     */
    private void flashDamage(float intensity, boolean hurt) {
        intensity = Math.min(1, intensity);
        float buffer = 60;
        float red = 0;
        float green = 1;
        if (hurt) {
            red = 1;
            green = 0;
        }

        invertedQuadGlow(0, height, width, height, buffer, red, green, 0, intensity);
    }

    /**
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue) {
        drawAura(centre, radius, strokeWidth, red, green, blue, 1);
    }

    /**
     * draws glow round circles
     * @param centre the centre of the glow
     * @param radius the radius from the centre
     * @param strokeWidth how think the line is
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param intensity the brightness of the glow
     */
    void drawAura(Vector2 centre, float radius, float strokeWidth, float red, float green, float blue, float intensity) {
        glColor4f(red, green, blue, intensity);
        glBegin(GL_QUAD_STRIP);
        float cx = centre.getX();
        float cy = height-centre.getY();
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, 0);
        glVertex2f(cx, cy+radius);
        for (int i = 1; i < 360; i++) {
            glColor4f(red, green, blue, intensity);
            glVertex2d(cx+((radius-strokeWidth)*Math.sin(Math.toRadians(i))), cy+((radius-strokeWidth)*Math.cos(Math.toRadians(i))));
            glColor4f(red, green, blue, 0);
            glVertex2d(cx+((radius)*Math.sin(Math.toRadians(i))), cy+((radius)*Math.cos(Math.toRadians(i))));
        }
        glColor4f(red, green, blue, intensity);
        glVertex2f(cx, cy+(radius-strokeWidth));
        glColor4f(red, green, blue, 0);
        glVertex2f(cx, cy+radius);
        glEnd();
    }

    /**
     * draws a glow inwards in a rectangle
     * @param xStart the left of the glow
     * @param yStart the top of the glow
     * @param rectWidth the width of the rectangle
     * @param rectHeight the height of the rectangle
     * @param strokeWidth the width of the line
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     * @param intensity the brightness of the glow
     */
    private static void invertedQuadGlow(float xStart, float yStart, float rectWidth, float rectHeight, float strokeWidth, float red, float green, float blue, float intensity) {
        yStart = SCREEN_HEIGHT-yStart;

        glBegin(GL_QUAD_STRIP);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart+rectWidth,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+rectWidth-strokeWidth, yStart+strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart+rectWidth, yStart+rectHeight);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+rectWidth-strokeWidth, yStart+rectHeight-strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart+rectHeight);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+rectHeight-strokeWidth);
        glColor4f(red, green, blue, intensity);
        glVertex2f(xStart,yStart);
        glColor4f(red, green, blue, 0);
        glVertex2f(xStart+strokeWidth, yStart+strokeWidth);
        glEnd();
    }

    /**
     * Draws the scoreboard on the screen in points order
     * Also highlights which player you are
     * And shows total team score
     * If the game has ended, show win or loss
     * @param gameEnded whether the game has ended
     */
    void drawScoreboard(boolean gameEnded) {
        shadeScreen();
        Scoreboard sb = gameData.getScoreboard();

        if (smallText == null) {
            smallText = new TextRenderer(20);
        }
        if (largeText == null) {
            largeText = new TextRenderer(25);
        }

        float intensity = 0.8f;
        glDisable(GL_TEXTURE_2D);

        int[] scores = sb.getPlayerScores();
        float xStart = 800/6;
        float yStart = 150;
        float rectWidth = xStart*2;
        float rectHeight = 50;

        float red = 0;
        float green = 1;
        float blue = 0;
        float buffer = 20;

        //draws the victory or defeat view
        if (gameEnded) {
            float endY = yStart-buffer-rectHeight;
            float team = gameData.getPlayer(playerID).getTeam();
            glColor4f(1-team, team, 1-team, intensity);
            drawRect(xStart, endY, rectWidth*2, rectHeight);
            invertedQuadGlow(xStart,endY+rectHeight, rectWidth*2, rectHeight,10,1,1,1,1);

            if (sb.getTeam0Score() > sb.getTeam1Score()) {
                if (team == 0) {
                    //enclave won (you)
                    drawText(largeText, "VICTORY", ClientSettings.SCREEN_WIDTH/2-50, endY+3*rectHeight/4+3);
                }
                else {
                    //enclave won (not you)
                    drawText(largeText,"DEFEAT",ClientSettings.SCREEN_WIDTH/2-40,endY+3*rectHeight/4+3);
                }
            }
            else {
                if (team == 0) {
                    //landscapers won (not you)
                    drawText(largeText, "DEFEAT", ClientSettings.SCREEN_WIDTH/2-40, endY+3*rectHeight/4+3);
                }
                else {
                    //landscapers won (you)
                    drawText(largeText,"VICTORY",ClientSettings.SCREEN_WIDTH/2-50,endY+3*rectHeight/4+3);
                }
            }
        }

        //sets up team headers
        glColor4f(red, green, blue, intensity);
        drawRect(xStart, yStart, rectWidth, rectHeight);

        drawText(largeText, "Landscapers", xStart+10, yStart+3*rectHeight/4+3);
        drawText(largeText, ((Integer) sb.getTeam1Score()).toString(), xStart+rectWidth-((Integer) sb.getTeam1Score()).toString().length()*18-10, yStart+3*rectHeight/4+3);

        glColor4f(1, 0, 1, intensity);
        drawRect(xStart+rectWidth, yStart, rectWidth, rectHeight);
        drawText(largeText, "The Enclave", xStart+rectWidth+10, yStart+3*rectHeight/4+2);
        rectWidth *= 2;
        drawText(largeText, ((Integer) sb.getTeam0Score()).toString(), xStart+rectWidth-((Integer) sb.getTeam0Score()).toString().length()*18-10, yStart+3*rectHeight/4+3);

        yStart += rectHeight+buffer;

        //sets up sorting the scores
        int[] sortedScores = scores.clone();
        int removed = 0;
        while (removed < sortedScores.length) {
            int max = Integer.MIN_VALUE;
            int index = -1;
            for (int i = 0; i < sortedScores.length; i++) {
                if (sortedScores[i] > max) {
                    index = i;
                    max = sortedScores[i];
                }
            }
            if (index != -1) {
                if (gameData.getPlayer(index).getTeam() == 0) {
                    red = 1;
                    green = 0;
                    blue = 1;
                }
                else {
                    red = 0;
                    green = 1;
                    blue = 0;
                }
                //draws the rectangle in team colours
                glColor4f(red,green,blue, intensity);
                drawRect(xStart, yStart, rectWidth, rectHeight);

                //lists the names
                String name = gameData.getLobbyData().getPlayers()[index].getName().toString();
                if (gameData.getLobbyData().getPlayers()[index].isAI()) name += " (AI)";
                if (index == playerID) name += " (You)";


                drawText(smallText, name, xStart+10, yStart+3*rectHeight/4+1);
                drawText(smallText, ((Integer) max).toString(), xStart+rectWidth-((Integer) max).toString().length()*15-10, yStart+3*rectHeight/4+1);

                yStart+=rectHeight;

                //highlights if it's you or not
                if (index == playerID) {
                    invertedQuadGlow(xStart,yStart,rectWidth,rectHeight,10,1,1,1,1);
                }

                sortedScores[index] = Integer.MIN_VALUE;
            }
            else {
                break;
            }
            removed++;
        }
    }

    /**
     * Draws the lobby screen
     * @param ld the lobby data to show the lobby
     * @param map the map the display name of
     * @param playerID this player id
     * @param smallText the text renderer
     */
    static void drawLobby(LobbyData ld, Map map, int playerID, TextRenderer smallText) {

        //TextRenderer largeText = new TextRenderer(25);

        float intensity = 1f;
        glDisable(GL_TEXTURE_2D);

        float xStart = 800/6;
        float yStart = 150;
        float rectWidth = xStart*4;
        float rectHeight = 50;

        float red = 0;
        float green = 1;
        float blue = 0;
        float buffer = 20;


        glColor4f(red, green, blue, intensity);
        drawRect(xStart, yStart, rectWidth, rectHeight);

        InitPlayer[] players = ld.getPlayers();

        for (int i = 0; i < players.length; i++) {

            if (players[i].getTeam() == 0) {
                red = 1;
                green = 0;
                blue = 1;
            } else {
                red = 0;
                green = 1;
                blue = 0;
            }

            String name = players[i].getName().toString();
            if (players[i].isAI()) {
                name += " (Not connected: AI)";
                red *= 0.5f;
                green *= 0.5f;
                blue *= 0.5f;
            }
            if (i == playerID) name += " (You)";

            glColor4f(red,green,blue, intensity);
            drawRect(xStart, yStart, rectWidth, rectHeight);

            drawText(smallText, name, xStart+10, yStart+3*rectHeight/4+1);

            yStart+=rectHeight;

            if (i == playerID) {
                out("Drawing me");
                invertedQuadGlow(xStart,yStart,rectWidth,rectHeight,10,1,1,1,1);
            }

        }

        glEnable(GL_TEXTURE_2D);
    }


    /**
     * draws text in given position
     * @param tx the text renderer
     * @param s the string to draw
     * @param x left of the string
     * @param y the top of the string
     */
    private static void drawText(TextRenderer tx, String s, float x, float y) {
        drawText(tx, s, x, y, 0, 0, 0);
    }

    /**
     * draws text in given position
     * @param tx the text renderer
     * @param s the string to draw
     * @param x left of the string
     * @param y the top of the string
     * @param red the red component
     * @param green the green component
     * @param blue the blue component
     */
    private static void drawText(TextRenderer tx, String s, float x, float y, float red, float green, float blue) {
        glEnable(GL_TEXTURE_2D);
        glColor4f(red,green,blue,1);
        tx.drawText(s, x, y);
        glDisable(GL_TEXTURE_2D);
    }

    /**
     * Shades the screen in grey for use behind a scoreboard
     */
    private void shadeScreen() {
        glColor4f(1,1,1,0.3f);
        drawRect(0,0,width,height);
    }

    /**
     * colours the background of a map depending on the phase
     * @param phase which phase you're in
     */
    void colourBackground(int phase) {
        glColor4f(phase*0.1f,0,(1-phase)*0.1f,1f);
        drawRect(0,0,width,height);
    }

    /**
     * draws the heat bar to the right of the screen
     * @param heat how filled up the heat metre is
     * @param maxHeat the maximum heat there could be
     */
    void drawHeatBar(double heat, double maxHeat) {
        float heatBarSensitivity = 0.1f;
        if (heat != oldHeat) {
            oldHeat = heat;
        }

        //allows for a gliding motion
        double heatTick = heatBarSensitivity*(oldHeat-displayHeat);

        if (displayHeat > oldHeat && displayHeat+heatTick < oldHeat) {
            displayHeat = oldHeat;
        }
        else if (displayHeat < oldHeat && displayHeat+heatTick > oldHeat) {
            displayHeat = oldHeat;
        } else {
            displayHeat += heatTick;
        }

        if (displayHeat > maxHeat) displayHeat = maxHeat;
        else if (displayHeat < 0) displayHeat = 0;

        int heatWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float heatRatio = (float) (displayHeat/maxHeat);
        float green = (204f/255f)*(1-heatRatio);

        glColor3f(heatRatio, green, 0f);
        drawRect(width - (buffer+heatWidth), buffer+(1-heatRatio)*maxHeight, heatWidth, maxHeight*heatRatio);
    }

    /**
     * Draws the health bar
     * @param health the current health
     * @param maxHealth the max health
     */
    void drawHealthBar(double health, double maxHealth) {
        float healthBarSensitivity = 0.1f;
        if (health != oldHealth) {
            oldHealth = (int) health;
        }

        double healthTick = healthBarSensitivity*(oldHealth-displayHealth);

        if (displayHealth > oldHealth && displayHealth+healthTick < oldHealth) {
            displayHealth = oldHealth;
        }
        else if (displayHealth < oldHealth && displayHealth+healthTick > oldHealth) {
            displayHealth = oldHealth;
        } else {
            displayHealth += healthTick;
        }

        if (displayHealth > maxHealth) displayHealth = maxHealth;
        else if (displayHealth < 0) displayHealth = 0;

        if ((displayHealth-oldHealth) < 0.3 && (displayHealth-oldHealth) > -0.3) {
            displayHealth = oldHealth;
        }

        if (displayHealth != oldHealth) {
            //shows warnings to play that health is changing
            flashDamage((float) Math.min(1, Math.abs(healthTick)), (healthTick < 0));
            if (healthTick > 0) AudioManager.playHealthUp();
        }

        int healthWidth = 10;
        int buffer = 20;
        float maxHeight = height-buffer*2;
        float healthRatio = (float) (displayHealth/maxHealth);
        float green = (204f/255f)*healthRatio;
        glColor3f(1-healthRatio, green, 0f);
        drawRect(buffer, buffer+(1-healthRatio)*maxHeight, healthWidth, maxHeight*healthRatio);
    }

    /**
     * Draws a circle
     * @param cx centre of the circle
     * @param cy the centre of the circle (from the bottom)
     * @param r the radius of the circle
     * @param num_segments the number of segments, the more the smoother it looks
     */
    void drawCircle(float cx, float cy, float r, int num_segments) {
        float theta = (float) (2 * 3.1415926 / (num_segments));
        float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor
        float radial_factor = (float) Math.cos(theta);//calculate the radial factor

        float x = r;//we start at angle = 0
        float y = 0;

        glBegin(GL_TRIANGLE_FAN);

        for (int i = 0; i < num_segments; i++) {
            glVertex2f(x + cx, y + cy);//output vertex

            //calculate the tangential vector; remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them
            float tx = -y;
            float ty = x;

            //add the tangential vector
            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor
            x *= radial_factor;
            y *= radial_factor;
        }
        glEnd();
    }

    /**
     * Draws a rectangle
     * @param xStart the start of the rect from the left
     * @param yStart the start of the rect from the top
     * @param rectWidth the width of the rectangle
     * @param rectHeight the height of the rectange
     */
    private static void drawRect(float xStart, float yStart, float rectWidth, float rectHeight) {
        glBegin(GL_QUADS);
        glVertex2f(checkX(xStart), checkY(ClientSettings.SCREEN_HEIGHT - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(SCREEN_HEIGHT - yStart));
        glVertex2f(checkX(xStart + rectWidth), checkY(SCREEN_HEIGHT - (yStart+rectHeight)));
        glVertex2f(checkX(xStart), checkY(SCREEN_HEIGHT - (yStart+rectHeight)));
        glEnd();
    }

    /**
     * Keep x coordinate on the screen
     *
     * @param x Initial x coordinate
     * @return Checked x coordinate
     */
    private static float checkX(float x) {
        if (x < 0) x = 0;
        if (x > SCREEN_WIDTH) x = SCREEN_WIDTH;
        return x;
    }

    /**
     * Keep y coordinate on the screen
     *
     * @param y Initial y coordinate
     * @return Checked y coordinate
     */
    private static float checkY(float y) {
        if (y < 0) y = 0;
        if (y > SCREEN_HEIGHT) y = SCREEN_HEIGHT;
        return y;
    }

}
