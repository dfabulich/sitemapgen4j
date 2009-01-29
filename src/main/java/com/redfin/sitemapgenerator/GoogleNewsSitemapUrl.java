package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

/**
 * One configurable Google News Search URL.  To configure, use {@link Options}
 * @author Dan Fabulich
 * @see Options
 * @see <a href="http://www.google.com/support/news_pub/bin/answer.py?answer=74288">Creating a News Sitemap</a>
 */
public class GoogleNewsSitemapUrl extends WebSitemapUrl {

	private final Date publicationDate;
	private final String keywords;
	
	/** Options to configure Google News URLs */
	public static class Options extends AbstractSitemapUrlOptions<GoogleNewsSitemapUrl, Options> {
		private Date publicationDate;
		private String keywords;
	
		/** Specifies an URL and publication date (which is mandatory for Google News) */
		public Options(String url, Date publicationDate) throws MalformedURLException {
			this(new URL(url), publicationDate);
		}
		
		/** Specifies an URL and publication date (which is mandatory for Google News) */
		public Options(URL url, Date publicationDate) {
			super(url, GoogleNewsSitemapUrl.class);
			if (publicationDate == null) throw new NullPointerException("publicationDate must not be null");
			this.publicationDate = publicationDate;
		}
		
		/** Specifies a list of comma-delimited keywords */
		public Options keywords(String keywords) {
			this.keywords = keywords;
			return this;
		}
		
		/** Specifies a list of comma-delimited keywords */
		public Options keywords(Iterable<String> keywords) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (String keyword : keywords) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(keyword);
			}
			this.keywords = sb.toString();
			return this;
		}
		
		/** Specifies a list of comma-delimited keywords */
		public Options keywords(String... keywords) {
			return keywords(Arrays.asList(keywords));
		}
		
	}
	
	/** Specifies an URL and publication date (which is mandatory for Google News) */
	public GoogleNewsSitemapUrl(URL url, Date publicationDate) {
		this(new Options(url, publicationDate));
	}
	
	/** Specifies an URL and publication date (which is mandatory for Google News) */
	public GoogleNewsSitemapUrl(String url, Date publicationDate) throws MalformedURLException {
		this(new Options(url, publicationDate));
	}

	/** Configures an URL with options */
	public GoogleNewsSitemapUrl(Options options) {
		super(options);
		publicationDate = options.publicationDate;
		keywords = options.keywords;
	}

	/** Retrieves the publication date */
	public Date getPublicationDate() {
		return publicationDate;
	}

	/** Retrieves the list of comma-delimited keywords */
	public String getKeywords() {
		return keywords;
	}




}
