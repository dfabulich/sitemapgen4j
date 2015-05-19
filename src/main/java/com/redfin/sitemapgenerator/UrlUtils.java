package com.redfin.sitemapgenerator;

import java.util.HashMap;

class UrlUtils {

	/**
     * Verify that a url matches a baseUrl
 	 * @param url
	 * @param baseUrl
	 * @throws InvalidURLException if the url does not match the baseUrl.
	 */

	static void checkUrl(String url, String baseUrl) throws InvalidURLException {
		// Is there a better test to use here?
		if (!url.startsWith(baseUrl)) {
			throw new InvalidURLException("Url " + url + " doesn't start with base URL " + baseUrl);
		}
	}

	static <K,V> HashMap<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
}
