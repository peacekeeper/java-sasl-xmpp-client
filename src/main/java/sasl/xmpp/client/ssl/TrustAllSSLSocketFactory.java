package sasl.xmpp.client.ssl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustAllSSLSocketFactory {

    private static final Logger log = LogManager.getLogger(TrustAllSSLSocketFactory.class);

    public static SSLSocketFactory getSSLSocketFactory() {

        TrustManager localTrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                log.debug("X509TrustManager#getAcceptedIssuers");
                return new X509Certificate[]{};
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                log.debug("X509TrustManager#checkServerTrusted");
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                log.debug("X509TrustManager#checkClientTrusted");
            }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { localTrustManager }, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}