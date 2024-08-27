package sasl.xmpp.client.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.xmpp.modules.muc.MucModule;
import tigase.jaxmpp.core.client.xmpp.modules.muc.Room;
import tigase.jaxmpp.core.client.xmpp.stanzas.Message;
import tigase.jaxmpp.core.client.xmpp.stanzas.Presence;

import java.util.Date;

public class MyMucHandler implements MucModule.MucMessageReceivedHandler, MucModule.MessageErrorHandler, MucModule.PresenceErrorHandler, MucModule.StateChangeHandler {

    private static final Logger log = LogManager.getLogger(MyMucHandler.class);

    @Override
    public void onMucMessageReceived(SessionObject sessionObject, Message message, Room room, String nickname, Date timestamp) {
        log.info("Muc message received: {}, {}, {}, {}, {}", sessionObject.getUserBareJid(), message, room, nickname, timestamp);
    }

    @Override
    public void onMessageError(SessionObject sessionObject, Message message, Room room, String nickname, Date timestamp) {
        log.info("Message error: {}, {}, {}, {}, {}", sessionObject.getUserBareJid(), message, room, nickname, timestamp);
    }

    @Override
    public void onPresenceError(SessionObject sessionObject, Room room, Presence presence, String nickname) {
        log.info("Presence error: {}, {}, {}, {}", sessionObject.getUserBareJid(), room, presence, nickname);
    }

    @Override
    public void onStateChange(SessionObject sessionObject, Room room, Room.State oldState, Room.State newState) {
        log.info("State change: {}, {}, {}, {}", sessionObject.getUserBareJid(), room, oldState, newState);
    }
}