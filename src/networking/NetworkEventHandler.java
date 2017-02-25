package networking;

import objects.Sendable;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static networking.Connection.out;

/**
 * This enables other classes to provide functionality for when a specific command is received.
 */
public class NetworkEventHandler implements Runnable {

    private HashMap<String, ArrayList<Consumer<Sendable>>> allConsumers = new HashMap<>();
    private List<Sendable> toExecute = Collections.synchronizedList(new ArrayList<>());

    private boolean isRunning;

    public void run() {
        isRunning = true;

        while (isRunning) {
            Sendable sendable;
            if ((sendable = popSendable()) != null) {
                String className = getClassName(sendable);
                ArrayList<Consumer<Sendable>> consumers = allConsumers.get(className);

                if (consumers != null) {
                    for (Consumer<Sendable> consumer : consumers) {
                        System.out.println("Working on: " + className);
                        consumer.accept(sendable);
                    }
                } else {
                    out("Network doesn't know how to handle the class: " + className);
                }
            }
        }
    }

    private Sendable popSendable() {
        if (!toExecute.isEmpty()) {
            Sendable sendable = toExecute.get(0);
            toExecute.remove(0);
            return sendable;
        } else {
            return null;
        }
    }

    private String getClassName(Sendable sendable) {
        String full = sendable.getClass().toString();
        String[] split = full.split("\\.");
        return split[split.length - 1];
    }

    /**
     * Adds a function to be executed whenever an object of the specified type is received.
     *
     * @param objName  Class name.
     * @param consumer Consumer which will exe.
     */
    public void addFunction(String objName, Consumer<Sendable> consumer) {
        ArrayList<Consumer<Sendable>> consumerList;
        if (allConsumers.containsKey(objName)) {
            consumerList = this.allConsumers.get(objName);
        } else {
            consumerList = new ArrayList<>();
        }
        consumerList.add(consumer);
        allConsumers.put(objName, consumerList);
    }

    public void queueForExecution(Sendable received) {
        toExecute.add(received);
        if (!this.isRunning) new Thread(this).start();
    }
}
