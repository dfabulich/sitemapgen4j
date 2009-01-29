package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * One configurable Google Mobile Search URL.  To configure, use {@link Options}
 * @author Dan Fabulich
 * @see Options
 * @see <a href="http://www.google.com/support/webmasters/bin/answer.py?answer=34648">Creating Mobile Sitemaps</a>
 */
public class GoogleMobileSitemapUrl extends WebSitemapUrl {

	/** Options to configure mobile URLs */
	public static class Options extends AbstractSitemapUrlOptions<GoogleMobileSitemapUrl, Options> {

		/** Specifies the url */
		public Options(String url) throws MalformedURLException {
			this(new URL(url));
		}
		
		/** Specifies the url */
		public Options(URL url) {
			super(url, GoogleMobileSitemapUrl.class);
		}
	}
	
	/** Specifies the url */
	public GoogleMobileSitemapUrl(String url) throws MalformedURLException {
		this(new Options(url));
	}

	/** Specifies the url */
	public GoogleMobileSitemapUrl(URL url) {
		this(new Options(url));
	}

	/** Specifies configures url with options */
	public GoogleMobileSitemapUrl(Options options) {
		super(options);
	}

}
