## MQTT Sentinel

This is a RabbitMQ sentinel framework, implementing both RPC and Queue functionality.

For all messages received via Rabbit, on any of the channels Sentinel is listening, an `MQTTEvent` is fired via 
[Eventti](https://github.com/Fabricio20/Eventti).

Installation:
```gradle
compile group: 'net.notfab.sentinel', name: 'mqtt', version: '1.1'
```

Usage:
```java
// Initialize Sentinel as MailService
void initialize() {
    EventManager eventManager = new EventManager();
    MessageBroker broker = new MessageBroker("MailService", eventManager);
}

// Send a message to MailService and Receive a Response
void sendToMailService() {
    String message = broker.send("MailService", "add", "1");
}

// Send an ASYNC message to MailService and receive a Response
void sendToMailServiceAsync() {
    broker.sendAsync("MailService", "add", "1", (str) -> {
      // response in str
    });
    // TIP: To ignore the response, just don't pass a consumer (effectively queueing a task).
}

// If you need to listen on extra channels
void addExtraListeners() {
    MessageBroker broker = ...;
    broker.addListener("channelName");
}
```