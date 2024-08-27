# java-sasl-xmpp-client

This repository is one component of the project "Securing Internet protocols with DIDs, using SASL",
see https://github.com/peacekeeper/did-based-sasl for an overview and list of all components.

## Description

This repository contains a configuration of the [JaXMPP library](https://github.com/tigase/jaxmpp), with added support for the
DID-based SASL authentication mechanism described and implemented in https://github.com/peacekeeper/java-sasl-did-mechanism.

The custom SASL mechanism and configuration has been added as described in https://github.com/tigase/jaxmpp/tree/master/jaxmpp-core/src/main/java/tigase/jaxmpp/core/client/xmpp/modules/auth

- A custom Java Security Provider has been added that registers the DID-based SASL `SaslClientFactory` for the `DID-CHALLENGE` mechanism:
  - https://github.com/peacekeeper/java-sasl-xmpp-client/blob/main/src/main/resources/META-INF/java.security.Provider
  - https://github.com/peacekeeper/java-sasl-did-mechanism/blob/main/src/main/java/sasl/did/mechanism/DidSaslProvider.java

## Run

```
mvn clean install exec:java
```
