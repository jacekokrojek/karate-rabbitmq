package examples.rabbitmq;

import com.intuit.karate.junit5.Karate;

class SendRunner {
    
    @Karate.Test
    Karate testUsers() {
        return Karate.run("sendAndReceive").relativeTo(getClass());
    }    

}
