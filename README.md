# Sentinel

Sentinel is a **WORK-IN-PROGRESS** microservice-like SDK for Discord Bots inspired by [Fred's Sentinel](https://github.com/FredBoat/sentinel).

Example implementations are available in the `Examples` directory.

For a generic, non-discord implementation check out `SDK` (REDIS) and `MQTT` (RabbitMQ) directories.

### Design

The default Sentinel SDK is abstract, meaning you can use it outside of a Discord bot implementation.

Sentinel consists of two node types.

**Gateways**: These connect to Discord using any library of your liking (eg: JDA, D4J) and relay all events to the snetinel network.

**Workers**: These nodes receive tasks from the sentinel network and act on them. Workers are lib-agnostic.

A basic command has the following lifecycle:
```
WORKER: Register command "hello"
GATEWAY: Message Received
GATEWAY: Process & Detect Command + Prefix
GATEWAY: Does the network support command "hello"?
GATEWAY: Fires CommandEvent
WORKER: CommandEvent Received
WORKER: Processes "hello" command
```

**Footnote**: Commands live in queue exchanges, which means that you can have several _workers_ for the same command to have better response times
as well as allowing you to hotswap commands.