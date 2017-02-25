package networking;

import objects.String;

/**
 * Created by theo on 24/02/2017.
 */
public class SendingTester implements Runnable {

    Connection conn;

    public SendingTester(Connection conn){
        this.conn = conn;
    }

    public void run(){
        while(conn == null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        while(true){
            conn.send(new String("Communication is still going!"));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
