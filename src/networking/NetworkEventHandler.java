package networking;

import objects.Sendable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import static networking.Connection.out;

/**
 * This enables other classes to provide functionality for when a specific command is received.
 */
public class NetworkEventHandler implements Runnable {

    private HashMap<String,ArrayList<Consumer<Sendable>>> allConsumers = new HashMap<>();
    private ArrayList<Sendable> toExecute = new ArrayList<>();

    private boolean running = false;

    public void run(){
        running = true;

        while(!toExecute.isEmpty()){
            Sendable sendable = popSendable();
            String className = getClassName(sendable);

            ArrayList<Consumer<Sendable>> consumers = this.allConsumers.get(className);
            if(consumers == null){
                out("Network doesn't know hoe to handle the class: "+className);
            }
            else {
                for (Consumer<Sendable> consumer : consumers) {
                    consumer.accept(sendable);
                }
            }
        }
        running = false;
    }

    private String getClassName(Sendable sendable) {
        String full = sendable.getClass().toString();
        String[] split = full.split("\\.");
        return split[split.length-1];
    }

    /**
     * This adds a command to a list of tasks, then tells the handler to run if it isn't already.
     * @param command Command to execute.
     */
    synchronized void queueForExecution(Sendable command){
        addSendable(command);

        if (!running) {
            new Thread(this).start();
        }
    }

    /**
     * Enables synchronised pushing to the list of commands.
     * @param command Command to execute.
     */
    synchronized private void addSendable(Sendable command){
        toExecute.add(command);
    }

    /**
     * Enables synchronised popping from the list of commands.
     * @return Command to execute.
     */
    synchronized private Sendable popSendable(){
        Sendable sendable = toExecute.get(0);
        toExecute.remove(0);
        return sendable;
    }

    /**
     * Adds a function to be executed whenever an object of the specified type is received.
     * @param objName Class name.
     * @param consumer Consumer which will exe.
     */
    public void addFunction(String objName, Consumer<Sendable> consumer){
        ArrayList<Consumer<Sendable>> consumerList;
        if(allConsumers.containsKey(objName)){
            consumerList = this.allConsumers.get(objName);
        } else {
            consumerList = new ArrayList<>();
        }
        consumerList.add(consumer);
        allConsumers.put(objName,consumerList);
    }
}
