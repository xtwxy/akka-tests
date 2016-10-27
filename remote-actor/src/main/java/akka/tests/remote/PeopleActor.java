package akka.tests.remote;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by master on 10/27/16.
 */
public class PeopleActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    ActorSelection greetingActor = getContext().actorSelection("akka.tcp://ServerSystem@192.168.0.78:2552/user/greetingActor");
    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("From [{}]: [{}]", getSender().path().name(), o.toString());
        if(!"greetingActor".equalsIgnoreCase(getSender().path().name())) {
            greetingActor.tell(o, getSelf());
        }
    }
}
