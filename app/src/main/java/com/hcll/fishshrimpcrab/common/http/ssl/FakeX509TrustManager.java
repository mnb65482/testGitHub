package com.hcll.fishshrimpcrab.common.http.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * 伪造X.509信任管理
 * @author hWX201806
 *
 */
public class FakeX509TrustManager implements X509TrustManager {

	/**
	 * 证书数组
	 */
	private static final X509Certificate[] mCertificates = new X509Certificate[] {}; 
	 
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return mCertificates;
	} 

}
