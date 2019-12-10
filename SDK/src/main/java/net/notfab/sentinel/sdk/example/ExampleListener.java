package net.notfab.sentinel.sdk.example;

import net.notfab.eventti.EventHandler;
import net.notfab.eventti.Listener;

public class ExampleListener implements Listener {

    @EventHandler
    public void onExample(ExampleEvent exampleEvent) {
        System.out.println("Received " + exampleEvent.isTest());
    }

}
