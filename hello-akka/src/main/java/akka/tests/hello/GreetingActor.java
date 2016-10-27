package akka.tests.hello;

import akka.actor.UntypedActor;

/**
 * Created by master on 10/27/16.
 */
public class GreetingActor extends UntypedActor {
    @Override
    public void onReceive(Object o) throws Throwable {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello, ")
                .append(o.toString())
                .append("!");

        getSender().tell(sb.toString(), getSelf());
    }
}
