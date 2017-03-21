package server.ai.decision;

/**
 * This class contains figures that allow AI-controlled players to make pseudo-random
 * binary decisions and simulate human error.
 * Created by Rhys on 3/17/17.
 */
public final class AIConstants {

    public static final double MAX_TRAVEL_INACCURACY = 45;
    public static final double MAX_AIM_INACCURACY = 20;

    public static final double CHANCE_PHASE_SHIFT = 0.01;
    public static final double CHANCE_STRATEGIC_ERR = 0.4;
    public static final double CHANCE_STRATEGIC_RETHINK = 0.25;
    public static final double CHANCE_CORRECT_PHASE_SHIFT = 0.6;
    public static final double CHANCE_PURSUE_HEALTH = 0.5;

    public static final int REACTION_TIME_HIGH = 15;
    public static final int REACTION_TIME_AVG = 12;
    public static final int REACTION_TIME_LOW = 10;

    public static final double STRESS_INTIMIDATED = 0.7;
    public static final double STRESS_IRRITATED = 0.8;
    public static final double STRESS_AGGRESSIVE_FROM_BORED = 0.5;
    public static final double STRESS_AGGRESSIVE = 0.5;
    public static final double STRESS_DETERMINED_FROM_BORED = 0.2;
    public static final double STRESS_DETERMINED = 0.3;
    public static final double STRESS_BORED = 0.05;

    public static final int PHASE_SHIFT_DELAY = 120;
    public static final int STRATEGY_RETHINK_DELAY = 120;

    public static final float SNIPER_CEIL = 600;
    public static final float SMG_CEIL = 400;
    public static final float SHOTGUN_CEIL = 200;
    public static final float SHOTGUN_OPT = 75;

    public static final float WALL_WIDTH = 6;

    public static final double EASY_MULT = 0.7;
    public static final double MED_MULT = 1.0;
    public static final double HARD_MULT = 1.4;

    public static final int LOW_HEALTH_THRESHOLD = 30;
}
