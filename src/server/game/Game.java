package server.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by peran on 27/01/17.
 */
public class Game {
    Timer t;
    int countdown;
    int tick = 60;

    public Game() {
        t = new Timer();
        countdown = 10*60*tick; //ten minutes

        int rate = 1000/60;

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                countdown--;

                //stops the countdown when the timer has run out
                if (countdown < 0) {
                    endGame();
                }
            }
        }, rate, rate);
    }

    public void endGame() {
        t.cancel();
        t.purge();
    }
}
