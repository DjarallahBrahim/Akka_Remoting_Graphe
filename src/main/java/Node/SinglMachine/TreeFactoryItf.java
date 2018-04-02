package Node.SinglMachine;

import akka.actor.ActorRef;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public interface TreeFactoryItf {
    List<JSONObject> parseJsonData(JSONObject obj, String pattern) throws JSONException;

    ActorRef actorPoolFactory() throws Exception;

    ActorRef getRootActor();

    void terminatTree();
}
