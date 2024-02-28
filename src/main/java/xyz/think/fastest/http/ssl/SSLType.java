package xyz.think.fastest.http.ssl;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.util.Objects;

/**
 *
 * @Date: 2020/12/31
 */

public enum SSLType {
    SSL(SSLUtil.SSLInstance("SSL"), SSLUtil.getTrustManager()),
    TLS(SSLUtil.SSLInstance("TLS"), SSLUtil.getTrustManager()),
    DEFAULT(null, null);

    private SSLSocketFactory sslSocketFactory;
    private final HostnameVerifier hostnameVerifier;
    private TrustManager[] trustManagers;
    private final X509TrustManager trustManager;

    SSLType(SSLContext sslContext, X509TrustManager trustManager){
        this.hostnameVerifier = SSLUtil.getHostnameVerifier();
        if(Objects.isNull(sslContext)){
            this.trustManager = SSLUtil.systemDefaultTrustManager();
            this.sslSocketFactory = SSLUtil.systemDefaultSslSocketFactory(this.trustManager);
        }else {
            this.trustManager = trustManager;
            try {

                sslContext.init(null, new X509TrustManager[]{this.trustManager}, new SecureRandom());
                this.sslSocketFactory = sslContext.getSocketFactory();
            } catch (Throwable ignored) {
            }
        }
    }

    public SSLSocketFactory getSslSocketFactory(){
        return this.sslSocketFactory;
    }

    public HostnameVerifier getHostnameVerifier(){
        return this.hostnameVerifier;
    }

    public X509TrustManager getTrustManager(){
        return this.trustManager;
    }
}
