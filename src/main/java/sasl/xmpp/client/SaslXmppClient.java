package sasl.xmpp.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sasl.xmpp.client.handlers.MyConnectionHandler;
import sasl.xmpp.client.handlers.MyMessageHandler;
import sasl.xmpp.client.handlers.MyMucHandler;
import sasl.xmpp.client.handlers.MySaslAuthStartHandler;
import sasl.xmpp.client.ssl.TrustAllSSLSocketFactory;
import tigase.jaxmpp.core.client.BareJID;
import tigase.jaxmpp.core.client.Connector;
import tigase.jaxmpp.core.client.JID;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.xmpp.modules.auth.SaslModule;
import tigase.jaxmpp.core.client.xmpp.modules.chat.MessageModule;
import tigase.jaxmpp.core.client.xmpp.modules.muc.MucModule;
import tigase.jaxmpp.j2se.Jaxmpp;
import tigase.jaxmpp.j2se.connection.ConnectionManager;
import tigase.jaxmpp.j2se.connectors.socket.SocketConnector;

import java.util.concurrent.TimeUnit;

public class SaslXmppClient {

    private static final Logger log = LogManager.getLogger(SaslXmppClient.class);

    private static final String DOMAIN_NAME = "vienna2";
    private static final BareJID USER_BARE_JID = BareJID.bareJIDInstance("alice@" + DOMAIN_NAME);
    private static final String PASSWORD = "tigase";

    public static void main(String[] args) throws Exception {

        final Jaxmpp jaxmpp = new Jaxmpp();

        jaxmpp.getConnectionConfiguration().setServer("localhost");
        jaxmpp.getConnectionConfiguration().setPort(5222);
        jaxmpp.getConnectionConfiguration().setUseSASL(true);
        jaxmpp.getConnectionConfiguration().setDisableTLS(false);

        jaxmpp.getProperties().setUserProperty(SessionObject.DOMAIN_NAME, DOMAIN_NAME);
        jaxmpp.getProperties().setUserProperty(SessionObject.USER_BARE_JID, USER_BARE_JID);
        jaxmpp.getProperties().setUserProperty(SessionObject.PASSWORD, PASSWORD);
        jaxmpp.getProperties().setUserProperty(SocketConnector.SSL_SOCKET_FACTORY_KEY, TrustAllSSLSocketFactory.getSSLSocketFactory());

        tigase.jaxmpp.j2se.Presence.initialize(jaxmpp);

        log.debug("Adding modules...");
        addModules(jaxmpp);
        MessageModule messageModule = jaxmpp.getModule(MessageModule.class);
        SaslModule saslModule = jaxmpp.getModule(SaslModule.class);

        log.debug("Adding handlers...");
        addHandlers(jaxmpp);

        log.debug("Logging in...");
        jaxmpp.login(true);

        for (int i=0; i<10; i++) {
            log.debug("Connected: {}", jaxmpp.isConnected());
            if (jaxmpp.isConnected()) {
                messageModule.sendMessage(JID.jidInstance("bob@" + DOMAIN_NAME), "Test", "This is a test from alice");
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
        MyConnectionHandler myConnectionHandler = new MyConnectionHandler(jaxmpp);
        MyMessageHandler myMessageHandler = new MyMessageHandler(jaxmpp);
        MyMucHandler myMucHandler = new MyMucHandler(jaxmpp);
        MySaslAuthStartHandler mySaslAuthStartHandler = new MySaslAuthStartHandler(jaxmpp);
        jaxmpp.getEventBus().addHandler(Connector.ConnectedHandler.ConnectedEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(Connector.DisconnectedHandler.DisconnectedEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(Connector.ErrorHandler.ErrorEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionEstablishedHandler.ConnectionEstablishedEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionClosedHandler.ConnectionClosedEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(ConnectionManager.ConnectionFailedHandler.ConnectionFailedEvent.class, myConnectionHandler);
        jaxmpp.getEventBus().addHandler(MessageModule.MessageReceivedHandler.MessageReceivedEvent.class, myMessageHandler);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatCreatedHandler.ChatCreatedEvent.class, myMessageHandler);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatUpdatedHandler.ChatUpdatedEvent.class, myMessageHandler);
        jaxmpp.getEventBus().addHandler(MessageModule.ChatClosedHandler.ChatClosedEvent.class, myMessageHandler);
        jaxmpp.getEventBus().addHandler(MucModule.MucMessageReceivedHandler.MucMessageReceivedEvent.class, myMucHandler);
        jaxmpp.getEventBus().addHandler(MucModule.MessageErrorHandler.MessageErrorEvent.class, myMucHandler);
        jaxmpp.getEventBus().addHandler(MucModule.PresenceErrorHandler.PresenceErrorEvent.class, myMucHandler);
        jaxmpp.getEventBus().addHandler(MucModule.StateChangeHandler.StateChangeEvent.class, myMucHandler);
        jaxmpp.getEventBus().addHandler(SaslModule.SaslAuthStartHandler.SaslAuthStartEvent.class, mySaslAuthStartHandler);
        jaxmpp.getEventBus().addHandler(SaslModule.SaslAuthSuccessHandler.SaslAuthSuccessEvent.class, mySaslAuthStartHandler);
        jaxmpp.getEventBus().addHandler(SaslModule.SaslAuthFailedHandler.SaslAuthFailedEvent.class, mySaslAuthStartHandler);
    }
}