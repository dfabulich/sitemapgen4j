package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds a sitemap for Google News.  To configure options, use {@link #builder(URL, File)}
 * @author Dan Fabulich
 * @see <a href="http://www.google.com/support/news_pub/bin/answer.py?answer=74288">Creating a News Sitemap</a>
 */
public class GoogleNewsSitemapGenerator extends SitemapGenerator<GoogleNewsSitemapUrl,GoogleNewsSitemapGenerator> {

	/** 1000 URLs max in a Google News sitemap. */
	public static final int MAX_URLS_PER_SITEMAP = 1000;
	
	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleNewsSitemapGenerator> builder(URL baseUrl, File baseDir) {
		SitemapGeneratorBuilder<GoogleNewsSitemapGenerator> builder = 
			new SitemapGeneratorBuilder<GoogleNewsSitemapGenerator>(baseUrl, baseDir, GoogleNewsSitemapGenerator.class);
		builder.maxUrls = 1000;
		return builder;
	}
	
	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleNewsSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
		SitemapGeneratorBuilder<GoogleNewsSitemapGenerator> builder = 
			new SitemapGeneratorBuilder<GoogleNewsSitemapGenerator>(baseUrl, baseDir, GoogleNewsSitemapGenerator.class);
		builder.maxUrls = GoogleNewsSitemapGenerator.MAX_URLS_PER_SITEMAP;
		return builder;
	}
	
	GoogleNewsSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
		if (options.maxUrls > GoogleNewsSitemapGenerator.MAX_URLS_PER_SITEMAP) {
			throw new RuntimeException("Google News sitemaps can have only 1000 URLs per sitemap: " + options.maxUrls);
		}
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public GoogleNewsSitemapGenerator(String baseUrl, File baseDir)
			throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public GoogleNewsSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	private static class Renderer extends AbstractSitemapUrlRenderer<GoogleNewsSitemapUrl> implements ISitemapUrlRenderer<GoogleNewsSitemapUrl> {

		public Class<GoogleNewsSitemapUrl> getUrlClass() {
			return GoogleNewsSitemapUrl.class;
		}

		public void render(GoogleNewsSitemapUrl url, OutputStreamWriter out,
				W3CDateFormat dateFormat) throws IOException {
			StringBuilder sb = new StringBuilder();
			sb.append("    <news:news>\n");
			renderTag(sb, "news", "publication_date", dateFormat.format(url.getPublicationDate()));
			renderTag(sb, "news", "keywords", url.getKeywords());
			sb.append("    </news:news>\n");
			super.render(url, out, dateFormat, sb.toString());
			
		}

		public String getXmlNamespaces() {
			return "xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\"";
		}
		
	}

}
