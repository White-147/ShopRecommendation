package com.briup.shop.conf;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AliPayConfig {
	private static final String SERVER_URL = config("ALIPAY_SERVER_URL", "https://openapi-sandbox.dl.alipaydev.com/gateway.do");
	private static final String APP_ID = config("ALIPAY_APP_ID", "");
	private static final String APP_PRIVATE_KEY = config("ALIPAY_APP_PRIVATE_KEY", "");
	private static final String FORMAT = "json";
	private static final String CHARSET = "utf-8";
	private static final String ALIPAY_PUBLIC_KEY = config("ALIPAY_PUBLIC_KEY", "");
	private static final String SIGN_TYPE = "RSA2";

	public static AlipayClient getAlipayClient() {
		return new DefaultAlipayClient(SERVER_URL, APP_ID, APP_PRIVATE_KEY, FORMAT,
				CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
	}

	private static String config(String name, String defaultValue) {
		String value = System.getenv(name);
		return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
	}
}
