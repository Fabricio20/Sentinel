package net.notfab.sentinel.sdk.example;

import net.notfab.sentinel.sdk.ExchangeType;
import net.notfab.sentinel.sdk.MessageBroker;

public class Start {

    public static void main(String[] args) {
        MessageBroker br = new MessageBroker();

        br.addListener(new ExampleListener(), ExchangeType.DIRECT, "sentinel.tests");
        br.addListener(new ExampleListener(), ExchangeType.QUEUE, "sentinel.tests");

        ExampleEvent event = new ExampleEvent();
        event.setTest(true);
        br.publish(event, ExchangeType.DIRECT, "sentinel.tests");
        event.setTest(false);
        br.publish(event, ExchangeType.QUEUE, "sentinel.tests");
        event.setTest(true);
        br.publish(event, ExchangeType.QUEUE, "sentinel.tests");
    }

}
