package server.ai;

import server.game.WeaponSMG;
import server.game.WeaponShotgun;
import server.game.WeaponSniper;

import java.util.ArrayList;
import java.util.Random;

import static server.ai.Event.*;

/**
 * Facilitates simple randomised decision-making for a variety of contexts.
 * NOT YET OPERATIONAL.
 * Created by Rhys on 2/22/17.
 */
public class Chance {

    public enum chanceMode {WEAPON}
    private Random gen;
    private ArrayList<Event> weaponChoiceSet;


    /**
     * Constructs a general-purpose Chance object.
     */
    public Chance(){
        this.gen = new Random();

        this.weaponChoiceSet = new ArrayList<>();

        //THIS WON'T WORK. YOU CAN'T PASS EVERYONE THE SAME OBJECT.
    }

    /**
     * Determines whether or not an event with a pre-defined probability occurs.
     * @param event - the probability of the event occurring, out of 100.
     * @return true if the event should happen on this occasion.
     */
    public boolean decides(int event){
        return (gen.nextInt(100) <= event);
    }

    /**
     * Chooses between multiple options.
     * @param mode
     * @return
     *//*
    public Object choose(chanceMode mode){
        ArrayList<N> choiceSet = null;
        if (mode == chanceMode.WEAPON){
            choiceSet = weaponChoiceSet;
        }
        else {

        }

        for (int value : choiceSet){

        }
return choiceSet;

    } */


}

