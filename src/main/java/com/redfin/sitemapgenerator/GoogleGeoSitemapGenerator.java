package com.redfin.sitemapgenerator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

/** Builds a Google Geo Sitemap (which points to KML or GeoRSS files).  At this time, SitemapGen4j can't
 * generate either KML or GeoRSS (sorry).
 * 
 * @author Dan Fabulich
 * @see <a href="http://www.google.com/support/webmasters/bin/answer.py?answer=94555">Creating Geo Sitemaps</a>
 */
public class GoogleGeoSitemapGenerator extends SitemapGenerator<GoogleGeoSitemapUrl,GoogleGeoSitemapGenerator> {

	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleGeoSitemapGenerator> builder(URL baseUrl, File baseDir) {
		return new SitemapGeneratorBuilder<GoogleGeoSitemapGenerator>(baseUrl, baseDir, GoogleGeoSitemapGenerator.class);
	}
	
	/** Configures a builder so you can specify sitemap generator options
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleGeoSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
		return new SitemapGeneratorBuilder<GoogleGeoSitemapGenerator>(baseUrl, baseDir, GoogleGeoSitemapGenerator.class);
	}
	
	GoogleGeoSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
	}

	/**Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public GoogleGeoSitemapGenerator(String baseUrl, File baseDir)
			throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/**Configures the generator with a base URL and directory to write the sitemap files.
	 * 
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public GoogleGeoSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	private static class Renderer extends AbstractSitemapUrlRenderer<GoogleGeoSitemapUrl> implements ISitemapUrlRenderer<GoogleGeoSitemapUrl> {

		public Class<GoogleGeoSitemapUrl> getUrlClass() {
			return GoogleGeoSitemapUrl.class;
		}

		public void render(GoogleGeoSitemapUrl url, OutputStreamWriter out,
				W3CDateFormat dateFormat) throws IOException {
			StringBuilder sb = new StringBuilder();
			sb.append("    <geo:geo>\n");
			sb.append("      <geo:format>"+url.getFormat()+"</geo:format>\n");
			sb.append("    </geo:geo>\n");
			super.render(url, out, dateFormat, sb.toString());
			
		}

		public String getXmlNamespaces() {
			return "xmlns:geo=\"http://www.google.com/geo/schemas/sitemap/1.0\"";
		}
		
	}
}
