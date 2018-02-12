package com.hcll.fishshrimpcrab.common.http.ssl;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 * Http SSLContext
 * @author limin_he
 *
 */
public class HttpSSLContext {
	
	/**
	 * 取得伪造的SSLSocketFactory
	 * @return 伪造的SSLSocketFactory
	 */
	public static SSLSocketFactory getFakeSocketFactory() {
		try {
			TrustManager[] trustManagers = new TrustManager[] { new FakeX509TrustManager() }; 
			SSLContext sslContext = SSLContext.getInstance("TLS");
			// 初始化SSLContext实例 
	        sslContext.init(null, trustManagers, new SecureRandom());
	        return sslContext.getSocketFactory();
		}catch(Exception e) {
			Log.e(Const.TAG, e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 取得单向证书的SocketFactory
	 * @param context 上下文
	 * @param fileName Assets目录下面的证书文件路径
	 * @return SocketFactory
	 */
	public static SSLSocketFactory getSingleSocketFactoryByAssets(Context context, String fileName) {
		InputStream certStream = null;
		SSLSocketFactory socketFactory = null;
		try {
			certStream = context.getAssets().open(fileName);
			socketFactory = getSingleSocketFactory(certStream, KeyStore.getDefaultType(), "X.509", "TLS");
		} catch (IOException e) {
			Log.e(Const.TAG, e.getMessage(), e);
		}
		if (certStream != null) {
			try {
				certStream.close();
			} catch (IOException e) {
				Log.e(Const.TAG, e.getMessage(), e);
			}
			certStream = null;
		}
		return socketFactory;
	}
	
	/**
	 * 取得单向证书的SocketFactory
	 * @param certStream 证书内容
	 * @return SocketFactory
	 */
	public static SSLSocketFactory getSingleSocketFactory(InputStream certStream) {
		return getSingleSocketFactory(certStream, KeyStore.getDefaultType(), "X.509", "TLS");
	}
	/**
	 * 取得单向证书的SocketFactory
	 * @param certStream 证书内容
	 * @param keyStoreType keyStore类型
	 * @param certType 证书类型
	 * @param protocol Protocol
	 * @return SocketFactory
	 */
	public static SSLSocketFactory getSingleSocketFactory(InputStream certStream, String keyStoreType, String certType, String protocol) {
		try {
			// 此类表示密钥和证书的存储设施。 
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null);
			// 证书工厂。此处指明证书的类型     
			// 提供用于解析和管理证书、证书撤消列表 (CRL) 和证书路径的类和接口。
	        CertificateFactory certificateFactory = CertificateFactory.getInstance(certType);
	        // 根据传入的流类型，可以生成Cert实例，也可以生成CRL实例      
        	Certificate cert = certificateFactory.generateCertificate(certStream);
        	keyStore.setCertificateEntry("CertAlias", cert);
        	// 信任管理器
	        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); 
	        trustManagerFactory.init(keyStore);
	        // 获取一个SSLContext实例  
	        SSLContext sslContext = SSLContext.getInstance(protocol);
	        // 初始化SSLContext实例 
	        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
	        return sslContext.getSocketFactory();
		}catch(Exception e) {
			Log.e(Const.TAG, e.getMessage(), e);
		}
		return null;
	}
	
}
