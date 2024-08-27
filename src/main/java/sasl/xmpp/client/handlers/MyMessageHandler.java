package sasl.xmpp.client.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.xmpp.modules.chat.Chat;
import tigase.jaxmpp.core.client.xmpp.modules.chat.MessageModule;
import tigase.jaxmpp.core.client.xmpp.stanzas.Message;
import tigase.jaxmpp.j2se.Jaxmpp;

public class MyMessageHandler implements MessageModule.MessageReceivedHandler, MessageModule.ChatCreatedHandler, MessageModule.ChatUpdatedHandler, MessageModule.ChatClosedHandler {

    private static final Logger log = LogManager.getLogger(MyMessageHandler.class);

    private final Jaxmpp jaxmpp;

    public MyMessageHandler(Jaxmpp jaxmpp) {
        this.jaxmpp = jaxmpp;
    }

    @Override
    public void onMessageReceived(SessionObject sessionObject, Chat chat, Message stanza) {
        log.info("Message received: {}, {}, {}", sessionObject.getUserBareJid(), chat == null ? null : chat.getId(), stanza);
    }

    @Override
    public void onChatCreated(SessionObject sessionObject, Chat chat, Message message) {
        log.info("Chat created: {}, {}, {}", sessionObject.getUserBareJid(), chat == null ? null : chat.getId(), message);
    }

    @Override
    public void onChatUpdated(SessionObject sessionObject, Chat chat) {
        log.info("Chat updated: {}, {}", sessionObject.getUserBareJid(), chat == null ? null : chat.getId());
    }

    @Override
    public void onChatClosed(SessionObject sessionObject, Chat chat) {
        log.info("Chat closed: {}, {}", sessionObject.getUserBareJid(), chat == null ? null : chat.getId());
    }
}