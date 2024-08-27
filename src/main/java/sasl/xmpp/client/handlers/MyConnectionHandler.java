package sasl.xmpp.client.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tigase.jaxmpp.core.client.Connector;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.connector.StreamError;
import tigase.jaxmpp.core.client.xmpp.modules.connection.ConnectionSession;
import tigase.jaxmpp.j2se.connection.ConnectionManager;

import java.net.Socket;

public class MyConnectionHandler implements Connector.ConnectedHandler, Connector.DisconnectedHandler, Connector.ErrorHandler, ConnectionManager.ConnectionEstablishedHandler, ConnectionManager.ConnectionClosedHandler, ConnectionManager.ConnectionFailedHandler {

    private static final Logger log = LogManager.getLogger(MyConnectionHandler.class);

    @Override
    public void onConnected(SessionObject sessionObject) {
        log.info("Connected: {}", sessionObject.getUserBareJid());
    }

    @Override
    public void onDisconnected(SessionObject sessionObject) {
        log.info("Disconnected: {}", sessionObject.getUserBareJid());
    }

    @Override
    public void onError(SessionObject sessionObject, StreamError condition, Throwable caught) {
        log.error("Error: {}, {}, {}", sessionObject.getUserBareJid(), condition == null ? null : condition.getElementName(), caught == null ? null : caught.getMessage());
    }

    @Override
    public void onConnectionEstablished(SessionObject sessionObject, ConnectionSession connectionSession, Socket socket) {
        log.info("Connection established: {}, {}, {}, {}", sessionObject.getUserBareJid(), connectionSession == null ? null : connectionSession.getSid(), connectionSession == null ? null : connectionSession.getPeer(), socket == null ? null : socket);
    }

    @Override
    public void onConnectionClosed(SessionObject sessionObject) {
        log.info("Connection closed: {}", sessionObject.getUserBareJid());
    }

    @Override
    public void onConnectionFailed(SessionObject sessionObject, ConnectionSession connectionSession) {
        log.info("Connection failed: {}, {}, {}", sessionObject.getUserBareJid(), connectionSession == null ? null : connectionSession.getSid(), connectionSession == null ? null : connectionSession.getPeer());
    }
}