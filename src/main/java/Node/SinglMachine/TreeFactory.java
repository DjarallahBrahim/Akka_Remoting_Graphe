package Node.SinglMachine;


import akka.actor.*;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TreeFactory implements TreeFactoryItf {

    /**
     * the actor of the system
     */
    private ActorSystem tree = ActorSystem.create("system");
    /**
     * id of the root
     */
    private int racineId;

    /**
     * the root actor of the system
     */
    private ActorRef rootActor;

    /**
     *
     */
    private String pathFile ;

    /**
     * Constrector
     * @param tree
     */
    public TreeFactory(ActorSystem tree){
        this.tree = tree;
    }

    /**
     * Constrector
     * @param path
     */
    public TreeFactory(String path){
        this.pathFile = path;
    }

    /**
     * Constrector
     * @param tree
     */
    public TreeFactory(ActorSystem tree, String pathFile){
        this.tree = tree;
        this.pathFile = pathFile;
    }

    /**
     *
     */
    private ActorRef existeActor ;
    /**
     * @throws IOException
     * @throws JSONException
     */
    @Override
    public ActorRef actorPoolFactory() throws Exception {
        String JSON_DATA = new String(Files.readAllBytes(Paths.get(pathFile)), StandardCharsets.UTF_8);
        // using a JSON parser
        JSONObject obj = new JSONObject(JSON_DATA);
        return parseNode(obj, tree);
    }

    /**
     * created the Graphe/tree of actors
     *
     * @param obj
     * @param tree
     * @return the reference of an actor
     * @throws JSONException
     */
    private ActorRef parseNode(JSONObject obj, ActorSystem tree) throws Exception {
        if(!obj.isNull("host")){
            return tree.actorFor(obj.getString("host"));
        }
        int nodeId = Integer.parseInt(obj.getString("id"));
        List<JSONObject> children = parseJsonData(obj, "children");
        ArrayList<ActorRef> childrenActor = new ArrayList<>();
        for (JSONObject object : children) {
            childrenActor.add(parseNode(object, tree));
        }

        if (nodeId == this.racineId && !checkActor("node"+nodeId)) {
            this.rootActor = tree.actorOf(Props.create(NodeActor.class, nodeId, childrenActor), "node" + nodeId);
            return this.rootActor;
        } else if(!checkActor("node"+nodeId))
            return tree.actorOf(Props.create(NodeActor.class, nodeId, childrenActor), "node" + nodeId);
        else return this.existeActor;
    }

    /**
     * perseJsonData
     *
     * @param obj
     * @param pattern
     * @return List of JsonObject
     * @throws JSONException
     */
    @Override
    public List<JSONObject> parseJsonData(JSONObject obj, String pattern) throws JSONException {

        List<JSONObject> listObjs = new ArrayList<>();
        JSONArray geodata = obj.getJSONArray(pattern);
        for (int i = 0; i < geodata.length(); ++i) {
            final JSONObject site = geodata.getJSONObject(i);
            listObjs.add(site);
        }
        return listObjs;
    }

    /**
     * get the id of the racine
     *
     * @throws JSONException
     * @throws IOException
     */
    public void getRacineId() throws JSONException, IOException {
        String JSON_DATA = new String(Files.readAllBytes(Paths.get(ConfigFiles.configFile)), StandardCharsets.UTF_8);
        // using a JSON parser
        JSONObject obj = new JSONObject(JSON_DATA);
        this.racineId = obj.getInt("id_racine");
    }

    /**
     * get a reference of the rootActor
     *
     * @return
     */
    @Override
    public ActorRef getRootActor() {
        return rootActor;
    }

    /**
     * init system actors
     */
    @Override
    public void terminatTree() {
        this.tree.terminate();
        this.tree = ActorSystem.create("system");
    }

    /**
     * test if the actof of name nodeName exsit in system_Actors
     * @param nodeName
     * @return
     * @throws Exception
     */
    public boolean checkActor(final String nodeName) throws Exception {
        ActorSelection sel = tree.actorSelection("akka://system/user/"+nodeName);
        AskableActorSelection asker = new AskableActorSelection(sel);
        Future<Object> future = asker.ask(new Identify(1), new Timeout(5,
                TimeUnit.SECONDS));
        Timeout timeout = new Timeout(Duration.create(10, "seconds"));
        ActorIdentity identity = (ActorIdentity) Await.result(future, timeout.duration());
        this.existeActor = identity.getRef();

        return this.existeActor != null;
    }
}
