package com.redfin.sitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

/**
 * One configurable Google News Search URL.  To configure, use {@link Options}
 * @author Dan Fabulich
 * @see Options
 * @see <a href="http://www.google.com/support/news_pub/bin/answer.py?answer=74288">Creating a News Sitemap with images</a>
 */
public class GoogleNewsWithImageSitemapUrl extends WebSitemapUrl {

	public enum AccessType {
		NONE("none"),
		SUBSCRIPTION("subscription"),
		REGISTRATION("registration");

		private final String name;
		AccessType(String name) {
			this.name = name;
		}

		/** The pretty name for this filetype */
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return this.name().toLowerCase();
		}
	}

	private final Date publicationDate;
	private final String keywords;
	private final String genres;
	private final String title;
	private final GoogleNewsPublication publication;
	private final String imageLocation;
	private final String imageTitle;
	private final AccessType accessType; //Subscription or Registration (if applicable).

	/** Options to configure Google News URLs */
	public static class Options extends AbstractSitemapUrlOptions<GoogleNewsWithImageSitemapUrl, Options> {
		private Date publicationDate;
		private String keywords;
		private String genres;
		private String title;
		private GoogleNewsPublication publication;
		private String imageLocation;
		private String imageTitle;
		private final AccessType accessType;

		/** Specifies an URL and publication date (which is mandatory for Google News) */
		public Options(String url, Date publicationDate, String title, GoogleNewsPublication publication, String imageLocation, String imageTitle, AccessType accessType) throws MalformedURLException {
			this(new URL(url), publicationDate, title, publication, imageLocation, imageTitle, accessType);
		}

		public Options(String url, Date publicationDate, String title, String name, String language, String imageLocation, String imageTitle, AccessType accessType) throws MalformedURLException {
			this(new URL(url), publicationDate, title, new GoogleNewsPublication(name, language), imageLocation, imageTitle, accessType);
		}

		public Options(URL url, Date publicationDate, String title, String name, String language, String imageLocation, String imageTitle, AccessType accessType) {
			this(url, publicationDate, title, new GoogleNewsPublication(name, language), imageLocation, imageTitle, accessType);
		}

		/** Specifies an URL and publication date (which is mandatory for Google News) */
		public Options(URL url, Date publicationDate, String title, GoogleNewsPublication publication, String imageLocation, String imageTitle, AccessType accessType) {
			super(url, GoogleNewsWithImageSitemapUrl.class);
			if (publicationDate == null) throw new NullPointerException("publicationDate must not be null");
			this.publicationDate = publicationDate;
			if (title == null) throw new NullPointerException("title must not be null");
			this.title = title;
			if (publication == null) throw new NullPointerException("publication must not be null");
			if (publication.getName() == null) throw new NullPointerException("publication name must not be null");
			if (publication.getLanguage() == null) throw new NullPointerException("publication language must not be null");
			this.publication = publication;
			this.imageLocation = imageLocation;
			this.imageTitle = imageTitle;
			this.accessType = accessType;
		}

		/** Specifies a list of comma-delimited keywords */
		public Options keywords(String keywords) {
			this.keywords = keywords;
			return this;
		}

		/** Specifies a list of comma-delimited keywords */
		public Options keywords(Iterable<String> keywords) {
			this.keywords = getListAsCommaSeparatedString(keywords);
			return this;
		}

		public Options genres(String genres) {
			this.genres = genres;
			return this;
		}

		public Options genres(Iterable<String> genres) {
			this.genres = getListAsCommaSeparatedString(genres);
			return this;
		}

		private String getListAsCommaSeparatedString(Iterable<String> values) {
			StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (String value : values) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(value);
			}
			return sb.toString();
		}

		/** Specifies a list of comma-delimited keywords */
		public Options keywords(String... keywords) {
			return keywords(Arrays.asList(keywords));
		}

		public Options genres(String... genres) {
			return genres(Arrays.asList(genres));
		}

	}

	/** Specifies an URL and publication date, title and publication (which are mandatory for Google News) */
	public GoogleNewsWithImageSitemapUrl(URL url, Date publicationDate, String title, String name, String language, String imageLocation, String imageTitle, AccessType accessType) {
		this(new Options(url, publicationDate, title, name, language, imageLocation, imageTitle, accessType));
	}

	/** Specifies an URL and publication date, title and publication (which are mandatory for Google News) */
	public GoogleNewsWithImageSitemapUrl(URL url, Date publicationDate, String title, GoogleNewsPublication publication, String imageLocation, String imageTitle, AccessType accessType) {
		this(new Options(url, publicationDate, title, publication, imageLocation, imageTitle, accessType));
	}

	/** Specifies an URL and publication date, title and publication (which are mandatory for Google News) */
	public GoogleNewsWithImageSitemapUrl(String url, Date publicationDate, String title, String name, String language, String imageLocation, String imageTitle, AccessType accessType) throws MalformedURLException {
		this(new Options(url, publicationDate, title, name, language, imageLocation, imageTitle, accessType));
	}

	/** Specifies an URL and publication date, title and publication (which are mandatory for Google News) */
	public GoogleNewsWithImageSitemapUrl(String url, Date publicationDate, String title, GoogleNewsPublication publication, String imageLocation, String imageTitle, AccessType accessType) throws MalformedURLException {
		this(new Options(url, publicationDate, title, publication, imageLocation, imageTitle, accessType));
	}

	/** Configures an URL with options */
	public GoogleNewsWithImageSitemapUrl(Options options) {
		super(options);
		publicationDate = options.publicationDate;
		keywords = options.keywords;
		genres = options.genres;
		title = options.title;
		publication = options.publication;
		imageLocation = options.imageLocation;
		imageTitle = options.imageTitle;
		accessType = options.accessType;
	}

	/** Retrieves the publication date */
	public Date getPublicationDate() {
		return publicationDate;
	}

	/** Retrieves the list of comma-delimited keywords */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Retrieves the Genres
	 */
	public String getGenres() {
		return genres;
	}

	/**
	 * Retrieves the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Retrieves the publication with name and language
	 */
	public GoogleNewsPublication getPublication() {
		return publication;
	}

	public String getImageLocation() { return imageLocation; }

	public String getImageTitle() { return imageTitle; }

	public AccessType getAccessType() { return accessType;
	}
}


