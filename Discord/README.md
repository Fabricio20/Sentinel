## Network Channels
These are the network channels used by the Discord Extension.

### Hub Channels
These channels are DIRECT exchanges which receive the below events as soon as they happen.
- `Sentinel:Hub:Messenger`: MessengerEvent
- `Sentinel:Hub:Commands`: CommandRegisterEvent and CommandUnregisterEvent

### Registries
These channels are registries used for data storage, like command mappings.
- `Sentinel:Registry:Commands`: HashSet of available network commands.