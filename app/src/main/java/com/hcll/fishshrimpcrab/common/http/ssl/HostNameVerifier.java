package com.hcll.fishshrimpcrab.common.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class HostNameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
//		if(BuildConfig.DEBUG) {
//			return true;
//		} else {
//			return false;
//		}
        return false;
    }

}
