package server.ai.decision;

/**
 * This class contains figures that allow AI-controlled players to make pseudo-random
 * binary decisions and simulate human error.
 * Created by Rhys on 3/17/17.
 */
public final class AIConstants {

    public static final double MAX_TRAVEL_INACCURACY = 45;
    public static final double MAX_AIM_INACCURACY = 45;

    public static final double CHANCE_PHASE_SHIFT = 0.01;
    public static final double CHANCE_STRATEGIC_ERR = 0.4;
    public static final double CHANCE_STRATEGIC_RETHINK = 0.25;

    public static final int REACTION_TIME_HIGH = 10;
    public static final int REACTION_TIME_AVG = 9;
    public static final int REACTION_TIME_LOW = 8;

    public static final double STRESS_INTIMIDATED = 0.7;
    public static final double STRESS_IRRITATED = 0.8;
    public static final double STRESS_VENGEFUL = 0.2;
    public static final double STRESS_AGGRESSIVE_FROM_BORED = 0.5;
    public static final double STRESS_AGGRESSIVE = 0.5;
    public static final double STRESS_DETERMINED_FROM_BORED = 0.2;
    public static final double STRESS_DETERMINED = 0.3;
    public static final double STRESS_BORED = 0.05;

    public static final int PHASE_SHIFT_DELAY = 120;
    public static final int STRATEGY_RETHINK_DELAY = 120;
}
