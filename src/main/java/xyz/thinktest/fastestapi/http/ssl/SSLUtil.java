package xyz.thinktest.fastestapi.http.ssl;

import xyz.thinktest.fastestapi.common.exceptions.FastestBasicException;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 *SSL工具类
 * @Date: 2020/12/31
 */

public final class SSLUtil {

    private SSLUtil(){}

    //获取HostnameVerifier
    public static HostnameVerifier getHostnameVerifier() {
        return (s, sslSession) -> true;
    }

    public static X509TrustManager getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
        return (X509TrustManager) trustAllCerts[0];
    }

    public static SSLContext SSLInstance(String type){
        try {
            return SSLContext.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No "+type+" provider", e);
        }
    }

    public static SSLSocketFactory systemDefaultSslSocketFactory(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new FastestBasicException("No System TLS", e); // The system has no TLS. Just give up.
        }
    }

    public static X509TrustManager systemDefaultTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            //传NULL表示信任任何证书。
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new FastestBasicException("No System TLS", e); // The system has no TLS. Just give up.
        }
    }
}
