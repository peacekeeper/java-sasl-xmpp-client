package sasl.xmpp.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sasl.xmpp.client.handlers.MyConnectionHandler;
import sasl.xmpp.client.handlers.MyMessageHandler;
import sasl.xmpp.client.handlers.MyMucHandler;
import sasl.xmpp.client.ssl.TrustAllSSLSocketFactory;
import tigase.jaxmpp.core.client.BareJID;
import tigase.jaxmpp.core.client.Connector;
import tigase.jaxmpp.core.client.JID;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.xmpp.modules.auth.SaslMechanism;
import tigase.jaxmpp.core.client.xmpp.modules.auth.SaslModule;
import tigase.jaxmpp.core.client.xmpp.modules.chat.MessageModule;
import tigase.jaxmpp.core.client.xmpp.modules.muc.MucModule;
import tigase.jaxmpp.j2se.Jaxmpp;
import tigase.jaxmpp.j2se.connection.ConnectionManager;
import tigase.jaxmpp.j2se.connectors.socket.SocketConnector;

import java.util.concurrent.TimeUnit;

public class SaslXmppClient {

    private static final Logger log = LogManager.getLogger(SaslXmppClient.class);

    private static final MyConnectionHandler MY_CONNECTION_HANDLER = new MyConnectionHandler();
    private static final MyMessageHandler MY_MESSAGE_HANDLER = new MyMessageHandler();
    private static final MyMucHandler MY_MUC_HANDLER = new MyMucHandler();

    public static void main(String[] args) throws Exception {

        final Jaxmpp jaxmpp = new Jaxmpp();

        jaxmpp.getConnectionConfiguration().setServer("localhost");
        jaxmpp.getConnectionConfiguration().setPort(5222);
        jaxmpp.getConnectionConfiguration().setUseSASL(true);
        jaxmpp.getConnectionConfiguration().setDisableTLS(false);

        jaxmpp.getProperties().setUserProperty(SessionObject.DOMAIN_NAME, "vienna2");
        jaxmpp.getProperties().setUserProperty(SessionObject.USER_BARE_JID, BareJID.bareJIDInstance("alice@vienna2"));
        jaxmpp.getProperties().setUserProperty(SessionObject.PASSWORD, "tigase");
        jaxmpp.getProperties().setUserProperty(SocketConnector.SSL_SOCKET_FACTORY_KEY, TrustAllSSLSocketFactory.getSSLSocketFactory());

        tigase.jaxmpp.j2se.Presence.initialize(jaxmpp);

        log.debug("Adding modules...");
        addModules(jaxmpp);
        MessageModule messageModule = jaxmpp.getModule(MessageModule.class);
        SaslModule saslModule = jaxmpp.getModule(SaslModule.class);
        log.debug("Mechanisms: {}", saslModule.getMechanismsOrder().get(0));
        saslModule.addMechanism(SaslMechanism);

        log.debug("Adding handlers...");
        addHandlers(jaxmpp);

        log.debug("Logging in...");
        jaxmpp.login(true);

        for (int i=0; i<10; i++) {
            log.debug("Connected: {}", jaxmpp.isConnected());
            if (jaxmpp.isConnected()) {
                messageModule.sendMessage(JID.jidInstance("bob@vienna2"), "Test", "This is a test from alice@vienna2");
            }
            TimeUnit.SECONDS.sleep(5);
        }

        jaxmpp.disconnect();
    }

    private static void addModules(Jaxmpp jaxmpp) {
        jaxmpp.getModulesManager().register(new MessageModule());
        jaxmpp.getModulesManager().register(new SaslModule());
    }

    private static void addHandlers(Jaxmpp jaxmpp) {
        jaxmpp.getEventBus().addHandler(Connector.ConnectedHandler.ConnectedEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(Connector.DisconnectedHandler.DisconnectedEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(Connector.ErrorHandler.ErrorEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionEstablishedHandler.ConnectionEstablishedEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionClosedHandler.ConnectionClosedEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionFailedHandler.ConnectionFailedEvent.class, MY_CONNECTION_HANDLER);
        jaxmpp.getEventBus().addHandler(MessageModule.MessageReceivedHandler.MessageReceivedEvent.class, MY_MESSAGE_HANDLER);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatCreatedHandler.ChatCreatedEvent.class, MY_MESSAGE_HANDLER);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatUpdatedHandler.ChatUpdatedEvent.class, MY_MESSAGE_HANDLER);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatClosedHandler.ChatClosedEvent.class, MY_MESSAGE_HANDLER);
        jaxmpp.getEventBus().addHandler(MucModule.MucMessageReceivedHandler.MucMessageReceivedEvent.class, MY_MUC_HANDLER);
        jaxmpp.getEventBus().addHandler(MucModule.MessageErrorHandler.MessageErrorEvent.class, MY_MUC_HANDLER);
        jaxmpp.getEventBus().addHandler(MucModule.PresenceErrorHandler.PresenceErrorEvent.class, MY_MUC_HANDLER);
        jaxmpp.getEventBus().addHandler(MucModule.StateChangeHandler.StateChangeEvent.class, MY_MUC_HANDLER);
    }
}