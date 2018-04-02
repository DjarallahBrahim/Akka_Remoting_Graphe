package Node.SinglMachine;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.LinkedList;


public class NodeActor extends AbstractActor {

    private ArrayList<ActorRef> childrenNodes;
    private int idNode;
    private LinkedList<Integer> idMessagesList;

    public NodeActor(int idNode, ArrayList<ActorRef> childrenNodes) {
        this.idNode = idNode;
        this.childrenNodes = childrenNodes;
        this.idMessagesList = new LinkedList<>();
    }

    public NodeActor(int idNode) {
        this.idNode = idNode;
        this.childrenNodes = new ArrayList<ActorRef>();
        this.idMessagesList = new LinkedList<>();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NodeMessage.class , msg -> {
                    if (this.alreadyRead(msg)){
                        //getSender().tell("OK", getSender());
                        System.out.println("##########");
                        System.out.println("Message : " + msg.getMessage());
                        System.out.println("Sender : " + msg.getSender());
                        System.out.println("Node " + this.idNode + "Already recieved the message");
                        System.out.println("##########");
                        return;
                    }

                    System.out.println("----------");
                    System.out.println("Path : " + getSelf().path());
                    System.out.println("Message : " + msg.getMessage());
                    System.out.println("Sender : " + getSender().path().name());

                    System.out.println("Receiver : Node " + this.idNode);
                    System.out.println("----------");


                    for (ActorRef childNode : childrenNodes) {
                        childNode.forward(new NodeMessage(msg.getId_message(),msg.getMessage(),
                                self().path().name()),getContext());
                    }
//                    Replay to the parent "ACK"
                    //getSender().tell("OK", getSender());


                })
                .build();
    }


    /**
     * Méthode permettant de savoir si un message a déjà était lu par ce noeud
     *
     * @param message le message lu
     * @return Vrai si le message a déjà était reçu, faux sinon
     */
    private boolean alreadyRead(NodeMessage message) {
        if(this.idMessagesList.contains(message.getId_message())){
            return true;
        }else{
            if(this.idMessagesList.size()>10){
                this.idMessagesList.remove();
            }
            this.idMessagesList.add(message.getId_message());
            return false;
        }
    }

}
