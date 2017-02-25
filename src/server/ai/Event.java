package server.ai;

public class Event {

    private Object obj;
    private int probability;

    public static final int CHOOSE_SHOTGUN = 40;
    public static final int CHOOSE_SMG = 45;
    public static final int CHOOSE_SNIPER = 15;

    public Event(Object obj, int probability){
        this.obj = obj;
        this.probability = probability;
    }

    public Object getObject(){
        return obj;
    }

    public int getProbability(){
        return probability;
    }
}
