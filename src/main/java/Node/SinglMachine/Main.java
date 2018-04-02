package Node.SinglMachine;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.typesafe.config.Config ;

public class Main {
    public static void main(String[] args) throws Exception {
        String systemName="";
        String fileJson="";
        String port="";
        Config configSystem=null;
        if(args.length == 3){
            if(args[0].matches(".*_*(.*?)") && args[1].matches(".*_*(.*?)\\..*") && args[2].matches("[1-9][0-9]+")){
                System.out.println("Good");
                systemName = args[0];
                fileJson = args[1];
                port = args[2];
                configSystem = Main.createConfig(port);
            }else {
                System.out.println("Please use this Syntaxe <fileSystem> <fileName> <port>");
                return;
            }
        }else {
            System.out.println("Please use this Syntaxe <fileSystem> <fileName> <port>");
            return;
        }

        ActorSystem tree = ActorSystem.create(systemName , configSystem);

        TreeFactory factory = new TreeFactory(tree, fileJson);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Bienvenue sur l'application Akka.");
        String message = "";
        if (args.length > 0)
            System.out.println("Le noeud de départ est le noeud suivant : " +  args[0]);
        System.out.println("Veuillez saisir le message que vous souhaitez envoyer aux différents noeuds :");
        int id_message = 0;
        try {
            while ((message = br.readLine()) != null) {
                if ("QUIT".equals(message.toUpperCase()))
                    break;
                if("RESTART".equals(message.toUpperCase()))
                    Main.restart(factory);

                factory.getRacineId();
                factory.actorPoolFactory();
                ActorRef rootActor = factory.getRootActor();

                rootActor.tell(new NodeMessage(id_message,message,
                        rootActor.path().name()),rootActor);
                System.out.println("Veuillez saisir le message que vous souhaitez envoyer aux différents noeuds : ");
                id_message++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Merci d'avoir utilisé l'application.\nAu revoir.");

    }

    static void restart(final TreeFactory treeFactory){
        treeFactory.terminatTree();
        System.out.println("System actors has been RESTARED");

    }
    static Config createConfig(String port) {
        Map<String, Object> map = new HashMap<>();
        map.put("akka.actor.provider",   "akka.remote.RemoteActorRefProvider");
        map.put("akka.remote.transport", "akka.remote.netty.NettyRemoteTransport");
        map.put("akka.remote.netty.tcp.hostname", "127.0.0.1");
        map.put("akka.remote.netty.tcp.port", port);
        return ConfigFactory.parseMap(map);
    }

    static void testArgs(String [] args){
        if(args.length == 2){
            if(args[0].matches(".*_*(.*?)\\..*") && args[1].matches("[1-9][0-9]+")){

            }else {
                System.out.println("Please use this Syntaxe <fileName>? <port>");
            }
        }
    }

}
