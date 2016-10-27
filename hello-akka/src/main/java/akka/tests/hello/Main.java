package akka.tests.hello;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by master on 10/27/16.
 */
public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("GreetingSystem");
        ActorRef peopleActor = system.actorOf(Props.create(PeopleActor.class), "peopleActor");
        ActorRef greetingActor = system.actorOf(Props.create(GreetingActor.class), "greetingActor");

        for(String msg : args) {
            greetingActor.tell(msg, peopleActor);
        }

        system.terminate();
    }
}
