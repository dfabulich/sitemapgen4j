package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * Encapsulates a single URL to be inserted into a Web sitemap (as opposed to a Geo sitemap, a Mobile sitemap, a Video sitemap, etc which are Google specific).
 * Specifying a lastMod, changeFreq, or priority is optional; you specify those by using an Options object.
 * 
 * @see Options
 * @author Dan Fabulich
 *
 */
public class WebSitemapUrl implements ISitemapUrl {
	private final URL url;
	private final Date lastMod;
	private final ChangeFreq changeFreq;
	private final Double priority;
	
	/** Encapsulates a single simple URL */
	public WebSitemapUrl(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	/** Encapsulates a single simple URL */
	public WebSitemapUrl(URL url) {
		this.url = url;
		this.lastMod = null;
		this.changeFreq = null;
		this.priority = null;
	}
	
	/** Creates an URL with configured options */
	public WebSitemapUrl(Options options) {
		this((AbstractSitemapUrlOptions<?,?>)options);
	}
	
	WebSitemapUrl(AbstractSitemapUrlOptions<?,?> options) {
		this.url = options.url;
		this.lastMod = options.lastMod;
		this.changeFreq = options.changeFreq;
		this.priority = options.priority;
	}
	
	/** Retrieves the {@link Options#lastMod(Date)} */
	public Date getLastMod() { return lastMod; }
	/** Retrieves the {@link Options#changeFreq(ChangeFreq)} */
	public ChangeFreq getChangeFreq() { return changeFreq; }
	/** Retrieves the {@link Options#priority(Double)} */
	public Double getPriority() { return priority; }
	/** Retrieves the url */
	public URL getUrl() { return url; }
	
	/** Options to configure web sitemap URLs */
	public static class Options extends AbstractSitemapUrlOptions<WebSitemapUrl, Options> {

		/** Configure this URL */
		public Options(String url)throws MalformedURLException {
			this(new URL(url));
		}

		/** Configure this URL */
		public Options(URL url) {
			super(url, WebSitemapUrl.class);
		}
		
	}
}
