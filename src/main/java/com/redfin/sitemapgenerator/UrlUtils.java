package com.redfin.sitemapgenerator;

import java.net.URL;
import java.util.HashMap;

class UrlUtils {
	static String escapeXml(String string){
		return string
		.replaceAll("&", "&amp;")
		.replaceAll("'", "&apos;")
		.replaceAll("\"", "&quot;")
		.replaceAll(">", "&gt;")
		.replaceAll("<", "&lt;");
	}
	static void checkUrl(URL url, URL baseUrl) {
		// Is there a better test to use here?
		
		if (baseUrl.getHost() == null) {
			throw new RuntimeException("base URL is null");
		}
		
		if (!baseUrl.getHost().equalsIgnoreCase(url.getHost())) {
			throw new RuntimeException("Domain of URL " + url + " doesn't match base URL " + baseUrl);
		}
	}

	static <K,V> HashMap<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
}
