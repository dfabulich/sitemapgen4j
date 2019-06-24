package com.redfin.sitemapgenerator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Builds a sitemap for Google News.  To configure options, use {@link #builder(URL, File)}
 * @author Dan Fabulich
 * @see <a href="http://www.google.com/support/news_pub/bin/answer.py?answer=74288">Creating a News Sitemap</a>
 */
public class GoogleNewsWthImageSitemapGenerator extends SitemapGenerator<GoogleNewsWithImageSitemapUrl, GoogleNewsWthImageSitemapGenerator> {

	/** 1000 URLs max in a Google News sitemap. */
	public static final int MAX_URLS_PER_SITEMAP = 1000;

	/** Configures a builder so you can specify sitemap generator options
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator> builder(URL baseUrl, File baseDir) {
		SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator> builder =
			new SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator>(baseUrl, baseDir, GoogleNewsWthImageSitemapGenerator.class);
		builder.maxUrls = 1000;
		return builder;
	}

	/** Configures a builder so you can specify sitemap generator options
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @return a builder; call .build() on it to make a sitemap generator
	 */
	public static SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator> builder(String baseUrl, File baseDir) throws MalformedURLException {
		SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator> builder =
			new SitemapGeneratorBuilder<GoogleNewsWthImageSitemapGenerator>(baseUrl, baseDir, GoogleNewsWthImageSitemapGenerator.class);
		builder.maxUrls = GoogleNewsWthImageSitemapGenerator.MAX_URLS_PER_SITEMAP;
		return builder;
	}

	GoogleNewsWthImageSitemapGenerator(AbstractSitemapGeneratorOptions<?> options) {
		super(options, new Renderer());
		if (options.maxUrls > GoogleNewsWthImageSitemapGenerator.MAX_URLS_PER_SITEMAP) {
			throw new RuntimeException("Google News sitemaps can have only 1000 URLs per sitemap: " + options.maxUrls);
		}
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 * @throws MalformedURLException
	 */
	public GoogleNewsWthImageSitemapGenerator(String baseUrl, File baseDir)
			throws MalformedURLException {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/** Configures the generator with a base URL and directory to write the sitemap files.
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 * @param baseDir Sitemap files will be generated in this directory as either "sitemap.xml" or "sitemap1.xml" "sitemap2.xml" and so on.
	 */
	public GoogleNewsWthImageSitemapGenerator(URL baseUrl, File baseDir) {
		this(new SitemapGeneratorOptions(baseUrl, baseDir));
	}

	/**Configures the generator with a base URL and a null directory. The object constructed
	 * is not intended to be used to write to files. Rather, it is intended to be used to obtain
	 * XML-formatted strings that represent sitemaps.
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 */
	public GoogleNewsWthImageSitemapGenerator(String baseUrl) throws MalformedURLException {
		this(new SitemapGeneratorOptions(new URL(baseUrl)));
	}

	/**Configures the generator with a base URL and a null directory. The object constructed
	 * is not intended to be used to write to files. Rather, it is intended to be used to obtain
	 * XML-formatted strings that represent sitemaps.
	 *
	 * @param baseUrl All URLs in the generated sitemap(s) should appear under this base URL
	 */
	public GoogleNewsWthImageSitemapGenerator(URL baseUrl) {
		this(new SitemapGeneratorOptions(baseUrl));
	}
	
	private static class Renderer extends AbstractSitemapUrlRenderer<GoogleNewsWithImageSitemapUrl> implements ISitemapUrlRenderer<GoogleNewsWithImageSitemapUrl> {

		public Class<GoogleNewsWithImageSitemapUrl> getUrlClass() {
			return GoogleNewsWithImageSitemapUrl.class;
		}

		public String getXmlNamespaces() {
			return "xmlns:news=\"http://www.google.com/schemas/sitemap-news/0.9\" xmlns:news=\"http://www.google.com/schemas/sitemap-image/1.1\"";
		}

		public void render(GoogleNewsWithImageSitemapUrl url, StringBuilder sb, W3CDateFormat dateFormat) {
			StringBuilder tagSb = new StringBuilder();
			tagSb.append("    <news:news>\n");
			tagSb.append("      <news:publication>\n");
			renderSubTag(tagSb, "news", "name", url.getPublication().getName());
			renderSubTag(tagSb, "news", "language", url.getPublication().getLanguage());
			tagSb.append("      </news:publication>\n");
			renderTag(tagSb, "news", "genres", url.getGenres());
			renderTag(tagSb, "news", "publication_date", dateFormat.format(url.getPublicationDate()));
			renderTag(tagSb, "news", "title", url.getTitle());
			renderTag(tagSb, "news", "keywords", url.getKeywords());
			tagSb.append("    </news:news>\n");
			tagSb.append("    <image:image>\n");
			renderSubTag(tagSb, "image", "loc", url.getImageLocation());
			renderSubTag(tagSb, "image", "title", url.getImageTitle());
			tagSb.append("    </image:image>\n");


			super.render(url, sb, dateFormat, tagSb.toString());
		}
		
	}

}
