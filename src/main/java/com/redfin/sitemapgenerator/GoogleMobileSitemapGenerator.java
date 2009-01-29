package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds a Google Mobile Sitemap, consisting of only mobile-friendly content.  To configure options, use {@link #builder(URL, File)}
 * @author Dan Fabulich
 * @see <a href="http://www.google.com/support/webmasters/bin/answer.py?answer=34648">Creating Mobile Sitemaps</a>
 */
public class GoogleMobileSitemapGenerator extends SitemapGenerator<GoogleMobileSitemapUrl,GoogleMobileSitemapGenerator> {

	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleMobileSitemapGenerator> builder(URL baseUrl, File baseDir) {
		return new SitemapGeneratorBuilder<GoogleMobileSitemapGenerator>(baseUrl, baseDir, GoogleMobileSitemapGenerator.class);
	}
	
	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleMobileSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
		return new SitemapGeneratorBuilder<GoogleMobileSitemapGenerator>(baseUrl, baseDir, GoogleMobileSitemapGenerator.class);
	}
	
	GoogleMobileSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public GoogleMobileSitemapGenerator(String baseUrl, File baseDir)
			throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public GoogleMobileSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	private static class Renderer extends AbstractSitemapUrlRenderer<GoogleMobileSitemapUrl> implements ISitemapUrlRenderer<GoogleMobileSitemapUrl> {

		public Class<GoogleMobileSitemapUrl> getUrlClass() {
			return GoogleMobileSitemapUrl.class;
		}

		public void render(GoogleMobileSitemapUrl url, OutputStreamWriter out,
				W3CDateFormat dateFormat) throws IOException {
			String additionalData = "    <mobile:mobile/>\n";
			super.render(url, out, dateFormat, additionalData);
			
		}

		public String getXmlNamespaces() {
			return "xmlns:mobile=\"http://www.google.com/schemas/sitemap-mobile/1.0\"";
		}
		
	}
}
