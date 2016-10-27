package akka.tests.hello;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by master on 10/27/16.
 */
public class PeopleActor extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o) throws Throwable {
        log.info("From [{}]: [{}]", getSender().path().name(), o.toString());
    }
}
