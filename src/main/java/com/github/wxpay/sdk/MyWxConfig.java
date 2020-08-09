package com.github.wxpay.sdk;

import java.io.InputStream;

public class MyWxConfig extends WXPayConfig {

    @Override
    //微信公众号的ID
    public String getAppID() {
        return "wxa1e44e130a9a8eee";
    }

    //微信支付商户号
    @Override
    public String getMchID() {
        return "1507758211";
    }

    //微信支付密钥
    @Override
    public String getKey() {
        return "feihujiaoyu12345678yuxiaoyang123";
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
