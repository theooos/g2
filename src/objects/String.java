package objects;

/**
 * Created by theooos on 22/01/2017.
 */
public class String implements Sendable {

    private java.lang.String message;

    public String(java.lang.String message){
        this.message = message;
    }

    public java.lang.String toString(){
        return message;
    }
}
