package sasl.xmpp.client.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tigase.jaxmpp.core.client.SessionObject;
import tigase.jaxmpp.core.client.xmpp.modules.auth.SaslModule;
import tigase.jaxmpp.j2se.Jaxmpp;

public class MySaslAuthStartHandler implements SaslModule.SaslAuthStartHandler, SaslModule.SaslAuthSuccessHandler, SaslModule.SaslAuthFailedHandler {

    private static final Logger log = LogManager.getLogger(MySaslAuthStartHandler.class);

    private final Jaxmpp jaxmpp;

    public MySaslAuthStartHandler(Jaxmpp jaxmpp) {
        this.jaxmpp = jaxmpp;
    }

    @Override
    public void onAuthStart(SessionObject sessionObject, String mechanismName) {
        log.info("Auth start: {}, {}", sessionObject.getUserBareJid(), mechanismName);
    }

    @Override
    public void onAuthSuccess(SessionObject sessionObject) {
        log.info("Auth success: {}", sessionObject.getUserBareJid());
    }

    @Override
    public void onAuthFailed(SessionObject sessionObject, SaslModule.SaslError error) {
        log.info("Auth failed: {}, {}", sessionObject.getUserBareJid(), error);
    }
}