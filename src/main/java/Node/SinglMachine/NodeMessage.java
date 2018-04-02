package Node.SinglMachine;

import java.io.Serializable;

public class NodeMessage implements Serializable{

    private String message;
    private String sender;
    private int id_message;

    public NodeMessage(int id_message, String msg)
    {

        this.message = msg;
        this.id_message = id_message;
    }

    public NodeMessage(int id_message , String msg, String sender)
    {
        this.message = msg;
        this.sender = sender;
        this.id_message = id_message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public String getSender()
    {
        return this.sender;
    }

    public int getId_message()
    {
        return this.id_message;
    }
}
