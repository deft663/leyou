package com.github.wxpay.sdk;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.InputStream;

/**
 * @author zhang
 * @date 2019年04月30日 15:37
 */
public class PayConfig extends WXPayConfig {
    private String appID;
    private String mchID;
    private String key;
    private IWXPayDomain WXPayDomain;
    private int httpConnectTimeoutMs;
    private int httpReadTimeoutMs;
    private String notifyUrl;

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    @Override
    String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public void setMchID(String mchID) {
        this.mchID = mchID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setWXPayDomain(IWXPayDomain WXPayDomain) {
        this.WXPayDomain = WXPayDomain;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        // 这个方法需要这样实现, 否则无法正常初始化WXPay
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {

            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
        return iwxPayDomain;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return httpConnectTimeoutMs;
    }

    public void setHttpConnectTimeoutMs(int httpConnectTimeoutMs) {
        this.httpConnectTimeoutMs = httpConnectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return httpReadTimeoutMs;
    }

    public void setHttpReadTimeoutMs(int httpReadTimeoutMs) {
        this.httpReadTimeoutMs = httpReadTimeoutMs;
    }
}
