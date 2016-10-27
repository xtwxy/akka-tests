package akka.tests.remote;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by master on 10/27/16.
 */
public class Main {
    public static void main(String[] args) {
        if(args.length == 0) {
            usage();
        }
        if("client".equalsIgnoreCase(args[0])) {
            runClient();
        } else if("server".equalsIgnoreCase(args[0])) {
            runServer();
        } else {
            usage();
        }
    }

    private static void runClient() {
        ActorSystem serverSystem = ActorSystem.create("ClientSystem", ConfigFactory.load("client"));
        ActorRef peopleActor = serverSystem.actorOf(Props.create(PeopleActor.class), "peopleActor");
        while (true) {
            String line = System.console().readLine();
            peopleActor.tell(line, ActorRef.noSender());
        }
    }

    private static void runServer() {
        ActorSystem serverSystem = ActorSystem.create("ServerSystem", ConfigFactory.load("server"));
        ActorRef greetingActor = serverSystem.actorOf(Props.create(GreetingActor.class), "greetingActor");
    }

    private static void usage() {
        System.out.println("Usage: \n" +
                "<cmd> <[client|server]>");
    }
}
