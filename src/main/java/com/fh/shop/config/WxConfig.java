package com.fh.shop.config;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;

import java.io.InputStream;

public class WxConfig extends WXPayConfig {

    public String getAppID() {
        return "wxa1e44e130a9a8eee";
    }

    public String getMchID() {
        return "1507758211";
    }

    public String getKey() {
        return "feihujiaoyu12345678yuxiaoyang123";
    }

    @Override
    protected InputStream getCertStream() {
        return null;
    }

    @Override
    protected IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API,true);
            }
        };
    }


}
